package zimnycat.reznya.commands;

import zimnycat.reznya.base.CommandBase;

public class ClearCmd extends CommandBase {

    public ClearCmd() { super("clear", "Clears the chat"); }

    @Override
    public void run(String[] args) { mc.inGameHud.getChatHud().clear(false); }
}
