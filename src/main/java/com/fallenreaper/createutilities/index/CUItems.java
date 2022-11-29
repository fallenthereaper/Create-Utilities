package com.fallenreaper.createutilities.index;


import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.armor.BrassJetPackItem;
import com.fallenreaper.createutilities.content.armor.BrassSuitArmorItem;
import com.fallenreaper.createutilities.content.items.*;
import com.fallenreaper.createutilities.content.items.watering_can.WateringCanItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import com.simibubi.create.repack.registrate.util.nullness.NonNullFunction;
import com.simibubi.create.repack.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CUItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreateUtilities.ID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CreateUtilities.ID);
    private static final CreateRegistrate REGISTRATE = CreateUtilities.registrate().creativeModeTab(() -> CreateUtilities.TAB);

    public static final ItemEntry<? extends Item> PUNCHCARD = registerItem("punchcard", "Punchcard", (p) -> new PunchcardItem(p.tab(CreateUtilities.TAB))
            , (p) -> p.stacksTo(1));
    public static final ItemEntry<? extends Item> WAX =
            registerItem("red_wax", "Wax", (p) -> new BaseItem(p.tab(CreateUtilities.TAB))
                  , (p) -> p.stacksTo(64));

    public static final ItemEntry<? extends Item> NOTE_ITEM =
            registerItem("note_item", "Notes", NotesItem::new,
                    (p) -> p.stacksTo(1));

    public static final ItemEntry<? extends Item> WATERING_CAN =
            registerItem("watering_can", "Watering Can", (p) -> new WateringCanItem(p.tab(CreateUtilities.TAB)),
                    (p) -> p.stacksTo(1));

    public static final ItemEntry<? extends Item> DEV_ITEM =
            registerItem("dev_item", "Dev Tool", (p) -> new DevItem(p.tab(CreateUtilities.TAB)).addDescription((ChatFormatting.ITALIC), ChatFormatting.DARK_PURPLE),
                    (p) -> p.stacksTo(1));


    public static final ItemEntry<? extends BrassSuitArmorItem> BRASS_JETPACK =
            REGISTRATE.item("brass_jetpack", BrassJetPackItem::new)
                    .register();

  /*  RegistryObject<? extends Item> DUMMY_ITEM =
            registerItemDefault("dummy_item", () -> new BaseItem(new Item.Properties())
            .addDescription("ballin' in the streets")
            .setDescriptionColor(ChatFormatting.YELLOW));
            */

    private static <T extends Item> RegistryObject<T> registerItemDefault(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }


    public static ItemEntry<? extends Item> registerItem(String name, String lang, NonNullFunction<Item.Properties, ? extends Item> itemSup, NonNullUnaryOperator<Item.Properties> prop) {
        return REGISTRATE.item(name, itemSup)
                .properties(prop)
                .lang(lang)
                .register();
    }

    public static void register() {
    }
}
