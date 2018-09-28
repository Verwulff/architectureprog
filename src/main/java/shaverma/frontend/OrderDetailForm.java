package shaverma.frontend;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.javatuples.Quintet;
import org.javatuples.Triplet;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class OrderDetailForm extends BaseApplicationForm {
    private JPanel orderDetailPanel;
    private JTable itemsTable;
    private JLabel orderNumber;
    private JLabel totalCost;
    private JLabel clientName;
    private JLabel orderStatus;
    private Integer orderId;

    @Override
    JPanel createFormPanel() {
        return orderDetailPanel;
    }

    @Override
    String getTitle() {
        return "���������� � ������";
    }

    @Override
    Dimension getSize() {
        return new Dimension(600, 300);
    }

    public OrderDetailForm(Application app, Integer orderId) {
        super(app);
        this.orderId = orderId;
        initComponents();
    }

    private void initComponents() {
        Quintet<Integer, List<Triplet<String, Integer, Integer>>, Integer, String, String> info =
                getService().getOrderDetailInfo(orderId);
        orderNumber.setText(info.getValue0().toString());
        totalCost.setText(info.getValue2().toString());
        clientName.setText(info.getValue3());
        orderStatus.setText(info.getValue4());
        List<Triplet<String, Integer, Integer>> itemInfo = info.getValue1();
        String column_names[] = {"������������", "����������", "����"};
        itemsTable.setModel(new DefaultTableModel(column_names, 3) {
            @Override
            public int getRowCount() {
                return itemInfo.size();
            }

            @Override
            public Object getValueAt(int i, int j) {
                return itemInfo.get(i).getValue(j);
            }

            @Override
            public boolean isCellEditable(int i, int j) {
                return false;
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
        orderDetailPanel = new JPanel();
        orderDetailPanel.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("����� ������");
        orderDetailPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        orderDetailPanel.add(spacer1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        orderNumber = new JLabel();
        orderNumber.setText("order number val");
        orderDetailPanel.add(orderNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        orderDetailPanel.add(panel1, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        itemsTable = new JTable();
        scrollPane1.setViewportView(itemsTable);
        final JLabel label2 = new JLabel();
        label2.setText("������");
        orderDetailPanel.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clientName = new JLabel();
        clientName.setText("��� �������");
        orderDetailPanel.add(clientName, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("���������");
        orderDetailPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        totalCost = new JLabel();
        totalCost.setText("Cost value");
        orderDetailPanel.add(totalCost, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("������");
        orderDetailPanel.add(label4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderStatus = new JLabel();
        orderStatus.setText("Status text");
        orderDetailPanel.add(orderStatus, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        orderDetailPanel.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return orderDetailPanel;
    }
}