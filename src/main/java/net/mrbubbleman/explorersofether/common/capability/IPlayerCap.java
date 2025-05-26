package net.mrbubbleman.explorersofether.common.capability;

import java.util.ArrayList;

public interface IPlayerCap {
    ArrayList<Character> getCurrentCombo();

    void addToCurrentCombo(char var1);
}
