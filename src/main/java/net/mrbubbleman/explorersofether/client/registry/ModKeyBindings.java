//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.mrbubbleman.explorersofether.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.mrbubbleman.explorersofether.ExplorersofEther;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(
        modid = ExplorersofEther.MOD_ID,
        value = {Dist.CLIENT},
        bus = Bus.MOD
)
public class ModKeyBindings {
    public static final String CATEGORY = "key.category.explorers_of_ether.general";
    public static final KeyMapping COMBO_MODE = new KeyMapping("key.explorers_of_ether.combo_mode",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, CATEGORY);

    public static final KeyMapping COMBO_UP = new KeyMapping("key.explorers_of_ether.combo_up",
                    87, CATEGORY);
            // InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_W, CATEGORY);
    public static final KeyMapping COMBO_DOWN = new KeyMapping("key.explorers_of_ether.combo_down",
                    83, CATEGORY);
                    // InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_S, CATEGORY);
    public static final KeyMapping COMBO_LEFT = new KeyMapping("key.explorers_of_ether.combo_left",
                            65, CATEGORY);
                            // InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_A, CATEGORY);
    public static final KeyMapping COMBO_RIGHT = new KeyMapping("key.explorers_of_ether.combo_right",
                                    68, CATEGORY);
                                    // InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_D, CATEGORY);

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(COMBO_MODE);
        event.register(COMBO_UP);
        event.register(COMBO_DOWN);
        event.register(COMBO_LEFT);
        event.register(COMBO_RIGHT);
    }
}
