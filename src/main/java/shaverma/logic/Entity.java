package shaverma.logic;

import shaverma.exceptions.ApplicationException;

import java.lang.reflect.Field;

public abstract class Entity {

    private int id;
    private AccessorRegistry registry;

    protected Entity(AccessorRegistry registry) {
        this.registry = registry;
    }

    protected Entity(AccessorRegistry registry, int id) {
        this.registry = registry;
        this.id = id;
    }

    protected abstract Class accessorRegistryKey();

    protected Accessor getAccessor() {
        return registry.getAccessor(accessorRegistryKey());
    }

    protected AccessorRegistry getRegistry() {
        return registry;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void create() throws ApplicationException {
        this.id = getAccessor().saveObject(this);
    }

    public void update() throws ApplicationException {
        getAccessor().updateObject(this);
    }

    public Entity read() throws ApplicationException {
        return getAccessor().getById(id);
    }

    public void setField(String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Class clazz = getClass();
        Field field = null;
        while (clazz != Entity.class) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException ex) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null)
            throw new NoSuchFieldException(fieldName);
        field.setAccessible(true);
        field.set(this, value);
    }
}
