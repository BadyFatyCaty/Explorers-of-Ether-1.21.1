package net.badyfatycaty.explorersofether.block.entity;

import net.badyfatycaty.explorersofether.screen.custom.ForgeMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(12) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == FUEL_SLOT) {
                return stack.getItem() == Items.LAVA_BUCKET;
            }
            return true; // Allow all other slots to accept any item (can fine-tune later)
        }
    };

    private static final int CRAFTING_START = 0; // slots 0–8
    private static final int CRAFTING_END = 8;
    private static final int CRUCIBLE_SLOT = 9;
    private static final int FUEL_SLOT = 10;
    private static final int OUTPUT_SLOT = 11;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;
    public int burnTime = 0;
    private int maxBurnTime = 40000; // e.g., lava bucket burn time

    public ForgeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FORGE_BE.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ForgeBlockEntity.this.progress;
                    case 1 -> ForgeBlockEntity.this.maxProgress;
                    case 2 -> ForgeBlockEntity.this.burnTime;
                    case 3 -> ForgeBlockEntity.this.maxBurnTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> ForgeBlockEntity.this.progress = value;
                    case 1 -> ForgeBlockEntity.this.maxProgress = value;
                    case 2 -> ForgeBlockEntity.this.burnTime = value;
                    case 3 -> ForgeBlockEntity.this.maxBurnTime = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.explorers_of_ether.forge");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ForgeMenu(i, inventory, this, this.data);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < 12; i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("forge.progress", progress);
        pTag.putInt("forge.max_progress", maxProgress);
        pTag.putInt("forge.burn_time", burnTime);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("forge.progress");
        maxProgress = pTag.getInt("forge.max_progress");
        burnTime = pTag.getInt("forge.burn_time");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        boolean hasLavaBucket = itemHandler.getStackInSlot(FUEL_SLOT).is(Items.LAVA_BUCKET);
        boolean hasValidRecipe = hasRecipe();

        // Start burning if not already burning and there's a lava bucket
        if (burnTime <= 0 && hasLavaBucket) {
            burnTime = maxBurnTime;
            // Consume lava bucket, leave empty bucket
            itemHandler.setStackInSlot(FUEL_SLOT, new ItemStack(Items.BUCKET));
        }

        // Burn time decreases continuously if burning
        if (burnTime > 0) {
            burnTime--;
        }

        // Only advance progress if both recipe and fuel are active
        if (hasValidRecipe && burnTime > 0) {
            progress++;
            if (progress >= maxProgress) {
                craftItem();
                progress = 0;
            }
        } else if (progress > 0) {
            progress--; // slowly decay progress if recipe/fuel stops
        }

        setChanged(level, blockPos, blockState);
    }

    private void craftItem() {
        // Remove 1 diamond per slot
        for (int i = CRAFTING_START; i <= CRAFTING_END; i++) {
            itemHandler.extractItem(i, 1, false);
        }
        // Remove bucket in crucible slot only (do NOT consume fuel slot bucket)
        itemHandler.extractItem(CRUCIBLE_SLOT, 1, false);

        // Add powdered snow bucket to output
        ItemStack result = new ItemStack(Items.POWDER_SNOW_BUCKET, 1);
        itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 72;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        // Check 9 diamonds in crafting grid
        for (int i = CRAFTING_START; i <= CRAFTING_END; i++) {
            if (!itemHandler.getStackInSlot(i).is(Items.DIAMOND)) {
                return false;
            }
        }

        // Check crucible slot has bucket
        if (!itemHandler.getStackInSlot(CRUCIBLE_SLOT).is(Items.BUCKET)) {
            return false;
        }

        // Check if output can accept 1 powdered snow bucket
        ItemStack output = new ItemStack(Items.POWDER_SNOW_BUCKET, 1);
        return canInsertItemIntoOutputSlot(output) && canInsertAmountIntoOutputSlot(output.getCount());
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                itemHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}