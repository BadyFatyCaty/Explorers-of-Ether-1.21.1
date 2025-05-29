package net.badyfatycaty.explorersofether.items.custom.weapon_types;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.List;
import java.util.function.Predicate;

public class ModMaceItem extends TieredItem {
    public static final float SMASH_ATTACK_FALL_THRESHOLD = 1.5F;
    private static final float SMASH_ATTACK_HEAVY_THRESHOLD = 5.0F;
    public static final float SMASH_ATTACK_KNOCKBACK_RADIUS = 3.5F;
    private static final float SMASH_ATTACK_KNOCKBACK_POWER = 0.7F;

    public ModMaceItem(Tier tier, Item.Properties properties) {
        super(tier, properties.component(DataComponents.TOOL, createToolProperties()));
    }

    public ModMaceItem(Tier p_tier, Item.Properties p_properties, Tool toolComponentData) {
        super(p_tier, p_properties.component(DataComponents.TOOL, toolComponentData));
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
        return createAttributes(tier, (float)attackDamage, attackSpeed);
    }

    public static ItemAttributeModifiers createAttributes(Tier p_330371_, float p_331976_, float p_332104_) {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)(p_331976_ + p_330371_.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)p_332104_, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof ServerPlayer serverplayer) {
            if (canSmashAttack(serverplayer)) {
                ServerLevel serverlevel = (ServerLevel)attacker.level();
                if (serverplayer.isIgnoringFallDamageFromCurrentImpulse() && serverplayer.currentImpulseImpactPos != null) {
                    if (serverplayer.currentImpulseImpactPos.y > serverplayer.position().y) {
                        serverplayer.currentImpulseImpactPos = serverplayer.position();
                    }
                } else {
                    serverplayer.currentImpulseImpactPos = serverplayer.position();
                }

                serverplayer.setIgnoreFallDamageFromCurrentImpulse(true);
                serverplayer.setDeltaMovement(serverplayer.getDeltaMovement().with(Direction.Axis.Y, (double)0.01F));
                serverplayer.connection.send(new ClientboundSetEntityMotionPacket(serverplayer));
                if (target.onGround()) {
                    serverplayer.setSpawnExtraParticlesOnFall(true);
                    SoundEvent soundevent = serverplayer.fallDistance > 5.0F ? SoundEvents.MACE_SMASH_GROUND_HEAVY : SoundEvents.MACE_SMASH_GROUND;
                    serverlevel.playSound((Player)null, serverplayer.getX(), serverplayer.getY(), serverplayer.getZ(), soundevent, serverplayer.getSoundSource(), 1.0F, 1.0F);
                } else {
                    serverlevel.playSound((Player)null, serverplayer.getX(), serverplayer.getY(), serverplayer.getZ(), SoundEvents.MACE_SMASH_AIR, serverplayer.getSoundSource(), 1.0F, 1.0F);
                }

                knockback(serverlevel, serverplayer, target);
            }
        }

        return true;
    }

    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        if (canSmashAttack(attacker)) {
            attacker.resetFallDistance();
        }

    }

    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        Entity f3 = damageSource.getDirectEntity();
        if (f3 instanceof LivingEntity livingentity) {
            if (!canSmashAttack(livingentity)) {
                return 0.0F;
            } else {
                float ff3 = 3.0F;
                float f = 8.0F;
                float f1 = livingentity.fallDistance;
                float f2;
                if (f1 <= 3.0F) {
                    f2 = 4.0F * f1;
                } else if (f1 <= 8.0F) {
                    f2 = 12.0F + 2.0F * (f1 - 3.0F);
                } else {
                    f2 = 22.0F + f1 - 8.0F;
                }

                Level var10 = livingentity.level();
                float var10000;
                if (var10 instanceof ServerLevel) {
                    ServerLevel serverlevel = (ServerLevel)var10;
                    var10000 = f2 + EnchantmentHelper.modifyFallBasedDamage(serverlevel, livingentity.getWeaponItem(), target, damageSource, 0.0F) * f1;
                } else {
                    var10000 = f2;
                }

                return var10000;
            }
        } else {
            return 0.0F;
        }
    }

    private static void knockback(Level level, Player player, Entity entity) {
        level.levelEvent(2013, entity.getOnPos(), 750);
        level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate((double)3.5F), knockbackPredicate(player, entity)).forEach((p_347296_) -> {
            Vec3 vec3 = p_347296_.position().subtract(entity.position());
            double d0 = getKnockbackPower(player, p_347296_, vec3);
            Vec3 vec31 = vec3.normalize().scale(d0);
            if (d0 > (double)0.0F) {
                p_347296_.push(vec31.x, (double)0.7F, vec31.z);
                if (p_347296_ instanceof ServerPlayer) {
                    ServerPlayer serverplayer = (ServerPlayer)p_347296_;
                    serverplayer.connection.send(new ClientboundSetEntityMotionPacket(serverplayer));
                }
            }

        });
    }

    private static Predicate<LivingEntity> knockbackPredicate(Player player, Entity entity) {
        return (p_344407_) -> {
            boolean flag;
            boolean flag1;
            boolean flag2;
            boolean flag6;
            label62: {
                flag = !p_344407_.isSpectator();
                flag1 = p_344407_ != player && p_344407_ != entity;
                flag2 = !player.isAlliedTo(p_344407_);
                if (p_344407_ instanceof TamableAnimal tamableanimal) {
                    if (tamableanimal.isTame() && player.getUUID().equals(tamableanimal.getOwnerUUID())) {
                        flag6 = true;
                        break label62;
                    }
                }

                flag6 = false;
            }

            boolean flag3;
            label55: {
                flag3 = !flag6;
                if (p_344407_ instanceof ArmorStand armorstand) {
                    if (armorstand.isMarker()) {
                        flag6 = false;
                        break label55;
                    }
                }

                flag6 = true;
            }

            boolean flag5 = entity.distanceToSqr(p_344407_) <= Math.pow((double)3.5F, (double)2.0F);
            return flag && flag1 && flag2 && flag3 && flag6 && flag5;
        };
    }

    private static double getKnockbackPower(Player player, LivingEntity entity, Vec3 entityPos) {
        return ((double)3.5F - entityPos.length()) * (double)0.7F * (double)(player.fallDistance > 5.0F ? 2 : 1) * ((double)1.0F - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
    }

    public static boolean canSmashAttack(LivingEntity entity) {
        return entity.fallDistance > 1.5F && !entity.isFallFlying();
    }
}