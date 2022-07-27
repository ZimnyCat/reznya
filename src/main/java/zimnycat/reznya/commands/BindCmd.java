package zimnycat.reznya.commands;

import com.google.gson.*;
import net.minecraft.client.util.InputUtil;
import zimnycat.reznya.base.CommandBase;
import zimnycat.reznya.Utilrun;
import zimnycat.reznya.libs.ModFile;

import java.util.Arrays;

public class BindCmd extends CommandBase {
    public BindCmd() { super("bind", "Bind " + Utilrun.name + " command"); }

    @Override
    public void run(String[] args) {
        if (args.length <= 1) {
            clientMessage("Syntax: \"" + Utilrun.highlight("bind <key> add/clear/list") + "\"");
            return;
        }

        String code = null;
        try {
            code = String.valueOf(InputUtil.fromTranslationKey("key.keyboard." + args[0].toLowerCase()).getCode());
        } catch (Exception e) { clientMessage("Invalid key"); }
        if (code == null) return;

        ModFile modFile = new ModFile("binds.json");
        if (modFile.readAsList().isEmpty()) modFile.write("{}", ModFile.WriteMode.OVERWRITE);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject data = JsonParser.parseString(modFile.readAsString()).getAsJsonObject();

        switch (args[1]) {
            case "add" -> {
                StringBuilder builder = new StringBuilder();
                for (String s : args) if (Arrays.asList(args).indexOf(s) > 1) builder.append(" " + s);
                if (builder.length() == 0) {
                    clientMessage("Syntax: \"" + Utilrun.highlight("bind <key> add <" + Utilrun.name + " command>") + "\"");
                    return;
                }

                if (!data.has(code)) {
                    JsonArray array = new JsonArray();
                    array.add(builder.substring(1));
                    data.add(code, array);
                }
                else data.get(code).getAsJsonArray().add(builder.substring(1));

                modFile.write(gson.toJson(data), ModFile.WriteMode.OVERWRITE);
                clientMessage(
                        "Bound " + Utilrun.highlight("\"" + builder.substring(1) + "\"") + " to " + Utilrun.highlight(args[0] + " (" + code + ")")
                );
            } case "clear" -> {
                data.remove(code);
                modFile.write(gson.toJson(data), ModFile.WriteMode.OVERWRITE);
                clientMessage("Cleared " + Utilrun.highlight(args[0] + " (" + code + ")"));
            } case "list" -> {
                if (data.has(code)) {
                    clientMessage("Command(s) bound to " + Utilrun.highlight(args[0] + " (" + code + ")" + ":"));
                    data.get(code).getAsJsonArray().forEach(element -> clientMessage(element.getAsString()));
                }
                else clientMessage("No commands bound to " + Utilrun.highlight(args[0] + " (" + code + ")"));
            }
        }
    }
}
