package net.azurune.lydialib;

import net.azurune.lydialib.platform.NeoForgeLydiaLibRegistryHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(LydiaLib.MOD_ID)
public class NeoForgeLydiaLib {
    
    public NeoForgeLydiaLib(IEventBus eventBus) {
        LydiaLib.init();

        NeoForgeLydiaLibRegistryHelper.BLOCK.register(eventBus);
        NeoForgeLydiaLibRegistryHelper.ITEM.register(eventBus);
        NeoForgeLydiaLibRegistryHelper.MOB_EFFECT.register(eventBus);
        NeoForgeLydiaLibRegistryHelper.ATTRIBUTE.register(eventBus);
        NeoForgeLydiaLibRegistryHelper.BLOCK_ENTITY_TYPE.register(eventBus);
        NeoForgeLydiaLibRegistryHelper.ENTITY_TYPE.register(eventBus);
        NeoForgeLydiaLibRegistryHelper.SOUND_EVENT.register(eventBus);
        NeoForgeLydiaLibRegistryHelper.CREATIVE_MODE_TAB.register(eventBus);
    }
}