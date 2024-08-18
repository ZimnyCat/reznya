package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.events.TickEvent;

public final class NoFall extends UtilBase {
    public NoFall() {
        super("NoFall", "Prevents you from getting fall damage");
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (!mc.player.isOnGround() && mc.player.fallDistance > 3) {
            PlayerMoveC2SPacket.Full packet = new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY() + 1e-9, mc.player.getZ(),
                    mc.player.getYaw(), mc.player.getPitch(), false);
            mc.player.networkHandler.sendPacket(packet);
        }
    }
}
