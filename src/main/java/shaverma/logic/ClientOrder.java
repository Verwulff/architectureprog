package shaverma.logic;

import shaverma.exceptions.ApplicationException;

public class ClientOrder extends Order {

    public ClientOrder(ForeignKey<BaseUser> from, AccessorRegistry registry) {
        super(registry);
        this.setFrom(from);
    }

    public ClientOrder(ForeignKey<BaseUser> from, AccessorRegistry registry, int id) {
        super(registry, id);
        this.setFrom(from);
    }

    BaseUser getClient() {
        return getFrom();
    }

    @Override
    boolean canBeSubmitted() throws ApplicationException {
        if (getStatus() != OrderStatus.NEW)
            return false;
        Storage storage = getRegistry().getStorage();
        for (Item item: this.getItems()) {
            if (!storage.componentExists(item.getComponent()))
                return false;
        }
        return true;
    }

    @Override
    boolean canBeAccepted () throws ApplicationException {
        Storage storage = getRegistry().getStorage();
        if (getStatus() != OrderStatus.SUBMITTED)
            return false;
        for (Item item: getItems()) {
            if (storage.componentAmount(item.getComponent()) < item.getAmount())
                return false;
        }
        return true;
    }


}
