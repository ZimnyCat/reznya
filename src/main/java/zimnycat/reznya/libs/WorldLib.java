package zimnycat.reznya.libs;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class WorldLib {
    static MinecraftClient mc = MinecraftClient.getInstance();

    public static boolean placeBlock(BlockPos pos, int slot) {
        Box posBox = new Box(pos);
        for (Entity e : mc.world.getEntities()) if (posBox.intersects(e.getBoundingBox())) return false;

        int pre = mc.player.getInventory().selectedSlot;
        mc.player.getInventory().selectedSlot = slot;

        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,
                new BlockHitResult(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos, true)
        );
        mc.player.swingHand(Hand.MAIN_HAND);

        mc.player.getInventory().selectedSlot = pre;
        return true;
    }

    public static boolean isHole(BlockPos p) {
        List<BlockPos> poses = Arrays.asList(
                p.north(),
                p.east(),
                p.south(),
                p.west(),
                p.down()
        );

        for (BlockPos pos : poses) {
            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN)
                    && !mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK)) return false;
        }
        return true;
    }
}
