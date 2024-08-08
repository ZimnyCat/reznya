package zimnycat.reznya.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zimnycat.reznya.Utilrun;
import zimnycat.reznya.events.EntityStatusEvent;
import zimnycat.reznya.events.ReadPacketEvent;
import zimnycat.reznya.events.SendPacketEvent;

@Mixin(ClientConnection.class)
public class MClientConnection {
    private static MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, CallbackInfo ci) {
        SendPacketEvent event = new SendPacketEvent(packet);
        Utilrun.bus.post(event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext channelHandlerContext_1, Packet<?> packet_1, CallbackInfo ci) {
        ReadPacketEvent event = new ReadPacketEvent(packet_1);
        Utilrun.bus.post(event);
        if (event.isCancelled()) ci.cancel();

        if (mc.world != null && packet_1 instanceof EntityStatusS2CPacket) {
            EntityStatusEvent statusEvent = new EntityStatusEvent(
                    ((EntityStatusS2CPacket) packet_1).getStatus(), ((EntityStatusS2CPacket) packet_1).getEntity(mc.world)
            );
            Utilrun.bus.post(statusEvent);
        }
    }
}
