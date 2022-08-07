package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.base.settings.SettingBool;
import zimnycat.reznya.events.TickEvent;
import zimnycat.reznya.libs.Finder;
import zimnycat.reznya.libs.WorldLib;

import java.util.Arrays;
import java.util.List;

public class SelfAnvil extends UtilBase {
    public SelfAnvil() {
        super(
                "SelfAnvil", "Places anvils over you",
                new SettingBool("selfTrap", false)
        );
    }

    @Subscribe
    public void onTick(TickEvent event) {
        Integer anvilSlot = Finder.find(Items.ANVIL, true);
        Integer obsidianSlot = Finder.find(Items.OBSIDIAN, true);
        BlockPos playerPos = mc.player.getBlockPos();

        if (anvilSlot == null) return;

        double x = Math.floor(mc.player.getX());
        double z = Math.floor(mc.player.getZ());
        mc.player.updatePosition(x + 0.5, mc.player.getY(), z + 0.5);
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x + 0.5, mc.player.getY(), z + 0.5, mc.player.isOnGround()));

        if (setting("selfTrap").bool().value && obsidianSlot != null) {
            List<BlockPos> poses = Arrays.asList(
                    playerPos.north(),
                    playerPos.east(),
                    playerPos.south(),
                    playerPos.west()
            );

            if (mc.world.getBlockState(playerPos.up(3)).isAir()){
                WorldLib.placeBlock(playerPos.up(3), obsidianSlot);
                return;
            }

            for (BlockPos pos : poses){
                if (mc.world.getBlockState(pos).isAir()) {
                    WorldLib.placeBlock(pos, obsidianSlot);
                    return;
                }
            }

            for (BlockPos pos : poses){
                if (mc.world.getBlockState(pos.up()).isAir()) {
                    WorldLib.placeBlock(pos.up(), obsidianSlot);
                    return;
                }
            }
        }

        List<BlockPos> poses = Arrays.asList(
                playerPos,
                playerPos.up(),
                playerPos.up(2)
        );

        for (BlockPos pos : poses) {
            if (mc.world.getBlockState(pos).isAir()){
                WorldLib.placeBlock(mc.player.getBlockPos().up(2), anvilSlot);
                break;
            }
        }
    }
}
