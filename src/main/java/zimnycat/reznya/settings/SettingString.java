package zimnycat.reznya.settings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import zimnycat.reznya.base.SettingBase;
import zimnycat.reznya.Utilrun;

public class SettingString extends SettingBase {
    public String value;

    public SettingString(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(
                Utilrun.highlight(">> ") + name + Utilrun.highlight(" = ") + "\"" + value + "\""
        ));
    }
}
