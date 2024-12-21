package net.azurune.lydialib.platform;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.azurune.lydialib.LydiaLib;
import net.azurune.lydialib.core.platform.services.LydiaLibRegistryHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class FabricLydiaLibRegistryHelper implements LydiaLibRegistryHelper {


    @Override
    public Holder<MobEffect> registerEffect(String id, MobEffect mobEffect) {
        var mobEffectReference = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, LydiaLib.id(id), mobEffect);
        return mobEffectReference;
    }

    @Override
    public Holder<MobEffect> registerVanillaEffect(String id, MobEffect mobEffect) {
        var mobEffectReference = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, LydiaLib.vanillaId(id), mobEffect);
        return mobEffectReference;
    }

    @Override
    public Holder<Attribute> registerAttribute(String id, double base, double min, double max) {
        Attribute attribute = new RangedAttribute("attribute.name." + LydiaLib.MOD_ID + "." + id, base, min, max).setSyncable(true);
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(LydiaLib.MOD_ID, id), attribute);
    }
}
