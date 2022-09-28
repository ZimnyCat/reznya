package zimnycat.reznya.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zimnycat.reznya.Utilrun;
import zimnycat.reznya.events.TickEvent;

@Mixin(ClientPlayerEntity.class)
public class MClientPlayerEntity extends AbstractClientPlayerEntity {
    private static MinecraftClient mc = MinecraftClient.getInstance();

    public MClientPlayerEntity(ClientWorld world, GameProfile profile, PlayerPublicKey key) {
        super(world, profile, key);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        if (mc.player == null || mc.world == null) return;
        Utilrun.bus.post(new TickEvent());
    }
}
