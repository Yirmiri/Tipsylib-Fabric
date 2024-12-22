package net.azurune.lydialib.core.platform.services;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface LydiaLibRegistryHelper {
    Holder<Block> registerBlock(String modid, String id, Block block, boolean hasItem);

    Holder<Item> registerItem(String modid, String id, Item item);

    Holder<BlockEntityType<?>> registerBlockEntity(String modid, String id, BlockEntityType<?> blockEntity);

    Holder<EntityType<?>> registerEntityType(String modid, String id, EntityType<?> entityType);

    Holder<SoundEvent> registerSoundEvent(String modid, String id);

    Holder<MobEffect> registerEffect(String modid, String id, MobEffect mobEffect);

    Holder<CreativeModeTab> registerCreativeModeTab(String modid, String id, CreativeModeTab tab);

    Holder<Attribute> registerAttribute(String modid, String id, double base, double min, double max);
}