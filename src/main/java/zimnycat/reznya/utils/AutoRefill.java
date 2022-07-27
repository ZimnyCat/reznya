package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.events.TickEvent;
import zimnycat.reznya.libs.Finder;

import java.util.HashMap;
import java.util.Map;

public class AutoRefill extends UtilBase {
    HashMap<Integer, Item> hotbar = new HashMap<>();

    public AutoRefill() {
        super("AutoRefill", "Refills items in hotbar");
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (!hotbar.isEmpty()) {
            for (Map.Entry map : hotbar.entrySet()) {
                int slot = (int) map.getKey();
                Item item = (Item) map.getValue();

                if (mc.player.getInventory().getStack(slot).isEmpty() && !item.equals(Items.AIR) && mc.currentScreen == null) {
                    Integer neww = Finder.find(item, false);
                    if (neww != null) {
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, neww < 9 ? (neww + 36) : (neww), 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot + 36, 0, SlotActionType.PICKUP, mc.player);
                        break;
                    }
                }
            }
        }

        for (int slot = 0; slot < 9; slot++) hotbar.put(slot, mc.player.getInventory().getStack(slot).getItem());
    }
}
