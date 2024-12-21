package net.azurune.lydialib;

import net.azurune.lydialib.platform.NeoForgeLydiaLibRegistryHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(LydiaLib.MOD_ID)
public class NeoForgeLydiaLib {
    
    public NeoForgeLydiaLib(IEventBus eventBus) {
        LydiaLib.init();
        NeoForgeLydiaLibRegistryHelper.MOB_EFFECTS.register(eventBus);
        NeoForgeLydiaLibRegistryHelper.ATTRIBUTES.register(eventBus);
    }
}