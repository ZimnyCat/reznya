package zimnycat.reznya.base;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import zimnycat.reznya.Utilrun;

public class CommandBase {
    public MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final String desc;

    public CommandBase(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public void run(String[] args) { }

    public String getName() { return name; }

    public String getDesc() { return desc; }

    public void clientMessage(String msg) {
        mc.inGameHud.getChatHud().addMessage(Text.of(
                "cmd" + Utilrun.highlight(">") + name + Utilrun.highlight("> ") + msg
        ));
    }
}
