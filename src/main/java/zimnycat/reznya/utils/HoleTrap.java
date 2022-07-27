package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.settings.SettingNum;
import zimnycat.reznya.events.TickEvent;
import zimnycat.reznya.libs.Delay;
import zimnycat.reznya.libs.Finder;
import zimnycat.reznya.libs.WorldLib;

import java.util.Arrays;
import java.util.List;

public class HoleTrap extends UtilBase {
    Delay delay = new Delay();

    public HoleTrap() {
        super(
                "HoleTrap", "Traps players in holes",
                new SettingNum("delay", 100, 1, 1000),
                new SettingNum("range", 5, 1, 6)
        );
    }

    @Subscribe
    public void onTick(TickEvent e) {
        delay.setDelay((long) setting("delay").num().value);
        if (!delay.check()) return;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (mc.player.distanceTo(player) >= 8 || player == mc.player
                    || player.getY() != Math.floor(player.getY())
                    || (player.getX() - Math.floor(player.getX())) > 0.7
                    || (player.getX() - Math.floor(player.getX())) < 0.3
                    || (player.getZ() - Math.floor(player.getZ())) > 0.7
                    || (player.getZ() - Math.floor(player.getZ())) < 0.3) continue;

            BlockPos playerPos = player.getBlockPos();

            List<BlockPos> poses = Arrays.asList(
                    playerPos.north(),
                    playerPos.east(),
                    playerPos.south(),
                    playerPos.west(),
                    playerPos.down(),
                    playerPos.up(2)
            );

            for (BlockPos pos : poses) {
                if (mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN)
                        || mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK)) continue;

                if (mc.world.getBlockState(pos).isAir() && Math.sqrt(pos.getSquaredDistance(mc.player.getPos())) < setting("range").num().value) {
                    Integer slot = Finder.find(Items.OBSIDIAN, true);
                    if (slot == null) return;
                    WorldLib.placeBlock(pos, slot);
                    return;
                }
            }
        }
    }
}
