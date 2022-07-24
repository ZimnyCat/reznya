package zimnycat.reznya.mixin;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zimnycat.reznya.base.Utilrun;
import zimnycat.reznya.events.ReadPacketEvent;
import zimnycat.reznya.events.SendPacketEvent;

@Mixin(ClientConnection.class)
public class MClientConnection {
    @Inject(method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> packetCallback, CallbackInfo ci) {
        SendPacketEvent event = new SendPacketEvent(packet);
        Utilrun.bus.post(event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext channelHandlerContext_1, Packet<?> packet_1, CallbackInfo ci) {
        ReadPacketEvent event = new ReadPacketEvent(packet_1);
        Utilrun.bus.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}
