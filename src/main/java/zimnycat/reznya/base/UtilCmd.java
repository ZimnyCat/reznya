package zimnycat.reznya.base;

import zimnycat.reznya.base.settings.SettingBool;
import zimnycat.reznya.base.settings.SettingNum;
import zimnycat.reznya.base.settings.SettingString;

import java.util.Arrays;

public class UtilCmd extends CommandBase {
    public UtilCmd() { super("util", "Manage utils"); }

    @Override
    public void run(String[] args) {
        if (args.length == 0) {
            clientMessage("Syntax: \"" + Utilrun.highlight("util toggle/list/settings") + "\"");
            return;
        }

        switch (args[0]) {
            case "toggle" -> {
                if (args.length == 1) {
                    clientMessage("Syntax: \"" + Utilrun.highlight("util toggle <utility name>") + "\"");
                    return;
                }
                UtilBase util = Manager.getUtilByName(args[1]);
                if (util == null) {
                    clientMessage("\"" + args[1].toLowerCase() + "\" util not found");
                    return;
                }
                util.toggle();
            }
            case "list" -> {
                clientMessage("Utils (" + Utilrun.highlight(String.valueOf(Manager.utils.size())) + "):");
                Manager.utils.forEach(u -> clientMessage(u.getName() + Utilrun.highlight(" - ")
                        + (u.isEnabled() ? "ON" : "OFF") +  Utilrun.highlight(" - ") + u.getDesc()));
            }
            case "settings" -> {
                if (args.length == 1) {
                    clientMessage("Syntax: \"" + Utilrun.highlight("util settings <util> <setting name> <value>") + "\"");
                    return;
                }
                UtilBase util = Manager.getUtilByName(args[1]);
                if (util == null) {
                    clientMessage("\"" + args[1].toLowerCase() + "\" util not found");
                    return;
                }
                if (args.length == 2) {
                    if (util.getSettings().isEmpty()) {
                        clientMessage(util.getName() + " has no settings");
                        return;
                    }
                    for (SettingBase setting : util.getSettings()) {
                        if (setting instanceof SettingNum)
                            clientMessage(setting.name + Utilrun.highlight(" = ") + setting.num().value
                                    + " (" + setting.num().getMin() + Utilrun.highlight("-") + setting.num().getMax() + ")");
                        if (setting instanceof SettingString)
                            clientMessage(setting.name + Utilrun.highlight(" = ") + "\"" + setting.string().value + "\"");
                        if (setting instanceof SettingBool)
                            clientMessage(setting.name + Utilrun.highlight(" = ") + setting.bool().value);
                    }
                } else {
                    SettingBase setting = util.setting(args[2]);
                    if (setting instanceof SettingNum) {
                        if (args.length == 3)
                            clientMessage(setting.name + Utilrun.highlight(" = ") + setting.num().value
                                    + " (" + setting.num().getMin() + Utilrun.highlight("-") + setting.num().getMax() + ")");
                        else {
                            try {
                                double val;
                                if (args[3].startsWith("+"))
                                    val = setting.num().value + Double.parseDouble(args[3].substring(1));
                                else if (args[3].startsWith("-"))
                                    val = setting.num().value - Double.parseDouble(args[3].substring(1));
                                else if (args[3].startsWith("*"))
                                    val = setting.num().value * Double.parseDouble(args[3].substring(1));
                                else if (args[3].startsWith("/"))
                                    val = setting.num().value / Double.parseDouble(args[3].substring(1));
                                else val = Double.parseDouble(args[3]);
                                if (isValid(setting.num(), val) || (args.length == 5 && args[4].equalsIgnoreCase("force")))
                                    setting.num().setValue(val);
                                else clientMessage("Invalid value");
                            } catch (Exception e) {
                                clientMessage("Not a number");
                            }
                        }
                    } if (setting instanceof SettingString) {
                        if (args.length == 3)
                            clientMessage(setting.name + Utilrun.highlight(" = ") + "\"" + setting.string().value + "\"");
                        else {
                            StringBuilder builder = new StringBuilder();
                            for (String s : args) if (Arrays.asList(args).indexOf(s) > 2) builder.append(" " + s);
                            setting.string().setValue(builder.substring(1));
                        }
                    } if (setting instanceof SettingBool) {
                        if (args.length == 3)
                            clientMessage(setting.name + Utilrun.highlight(" = ") + setting.bool().value);
                        else {
                            if (args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false"))
                                setting.bool().setValue(Boolean.parseBoolean(args[3]));
                            else if (args[3].equalsIgnoreCase("toggle"))
                                setting.bool().setValue(!setting.bool().value);
                        }
                    }
                }
            }
        }
    }

    public boolean isValid(SettingNum setting, double val) { return val >= setting.getMin() && val <= setting.getMax(); }
}
