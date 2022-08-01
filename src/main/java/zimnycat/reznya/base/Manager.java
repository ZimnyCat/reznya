package zimnycat.reznya.base;

import com.google.common.eventbus.Subscribe;
import com.google.gson.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.ArrayUtils;
import zimnycat.reznya.base.settings.SettingBool;
import zimnycat.reznya.base.settings.SettingNum;
import zimnycat.reznya.base.settings.SettingString;
import zimnycat.reznya.events.ClientStopEvent;
import zimnycat.reznya.events.KeyPressEvent;
import zimnycat.reznya.events.SendPacketEvent;
import zimnycat.reznya.libs.ModFile;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    static MinecraftClient mc = MinecraftClient.getInstance();

    public static List<CommandBase> commands = new ArrayList<>();
    public static List<UtilBase> utils = new ArrayList<>();

    public static void loadData() {
        commands.add(new BindCmd());
        commands.add(new UtilCmd());

        Utilrun.logger.info("Loading utils data");
        ModFile modFile = new ModFile("utils.json");
        if (modFile.readAsList().isEmpty()) modFile.write("{}", ModFile.WriteMode.OVERWRITE);
        JsonObject data = JsonParser.parseString(modFile.readAsString()).getAsJsonObject();

        utils.forEach(u -> {
            JsonElement je = data.get(u.getName().toLowerCase());
            if (je != null) {
                if (je.getAsJsonObject().get("enabled").getAsBoolean()) u.setEnabled(true);
                u.getSettings().forEach(s -> {
                    JsonElement val = je.getAsJsonObject().get(s.name.toLowerCase());
                    if (val != null) {
                        if (s instanceof SettingNum) s.num().value = val.getAsDouble();
                        if (s instanceof SettingString) s.string().value = val.getAsString();
                        if (s instanceof SettingBool) s.bool().value = val.getAsBoolean();
                    }
                });
            }
        });
    }

    @Subscribe
    public void command(SendPacketEvent event) {
        if (!(event.getPacket() instanceof ChatMessageC2SPacket)) return;
        String msg = ((ChatMessageC2SPacket) event.getPacket()).chatMessage();

        if (!msg.startsWith(Utilrun.prefix)) return;
        event.setCancelled(true);

        if (msg.equals(Utilrun.prefix)) {
            mc.inGameHud.getChatHud().addMessage(Text.of(
                    Utilrun.highlight(">> ") + "Commands (" + Utilrun.highlight(String.valueOf(commands.size())) + "):"
            ));
            commands.forEach(c -> mc.inGameHud.getChatHud().addMessage(Text.of(
                    Utilrun.highlight(">> ") + c.getName() + Utilrun.highlight(" - ") + c.getDesc()
            )));
            return;
        }

        runCommand(msg.replace(Utilrun.prefix, ""));
    }

    @Subscribe
    public void saveUtilsData(ClientStopEvent event) {
        Utilrun.logger.info("Saving utils data");
        JsonObject data = new JsonObject();
        utils.forEach(u -> {
            JsonObject util = new JsonObject();
            util.addProperty("enabled", u.isEnabled());
            if (!u.getSettings().isEmpty()) {
                u.getSettings().forEach(s -> {
                    if (s instanceof SettingNum) util.addProperty(s.name.toLowerCase(), s.num().value);
                    if (s instanceof SettingString) util.addProperty(s.name.toLowerCase(), s.string().value);
                    if (s instanceof SettingBool) util.addProperty(s.name.toLowerCase(), s.bool().value);
                });
            }
            data.add(u.getName().toLowerCase(), util);
        });
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ModFile modFile = new ModFile("utils.json");
        modFile.write(gson.toJson(data), ModFile.WriteMode.OVERWRITE);
    }

    @Subscribe
    public void keyPress(KeyPressEvent event) {
        ModFile modFile = new ModFile("binds.json");
        if (modFile.readAsList().isEmpty()) modFile.write("{}", ModFile.WriteMode.OVERWRITE);
        String key = String.valueOf(event.getKey());
        JsonArray array = JsonParser.parseString(modFile.readAsString()).getAsJsonObject().getAsJsonArray(key);

        if (array != null) array.forEach(element -> runCommand(element.getAsString()));
    }

    public static void runCommand(String cmd) {
        String[] split = cmd.split(" ");
        commands.forEach(c -> {
            if (c.getName().equals(split[0])) {
                mc.inGameHud.getChatHud().addMessage(Text.of(Formatting.GRAY + cmd));

                try { c.run(ArrayUtils.remove(split, 0)); }
                catch (Exception e) {
                    c.clientMessage("Exception caught! Check logs for more info.");
                    e.printStackTrace();
                }
            }
        });
    }

    public static UtilBase getUtilByName(String uName) {
        for (UtilBase util : utils) if (util.getName().equalsIgnoreCase(uName)) return util;
        return null;
    }
}
