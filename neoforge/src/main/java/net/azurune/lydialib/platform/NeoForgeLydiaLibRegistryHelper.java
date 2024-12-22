package net.azurune.lydialib.platform;

import net.azurune.lydialib.LydiaLib;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.azurune.lydialib.core.platform.services.LydiaLibRegistryHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NeoForgeLydiaLibRegistryHelper implements LydiaLibRegistryHelper {
    //TODO - Allow custom MODIDS
    public static final DeferredRegister<Block> BLOCK = DeferredRegister.create(Registries.BLOCK, LydiaLib.MOD_ID);
    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(Registries.ITEM, LydiaLib.MOD_ID);
    public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(Registries.MOB_EFFECT, LydiaLib.MOD_ID);
    public static final DeferredRegister<Attribute> ATTRIBUTE = DeferredRegister.create(Registries.ATTRIBUTE, LydiaLib.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LydiaLib.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(Registries.ENTITY_TYPE, LydiaLib.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(Registries.SOUND_EVENT, LydiaLib.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LydiaLib.MOD_ID);

    @Override
    public Holder<Block> registerBlock(String modid, String id, Block block, boolean hasItem) {
        var blockRegister = BLOCK.register(id, () -> block);
        if (hasItem) {
            ITEM.register(id, () -> new BlockItem(blockRegister.get(), new Item.Properties()));
        }
        return blockRegister;
    }

    @Override
    public Holder<Item> registerItem(String modid, String id, Item item) {
        return ITEM.register(id, () -> item);
    }

    @Override
    public Holder<BlockEntityType<?>> registerBlockEntity(String modid, String id, BlockEntityType<?> blockEntity) {
        return BLOCK_ENTITY_TYPE.register(id, () -> blockEntity);
    }

    @Override
    public Holder<EntityType<?>> registerEntityType(String modid, String id, EntityType<?> entityType) {
        return ENTITY_TYPE.register(id, () -> entityType);
    }

    @Override
    public Holder<SoundEvent> registerSoundEvent(String modid, String id) {
        return SOUND_EVENT.register(id, () -> SoundEvent.createVariableRangeEvent(LydiaLib.customId(modid, id)));
    }

    @Override
    public Holder<MobEffect> registerEffect(String modid, String id, MobEffect mobEffect) {
        return MOB_EFFECT.register(id, () -> mobEffect);
    }

    @Override
    public Holder<CreativeModeTab> registerCreativeModeTab(String modid, String id, CreativeModeTab tab) {
        return CREATIVE_MODE_TAB.register(id, () -> tab);
    }

    @Override
    public Holder<Attribute> registerAttribute(String modid, String id, double base, double min, double max) {
        return ATTRIBUTE.register(id, () -> new RangedAttribute("attribute.name." + modid + "." + id, base, min, max).setSyncable(true));
    }
}
