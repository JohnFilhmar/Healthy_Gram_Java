package healthy_gram;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import healthy_gram.controllers.InventoryController;
public class Inventory extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField textItemName;
    private JTextField textItemQty;
    private JTextField textItemPrice;
    private JComboBox<String> comboUnit;
    private DefaultTableModel tableModel;
    private String[][] itemRows;

    public Inventory() {
        setTitle("Inventory Management - Healthy Gram");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        contentPane = new JPanel();
        contentPane.setBorder(new LineBorder(new Color(0, 102, 51), 2));
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(51, 102, 51));
        setContentPane(contentPane);

        tableModel = new DefaultTableModel(
       	    new Object[][] {},
       	    new String[] {"Id", "Item Name", "Description", "Price", "Weight", "Expiration Date"}
       	);
       	table = new JTable(tableModel);
       	// Fetch items from the inventory controller
       	InventoryController ic = new InventoryController();
       	List<Map<String, Object>> items = ic.fetchItems();
       	// Initialize the itemRows array
       	String[][] itemRows = new String[items.size()][6]; // 6 columns based on the table headers
       	int j = 0;
       	for (Map<String, Object> row : items) {
       	    int k = 0;
       	    for (Map.Entry<String, Object> entry : row.entrySet()) {
       	        itemRows[j][k] = entry.getValue().toString(); // Convert value to string
       	        k++;
       	    }
       	    j++;
       	}
        // Add rows to the table model
        for (String[] row : itemRows) {
            tableModel.addRow(row);
        }
        
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setBorder(new LineBorder(new Color(85, 136, 59), 1));
        table.setBackground(new Color(255, 255, 204));
        table.setBounds(20, 20, 500, 400);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 500, 400);
        contentPane.add(scrollPane);

        // Labels and text fields for item details
        JLabel lblItemName = new JLabel("Item Name:");
        lblItemName.setFont(new Font("Bradley Hand ITC", Font.BOLD, 21));
        lblItemName.setForeground(new Color(255, 255, 204));
        lblItemName.setBounds(540, 20, 150, 30);
        contentPane.add(lblItemName);

        textItemName = new JTextField();
        textItemName.setFont(new Font("Arial", Font.PLAIN, 14));
        textItemName.setBounds(540, 60, 220, 30);
        textItemName.setBackground(new Color(255, 255, 204));
        contentPane.add(textItemName);

        JLabel lblItemQty = new JLabel("Quantity:");
        lblItemQty.setFont(new Font("Bradley Hand ITC", Font.BOLD, 21));
        lblItemQty.setForeground(new Color(255, 255, 204));
        lblItemQty.setBounds(540, 100, 150, 30);
        contentPane.add(lblItemQty);

        textItemQty = new JTextField();
        textItemQty.setFont(new Font("Arial", Font.PLAIN, 14));
        textItemQty.setBounds(540, 140, 220, 30);
        textItemQty.setBackground(new Color(255, 255, 204));
        contentPane.add(textItemQty);

        JLabel lblUnit = new JLabel("Unit:");
        lblUnit.setFont(new Font("Bradley Hand ITC", Font.BOLD, 21));
        lblUnit.setForeground(new Color(255, 255, 204));
        lblUnit.setBounds(540, 180, 150, 30);
        contentPane.add(lblUnit);

        // Combo box for unit selection
        comboUnit = new JComboBox<>(new String[] {"grams", "kilos", "packs"});
        comboUnit.setFont(new Font("Arial", Font.PLAIN, 14));
        comboUnit.setBounds(540, 220, 220, 30);
        comboUnit.setBackground(new Color(255, 255, 204));
        contentPane.add(comboUnit);

        JLabel lblItemPrice = new JLabel("Price:");
        lblItemPrice.setFont(new Font("Bradley Hand ITC", Font.BOLD, 21));
        lblItemPrice.setForeground(new Color(255, 255, 204));
        lblItemPrice.setBounds(540, 260, 150, 30);
        contentPane.add(lblItemPrice);

        textItemPrice = new JTextField();
        textItemPrice.setFont(new Font("Arial", Font.PLAIN, 14));
        textItemPrice.setBounds(540, 300, 220, 30);
        textItemPrice.setBackground(new Color(255, 255, 204));
        contentPane.add(textItemPrice);

        // Buttons for inventory actions
        JButton btnAddItem = new JButton("Add Item");
        btnAddItem.setFont(new Font("Bodoni MT Condensed", Font.BOLD, 24));
        btnAddItem.setBounds(540, 350, 220, 40);
        btnAddItem.setBackground(new Color(85, 136, 59));
        btnAddItem.setForeground(Color.WHITE);
        btnAddItem.setBorder(BorderFactory.createLineBorder(new Color(85, 136, 59), 1));
        contentPane.add(btnAddItem);

        JButton btnEditItem = new JButton("Edit Item");
        btnEditItem.setFont(new Font("Bodoni MT Condensed", Font.BOLD, 24));
        btnEditItem.setBounds(540, 400, 220, 40);
        btnEditItem.setBackground(new Color(194, 230, 154));
        btnEditItem.setForeground(new Color(85, 136, 59));
        btnEditItem.setBorder(BorderFactory.createLineBorder(new Color(85, 136, 59), 1));
        contentPane.add(btnEditItem);

        JButton btnDeleteItem = new JButton("Delete Item");
        btnDeleteItem.setFont(new Font("Bodoni MT Condensed", Font.BOLD, 24));
        btnDeleteItem.setBounds(540, 450, 220, 40);
        btnDeleteItem.setBackground(new Color(194, 230, 154));
        btnDeleteItem.setForeground(new Color(85, 136, 59));
        btnDeleteItem.setBorder(BorderFactory.createLineBorder(new Color(85, 136, 59), 1));
        contentPane.add(btnDeleteItem);

        // Action listeners for buttons
        btnAddItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = textItemName.getText();
                int qty = Integer.parseInt(textItemQty.getText());
                String unit = comboUnit.getSelectedItem().toString();
                double price = Double.parseDouble(textItemPrice.getText());
                tableModel.addRow(new Object[]{name, qty, unit, price});
                clearFields();
            }
        });

        btnEditItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.setValueAt(textItemName.getText(), selectedRow, 0);
                    tableModel.setValueAt(Integer.parseInt(textItemQty.getText()), selectedRow, 1);
                    tableModel.setValueAt(comboUnit.getSelectedItem().toString(), selectedRow, 2);
                    tableModel.setValueAt(Double.parseDouble(textItemPrice.getText()), selectedRow, 3);
                    clearFields();
                }
            }
        });

        btnDeleteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                }
            }
        });
    }

    private void clearFields() {
        textItemName.setText("");
        textItemQty.setText("");
        textItemPrice.setText("");
        comboUnit.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Inventory frame = new Inventory();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}