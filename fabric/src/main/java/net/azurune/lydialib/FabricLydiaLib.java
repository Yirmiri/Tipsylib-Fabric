package net.azurune.lydialib;

import net.fabricmc.api.ModInitializer;

public class FabricLydiaLib implements ModInitializer {
    
    @Override
    public void onInitialize() {
        LydiaLib.init();
    }
}
