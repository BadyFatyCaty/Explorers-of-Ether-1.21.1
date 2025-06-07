package net.badyfatycaty.explorersofether.events;

import net.badyfatycaty.explorersofether.items.custom.weapon_types.ModBowItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ModBowEvents {
    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack held = player.getMainHandItem();

        if (!(held.getItem() instanceof ModBowItem bow)) return;

        if (!player.level().isClientSide()) {
            bow.tryLeftClickShoot(player.level(), player, held);

            double attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED).getValue();
            int cooldownTicks = (int) Math.round(20.0 / attackSpeed);
            player.getCooldowns().addCooldown(held.getItem(), cooldownTicks);
        }
    }
}
