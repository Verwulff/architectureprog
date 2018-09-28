package shaverma.logic;

import shaverma.exceptions.ApplicationException;
import shaverma.exceptions.ApplicationException.Type;
import java.util.ArrayList;
import java.util.List;

public class Client extends BaseUser implements User {

    Client(String name, AccessorRegistry registry) {
        super(name, registry);
    }

    @Override
    public Role getRole() {
        return Role.CLIENT;
    }

    public ClientOrder makeOrder() throws ApplicationException {
        ClientOrder newOrder = this.getRegistry().newOrder(this);
        newOrder.create();
        return newOrder;
    }

    public void submitOrder(ClientOrder order) throws ApplicationException {
        if (order.canBeSubmitted()) {
            Storage storage = getRegistry().getStorage();
            List<Item> itemsWithPrice = new ArrayList<>();
            for (Item itemInOrder: order.getItems()) {
                Component component = itemInOrder.getComponent();
                Item newItem = getRegistry().newItem(component, itemInOrder.getAmount(), storage.componentPrice(component));
                newItem.create();
                itemsWithPrice.add(newItem);
            }
            order.setItems(itemsWithPrice);
            order.setStatus(Order.OrderStatus.SUBMITTED);
            order.update();
        }
    }

    public void addItemToOrder(ClientOrder order, Component component, int amount) throws ApplicationException{
        Item newItem = this.getRegistry().newItem(component, amount);
        newItem.create();
        order.addItem(newItem);
        order.update();
    }

    public void changeComponentAmountInOrder(Component component, Order order, int amount) throws ApplicationException {
        order.changeAmount(component, amount);
        order.update();
    }

    public void closeCompleteOrder(ClientOrder order) throws ApplicationException {
        if (!order.canBeClosed())
            throw new ApplicationException("Order can't be closed", Type.ORDER_STATUS);
        order.setStatus(Order.OrderStatus.CLOSED);
        order.getPayment().close();
        order.getPayment().update();
        order.update();
    }

    public void payForOrder(ClientOrder order) throws ApplicationException{
        if (order.getStatus() != Order.OrderStatus.ACCEPTED)
            throw new ApplicationException("Wrong order status");
        Payment payment = order.getPayment();
        if (payment == null)
            throw new ApplicationException("No payment in order");
        payment.setPaid();
        order.setStatus(Order.OrderStatus.PAID);
        payment.update();
        order.update();
    }

    public void cancelOrder (ClientOrder order) throws ApplicationException {
        switch (order.getStatus()) {
            case NEW:
            case SUBMITTED:
                order.setStatus(Order.OrderStatus.CANCELED);
                break;
            case ACCEPTED:
            case PAID:
            case DONE:
                order.cancelPayment();
                order.returnItemsToStorage();
                order.setStatus(Order.OrderStatus.CANCELED);
                break;
            default:
                throw new ApplicationException("Can't cancel order in current status", ApplicationException.Type.ORDER_STATUS);
        }
    }

    public List<ClientOrder> getOrders() {
        OrderAccessor orderAccessor = (OrderAccessor) getRegistry().getAccessor(Order.class);
        try {
            return orderAccessor.getOrdersByClient(this);
        } catch (ApplicationException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
