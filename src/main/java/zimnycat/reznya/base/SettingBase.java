package zimnycat.reznya.base;

import zimnycat.reznya.base.settings.SettingBool;
import zimnycat.reznya.base.settings.SettingNum;
import zimnycat.reznya.base.settings.SettingString;

public class SettingBase {
    public String name;

    public SettingNum num() { return (SettingNum) this; }

    public SettingString string() { return (SettingString) this; }

    public SettingBool bool() { return (SettingBool) this; }
}
