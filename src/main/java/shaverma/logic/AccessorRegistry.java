package shaverma.logic;

import shaverma.exceptions.ApplicationException;

import java.util.HashMap;
import java.util.Map;

public class AccessorRegistry {

    private Map<Class, Accessor> registry;

    public AccessorRegistry(){}

    public void setUp(ItemAccessor ia, ComponentAccessor ca, UserAccessor ua, StorageAccessor sa,
                      OrderAccessor oa, PaymentAccessor pa) {
        registry = new HashMap<>();
        registry.put(Item.class, ia);
        registry.put(Component.class, ca);
        registry.put(BaseUser.class, ua);
        registry.put(Storage.class, sa);
        registry.put(Order.class, oa);
        registry.put(Payment.class, pa);
    }

    public Accessor getAccessor(Class key) {
        return registry.get(key);
    }

    public Item newItem(Component component, int amount, int price) {
        ForeignKey<Component> componentForeignKey = new ForeignKey<>(component);
        return new Item(componentForeignKey, amount, price, this);

    }

    public Item newItem(Component component, int amount) {
        return newItem(component, amount, 0);
    }

    public ClientOrder newOrder(Client from) {
        return new ClientOrder(new ForeignKey<>(from),this);
    }

    public Payment newPayment(BaseUser from, BaseUser to, int amount) {
        return new Payment(new ForeignKey<>(from), new ForeignKey<>(to), amount, this);
    }

    public BaseUser newUser(String name, User.Role role) throws ApplicationException {
        switch (role) {
            case CLIENT:
                return new Client(name, this);
            default:
                throw new ApplicationException();
        }
    }

    public Component newComponent(String name) {
        return new Component(name, this);
    }

    public Storage getStorage() throws ApplicationException {
        StorageAccessor accessor = (StorageAccessor)getAccessor(Storage.class);
        return accessor.getInstance();
    }
}
