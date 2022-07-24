package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.events.TickEvent;
import zimnycat.reznya.libs.Finder;
import zimnycat.reznya.libs.WorldLib;

public class HoleTrap extends UtilBase {
    public HoleTrap() { super("HoleTrap", "Traps players in holes"); }

    @Subscribe
    public void onTick(TickEvent e) {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (mc.player.distanceTo(player) >= 5 || player == mc.player) continue;

            BlockPos obsidian = player.getBlockPos().up(2);
            if (!mc.world.getBlockState(obsidian).getBlock().equals(Blocks.AIR) || !WorldLib.isHole(player.getBlockPos())) continue;

            Integer slot = Finder.find(Items.OBSIDIAN, true);
            if (slot == null) return;

            int pre = mc.player.getInventory().selectedSlot;
            mc.player.getInventory().selectedSlot = slot;

            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,
                    new BlockHitResult(new Vec3d(obsidian.getX(), obsidian.getY(), obsidian.getZ()), Direction.UP, obsidian, true)
            );
            mc.player.swingHand(Hand.MAIN_HAND);

            mc.player.getInventory().selectedSlot = pre;
        }
    }
}
