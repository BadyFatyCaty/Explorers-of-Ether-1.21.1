package net.mrbubbleman.explorersofether.client.keybindings;

import net.minecraft.client.Minecraft;
import net.mrbubbleman.explorersofether.ExplorersofEther;
import net.mrbubbleman.explorersofether.client.registry.ModKeyBindings;
import net.mrbubbleman.explorersofether.common.network.Networking;
import net.mrbubbleman.explorersofether.common.network.packets.PacketComboKeyPressed;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(
        value = {Dist.CLIENT},
        modid = ExplorersofEther.MOD_ID
)
public class KeyHandler {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    public static void checkComboKeyPressed(int key) {
        if (key == ModKeyBindings.COMBO_UP.getKey().getValue() && !ModKeyBindings.COMBO_UP.isUnbound() && ModKeyBindings.COMBO_UP.consumeClick()) {
            Networking.sendToServer(new PacketComboKeyPressed('U'));

        } else if (key == ModKeyBindings.COMBO_DOWN.getKey().getValue() && !ModKeyBindings.COMBO_DOWN.isUnbound() && ModKeyBindings.COMBO_DOWN.consumeClick()) {
            Networking.sendToServer(new PacketComboKeyPressed('D'));

        } else if (key == ModKeyBindings.COMBO_LEFT.getKey().getValue() && !ModKeyBindings.COMBO_LEFT.isUnbound() && ModKeyBindings.COMBO_LEFT.consumeClick()) {
            Networking.sendToServer(new PacketComboKeyPressed('L'));

        } else if (key == ModKeyBindings.COMBO_RIGHT.getKey().getValue() && !ModKeyBindings.COMBO_RIGHT.isUnbound() && ModKeyBindings.COMBO_RIGHT.consumeClick()) {
            Networking.sendToServer(new PacketComboKeyPressed('R'));

        }
    }

    @SubscribeEvent
    public static void mouseEvent(InputEvent.MouseButton.Post event) {
        if (MINECRAFT.player != null && event.getAction() == 1) {
        }
    }

    @SubscribeEvent
    public static void keyEvent(InputEvent.Key event) {
        if (MINECRAFT.player != null ) { // && event.getAction() == 1) {
            if (ModKeyBindings.COMBO_MODE.isDown()) {
                checkComboKeyPressed(event.getKey());
                // ExplorersofEther.LOGGER.info("Combo key pressed: " + event.getKey());

            }
            // ExplorersofEther.LOGGER.info("key pressed: " + event.getKey());
        }
    }

    /*
    public static void sendHotkeyPacket(PacketHotkeyPressed.Key key) {
        // Networking.sendToServer(new PacketHotkeyPressed(key));
    }
     */
}
