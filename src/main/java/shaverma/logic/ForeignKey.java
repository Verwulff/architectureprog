package shaverma.logic;

import shaverma.exceptions.ApplicationException;

public class ForeignKey<EntityClass extends Entity> {
    private Entity entity;
    private int id;
    private Accessor accessor;

    public ForeignKey() {

    }

    public ForeignKey(EntityClass entity) {
        this.entity = entity;
        this.id = entity.getId();
    }

    public ForeignKey(int id, Accessor accessor) {
        this.id = id;
        this.accessor = accessor;
    }

    public Entity getEntity() throws ApplicationException {
        if (entity != null) {
            return entity;
        } else if (accessor != null && id != 0) {
            entity = accessor.getById(id);
            return entity;
        } else {
            return null;
        }
    }

    public int getId() {
        return this.id;
    }
}
