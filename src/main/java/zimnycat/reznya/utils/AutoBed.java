package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.base.settings.SettingBool;
import zimnycat.reznya.base.settings.SettingNum;
import zimnycat.reznya.events.TickEvent;
import zimnycat.reznya.libs.Delay;
import zimnycat.reznya.libs.TickQueue;
import zimnycat.reznya.libs.WorldLib;

import java.util.HashMap;
import java.util.Map;

public class AutoBed extends UtilBase {
    Delay delay = new Delay();
    BlockPos lastBed = null;
    Integer prevSlot = null;

    public AutoBed() {
        super(
                "AutoBed", "Places beds to kill players",
                new SettingNum("delay", 10, 0, 100),
                new SettingNum("range", 5, 1, 8),
                new SettingNum("maxHealth", 34, 5, 34),
                new SettingBool("autoSwitch", false)
        );
    }

    @Subscribe
    public void onTick(TickEvent e) {
        if (!TickQueue.getCurrentUtil().equalsIgnoreCase(getName())) return;

        delay.setDelay((long) setting("delay").num().value);
        if (mc.world.getRegistryKey().getValue().getPath().contains("overworld") || !delay.check()) return;

        if (lastBed != null) {
            if (Math.sqrt(lastBed.getSquaredDistance(mc.player.getPos())) > setting("range").num().value
                    || !(mc.world.getBlockState(lastBed).getBlock().asItem() instanceof BedItem)) lastBed = null;
            else {
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,
                        new BlockHitResult(new Vec3d(lastBed.getX(), lastBed.getY(), lastBed.getZ()), Direction.UP, lastBed, true)
                );
                return;
            }
        }

        for (PlayerEntity p : mc.world.getPlayers()) {
            if (mc.player.isSneaking() || p == mc.player || p.getBlockPos().equals(mc.player.getBlockPos()) || p.isDead()
                    || mc.player.distanceTo(p) > 8 || (p.getHealth() + p.getAbsorptionAmount()) > setting("maxHealth").num().value) continue;

            BlockPos up = p.getBlockPos().up();
            if (!mc.world.getBlockState(up).isAir() || (!mc.world.getBlockState(p.getBlockPos()).isAir()
                    && !mc.world.getBlockState(p.getBlockPos()).getBlock().equals(Blocks.FIRE))) continue;

            HashMap<BlockPos, Float> poses = new HashMap<>();
            poses.put(up.north(), 0f);
            poses.put(up.east(), 90f);
            poses.put(up.south(), 180f);
            poses.put(up.west(), -90f);

            for (Map.Entry pos : poses.entrySet()) {
                BlockPos pos2 = (BlockPos) pos.getKey();
                if (Math.sqrt(pos2.getSquaredDistance(mc.player.getPos())) > setting("range").num().value) continue;

                if (mc.world.getBlockState(pos2).isAir() || mc.world.getBlockState(pos2).getBlock().equals(Blocks.FIRE)) {
                    if (!(mc.player.getMainHandStack().getItem() instanceof BedItem)) {
                        if (setting("autoSwitch").bool().value && !mc.options.useKey.isPressed()) {
                            Integer bedSlot = null;

                            for (int slot = 0; slot < 9; slot++)
                                if (mc.player.getInventory().getStack(slot).getItem() instanceof BedItem) bedSlot = slot;

                            if (bedSlot == null) {
                                if (prevSlot != null) {
                                    mc.player.getInventory().selectedSlot = prevSlot;
                                    prevSlot = null;
                                }
                                return;
                            }

                            prevSlot = mc.player.getInventory().selectedSlot;
                            mc.player.getInventory().selectedSlot = bedSlot;
                        } else return;
                    }

                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                            (float) pos.getValue(), mc.player.getPitch(), true)
                    );
                    if (WorldLib.placeBlock(pos2, mc.player.getInventory().selectedSlot)) {
                        lastBed = pos2;
                        break;
                    }
                }
            }
        }
    }
}
