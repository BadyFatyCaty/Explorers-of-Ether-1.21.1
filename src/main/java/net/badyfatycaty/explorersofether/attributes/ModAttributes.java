package net.badyfatycaty.explorersofether.attributes;

import net.badyfatycaty.explorersofether.attributes.crit.CritDamage;
import net.badyfatycaty.explorersofether.attributes.crit.CritRate;
import net.badyfatycaty.explorersofether.attributes.element.dark.DarkDamage;
import net.badyfatycaty.explorersofether.attributes.element.dark.DarkResistance;
import net.badyfatycaty.explorersofether.attributes.element.earth.EarthDamage;
import net.badyfatycaty.explorersofether.attributes.element.earth.EarthResistance;
import net.badyfatycaty.explorersofether.attributes.element.fire.FireDamage;
import net.badyfatycaty.explorersofether.attributes.element.fire.FireResistance;
import net.badyfatycaty.explorersofether.attributes.element.light.LightDamage;
import net.badyfatycaty.explorersofether.attributes.element.light.LightResistance;
import net.badyfatycaty.explorersofether.attributes.element.plant.PlantDamage;
import net.badyfatycaty.explorersofether.attributes.element.plant.PlantResistance;
import net.badyfatycaty.explorersofether.attributes.element.water.WaterDamage;
import net.badyfatycaty.explorersofether.attributes.element.water.WaterResistance;
import net.badyfatycaty.explorersofether.attributes.element.wind.WindDamage;
import net.badyfatycaty.explorersofether.attributes.element.wind.WindResistance;
import net.badyfatycaty.explorersofether.attributes.magic.MagicResistance;
import net.badyfatycaty.explorersofether.attributes.mana.Mana;
import net.badyfatycaty.explorersofether.attributes.mana.ManaRegen;
import net.badyfatycaty.explorersofether.attributes.mana.MaxMana;
import net.badyfatycaty.explorersofether.attributes.ranged.DrawSpeed;
import net.badyfatycaty.explorersofether.attributes.ranged.ProjectileDamage;
import net.badyfatycaty.explorersofether.attributes.ranged.ProjectileResistance;
import net.badyfatycaty.explorersofether.attributes.spirit.SpiritAttackSpeed;
import net.badyfatycaty.explorersofether.attributes.spirit.SpiritDamage;
import net.badyfatycaty.explorersofether.attributes.spirit.SpiritHealth;
import net.badyfatycaty.explorersofether.attributes.spirit.SpiritMovementSpeed;
import net.badyfatycaty.explorersofether.attributes.stamina.MaxStamina;
import net.badyfatycaty.explorersofether.attributes.stamina.Stamina;
import net.badyfatycaty.explorersofether.attributes.stamina.StaminaRegen;
import net.neoforged.bus.api.IEventBus;

public class ModAttributes {
    public static void registerAllAttributes(IEventBus modEventBus) {
        CritDamage.REGISTRY.register(modEventBus);
        CritRate.REGISTRY.register(modEventBus);

        DarkDamage.REGISTRY.register(modEventBus);
        DarkResistance.REGISTRY.register(modEventBus);
        EarthDamage.REGISTRY.register(modEventBus);
        EarthResistance.REGISTRY.register(modEventBus);
        FireDamage.REGISTRY.register(modEventBus);
        FireResistance.REGISTRY.register(modEventBus);
        LightDamage.REGISTRY.register(modEventBus);
        LightResistance.REGISTRY.register(modEventBus);
        PlantDamage.REGISTRY.register(modEventBus);
        PlantResistance.REGISTRY.register(modEventBus);
        WaterDamage.REGISTRY.register(modEventBus);
        WaterResistance.REGISTRY.register(modEventBus);
        WindDamage.REGISTRY.register(modEventBus);
        WindResistance.REGISTRY.register(modEventBus);

        MagicResistance.REGISTRY.register(modEventBus);

        Mana.REGISTRY.register(modEventBus);
        MaxMana.REGISTRY.register(modEventBus);
        ManaRegen.REGISTRY.register(modEventBus);

        DrawSpeed.REGISTRY.register(modEventBus);
        ProjectileDamage.REGISTRY.register(modEventBus);
        ProjectileResistance.REGISTRY.register(modEventBus);

        SpiritAttackSpeed.REGISTRY.register(modEventBus);
        SpiritDamage.REGISTRY.register(modEventBus);
        SpiritHealth.REGISTRY.register(modEventBus);
        SpiritMovementSpeed.REGISTRY.register(modEventBus);

        Stamina.REGISTRY.register(modEventBus);
        MaxStamina.REGISTRY.register(modEventBus);
        StaminaRegen.REGISTRY.register(modEventBus);

        HealthRegen.REGISTRY.register(modEventBus);
    }

}
