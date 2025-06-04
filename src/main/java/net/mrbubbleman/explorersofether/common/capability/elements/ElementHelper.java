package net.mrbubbleman.explorersofether.common.capability.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ElementHelper {
    public static final ArrayList<String> basicElementNames = new ArrayList<>(List.of("fire", "water", "wind", "earth", "light", "plant"));

    public static final ArrayList<String> deviantElementNames = new ArrayList<>(List.of("lightning", "ice", "sound", "gravity", "dark", "fungi"));
    public static final ArrayList<String> specialElementNames = new ArrayList<>(List.of("portals"));

    public static final HashMap<String, String> basicToDeviantMap = new HashMap<>(Map.ofEntries(
            Map.entry("fire", "lightning"),
            Map.entry("water", "ice"),
            Map.entry("wind", "sound"),
            Map.entry("earth", "gravity"),
            Map.entry("light", "dark"),
            Map.entry("plant", "fungi")
    ));
}
