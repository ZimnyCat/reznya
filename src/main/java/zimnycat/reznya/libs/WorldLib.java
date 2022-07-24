package zimnycat.reznya.libs;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class WorldLib {
    static MinecraftClient mc = MinecraftClient.getInstance();

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
