package net.badyfatycaty.explorersofether.items.custom.weapon_types;


import net.badyfatycaty.explorersofether.attributes.ranged.DrawSpeed;
import net.badyfatycaty.explorersofether.attributes.ranged.ProjectileDamage;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class ModBowItem extends ProjectileWeaponItem {
    private final Tier tier;
    public static final int BASE_DRAW_DURATION_TICKS = 20;
    public static final int DEFAULT_RANGE = 15;

    public ModBowItem(Tier tier, Item.Properties properties) {
        super(properties.durability(tier.getUses()).stacksTo(1).component(DataComponents.TOOL, createToolProperties()));
        this.tier = tier;
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }

    public ModBowItem(Tier p_tier, Item.Properties p_properties, Tool toolComponentData) {
        super(p_properties.durability(p_tier.getUses()).stacksTo(1).component(DataComponents.TOOL, toolComponentData));
        this.tier = p_tier;
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, float projectileDamage, float drawSpeed, float attackSpeed) {
        return ItemAttributeModifiers.builder().add(ProjectileDamage.PROJECTILE_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)(projectileDamage + tier.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(DrawSpeed.DRAW_SPEED, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)drawSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }


    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            ItemStack itemstack = player.getProjectile(stack);
            if (!itemstack.isEmpty()) {
                int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                i = EventHooks.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
                if (i < 0) {
                    return;
                }

                float f = getPowerForTime(i);
                double projectileDamage = getProjectileDamage(player);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(stack, itemstack, player);
                    if (level instanceof ServerLevel) {
                        ServerLevel serverlevel = (ServerLevel)level;
                        if (!list.isEmpty()) {
                            this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 3.0F, 1.0F, f == 1.0F, (LivingEntity)null);
                        }
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }

    }

    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        double projectileDamageValue = getProjectileDamage(shooter);
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, velocity, inaccuracy);
        if (projectile instanceof AbstractArrow arrow) {
            double base = arrow.getBaseDamage();
            arrow.setBaseDamage(projectileDamageValue);
        }
    }

    public static float getPowerForTime(int charge) {
        float f = (float)charge / 20F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        double drawSpeed = entity.getAttributeValue(DrawSpeed.DRAW_SPEED);
        return (int)(drawSpeed * 3F * 20F);
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean flag = !player.getProjectile(itemstack).isEmpty();
        InteractionResultHolder<ItemStack> ret = EventHooks.onArrowNock(itemstack, level, player, hand, flag);
        if (ret != null) {
            return ret;
        } else if (!player.hasInfiniteMaterials() && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public int getDefaultProjectileRange() {
        return 15;
    }

    public double getProjectileDamage(LivingEntity entity) {
        return entity.getAttributeValue(ProjectileDamage.PROJECTILE_DAMAGE);
    }

    public static double getDrawSpeed(LivingEntity entity) {
        return entity.getAttributeValue(DrawSpeed.DRAW_SPEED);
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

    public void tryLeftClickShoot(Level level, Player player, ItemStack bowStack) {
        if (level.isClientSide()) return; // Ensure server-side only

        ItemStack ammo = player.getProjectile(bowStack);
        if (ammo.isEmpty()) {
            System.out.println("No ammo found.");
            return;
        }

        float power = 0.4F; // Short bow-style instant shot
        double projDamage = getProjectileDamage(player);

        ItemStack arrowStack = ammo.copy();
        arrowStack.setCount(1);
        List<ItemStack> arrows = List.of(arrowStack);
        System.out.println("Arrows drawn (manual): " + arrows.size());

        if (!arrows.isEmpty() && level instanceof ServerLevel serverLevel) {
            this.shoot(serverLevel, player, InteractionHand.MAIN_HAND, bowStack, arrows, power * 3.0F, 1.0F, false, null);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F,
                    1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
            player.awardStat(Stats.ITEM_USED.get(this));
            bowStack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        } else {
            System.out.println("No arrows to shoot or not server level.");
        }
    }
}