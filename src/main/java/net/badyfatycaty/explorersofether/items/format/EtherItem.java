package net.badyfatycaty.explorersofether.items.format;
import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

import net.badyfatycaty.explorersofether.rarity.ModRarity;
import net.minecraft.network.chat.Style;
import net.minecraft.client.gui.screens.Screen;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.common.extensions.IItemPropertiesExtensions;
import net.neoforged.neoforge.registries.GameData;
import org.slf4j.Logger;

public class EtherItem extends Item implements FeatureElement, ItemLike, IItemExtension {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Map<Block, net.minecraft.world.item.Item> BY_BLOCK = GameData.getBlockItemMap();
    public static final ResourceLocation BASE_ATTACK_DAMAGE_ID = ResourceLocation.withDefaultNamespace("base_attack_damage");
    public static final ResourceLocation BASE_ATTACK_SPEED_ID = ResourceLocation.withDefaultNamespace("base_attack_speed");
    public static final int DEFAULT_MAX_STACK_SIZE = 64;
    public static final int ABSOLUTE_MAX_STACK_SIZE = 99;
    public static final int MAX_BAR_WIDTH = 13;
    private final Holder.Reference<net.minecraft.world.item.Item> builtInRegistryHolder;
    private DataComponentMap components;
    @Nullable
    private final net.minecraft.world.item.Item craftingRemainingItem;
    @Nullable
    private String descriptionId;
    private final FeatureFlagSet requiredFeatures;
    protected final boolean canRepair;
    private final ModRarity modRarity;

    public static int getId(net.minecraft.world.item.Item item) {
        return item == null ? 0 : BuiltInRegistries.ITEM.getId(item);
    }

    public static net.minecraft.world.item.Item byId(int id) {
        return (net.minecraft.world.item.Item)BuiltInRegistries.ITEM.byId(id);
    }


    public EtherItem(Properties properties, ModRarity modRarity) {
        super(properties.rarity(modRarity.toVanilla()));
        this.builtInRegistryHolder = BuiltInRegistries.ITEM.createIntrusiveHolder(this);
        this.components = properties.buildAndValidateComponents();
        this.craftingRemainingItem = properties.craftingRemainingItem;
        this.requiredFeatures = properties.requiredFeatures;
        this.canRepair = properties.canRepair;
        this.modRarity = modRarity;
    }

    public EtherItem(Properties properties) {
        super(properties.rarity(ModRarity.COMMON.toVanilla()));
        this.builtInRegistryHolder = BuiltInRegistries.ITEM.createIntrusiveHolder(this);
        this.components = properties.buildAndValidateComponents();
        this.craftingRemainingItem = properties.craftingRemainingItem;
        this.requiredFeatures = properties.requiredFeatures;
        this.canRepair = properties.canRepair;
        this.modRarity = ModRarity.COMMON;
    }

    public ModRarity getModRarity() {
        return modRarity;
    }

    public int getDefaultMaxStackSize() {
        return (Integer)this.components.getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
    }

    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
    }

    /** @deprecated */
    @Deprecated
    public void onDestroyed(ItemEntity itemEntity) {
    }

    public void verifyComponentsAfterLoad(ItemStack stack) {
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return true;
    }

    public Item asItem() {
        return this;
    }

    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Tool tool = (Tool)stack.get(DataComponents.TOOL);
        return tool != null ? tool.getMiningSpeed(state) : 1.0F;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        FoodProperties foodproperties = itemstack.getFoodProperties(player);
        if (foodproperties != null) {
            if (player.canEat(foodproperties.canAlwaysEat())) {
                player.startUsingItem(usedHand);
                return InteractionResultHolder.consume(itemstack);
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        } else {
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));
        }
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        FoodProperties foodproperties = stack.getFoodProperties(livingEntity);
        return foodproperties != null ? livingEntity.eat(level, stack, foodproperties) : stack;
    }

    public boolean isBarVisible(ItemStack stack) {
        return stack.isDamaged();
    }

    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (float)stack.getDamageValue() * 13.0F / (float)this.getMaxDamage(stack));
    }

    public int getBarColor(ItemStack stack) {
        int i = stack.getMaxDamage();
        float stackMaxDamage = (float)this.getMaxDamage(stack);
        float f = Math.max(0.0F, (stackMaxDamage - (float)stack.getDamageValue()) / stackMaxDamage);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        return false;
    }

    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        return false;
    }

    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        return 0.0F;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return false;
    }

    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        Tool tool = (Tool)stack.get(DataComponents.TOOL);
        if (tool == null) {
            return false;
        } else {
            if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F && tool.damagePerBlock() > 0) {
                stack.hurtAndBreak(tool.damagePerBlock(), miningEntity, EquipmentSlot.MAINHAND);
            }

            return true;
        }
    }

    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        Tool tool = (Tool)stack.get(DataComponents.TOOL);
        return tool != null && tool.isCorrectForDrops(state);
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        return InteractionResult.PASS;
    }

    public Component getDescription() {
        return Component.translatable(this.getDescriptionId());
    }

    public String toString() {
        return BuiltInRegistries.ITEM.wrapAsHolder(this).getRegisteredName();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this));
        }

        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public String getDescriptionId(ItemStack stack) {
        return this.getDescriptionId();
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
    }

    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        this.onCraftedPostProcess(stack, level);
    }

    public void onCraftedPostProcess(ItemStack stack, Level level) {
    }

    public boolean isComplex() {
        return false;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return stack.has(DataComponents.FOOD) ? UseAnim.EAT : UseAnim.NONE;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        FoodProperties foodproperties = stack.getFoodProperties((LivingEntity)null);
        return foodproperties != null ? foodproperties.eatDurationTicks() : 0;
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        char icon = 0;
        ResourceLocation fontLocation = null;
        if (!tooltipComponents.isEmpty()) {
            Component original = tooltipComponents.get(0);
            icon = (char) ('\uE000' + modRarity.getId());
            // Create icon component using custom font
            fontLocation = ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MOD_ID, "rarities");
            Component first = tooltipComponents.get(0);
            tooltipComponents.set(0,first.copy().withStyle(modRarity.getStyleModifier()));
        }
        tooltipComponents.add(
                Component.literal(String.valueOf(icon))
                        .setStyle(Style.EMPTY.withFont(fontLocation).withColor(ChatFormatting.WHITE)));
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.empty();
    }

    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId(stack));
    }

    public boolean isFoil(ItemStack stack) {
        return stack.isEnchanted();
    }

    public boolean isEnchantable(ItemStack stack) {
        return stack.getMaxStackSize() == 1 && stack.has(DataComponents.MAX_DAMAGE);
    }

    public static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluidMode) {
        Vec3 vec3 = player.getEyePosition();
        Vec3 vec31 = vec3.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(player.blockInteractionRange()));
        return level.clip(new ClipContext(vec3, vec31, net.minecraft.world.level.ClipContext.Block.OUTLINE, fluidMode, player));
    }

    /** @deprecated */
    @Deprecated
    public int getEnchantmentValue() {
        return 0;
    }

    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return false;
    }

    /** @deprecated */
    @Deprecated
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return ItemAttributeModifiers.EMPTY;
    }

    public boolean isRepairable(ItemStack stack) {
        return this.canRepair && this.isDamageable(stack);
    }

    public boolean useOnRelease(ItemStack stack) {
        return stack.getItem() == Items.CROSSBOW;
    }

    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }

    public SoundEvent getBreakingSound() {
        return SoundEvents.ITEM_BREAK;
    }

    public boolean canFitInsideContainerItems() {
        return true;
    }

    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    @Override
    public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return super.isEnabled(enabledFeatures);
    }

    public static class Properties extends Item.Properties implements IItemPropertiesExtensions {
        private static final Interner<DataComponentMap> COMPONENT_INTERNER = Interners.newStrongInterner();
        @Nullable
        private DataComponentMap.Builder components;
        @Nullable
        net.minecraft.world.item.Item craftingRemainingItem;
        FeatureFlagSet requiredFeatures;
        private boolean canRepair;

        public Properties() {
            this.requiredFeatures = FeatureFlags.VANILLA_SET;
            this.canRepair = true;
        }

        public Properties food(FoodProperties food) {
            return this.component(DataComponents.FOOD, food);
        }

        public Properties stacksTo(int maxStackSize) {
            return this.component(DataComponents.MAX_STACK_SIZE, maxStackSize);
        }

        public Properties durability(int maxDamage) {
            this.component(DataComponents.MAX_DAMAGE, maxDamage);
            this.component(DataComponents.MAX_STACK_SIZE, 1);
            this.component(DataComponents.DAMAGE, 0);
            return this;
        }

        public Properties craftRemainder(net.minecraft.world.item.Item craftingRemainingItem) {
            this.craftingRemainingItem = craftingRemainingItem;
            return this;
        }

        public Properties rarity(Rarity rarity) {
            return this.component(DataComponents.RARITY, rarity);
        }

        public Properties fireResistant() {
            return this.component(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
        }

        public Properties jukeboxPlayable(ResourceKey<JukeboxSong> song) {
            return this.component(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder(song), true));
        }

        public Properties setNoRepair() {
            this.canRepair = false;
            return this;
        }

        public Properties requiredFeatures(FeatureFlag... requiredFeatures) {
            this.requiredFeatures = FeatureFlags.REGISTRY.subset(requiredFeatures);
            return this;
        }

        public <T> Properties component(DataComponentType<T> component, T value) {
            CommonHooks.validateComponent(value);
            if (this.components == null) {
                this.components = DataComponentMap.builder().addAll(DataComponents.COMMON_ITEM_COMPONENTS);
            }

            this.components.set(component, value);
            return this;
        }

        public Properties attributes(ItemAttributeModifiers attributes) {
            return this.component(DataComponents.ATTRIBUTE_MODIFIERS, attributes);
        }

        DataComponentMap buildAndValidateComponents() {
            DataComponentMap datacomponentmap = this.buildComponents();
            return validateComponents(datacomponentmap);
        }

        public static DataComponentMap validateComponents(DataComponentMap datacomponentmap) {
            if (datacomponentmap.has(DataComponents.DAMAGE) && (Integer)datacomponentmap.getOrDefault(DataComponents.MAX_STACK_SIZE, 1) > 1) {
                throw new IllegalStateException("Item cannot have both durability and be stackable");
            } else {
                return datacomponentmap;
            }
        }

        private DataComponentMap buildComponents() {
            return this.components == null ? DataComponents.COMMON_ITEM_COMPONENTS : (DataComponentMap)COMPONENT_INTERNER.intern(this.components.build());
        }

        @Override
        public <T> Properties component(Supplier<? extends DataComponentType<T>> componentType, T value) {
            // Delegate to the superclass implementation, then return this instance
            super.component(componentType, value);
            return this;
        }
    }

    public interface TooltipContext {
        net.minecraft.world.item.Item.TooltipContext EMPTY = new net.minecraft.world.item.Item.TooltipContext() {
            @Nullable
            public HolderLookup.Provider registries() {
                return null;
            }

            public float tickRate() {
                return 20.0F;
            }

            @Nullable
            public MapItemSavedData mapData(MapId p_339618_) {
                return null;
            }
        };

        @Nullable
        HolderLookup.Provider registries();

        float tickRate();

        @Nullable
        MapItemSavedData mapData(MapId var1);

        @Nullable
        default Level level() {
            return null;
        }

        static net.minecraft.world.item.Item.TooltipContext of(@Nullable final Level level) {
            return level == null ? EMPTY : new net.minecraft.world.item.Item.TooltipContext() {
                public HolderLookup.Provider registries() {
                    return level.registryAccess();
                }

                public float tickRate() {
                    return level.tickRateManager().tickrate();
                }

                public MapItemSavedData mapData(MapId p_339628_) {
                    return level.getMapData(p_339628_);
                }

                public Level level() {
                    return level;
                }
            };
        }

        static net.minecraft.world.item.Item.TooltipContext of(final HolderLookup.Provider registries) {
            return new net.minecraft.world.item.Item.TooltipContext() {
                public HolderLookup.Provider registries() {
                    return registries;
                }

                public float tickRate() {
                    return 20.0F;
                }

                @Nullable
                public MapItemSavedData mapData(MapId p_339679_) {
                    return null;
                }
            };
        }
    }
}
