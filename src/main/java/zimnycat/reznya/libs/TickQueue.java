package zimnycat.reznya.libs;

import com.google.common.eventbus.Subscribe;
import zimnycat.reznya.base.Manager;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.events.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TickQueue {
    private static int current = 0;

    private final List<String> u = Arrays.asList(
            "AutoBed",
            "HoleTrap",
            "KillAura",
            "SelfTrap"
    );

    private static List<UtilBase> queue = new ArrayList<>();

    @Subscribe
    public void onTick(TickEvent event) {
        queue = Manager.utils.stream().filter(util -> util.isEnabled() && u.contains(util.getName())).toList();

        if (current + 1 >= queue.size()) current = 0;
        else current++;
    }

    public static String getCurrentUtil() { return queue.get(current).getName(); }
}
