package shaverma.frontend;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import shaverma.service.StorageItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientForm extends BaseApplicationForm {

    private JPanel clientForm;
    private JButton changeUserButton;
    private JTabbedPane userTabs;
    private JPanel ordersTab;
    private JPanel makeOrderTab;
    private JTable ordersTable;
    private JButton btnCreateOrder;
    private JTable newOrderTable;
    private JButton deleteItemButton;
    private JButton addItemButton;
    private JButton submitButton;
    private JButton discardButton;
    private JTable clientPaymentsTable;
    private JButton makePaymentButton;
    private JButton cancelButton;
    private JTable completeOrdersTable;
    private JButton acceptButton;
    private JButton rejectButton;
    private Map<String, Integer> priceMap;
    private Map<String, Object> selections;

    @Override
    JPanel createFormPanel() {
        return clientForm;
    }

    @Override
    String getTitle() {
        return String.format("Пользователь %s", getService().activeUserName());
    }

    @Override
    Dimension getSize() {
        return new Dimension(1000, 650);
    }

    public ClientForm(Application app) {
        super(app);
        priceMap = getService().getStoragePrices();
        initSelections();
        initChangeUserButton();
        initOrdersTable();
        initAddItemButton();
        initNewOrderTable();
        initRemoveItemButton();
        initDiscardButton();
        initSubmitButton();
        initClientPaymentsTable();
        initMakePaymentButton();
        initCancelOrderButton();
        initCompleteOrdersTable();
          initRejectButton();
       initAcceptButton();
        initTabs();
    }

    private void initSelections() {
        selections = new HashMap<>();
        String[] fields = {
                "selectedListOrder",
                "selectedCompleteOrderId",
                "selectedPaymentOrderId",
                "selectedItemName"
        };
        for (String field : fields)
            selections.put(field, null);
    }

    private void initializationSwitch(int tabIndex) {
        switch (tabIndex) {
            case 0:
                initOrdersTable();
                break;
            case 1:
                initNewOrderTable();
                break;
            case 2:
                initClientPaymentsTable();
                break;
            case 3:
                initCompleteOrdersTable();
                break;
            default:
                System.out.println(tabIndex);
        }
    }

    private void initTabs() {
        userTabs.addChangeListener(changeEvent -> {
            JTabbedPane pane = (JTabbedPane) changeEvent.getSource();
            int tabIndex = pane.getSelectedIndex();
            initializationSwitch(tabIndex);
        });
    }

    private void initChangeUserButton() {
        changeUserButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                getService().logout();
                switchToForm(new LoginForm(getApp()));
            }
        });
    }

    private void initOrdersTable() {
        List<Triplet<Integer, String, String>> clientOrders = getService().getActiveClientOrdersList();
        String column_names[] = {"Количество заказов", "Товар", "Статус"};
        ordersTable.setModel(new DefaultTableModel(column_names, 3) {
            @Override
            public int getRowCount() {
                return clientOrders.size();
            }

            @Override
            public Object getValueAt(int i, int j) {
                return clientOrders.get(i).getValue(j);
            }

            @Override
            public boolean isCellEditable(int i, int j) {
                return false;
            }
        });
        initTableClickHandler(ordersTable, "selectedListOrder");
        initEnterPressHandler(ordersTable, "selectedListOrder");
    }

    private void initTableClickHandler(JTable table, String fieldName) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < table.getRowCount()) {
                    selections.put(fieldName, table.getValueAt(selectedRow, 0));
                } else {
                    selections.put(fieldName, null);
                }
                if (mouseEvent.getClickCount() == 2) {
                    Object field = selections.get(fieldName);
                    if (field instanceof Integer)
                        getApp().openForm(new OrderDetailForm(getApp(), (Integer) field));
                }
            }
        });
    }

    private void initEnterPressHandler(JTable table, String fieldName) {
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        table.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (selections.get(fieldName) != null) {
                    System.out.println("Enter pressed with key" + selections.get(fieldName));
                }
            }
        });
    }

    private void initAddItemButton() {
        addItemButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                Application app = getApp();
                app.openForm(new AddItemForm(app));
            }
        });
    }

    private void initNewOrderTable() {
        List<StorageItem> newOrderItems = getService().getNewOrderItems();
        String column_names[] = {"Наименование", "Колицество", "Цена", "Стоимость"};
        newOrderTable.setModel(new DefaultTableModel(column_names, 4) {
            @Override
            public int getRowCount() {
                return newOrderItems.size();
            }

            @Override
            public Object getValueAt(int i, int j) {
                String name = newOrderItems.get(i).getName();
                int amount = newOrderItems.get(i).getAmount();
                int price = priceMap.getOrDefault(name, -1);
                long cost = price >= 0 ? price * amount : -1;
                switch (j) {
                    case 0:
                        return name;
                    case 1:
                        return amount;
                    case 2:
                        return price >= 0 ? price : "N/A";
                    case 3:
                        return cost >= 0 ? cost : "N/A";
                }
                return "";
            }

            @Override
            public boolean isCellEditable(int i, int j) {
                return false;
            }
        });
        initTableClickHandler(newOrderTable, "selectedItemName");
    }

    private void initRemoveItemButton() {
        deleteItemButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                String selectedItemName = (String) selections.get("selectedItemName");
                if (selectedItemName != null) {
                    getService().removeComponentFromNewOrder(selectedItemName);
                    selections.put("selectedItemName", null);
                    initNewOrderTable();
                }
            }
        });
    }

    private void initDiscardButton() {
        discardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                getService().discardClientOrder();
                initNewOrderTable();
            }
        });
    }

    private void initSubmitButton() {
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                getService().submitClientOrder();
                initOrdersTable();
                initNewOrderTable();
            }
        });
    }

    @Override
    public void updateForm() {
        initNewOrderTable();
    }

    private void initClientPaymentsTable() {
        List<Triplet<Integer, Integer, String>> payments = getService().getActiveClientPayments();
        String column_names[] = {"Номер заказа", "Количество", "Статус оплаты"};
        clientPaymentsTable.setModel(new DefaultTableModel(column_names, 3) {
            @Override
            public int getRowCount() {
                return payments.size();
            }

            @Override
            public Object getValueAt(int i, int j) {
                return payments.get(i).getValue(j);
            }

            @Override
            public boolean isCellEditable(int i, int j) {
                return false;
            }
        });
        initTableClickHandler(clientPaymentsTable, "selectedPaymentOrderId");
    }

    private void initMakePaymentButton() {
        makePaymentButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                Integer selectedPaymentOrderId = (Integer) selections.get("selectedPaymentOrderId");
                if (selectedPaymentOrderId != null) {
                    getService().makePayment(selectedPaymentOrderId);
                    initClientPaymentsTable();
                    initOrdersTable();
                }
            }
        });
    }

    private void initCancelOrderButton() {
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                Integer selectedPaymentOrderId = (Integer) selections.get("selectedPaymentOrderId");
                if (selectedPaymentOrderId != null) {
                    getService().cancelOrderByClient(selectedPaymentOrderId);
                    initClientPaymentsTable();
                    initOrdersTable();
                }
            }
        });
    }

    private void initCompleteOrdersTable() {
        List<Pair<Integer, String>> orders = getService().getCompleteClientOrders();
        String column_names[] = {"Номер заказа", "Состав"};
        completeOrdersTable.setModel(new DefaultTableModel(column_names, 2) {
            @Override
            public int getRowCount() {
                return orders.size();
            }

            @Override
            public Object getValueAt(int i, int j) {
                return orders.get(i).getValue(j);
            }

            @Override
            public boolean isCellEditable(int i, int j) {
                return false;
            }
        });
        initTableClickHandler(completeOrdersTable, "selectedCompleteOrderId");
    }

    private void initRejectButton() {
        rejectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                Integer selectedCompleteOrderId = (Integer) selections.get("selectedCompleteOrderId");
                if (selectedCompleteOrderId != null) {
                    getService().cancelOrderByClient(selectedCompleteOrderId);
                    initCompleteOrdersTable();
                    initClientPaymentsTable();
                    initOrdersTable();
                }
            }
        });
    }

    private void initAcceptButton() {
        acceptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                Integer selectedCompleteOrderId = (Integer) selections.get("selectedCompleteOrderId");
                if (selectedCompleteOrderId != null) {
                    getService().acceptCompleteOrder(selectedCompleteOrderId);
                    initCompleteOrdersTable();
                    initClientPaymentsTable();
                    initOrdersTable();
                }
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        clientForm = new JPanel();
        clientForm.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        changeUserButton = new JButton();
        changeUserButton.setText("Сменить пользователя");
        clientForm.add(changeUserButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        clientForm.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        userTabs = new JTabbedPane();
        clientForm.add(userTabs, new GridConstraints(1, 0, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        ordersTab = new JPanel();
        ordersTab.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        userTabs.addTab("Заказы", ordersTab);
        final JScrollPane scrollPane1 = new JScrollPane();
        ordersTab.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ordersTable = new JTable();
        scrollPane1.setViewportView(ordersTable);
        makeOrderTab = new JPanel();
        makeOrderTab.setLayout(new GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        userTabs.addTab("Сделать заказ", makeOrderTab);
        final Spacer spacer2 = new Spacer();
        makeOrderTab.add(spacer2, new GridConstraints(1, 0, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 1, new Insets(5, 5, 5, 5), -1, -1));
        makeOrderTab.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addItemButton = new JButton();
        addItemButton.setText("Добавить позицию");
        panel1.add(addItemButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteItemButton = new JButton();
        deleteItemButton.setText("Удалить позицию");
        panel1.add(deleteItemButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        submitButton = new JButton();
        submitButton.setText("Подтвердить");
        panel1.add(submitButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        discardButton = new JButton();
        discardButton.setText("Отменить");
        panel1.add(discardButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        makeOrderTab.add(scrollPane2, new GridConstraints(0, 1, 7, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        newOrderTable = new JTable();
        scrollPane2.setViewportView(newOrderTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        userTabs.addTab("Оплата", panel2);
        final JScrollPane scrollPane3 = new JScrollPane();
        panel2.add(scrollPane3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        clientPaymentsTable = new JTable();
        scrollPane3.setViewportView(clientPaymentsTable);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        makePaymentButton = new JButton();
        makePaymentButton.setText("Оплатить");
        panel3.add(makePaymentButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Отменить");
        panel3.add(cancelButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        userTabs.addTab("Завершенные", panel4);
        final JScrollPane scrollPane4 = new JScrollPane();
        panel4.add(scrollPane4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        completeOrdersTable = new JTable();
        scrollPane4.setViewportView(completeOrdersTable);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        acceptButton = new JButton();
        acceptButton.setText("Принять");
        panel5.add(acceptButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel5.add(spacer4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        rejectButton = new JButton();
        rejectButton.setText("Отказать");
        panel5.add(rejectButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return clientForm;
    }
}