package zimnycat.reznya.events;

import net.minecraft.network.Packet;
import zimnycat.reznya.base.EventBase;

public class ReadPacketEvent extends EventBase {
    private final Packet<?> packet;

    public ReadPacketEvent(Packet<?> packet) { this.packet = packet; }

    public Packet<?> getPacket() { return packet; }
}
