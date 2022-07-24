package zimnycat.reznya.base.settings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import zimnycat.reznya.base.SettingBase;
import zimnycat.reznya.base.Utilrun;

public class SettingNum extends SettingBase {
    public double value;
    private final double min;
    private final double max;

    public SettingNum(String name, double value, double min, double max) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public double getMin() { return min; }

    public double getMax() { return max; }

    public void setValue(double value) {
        this.value = value;
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(
                Utilrun.highlight(">> ") + name + Utilrun.highlight(" = ") + value
                        + " (" + min + Utilrun.highlight("-") + max + ")"
        ));
    }
}
