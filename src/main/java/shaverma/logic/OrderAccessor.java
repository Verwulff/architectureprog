package shaverma.logic;

import shaverma.exceptions.ApplicationException;

import java.util.List;

public interface OrderAccessor extends Accessor {
    List<ClientOrder> getOrdersByClient(Client client) throws ApplicationException;

    List<WholesaleOrder> getAllWholesaleOrders() throws ApplicationException;

    List<ClientOrder> getAllClientOrders() throws ApplicationException;
}
