package net.azurune.lydialib;


import net.azurune.lydialib.core.registry.LLAttributes;
import net.minecraft.resources.ResourceLocation;
import net.azurune.lydialib.core.registry.LLEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LydiaLib {
    public static final String MOD_ID = "lydialib";
    public static final String MOD_NAME = "LydiaLib";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        LLEffects.loadEffects();
        LLAttributes.loadAttributes();
    }

    public static ResourceLocation id(String id) {
        return ResourceLocation.fromNamespaceAndPath(LydiaLib.MOD_ID, id);
    }

    public static ResourceLocation vanillaId(String id) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", id);
    }
}