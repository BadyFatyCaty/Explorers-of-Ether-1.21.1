package net.badyfatycaty.explorersofether.items.custom.weapon_types;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class TieredProjectileWeaponItem extends TieredItem {
    private final Tier tier;
    public static final Predicate<ItemStack> ARROW_ONLY = (p_43017_) -> p_43017_.is(ItemTags.ARROWS);
    public static final Predicate<ItemStack> ARROW_OR_FIREWORK;

    public TieredProjectileWeaponItem(Tier tier, Item.Properties properties) {
        super(tier, properties);
        this.tier = tier;
    }

    /** @deprecated */
    @Deprecated
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return this.getAllSupportedProjectiles();
    }

    /** @deprecated */
    @Deprecated
    public abstract Predicate<ItemStack> getAllSupportedProjectiles();

    public Predicate<ItemStack> getSupportedHeldProjectiles(ItemStack stack) {
        return this.getAllSupportedProjectiles(stack).or(this.getSupportedHeldProjectiles());
    }

    public Predicate<ItemStack> getAllSupportedProjectiles(ItemStack stack) {
        return this.getAllSupportedProjectiles();
    }

    public static ItemStack getHeldProjectile(LivingEntity shooter, Predicate<ItemStack> isAmmo) {
        if (isAmmo.test(shooter.getItemInHand(InteractionHand.OFF_HAND))) {
            return shooter.getItemInHand(InteractionHand.OFF_HAND);
        } else {
            return isAmmo.test(shooter.getItemInHand(InteractionHand.MAIN_HAND)) ? shooter.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
        }
    }

    public abstract int getDefaultProjectileRange();

    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        float f = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F);
        float f1 = projectileItems.size() == 1 ? 0.0F : 2.0F * f / (float)(projectileItems.size() - 1);
        float f2 = (float)((projectileItems.size() - 1) % 2) * f1 / 2.0F;
        float f3 = 1.0F;

        for(int i = 0; i < projectileItems.size(); ++i) {
            ItemStack itemstack = (ItemStack)projectileItems.get(i);
            if (!itemstack.isEmpty()) {
                float f4 = f2 + f3 * (float)((i + 1) / 2) * f1;
                f3 = -f3;
                Projectile projectile = this.createProjectile(level, shooter, weapon, itemstack, isCrit);
                this.shootProjectile(shooter, projectile, i, velocity, inaccuracy, f4, target);
                level.addFreshEntity(projectile);
                weapon.hurtAndBreak(this.getDurabilityUse(itemstack), shooter, LivingEntity.getSlotForHand(hand));
                if (weapon.isEmpty()) {
                    break;
                }
            }
        }

    }

    protected int getDurabilityUse(ItemStack stack) {
        return tier.getUses();
    }

    protected abstract void shootProjectile(LivingEntity var1, Projectile var2, int var3, float var4, float var5, float var6, @Nullable LivingEntity var7);

    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        Item var8 = ammo.getItem();
        ArrowItem var10000;
        if (var8 instanceof ArrowItem arrowitem1) {
            var10000 = arrowitem1;
        } else {
            var10000 = (ArrowItem)Items.ARROW;
        }

        ArrowItem arrowitem = var10000;
        AbstractArrow abstractarrow = arrowitem.createArrow(level, ammo, shooter, weapon);
        if (isCrit) {
            abstractarrow.setCritArrow(true);
        }

        return this.customArrow(abstractarrow, ammo, weapon);
    }

    protected static List<ItemStack> draw(ItemStack weapon, ItemStack ammo, LivingEntity shooter) {
        if (ammo.isEmpty()) {
            return List.of();
        } else {
            Level itemstack1 = shooter.level();
            int var10000;
            if (itemstack1 instanceof ServerLevel) {
                ServerLevel serverlevel = (ServerLevel)itemstack1;
                var10000 = EnchantmentHelper.processProjectileCount(serverlevel, weapon, shooter, 1);
            } else {
                var10000 = 1;
            }

            int i = var10000;
            List<ItemStack> list = new ArrayList(i);
            ItemStack itemstack_one = ammo.copy();

            for(int j = 0; j < i; ++j) {
                ItemStack itemstack = useAmmo(weapon, j == 0 ? ammo : itemstack_one, shooter, j > 0);
                if (!itemstack.isEmpty()) {
                    list.add(itemstack);
                }
            }

            return list;
        }
    }

    protected static ItemStack useAmmo(ItemStack weapon, ItemStack ammo, LivingEntity shooter, boolean intangable) {
        int var10000;
        label35: {
            ServerLevel serverlevel;
            label34: {
                if (!intangable) {
                    Level var7 = shooter.level();
                    if (var7 instanceof ServerLevel) {
                        serverlevel = (ServerLevel)var7;
                        if (!shooter.hasInfiniteMaterials()) {
                            Item var11 = ammo.getItem();
                            if (!(var11 instanceof ArrowItem)) {
                                break label34;
                            }

                            ArrowItem ai = (ArrowItem)var11;
                            if (!ai.isInfinite(ammo, weapon, shooter)) {
                                break label34;
                            }
                        }
                    }
                }

                var10000 = 0;
                break label35;
            }

            var10000 = EnchantmentHelper.processAmmoUse(serverlevel, weapon, ammo, 1);
        }

        int i = var10000;
        if (i > ammo.getCount()) {
            return ItemStack.EMPTY;
        } else if (i == 0) {
            ItemStack itemstack1 = ammo.copyWithCount(1);
            itemstack1.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
            return itemstack1;
        } else {
            ItemStack itemstack = ammo.split(i);
            if (ammo.isEmpty() && shooter instanceof Player) {
                Player player = (Player)shooter;
                player.getInventory().removeItem(ammo);
            }

            return itemstack;
        }
    }

    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        return arrow;
    }

    public ItemStack getDefaultCreativeAmmo(@Nullable Player player, ItemStack projectileWeaponItem) {
        return Items.ARROW.getDefaultInstance();
    }

    static {
        ARROW_OR_FIREWORK = ARROW_ONLY.or((p_43015_) -> p_43015_.is(Items.FIREWORK_ROCKET));
    }

    public Tier getTier() {
        return this.tier;
    }

    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }

    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
    }
}