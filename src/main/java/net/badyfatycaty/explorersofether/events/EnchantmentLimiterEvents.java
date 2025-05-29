package net.badyfatycaty.explorersofether.events;

import net.badyfatycaty.explorersofether.Config;
import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.badyfatycaty.explorersofether.components.EnchantmentLimitComponent;
import net.badyfatycaty.explorersofether.components.ModDataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@EventBusSubscriber(modid = ExplorersofEther.MODID, bus = EventBusSubscriber.Bus.GAME, value = {Dist.CLIENT})
public class EnchantmentLimiterEvents {
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        int baseMax = Config.baseMaxEnchantments;
        int extra = left.getOrDefault(ModDataComponents.ENCHANT_LIMIT.get(), new EnchantmentLimitComponent(0)).getExtraSlots();
        int maxEnchantments = baseMax + extra;

        var leftEnchants = net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(left);
        var rightEnchants = net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(right);
        int total = leftEnchants.entrySet().size() + rightEnchants.entrySet().size();
        if (total > maxEnchantments) {
            event.setCanceled(true);
        }
    }
}