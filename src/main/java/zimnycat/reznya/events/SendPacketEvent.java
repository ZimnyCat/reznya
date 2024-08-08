package zimnycat.reznya.events;

import net.minecraft.network.packet.Packet;
import zimnycat.reznya.base.EventBase;

public class SendPacketEvent extends EventBase {
    private final Packet<?> packet;

    public SendPacketEvent(Packet<?> packet) { this.packet = packet; }

    public Packet<?> getPacket() { return packet; }
}
