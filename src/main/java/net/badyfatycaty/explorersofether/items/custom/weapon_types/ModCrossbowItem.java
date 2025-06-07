package net.badyfatycaty.explorersofether.items.custom.weapon_types;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.badyfatycaty.explorersofether.attributes.ranged.DrawSpeed;
import net.badyfatycaty.explorersofether.attributes.ranged.ProjectileDamage;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.world.item.Tier;

public class ModCrossbowItem extends ProjectileWeaponItem {
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;
    private static final CrossbowItem.ChargingSounds DEFAULT_SOUNDS;

    private final Tier tier;

    public ModCrossbowItem(Tier tier, Item.Properties properties) {
        super(properties.durability(tier.getUses()).stacksTo(1).component(DataComponents.TOOL, createToolProperties()));
        this.tier = tier;
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }

    public ModCrossbowItem(Tier p_tier, Item.Properties p_properties, Tool toolComponentData) {
        super(p_properties.durability(p_tier.getUses()).stacksTo(1).component(DataComponents.TOOL, toolComponentData));
        this.tier = p_tier;
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, float projectileDamage, float drawSpeed, float attackSpeed) {
        return ItemAttributeModifiers.builder().add(ProjectileDamage.PROJECTILE_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)(projectileDamage + tier.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(DrawSpeed.DRAW_SPEED, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)drawSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ARROW_OR_FIREWORK;
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ChargedProjectiles chargedprojectiles = (ChargedProjectiles)itemstack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
            this.performShooting(level, player, hand, itemstack, getShootingPower(chargedprojectiles), 1.0F, (LivingEntity)null);
            return InteractionResultHolder.consume(itemstack);
        } else if (!player.getProjectile(itemstack).isEmpty()) {
            this.startSoundPlayed = false;
            this.midLoadSoundPlayed = false;
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    private static float getShootingPower(ChargedProjectiles projectile) {
        return projectile.contains(Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack, entityLiving) - timeLeft;
        float f = getPowerForTime(i, stack, entityLiving);
        if (f >= 1.0F && !isCharged(stack) && tryLoadProjectiles(entityLiving, stack)) {
            CrossbowItem.ChargingSounds crossbowitem$chargingsounds = this.getChargingSounds(stack);
            crossbowitem$chargingsounds.end().ifPresent((p_352852_) -> level.playSound((Player)null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), (SoundEvent)p_352852_.value(), entityLiving.getSoundSource(), 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F));
        }

    }

    private static boolean tryLoadProjectiles(LivingEntity shooter, ItemStack crossbowStack) {
        List<ItemStack> list = draw(crossbowStack, shooter.getProjectile(crossbowStack), shooter);
        if (!list.isEmpty()) {
            crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCharged(ItemStack crossbowStack) {
        ChargedProjectiles chargedprojectiles = (ChargedProjectiles)crossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        return !chargedprojectiles.isEmpty();
    }

    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        Vector3f vector3f;
        if (target != null) {
            double d0 = target.getX() - shooter.getX();
            double d1 = target.getZ() - shooter.getZ();
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            double d3 = target.getY(0.3333333333333333) - projectile.getY() + d2 * (double)0.2F;
            vector3f = getProjectileShotVector(shooter, new Vec3(d0, d3, d1), angle);
        } else {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(angle * ((float)Math.PI / 180F)), vec3.x, vec3.y, vec3.z);
            Vec3 vec31 = shooter.getViewVector(1.0F);
            vector3f = vec31.toVector3f().rotate(quaternionf);
        }

        projectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), velocity, inaccuracy);
        float f = getShotPitch(shooter.getRandom(), index);
        shooter.level().playSound((Player)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, shooter.getSoundSource(), 1.0F, f);
    }

    private static Vector3f getProjectileShotVector(LivingEntity shooter, Vec3 distance, float angle) {
        Vector3f vector3f = distance.toVector3f().normalize();
        Vector3f vector3f1 = (new Vector3f(vector3f)).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f1.lengthSquared() <= 1.0E-7) {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            vector3f1 = (new Vector3f(vector3f)).cross(vec3.toVector3f());
        }

        Vector3f vector3f2 = (new Vector3f(vector3f)).rotateAxis(((float)Math.PI / 2F), vector3f1.x, vector3f1.y, vector3f1.z);
        return (new Vector3f(vector3f)).rotateAxis(angle * ((float)Math.PI / 180F), vector3f2.x, vector3f2.y, vector3f2.z);
    }

    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        if (ammo.is(Items.FIREWORK_ROCKET)) {
            return new FireworkRocketEntity(level, ammo, shooter, shooter.getX(), shooter.getEyeY() - (double)0.15F, shooter.getZ(), true);
        } else {
            Projectile projectile = super.createProjectile(level, shooter, weapon, ammo, isCrit);
            if (projectile instanceof AbstractArrow) {
                AbstractArrow abstractarrow = (AbstractArrow)projectile;
                abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
            }

            return projectile;
        }
    }

    protected int getDurabilityUse(ItemStack stack) {
        return stack.is(Items.FIREWORK_ROCKET) ? 3 : 1;
    }

    public void performShooting(Level level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, float velocity, float inaccuracy, @Nullable LivingEntity target) {
        if (level instanceof ServerLevel serverlevel) {
            if (shooter instanceof Player player) {
                if (EventHooks.onArrowLoose(weapon, shooter.level(), player, 1, true) < 0) {
                    return;
                }
            }

            ChargedProjectiles chargedprojectiles = (ChargedProjectiles)weapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
                this.shoot(serverlevel, shooter, hand, weapon, chargedprojectiles.getItems(), velocity, inaccuracy, shooter instanceof Player, target);
                if (shooter instanceof ServerPlayer) {
                    ServerPlayer serverplayer = (ServerPlayer)shooter;
                    CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, weapon);
                    serverplayer.awardStat(Stats.ITEM_USED.get(weapon.getItem()));
                }
            }
        }

    }

    private static float getShotPitch(RandomSource random, int index) {
        return index == 0 ? 1.0F : getRandomShotPitch((index & 1) == 1, random);
    }

    private static float getRandomShotPitch(boolean isHighPitched, RandomSource random) {
        float f = isHighPitched ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        if (!level.isClientSide) {
            CrossbowItem.ChargingSounds crossbowitem$chargingsounds = this.getChargingSounds(stack);
            float f = (float)(stack.getUseDuration(livingEntity) - count) / (float)getChargeDuration(stack, livingEntity);
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                crossbowitem$chargingsounds.start().ifPresent((p_352849_) -> level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), (SoundEvent)p_352849_.value(), SoundSource.PLAYERS, 0.5F, 1.0F));
            }

            if (f >= 0.5F && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                crossbowitem$chargingsounds.mid().ifPresent((p_352855_) -> level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), (SoundEvent)p_352855_.value(), SoundSource.PLAYERS, 0.5F, 1.0F));
            }
        }

    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return (int)(getDrawSpeed(entity) * 3F * 20F);
    }

    public static int getChargeDuration(ItemStack stack, LivingEntity shooter) {
        float f = EnchantmentHelper.modifyCrossbowChargingTime(stack, shooter, 1.25F);
        return Mth.floor(f * 20.0F);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    CrossbowItem.ChargingSounds getChargingSounds(ItemStack stack) {
        return (CrossbowItem.ChargingSounds)EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS).orElse(DEFAULT_SOUNDS);
    }

    private static float getPowerForTime(int timeLeft, ItemStack stack, LivingEntity shooter) {
        float f = (float)timeLeft / (float)getChargeDuration(stack, shooter);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ChargedProjectiles chargedprojectiles = (ChargedProjectiles)stack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
            ItemStack itemstack = (ItemStack)chargedprojectiles.getItems().get(0);
            tooltipComponents.add(Component.translatable("item.minecraft.crossbow.projectile").append(CommonComponents.SPACE).append(itemstack.getDisplayName()));
            if (tooltipFlag.isAdvanced() && itemstack.is(Items.FIREWORK_ROCKET)) {
                List<Component> list = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendHoverText(itemstack, context, list, tooltipFlag);
                if (!list.isEmpty()) {
                    for(int i = 0; i < list.size(); ++i) {
                        list.set(i, Component.literal("  ").append((Component)list.get(i)).withStyle(ChatFormatting.GRAY));
                    }

                    tooltipComponents.addAll(list);
                }
            }
        }

    }

    public boolean useOnRelease(ItemStack stack) {
        return stack.is(this);
    }

    public int getDefaultProjectileRange() {
        return 8;
    }

    static {
        DEFAULT_SOUNDS = new CrossbowItem.ChargingSounds(Optional.of(SoundEvents.CROSSBOW_LOADING_START), Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE), Optional.of(SoundEvents.CROSSBOW_LOADING_END));
    }

    public static record ChargingSounds(Optional<Holder<SoundEvent>> start, Optional<Holder<SoundEvent>> mid, Optional<Holder<SoundEvent>> end) {
        public static final Codec<CrossbowItem.ChargingSounds> CODEC = RecordCodecBuilder.create((p_345672_) -> p_345672_.group(SoundEvent.CODEC.optionalFieldOf("start").forGetter(CrossbowItem.ChargingSounds::start), SoundEvent.CODEC.optionalFieldOf("mid").forGetter(CrossbowItem.ChargingSounds::mid), SoundEvent.CODEC.optionalFieldOf("end").forGetter(CrossbowItem.ChargingSounds::end)).apply(p_345672_, CrossbowItem.ChargingSounds::new));

        public ChargingSounds(Optional<Holder<SoundEvent>> start, Optional<Holder<SoundEvent>> mid, Optional<Holder<SoundEvent>> end) {
            this.start = start;
            this.mid = mid;
            this.end = end;
        }

        public Optional<Holder<SoundEvent>> start() {
            return this.start;
        }

        public Optional<Holder<SoundEvent>> mid() {
            return this.mid;
        }

        public Optional<Holder<SoundEvent>> end() {
            return this.end;
        }
    }

    public static double getDrawSpeed(LivingEntity entity) {
        return entity.getAttributeValue(DrawSpeed.DRAW_SPEED);
    }
}
