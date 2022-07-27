package zimnycat.reznya.base;

import zimnycat.reznya.settings.SettingBool;
import zimnycat.reznya.settings.SettingNum;
import zimnycat.reznya.settings.SettingString;

public class SettingBase {
    public String name;

    public SettingNum num() { return (SettingNum) this; }

    public SettingString string() { return (SettingString) this; }

    public SettingBool bool() { return (SettingBool) this; }
}
