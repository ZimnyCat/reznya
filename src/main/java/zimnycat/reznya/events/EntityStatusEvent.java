package zimnycat.reznya.events;

import net.minecraft.entity.Entity;
import zimnycat.reznya.base.EventBase;

public class EntityStatusEvent extends EventBase {
    private final byte status;
    private final Entity entity;

    public EntityStatusEvent(byte status, Entity entity) {
        this.status = status;
        this.entity = entity;
    }

    public byte getStatus() { return status; }

    public Entity getEntity() { return entity; }
}
