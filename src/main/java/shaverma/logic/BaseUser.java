package shaverma.logic;

import shaverma.exceptions.ApplicationException;

public class BaseUser extends Entity implements User {
    private String name;

    public BaseUser(String name, AccessorRegistry registry) {
        super(registry);
        this.name = name;
    }

    public BaseUser(String name, AccessorRegistry registry, int id) {
        super(registry);
        this.name = name;
        this.setId(id);
    }

    public String getName() {
        return this.name;
    }

    public Class accessorRegistryKey() {
        return BaseUser.class;
    }

    @Override
    public Role getRole() {
        return Role.NONE;
    }

    @Override
    public void create() throws ApplicationException {
        UserAccessor accessor = (UserAccessor) getAccessor();
        int newId = accessor.addUser(getName(), getRole());
        setId(newId);
    }

    public BaseUser login() {
        try {
            UserAccessor accessor = (UserAccessor) getAccessor();
            return accessor.getUser(this.getName(), this.getRole());
        } catch (ApplicationException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}