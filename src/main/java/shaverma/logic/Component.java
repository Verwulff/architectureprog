package shaverma.logic;

public class Component extends Entity {
    private String name;

    Component(String name, AccessorRegistry registry) {
        super(registry);
        this.setName(name);
    }

    public Component(String name, AccessorRegistry registry, int id) {
        super(registry);
        this.name = name;
        this.setId(id);
    }

    @Override
    protected Class accessorRegistryKey() {
        return Component.class;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Component another) {
        return another.getName().equals(this.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
