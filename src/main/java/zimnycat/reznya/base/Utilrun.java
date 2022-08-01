package zimnycat.reznya.base;

import com.google.common.eventbus.EventBus;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zimnycat.reznya.commands.SayCmd;
import zimnycat.reznya.utils.AutoBed;
import zimnycat.reznya.utils.AutoRefill;
import zimnycat.reznya.utils.HoleTrap;

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

		Manager.commands.add(new SayCmd());

		Manager.utils.add(new AutoBed());
		Manager.utils.add(new AutoRefill());
		Manager.utils.add(new HoleTrap());

		Manager.loadData();
		bus.register(new Manager());
	}

	public static String highlight(String str) { return Formatting.RED + str + Formatting.WHITE; }
}