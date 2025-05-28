package net.mrbubbleman.explorersofether.common.capability.playercaps;

import dev.kosmx.playerAnim.api.IPlayer;
import net.mrbubbleman.explorersofether.common.elements.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public interface IPlayerCap {
    ArrayList<Character> getCombo();
    ArrayList<Character> addToCombo(char var1);
    void setCombo(ArrayList<Character> var1);

    int getSpellCharge();
    int addSpellCharge(int var1);
    void setSpellCharge(int var1);

    void addAdvancementsFromElements();

    Elements getElement(String var1);

    Elements getDeviant(String var1);
    Elements getDeviant(Elements var1);

    HashMap<String, Elements> getPlayerElements();
    void setPlayerElements(HashMap<String, Elements> map);
    void setElement(String var1, Elements var2);

    void copyFrom(IPlayerCap var1);
}
