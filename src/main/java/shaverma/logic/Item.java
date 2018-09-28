package shaverma.logic;

import shaverma.exceptions.ApplicationException;

public class Item extends Entity {
    private ForeignKey<Component> component;
    private int price;
    private int amount;

    public Item(ForeignKey<Component> component, int amount, int price, int id, AccessorRegistry registry) {
        super(registry);
        this.amount = amount;
        this.component = component;
        this.price = price;
        this.setId(id);
    }

    public Item(ForeignKey<Component> component, int amount, int price, AccessorRegistry registry) {
        super(registry);
        this.amount = amount;
        this.component = component;
        this.price = price;
    }

    public Item(ForeignKey<Component> component, int amount, AccessorRegistry registry){
        super(registry);
        this.amount = amount;
        this.component = component;
        this.price = 0;
    }

    public Component getComponent() throws ApplicationException {
        return (Component) component.getEntity();
    }

    public int getComponentId() {
        return component.getId();
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    protected Class accessorRegistryKey() {
        return Item.class;
    }
}
