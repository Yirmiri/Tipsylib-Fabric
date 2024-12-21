package net.azurune.lydialib.core.platform;

import net.azurune.lydialib.LydiaLib;
import net.azurune.lydialib.core.platform.services.IPlatformHelper;
import net.azurune.lydialib.core.platform.services.LydiaLibRegistryHelper;

import java.util.ServiceLoader;

public class Services {
    public static final LydiaLibRegistryHelper REGISTRY = load(LydiaLibRegistryHelper.class);
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LydiaLib.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}