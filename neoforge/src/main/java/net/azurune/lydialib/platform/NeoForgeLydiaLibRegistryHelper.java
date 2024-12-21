package net.azurune.lydialib.platform;

import net.azurune.lydialib.LydiaLib;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.azurune.lydialib.core.platform.services.LydiaLibRegistryHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NeoForgeLydiaLibRegistryHelper implements LydiaLibRegistryHelper {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, LydiaLib.MOD_ID);

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, LydiaLib.MOD_ID);

    @Override
    public Holder<MobEffect> registerEffect(String id, MobEffect mobEffect) {
        return MOB_EFFECTS.register(id, () -> mobEffect);
    }

    @Override
    public Holder<MobEffect> registerVanillaEffect(String id, MobEffect mobEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.withDefaultNamespace(id), mobEffect);
    }

    @Override
    public Holder<Attribute> registerAttribute(String id, double base, double min, double max) {
        return ATTRIBUTES.register(id, () -> new RangedAttribute("attribute.name." + LydiaLib.MOD_ID + "." + id, base, min, max).setSyncable(true));
    }
}
