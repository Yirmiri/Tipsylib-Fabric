package net.azurune.lydialib;

import net.azurune.lydialib.datagen.LLLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FabricLydiaLibDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.createPack().addProvider(LLLangProvider::new);
    }
}
