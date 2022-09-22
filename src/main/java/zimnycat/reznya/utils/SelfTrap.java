package zimnycat.reznya.utils;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.AnvilBlock;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import zimnycat.reznya.base.UtilBase;
import zimnycat.reznya.base.settings.SettingBool;
import zimnycat.reznya.events.TickEvent;
import zimnycat.reznya.libs.Delay;
import zimnycat.reznya.libs.Finder;
import zimnycat.reznya.libs.WorldLib;

import java.util.Arrays;
import java.util.List;

public class SelfTrap extends UtilBase {
    Delay autoCenterDelay = new Delay();

    public SelfTrap() {
        super(
                "SelfTrap", "Traps you in obsidian",
                new SettingBool("selfAnvil", false),
                new SettingBool("autoCenter", true),
                new SettingBool("holeCheck", false),
                new SettingBool("disableOnDeath", true)
        );
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (setting("disableOnDeath").bool().value && mc.player.isDead()) {
            toggle();
            return;
        }

        if (mc.player.getY() != Math.floor(mc.player.getY())) return;

        BlockPos playerPos = mc.player.getBlockPos();
        if (!WorldLib.isHole(playerPos) && setting("holeCheck").bool().value) return;
        Integer obsidianSlot = Finder.find(Items.OBSIDIAN, true);
        if (obsidianSlot == null) return;

        autoCenterDelay.setDelay(1000);
        if (setting("autoCenter").bool().value && !setting("holeCheck").bool().value && autoCenterDelay.check()) {
            double x = Math.floor(mc.player.getX());
            double z = Math.floor(mc.player.getZ());
            mc.player.updatePosition(x + 0.5, mc.player.getY(), z + 0.5);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x + 0.5, mc.player.getY(), z + 0.5, mc.player.isOnGround()));
        }

        BlockPos up = playerPos.up(setting("selfAnvil").bool().value ? 3 : 2);
        if (mc.world.getBlockState(up).isAir() && WorldLib.placeBlock(up, obsidianSlot)) return;

        List<BlockPos> poses = Arrays.asList(
                playerPos.north(),
                playerPos.east(),
                playerPos.south(),
                playerPos.west()
        );

        poses.forEach(pos -> {
            if (mc.world.getBlockState(pos).isAir() && WorldLib.placeBlock(pos, obsidianSlot)) return;
            if (mc.world.getBlockState(pos.up()).isAir() && WorldLib.placeBlock(pos.up(), obsidianSlot)) return;

            if (!setting("selfAnvil").bool().value) return;
            if (mc.world.getBlockState(pos.up(2)).isAir() && WorldLib.placeBlock(pos.up(2), obsidianSlot)) return;
        });

        if (setting("selfAnvil").bool().value
                && (mc.world.getBlockState(playerPos).isAir() || mc.world.getBlockState(playerPos).getBlock() instanceof AnvilBlock)
                && (mc.world.getBlockState(playerPos.up()).isAir() || mc.world.getBlockState(playerPos.up()).getBlock() instanceof AnvilBlock)
                && mc.world.getBlockState(playerPos.up(2)).isAir()) {
            Integer anvilSlot = Finder.find(Items.ANVIL, true);
            if (anvilSlot != null) WorldLib.placeBlock(playerPos.up(2), anvilSlot);
        }
    }
}
