package zimnycat.reznya.base.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import zimnycat.reznya.Utilrun;
import zimnycat.reznya.base.CommandBase;
import zimnycat.reznya.libs.ModFile;

import java.util.Arrays;

public class AliasCmd extends CommandBase {
    public AliasCmd() { super("alias", "Set an alias for " + Utilrun.name + " command"); }

    @Override
    public void run(String[] args) {
        if (args.length == 0) {
            clientMessage("Syntax: \"" + Utilrun.highlight("alias add/remove/list <alias>") + "\"");
            return;
        }

        ModFile modFile = new ModFile("aliases.json");
        if (modFile.readAsList().isEmpty()) modFile.write("{}", ModFile.WriteMode.OVERWRITE);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject data = JsonParser.parseString(modFile.readAsString()).getAsJsonObject();

        switch (args[0]) {
            case "add" -> {
                if (args.length == 1) {
                    clientMessage("Syntax: \"" + Utilrun.highlight("alias add <alias> <" + Utilrun.name + " command>") + "\"");
                    return;
                }

                StringBuilder command = new StringBuilder();
                for (String s : args) if (Arrays.asList(args).indexOf(s) > 1) command.append(" " + s);
                if (command.length() == 0) {
                    clientMessage("Syntax: \"" + Utilrun.highlight("alias add <alias> <" + Utilrun.name + " command>") + "\"");
                    return;
                }

                if (data.has(args[1])) {
                    clientMessage("This alias already exists");
                    return;
                }

                data.addProperty(args[1], command.substring(1));
                modFile.write(gson.toJson(data), ModFile.WriteMode.OVERWRITE);
                clientMessage("Added \"" + Utilrun.highlight(args[1]) + "\" alias");
            }
            case "remove" -> {
                if (args.length == 1) {
                    clientMessage("Syntax: \"" + Utilrun.highlight("alias remove <alias>") + "\"");
                    return;
                }

                data.remove(args[1]);
                modFile.write(gson.toJson(data), ModFile.WriteMode.OVERWRITE);
                clientMessage("Removed \"" + Utilrun.highlight(args[1]) + "\" alias");
            }
            case "list" -> {
                clientMessage("Aliases:");
                data.entrySet().forEach(alias -> clientMessage(alias.getKey() + Utilrun.highlight(" - ") + alias.getValue().getAsString()));
            }
        }
    }
}
