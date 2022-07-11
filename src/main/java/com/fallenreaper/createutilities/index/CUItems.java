package com.fallenreaper.createutilities.index;


import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.items.NotesItem;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class CUItems {
    private static final CreateRegistrate REGISTRATE = CreateUtilities.registrate().creativeModeTab(() -> CreateUtilities.TAB);

    public static final ItemEntry<PunchcardItem> PUNCHCARD =
            REGISTRATE.item("punchcard", PunchcardItem::new)
                    .properties(p -> p.stacksTo(1).durability(64).setNoRepair())
                    .register();
    public static final ItemEntry<Item> WAX =
            REGISTRATE.item("red_wax", Item::new)
                    .register();
    public static final ItemEntry<NotesItem> NOTE_ITEM =
            REGISTRATE.item("note_item", NotesItem::new)
                    .properties(p -> p.stacksTo(1))
                    .register();
    public static void register() {
    }
}
