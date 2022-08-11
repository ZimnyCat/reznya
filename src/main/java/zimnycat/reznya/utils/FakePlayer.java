package zimnycat.reznya.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.base.settings.SettingBool;
import zimnycat.reznya.base.settings.SettingString;

import java.util.UUID;

public final class FakePlayer extends UtilBase {
    private int id = -1;

    public FakePlayer() {
        super("FakePlayer", "Spawns client-sided player in your world",
                new SettingString("name", "popbob"),
                new SettingBool("copyInventory", false)
        );
    }

    @Override
    public void onEnable() {
        if (mc.world == null || mc.player == null) {
            this.setEnabled(false);
            return;
        }
        Entity entity = new OtherClientPlayerEntity(mc.world,
                new GameProfile(UUID.randomUUID(), setting("name").string().value),
                mc.player.getPublicKey()) {{
                    this.setHealth(20);
                    this.setPosition(mc.player.getPos());
                    this.setYaw(mc.player.getYaw());
                    this.setPitch(mc.player.getPitch());
                    this.bodyYaw = mc.player.bodyYaw;
                    this.prevBodyYaw = this.bodyYaw;
                    this.headYaw = mc.player.headYaw;
                    this.prevHeadYaw = this.headYaw;
                    this.setPose(mc.player.getPose());
                    if (setting("copyInventory").bool().value) {
                        this.getInventory().clone(mc.player.getInventory());
                    }
        }};

        mc.world.addEntity(entity.getId(), entity);
        this.id = entity.getId();
    }

    @Override
    public void onDisable() {
        if (mc.world == null || mc.player == null) return;
        mc.world.removeEntity(id, Entity.RemovalReason.DISCARDED);
        this.id = -1;
    }
}
