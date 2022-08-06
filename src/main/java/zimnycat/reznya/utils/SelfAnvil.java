package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.events.TickEvent;
import zimnycat.reznya.libs.Finder;
import zimnycat.reznya.libs.WorldLib;

import java.util.Arrays;
import java.util.List;

public class SelfAnvil extends UtilBase {
    public SelfAnvil() {
        super("SelfAnvil", "Places anvils over you");
    }

    @Subscribe
    public void onTick(TickEvent event) {
        Integer anvilSlot = Finder.find(Items.ANVIL, true);
        if (anvilSlot == null) return;

        List<BlockPos> poses = Arrays.asList(
                mc.player.getBlockPos(),
                mc.player.getBlockPos().up(),
                mc.player.getBlockPos().up(2)
        );

        for (BlockPos pos : poses) {
            if (mc.world.getBlockState(pos).isAir()){
                WorldLib.placeBlock(mc.player.getBlockPos().up(2), anvilSlot);
                break;
            }
        }
    }
}
