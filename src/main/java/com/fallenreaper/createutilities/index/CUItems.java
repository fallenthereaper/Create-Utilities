package com.fallenreaper.createutilities.index;


import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.items.NoteItem;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.grouptabs.CUItemTab;
import com.simibubi.create.content.contraptions.relays.belt.item.BeltConnectorItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class CUItems {
    private static final CreateRegistrate REGISTRATE = CreateUtilities.registrate().creativeModeTab(() -> CUItemTab.MAIN_GROUP);


    public static final ItemEntry<PunchcardItem> PUNCHCARD =
            REGISTRATE.item("punchcard", PunchcardItem::new)
                    .properties(p -> p.stacksTo(1).durability(100).setNoRepair())
                    .lang("Punchcard")
                    .register();
    public static final ItemEntry<Item> WAX =
            REGISTRATE.item("red_wax", Item::new)
                    .lang("red_wax")
                    .register();
    public static final ItemEntry<NoteItem> NOTE_ITEM =
            REGISTRATE.item("note_item", NoteItem::new)
                    .properties(p -> p.stacksTo(1))
                    .lang("Note Item")
                    .register();
    public static void register() {
    }
}
