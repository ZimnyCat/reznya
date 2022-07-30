package zimnycat.reznya.commands;

import zimnycat.reznya.base.CommandBase;
import zimnycat.reznya.base.Utilrun;

public class SayCmd extends CommandBase {
    public SayCmd() { super("say", "Send a message to the chat"); }

    public void run(String[] args) {
        if (args.length == 0) {
            clientMessage("Syntax: \"" + Utilrun.highlight("say <message>") + "\"");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String s : args) message.append(s + " ");
        mc.player.sendChatMessage(message.toString(), null);
    }
}
