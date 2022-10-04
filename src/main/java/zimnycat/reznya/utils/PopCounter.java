package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import zimnycat.reznya.Utilrun;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.base.settings.SettingBool;
import zimnycat.reznya.events.EntityStatusEvent;

import java.util.HashMap;

public class PopCounter extends UtilBase {
    HashMap<String, Integer> pops = new HashMap<>();

    public PopCounter() {
        super(
                "PopCounter", "Counts totem pops",
                new SettingBool("resetOnDeath", true),
                new SettingBool("countOwn", true)
        );
    }

    @Subscribe
    public void pop(EntityStatusEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof PlayerEntity) || (!setting("countOwn").bool().value && entity.equals(mc.player))) return;

        String name = entity.getDisplayName().getString();

        if (event.getStatus() == 35) {
            if (pops.containsKey(name)) {
                int num = pops.get(name) + 1;
                pops.replace(name, num);
                clientMessage(Utilrun.highlight(name) + " popped " + Utilrun.highlight(String.valueOf(num)) + " totems");
            } else {
                pops.put(name, 1);
                clientMessage(Utilrun.highlight(name) + " popped " + Utilrun.highlight("1") + " totem");
            }
        } else if (event.getStatus() == 3 && setting("resetOnDeath").bool().value && pops.containsKey(name)) {
            clientMessage(Utilrun.highlight(name) + " died after popping " + Utilrun.highlight(String.valueOf(pops.get(name))) + " totems");
            pops.remove(name);
            clientMessage("Pop counter reset for " + Utilrun.highlight(name));
        }
    }
}
