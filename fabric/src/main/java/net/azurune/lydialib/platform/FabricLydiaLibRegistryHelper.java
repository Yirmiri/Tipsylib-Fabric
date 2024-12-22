package net.azurune.lydialib.platform;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.azurune.lydialib.LydiaLib;
import net.azurune.lydialib.core.platform.services.LydiaLibRegistryHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricLydiaLibRegistryHelper implements LydiaLibRegistryHelper {

    @Override
    public Holder<Block> registerBlock(String modid, String id, Block block, boolean hasItem) {
        var blockRegister = Registry.registerForHolder(BuiltInRegistries.BLOCK, LydiaLib.customId(modid, id), block);
        if (hasItem) {
            Registry.register(BuiltInRegistries.ITEM, LydiaLib.customId(modid, id), new BlockItem(blockRegister.value(), new Item.Properties()));
        }
        return blockRegister;
    }

    @Override
    public Holder<Item> registerItem(String modid, String id, Item item) {
        return Registry.registerForHolder(BuiltInRegistries.ITEM, LydiaLib.customId(modid, id), item);
    }

    @Override
    public Holder<BlockEntityType<?>> registerBlockEntity(String modid, String id, BlockEntityType<?> blockEntity) {
        return Registry.registerForHolder(BuiltInRegistries.BLOCK_ENTITY_TYPE, LydiaLib.customId(modid, id), blockEntity);
    }

    @Override
    public Holder<EntityType<?>> registerEntityType(String modid, String id, EntityType<?> entityType) {
        return Registry.registerForHolder(BuiltInRegistries.ENTITY_TYPE, LydiaLib.customId(modid, id), entityType);
    }

    @Override
    public Holder<SoundEvent> registerSoundEvent(String modid, String id) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, LydiaLib.customId(modid, id), SoundEvent.createVariableRangeEvent(LydiaLib.customId(modid, id)));
    }

    @Override
    public Holder<MobEffect> registerEffect(String modid, String id, MobEffect mobEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, LydiaLib.customId(modid, id), mobEffect);
    }

    @Override
    public Holder<CreativeModeTab> registerCreativeModeTab(String modid, String id, CreativeModeTab tab) {
        return Registry.registerForHolder(BuiltInRegistries.CREATIVE_MODE_TAB, LydiaLib.customId(modid, id), tab);
    }

    @Override
    public Holder<Attribute> registerAttribute(String modid, String id, double base, double min, double max) {
        Attribute attribute = new RangedAttribute("attribute.name." + modid + "." + id, base, min, max).setSyncable(true);
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, LydiaLib.customId(modid, id), attribute);
    }
}