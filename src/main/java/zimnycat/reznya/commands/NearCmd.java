package zimnycat.reznya.commands;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import zimnycat.reznya.base.CommandBase;
import zimnycat.reznya.base.Utilrun;

import java.util.Comparator;
import java.util.List;

public class NearCmd extends CommandBase {
    public NearCmd() { super("near", "Info about nearby players"); }

    public void run(String[] args) {
        List<AbstractClientPlayerEntity> players = mc.world.getPlayers()
                .stream().filter(player -> !player.getDisplayName().equals(mc.player.getDisplayName())).toList()
                .stream().sorted(Comparator.comparingDouble(player -> mc.player.distanceTo(player))).toList();
        int num = 1;

        try {
            int parsed = Integer.parseInt(args[0]);
            if (parsed > 50) {
                clientMessage("The number is too high!");
                return;
            }
            num = parsed;
        } catch (Exception e) {}

        for (int element = 0; element < num; element++) {
            if (players.size() == element) break;

            AbstractClientPlayerEntity player = players.get(element);
            clientMessage(player.getDisplayName().getString() + Utilrun.highlight(" - ")
                    + player.getHealth() + Utilrun.highlight(" - ")
                    + mc.player.networkHandler.getPlayerListEntry(player.getUuid()).getLatency());
        }
    }
}
