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
import zimnycat.reznya.base.settings.SettingNum;
import zimnycat.reznya.events.TickEvent;
import zimnycat.reznya.libs.Delay;

import java.util.HashMap;
import java.util.Map;

public class AutoBed extends UtilBase {
    Delay delay = new Delay();

    public AutoBed() {
        super(
                "AutoBed", "Places beds to kill players",
                new SettingNum("delay", 100, 1, 1000),
                new SettingNum("range", 5, 1, 6)
        );
    }

    @Subscribe
    public void onTick(TickEvent e) {
        delay.setDelay((long) setting("delay").num().value);
        if (!mc.world.getRegistryKey().getValue().getPath().contains("nether") || !delay.check()) return;

        for (PlayerEntity p : mc.world.getPlayers()) {
            if (mc.player.isSneaking() || p == mc.player || p.getBlockPos().equals(mc.player.getBlockPos())) continue;

            BlockPos up = p.getBlockPos().up();
            if (mc.player.distanceTo(p) < setting("range").num().value && mc.world.getBlockState(up).getBlock().asItem() instanceof BedItem) {
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,
                        new BlockHitResult(new Vec3d(up.getX(), up.getY(), up.getZ()), Direction.UP, up, true)
                );
                break;
            }

            if (mc.player.distanceTo(p) >= 8 || p.isDead() || !mc.world.getBlockState(up).isAir()
                    || !(mc.player.getMainHandStack().getItem() instanceof BedItem)) continue;

            HashMap<BlockPos, Float> poses = new HashMap<>();
            poses.put(up.north(), 0f);
            poses.put(up.east(), 90f);
            poses.put(up.south(), 180f);
            poses.put(up.west(), -90f);

            for (Map.Entry pos : poses.entrySet()) {
                BlockPos pos2 = (BlockPos) pos.getKey();
                if (Math.sqrt(pos2.getSquaredDistance(mc.player.getPos())) >= setting("range").num().value) continue;

                if (mc.world.getBlockState(pos2).isAir() || mc.world.getBlockState(pos2).getBlock().equals(Blocks.FIRE)) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                            (float) pos.getValue(), mc.player.getPitch(), true)
                    );
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,
                            new BlockHitResult(new Vec3d(pos2.getX(), pos2.getY(), pos2.getZ()), Direction.UP, pos2, true)
                    );
                    mc.player.swingHand(Hand.MAIN_HAND);
                    break;
                }
            }
        }
    }
}
