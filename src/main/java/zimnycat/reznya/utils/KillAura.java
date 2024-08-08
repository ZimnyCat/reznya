package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.base.settings.SettingBool;
import zimnycat.reznya.base.settings.SettingNum;
import zimnycat.reznya.events.TickEvent;

import java.util.Comparator;

public class KillAura extends UtilBase {
    public KillAura() {
        super(
                "KillAura", "Attacks nearby players",
                new SettingNum("range", 5, 1, 10),
                new SettingBool("weaponOnly", false),
                new SettingBool("disableOnDeath", true)
        );
    }

    @Subscribe
    public void onTick(TickEvent e) {
        if (setting("disableOnDeath").bool().value && mc.player.isDead()) {
            toggle();
            return;
        }

        if (mc.player.getAttackCooldownProgress(0.5f) != 1.0f) return;

        Item mainHand = mc.player.getMainHandStack().getItem();
        if (setting("weaponOnly").bool().value && !(mainHand instanceof SwordItem) && !(mainHand instanceof AxeItem)) return;

        AbstractClientPlayerEntity player = mc.world.getPlayers().stream()
                .filter(p -> p.isAlive() && mc.player.distanceTo(p) <= setting("range").num().value && p != mc.player)
                .toList().stream().sorted(Comparator.comparingDouble(p -> mc.player.distanceTo(p)))
                .toList().get(0);

        boolean sprinting = mc.player.isSprinting();
        if (sprinting) mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));

        mc.interactionManager.attackEntity(mc.player, player);
        mc.player.swingHand(Hand.MAIN_HAND);

        if (sprinting) mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
    }
}
