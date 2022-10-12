package zimnycat.reznya;

import com.google.common.eventbus.EventBus;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zimnycat.reznya.base.Manager;
import zimnycat.reznya.commands.NearCmd;
import zimnycat.reznya.commands.SayCmd;
import zimnycat.reznya.libs.TickQueue;
import zimnycat.reznya.utils.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Utilrun implements ModInitializer {
	public static String name = "reznya";
	public static String prefix = ">>";
	public static Path path = Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), Utilrun.name, "/");

	public static final Logger logger = LoggerFactory.getLogger(name);
	public static EventBus bus = new EventBus();

	@Override
	public void onInitialize() {
		if (!path.toFile().exists()) {
			logger.info("Creating " + name + " folder");
			path.toFile().mkdirs();
		}

		Manager.commands.add(new NearCmd());
		Manager.commands.add(new SayCmd());

		Manager.utils.add(new AutoBed());
		Manager.utils.add(new AutoRefill());
		Manager.utils.add(new HoleTrap());
		Manager.utils.add(new KillAura());
		Manager.utils.add(new PopCounter());
		Manager.utils.add(new SelfTrap());
		Manager.utils.add(new FakePlayer());
		Manager.utils.add(new WaterDrop());

		Manager.loadData();
		bus.register(new Manager());
		bus.register(new TickQueue());
	}

	public static String highlight(String str) { return Formatting.RED + str + Formatting.WHITE; }
}
