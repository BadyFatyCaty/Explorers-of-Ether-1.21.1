package net.mrbubbleman.explorersofether.common.capability.elementclasses;

public class ElementData {
    public String name;
    public int experience;
    ElementData deviant;

    public ElementData(String name) {
        this.name = name;
        this.experience = 0;
        this.deviant = null;
    }

    public ElementData(String name, int experience) {
        this.name = name;
        this.experience = experience;
        this.deviant = null;
    }

    public ElementData(String name, int experience, ElementData deviant) {
        this.name = name;
        this.experience = experience;
        this.deviant = deviant;
    }

    public ElementData copyDataFrom(ElementData other) {
        this.name = other.name;
        this.experience = other.experience;

        if (other.deviant != null) {
            this.deviant.copyDataFrom(other.deviant);
        } else {
            this.deviant = null;
        }

        return this;
    }
}
