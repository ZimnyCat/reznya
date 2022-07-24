package zimnycat.reznya.events;

import zimnycat.reznya.base.EventBase;

public class KeyPressEvent extends EventBase {
    private final int key;

    public KeyPressEvent(int key) { this.key = key; }

    public int getKey() { return key; }
}
