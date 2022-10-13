package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.events.TickEvent;
import zimnycat.reznya.libs.Finder;

public class WaterDrop extends UtilBase {
    boolean wait = false;

    public WaterDrop() { super("WaterDrop", "Epic MLG waterdrop"); }

    @Subscribe
    public void onTick(TickEvent event) {
        if (wait) {
            Integer sl = Finder.find(Items.WATER_BUCKET, true);

            if (sl == null) {
                clientMessage("No water bucket in hotbar!");
                toggle();
                wait = false;
                return;
            }

            mc.player.getInventory().selectedSlot = sl;
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            wait = false;
            toggle();
            return;
        }

        BlockPos block = mc.player.getBlockPos().down(3);
        Vec3d vec = new Vec3d(block.getX(), block.getY(), block.getZ());

        if (!mc.world.getBlockState(block).isAir()) {
            clientMessage("Can't place the block!");
            toggle();
            return;
        }

        if (!(mc.player.getInventory().getMainHandStack().getItem() instanceof BlockItem)) {
            Integer blockSlot = null;
            for (int slot = 0; slot < 9; slot++) {
                Item item = mc.player.getInventory().getStack(slot).getItem();
                if (item instanceof BlockItem) {
                    blockSlot = slot;
                    break;
                }
            }
            if (blockSlot == null) {
                clientMessage("No blocks found in hotbar!");
                toggle();
                return;
            }
            mc.player.getInventory().selectedSlot = blockSlot;
        }

        mc.player.setPitch(90);
        double playerX = Math.floor(mc.player.getX());
        double playerZ = Math.floor(mc.player.getZ());

        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(
                vec, Direction.UP, block, true
        ));
        mc.player.swingHand(Hand.MAIN_HAND);

        if (mc.world.getBlockState(mc.player.getBlockPos().down(2)).getBlock() != Blocks.WATER
                && !mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("the_nether")) wait = true;
        else toggle();
    }
}
