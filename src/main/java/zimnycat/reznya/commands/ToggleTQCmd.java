package zimnycat.reznya.commands;

import zimnycat.reznya.base.CommandBase;

public class ToggleTQCmd extends CommandBase {
    public static boolean tq = true;

    public ToggleTQCmd() { super("toggletq", "Toggle tick queue for utils"); }

    @Override
    public void run(String[] args) {
        tq = !tq;
        clientMessage("Tick queue toggled (" + tq + ")");
    }
}
