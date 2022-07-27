package zimnycat.reznya.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zimnycat.reznya.Utilrun;
import zimnycat.reznya.events.ClientStopEvent;

@Mixin(MinecraftClient.class)
public class MMinecraftClient {
    @Inject(method = "stop", at = @At("HEAD"))
    public void stop(CallbackInfo ci) {
        Utilrun.bus.post(new ClientStopEvent());
    }
}
