package shaverma.data;

import shaverma.logic.ClientOrder;
import shaverma.logic.Order;
import shaverma.logic.UserAccessor;
import shaverma.logic.Item;
import shaverma.logic.OrderAccessor;
import shaverma.logic.WholesaleOrder;
import shaverma.logic.Entity;
import shaverma.logic.Client;
import shaverma.logic.PaymentAccessor;
import shaverma.logic.Payment;
import shaverma.logic.ForeignKey;
import shaverma.logic.BaseUser;
import shaverma.logic.AccessorRegistry;
import shaverma.exceptions.ApplicationException;
import shaverma.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderMapper extends BasicMapper implements OrderAccessor {

    private static final String CLIENT_TYPE = "CLIENT";
    private static final String MANAGER_TYPE = "MANAGER";

    public OrderMapper(String url, AccessorRegistry registry) {
        super(url, registry);
    }

    @Override
    Entity parseResultSetEntry(ResultSet resultSet, String idColumn) throws ApplicationException {
        try {
            int id = resultSet.getInt("id");
            String status = resultSet.getString("status");
            Integer from_id = resultSet.getInt("from_id");
            Integer to_id = resultSet.getInt("to_id");
            Integer payment_id = resultSet.getInt("payment_id");
            String orderType = resultSet.getString("order_type");
            UserAccessor userAccessor = (UserAccessor) getRegistry().getAccessor(BaseUser.class);
            PaymentAccessor paymentAccessor = (PaymentAccessor) getRegistry().getAccessor(Payment.class);
            ForeignKey<BaseUser> from = new ForeignKey<>(from_id, userAccessor);
            ForeignKey<BaseUser> to = new ForeignKey<>(to_id, userAccessor);
            ForeignKey<Payment> payment = new ForeignKey<>(payment_id, paymentAccessor);
            Order order;
            if (orderType.equals(CLIENT_TYPE)) {
                order = new ClientOrder(from, getRegistry(), id);
            } else {
                order = new WholesaleOrder(from, to, getRegistry(), id);
            }
            order.setFrom(from);
            order.setTo(to);
            order.setPayment(payment);
            order.setStatus(Order.OrderStatus.valueOf(status));
            return order;
        } catch (SQLException e) {
            throw new ApplicationException(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    @Override
    Map<String, Object> getDatabaseFields(Entity entity) {
        Map<String, Object> fieldMap = new HashMap<>();
        Order order = (Order) entity;
        BaseUser from = order.getFrom();
        Integer fromId = from == null ? null : from.getId();
        fieldMap.put("from_id", fromId);
        BaseUser to = order.getTo();
        Integer toId = to == null ? null : to.getId();
        fieldMap.put("to_id", toId);
        fieldMap.put("status", order.getStatus().name());
        Payment payment = order.getPayment();
        Integer paymentId = payment == null ? null : payment.getId();
        fieldMap.put("payment_id", paymentId);
        return fieldMap;
    }

    @Override
    String getTableNameBase() {
        return "order";
    }

    @Override
    public List<ClientOrder> getOrdersByClient(Client client) throws ApplicationException {
        try(Connection connection = getConnection()) {
            String statement = String.format((new StringBuilder())
                    .append("SELECT * ")
                    .append("FROM %s ")
                    .append("WHERE order_type=? AND from_id=?")
                    .toString(), getTableName());
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, CLIENT_TYPE);
            preparedStatement.setInt(2, client.getId());
            List<ClientOrder> resultOrders = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ClientOrder nextOrder = (ClientOrder) parseResultSetEntry(resultSet);
                resultOrders.add(nextOrder);
            }
            return resultOrders;
        } catch (SQLException ex) {
            throw new ApplicationException(String.format("SQL exception: %s", ex.getMessage()));
        }
    }

    public List<? extends Order> getOrdersBySourceUser(BaseUser user) throws ApplicationException {
        return getOrdersBySomeUser(user, "from_id");
    }

    public List<? extends Order> getOrdersByTargetUser(BaseUser user) throws ApplicationException {
        return getOrdersBySomeUser(user, "to_id");
    }

    private List<Order> getOrdersBySomeUser(BaseUser user, String userIdFieldName) throws ApplicationException{
        try(Connection connection = getConnection()) {
            String statement = String.format((new StringBuilder())
                    .append("SELECT * ")
                    .append("FROM %s ")
                    .append("WHERE %s=?")
                    .toString(), getTableName(), userIdFieldName);
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, user.getId());
            List<Order> resultOrders = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order nextOrder = (Order) parseResultSetEntry(resultSet);
                resultOrders.add(nextOrder);
            }
            return resultOrders;
        } catch (SQLException ex) {
            throw new ApplicationException(String.format("SQL exception: %s", ex.getMessage()));
        }
    }

    @Override
    Map<String, Pair<List<? extends Entity>, BasicMapper>> getM2MFields(Entity entity) {
        Map<String, Pair<List<? extends Entity>, BasicMapper>> m2mFields = new HashMap<>();
        Order order = (Order) entity;
        BasicMapper itemMapper = (ItemMapper) getRegistry().getAccessor(Item.class);
        m2mFields.put("items", new Pair<>(order.getItems(), itemMapper));
        return m2mFields;
    }

    public List<ClientOrder> getAllClientOrders() throws ApplicationException {
        try(Connection connection = getConnection()) {
            String statement = String.format((new StringBuilder())
                    .append("SELECT * ")
                    .append("FROM %s ")
                    .append("WHERE order_type=?")
                    .toString(), getTableName());
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, CLIENT_TYPE);
            List<ClientOrder> resultOrders = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ClientOrder nextOrder = (ClientOrder) parseResultSetEntry(resultSet);
                resultOrders.add(nextOrder);
            }
            return resultOrders;
        } catch (SQLException ex) {
            throw new ApplicationException(String.format("SQL exception: %s", ex.getMessage()));
        }
    }

    public List<WholesaleOrder> getAllWholesaleOrders() throws ApplicationException {
        try(Connection connection = getConnection()) {
            String statement = String.format((new StringBuilder())
                    .append("SELECT * ")
                    .append("FROM %s ")
                    .append("WHERE order_type=?")
                    .toString(), getTableName());
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, MANAGER_TYPE);
            List<WholesaleOrder> resultOrders = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                WholesaleOrder nextOrder = (WholesaleOrder) parseResultSetEntry(resultSet);
                resultOrders.add(nextOrder);
            }
            return resultOrders;
        } catch (SQLException ex) {
            throw new ApplicationException(String.format("SQL exception: %s", ex.getMessage()));
        }
    }
}
