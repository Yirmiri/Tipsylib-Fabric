package net.azurune.lydialib.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class LLLangProvider extends FabricLanguageProvider {
    public LLLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder build) {
        //MISC
        build.add("effect.lydialib.no_spawn_point", "Cannot find a valid spawn location to teleport to.");
        build.add("effect.lydialib.no_death_point", "Cannot find a valid death location to teleport to.");

        //EFFECTS
        build.add("effect.lydialib.water_walking", "Water Walking");
        build.add("effect.lydialib.water_walking.description", "Allows the user to stand on water.");

        build.add("effect.lydialib.lava_walking", "Lava Walking");
        build.add("effect.lydialib.lava_walking.description", "Allows the user to stand on lava.");

        build.add("effect.lydialib.perception", "Perception");
        build.add("effect.lydialib.perception.description", "Grants glowing to all entities nearby the user.");

        build.add("effect.lydialib.berserk", "Berserk");
        build.add("effect.lydialib.berserk.description", "Increases the attack damage of the user the lower their current health points are.");

        build.add("effect.lydialib.traversal", "Traversal");
        build.add("effect.lydialib.traversal.description", "Teleports the user to their spawn point if they have one set.");

        build.add("effect.lydialib.ferrymans_blessing", "Ferryman's Blessing");
        build.add("effect.lydialib.ferrymans_blessing.description", "Teleports the user to their last death location if they have one.");

        build.add("effect.lydialib.magnetism", "Magnetism");
        build.add("effect.lydialib.magnetism.description", "Pulls items in towards the user.");

        build.add("effect.lydialib.repulsion", "Repulsion");
        build.add("effect.lydialib.repulsion.description", "Pushes items away from the user.");

        build.add("effect.lydialib.heartbreak", "Heartbreak");
        build.add("effect.lydialib.heartbreak.description", "Decreases the maximum amount of health points of the user.");
    }
}
