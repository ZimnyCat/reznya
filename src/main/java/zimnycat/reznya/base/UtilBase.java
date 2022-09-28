package zimnycat.reznya.base;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import zimnycat.reznya.Utilrun;

import java.util.Arrays;
import java.util.List;

public class UtilBase {
    public MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final String desc;
    private boolean enabled;
    private final List<SettingBase> settings;

    public UtilBase(String name, String desc, SettingBase... settings) {
        this.name = name;
        this.desc = desc;
        this.enabled = false;
        this.settings = Arrays.asList(settings);
    }

    public String getName() { return name; }

    public String getDesc() { return desc; }

    public void onEnable() { }

    public void onDisable() { }

    public List<SettingBase> getSettings() { return settings; }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
            Utilrun.bus.register(this);
        }
        else {
            onDisable();
            Utilrun.bus.unregister(this);
        }
    }

    public void toggle() {
        setEnabled(!enabled);
        clientMessage(Utilrun.highlight(enabled ? "enabled" : "disabled"));
    }

    public void clientMessage(String msg) {
        mc.inGameHud.getChatHud().addMessage(Text.of(
                "util" + Utilrun.highlight(">") + name + Utilrun.highlight("> ") + msg
        ));
    }

    public SettingBase setting(String name) {
        try {
            return settings.stream().filter(setting -> setting.name.toLowerCase().startsWith(name.toLowerCase())).toList().get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
