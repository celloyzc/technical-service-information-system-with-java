/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.ui;

import java.awt.Dimension;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import system.model.Customer;
import system.model.document.SingleDocumentListener;
import system.model.table.CustomerTableModel;
import system.ui.dialog.NewCustomerDialog;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class CustomerScreen extends JFrame implements NewCustomerDialog.AddCustomerListener {

    private static final String TITLE = "Müşteri İşlemleri";

    private JPanel panel;
    private JTable tableCustomers;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JTextField fieldCustomerName;

    private Customer selectedItem;
    private CustomerTableModel tableModel;
    
    public CustomerScreen() {
        super(TITLE);
        panel = new JPanel();
        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);

        tableCustomers = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableCustomers);
        scrollPane.setBounds(10, 50, 880, 400);
        panel.add(scrollPane);

        buttonAdd = new JButton("Müşteri Ekle");
        buttonAdd.setBounds(10, 10, 120, 30);
        panel.add(buttonAdd);

        buttonRemove = new JButton("Müşteri Sil");
        buttonRemove.setBounds(140, 10, 120, 30);
        buttonRemove.setEnabled(false);
        panel.add(buttonRemove);

        fieldCustomerName = new JTextField();
        fieldCustomerName.setBounds(270, 10, 200, 30);
        panel.add(fieldCustomerName);

        panel.setLayout(null);

        setRenderer(tableCustomers);

        List<Customer> customers = fetchAllCustomers();
        listAll(customers);

        setSelectedItem(null);

        buttonAdd.addActionListener(e -> {
            new NewCustomerDialog(this).setVisible(true);
        });

        buttonRemove.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                    null,
                    selectedItem.getName() + " adlı müşteriyi silmek üzeresiniz. Onaylıyor musunuz?",
                    "Müşteri Silme Onayı",
                    JOptionPane.YES_NO_OPTION
            );
            if (n == JOptionPane.YES_OPTION) {
                removeCustomer();
            }
        });

        fieldCustomerName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldCustomerName.getText());
        });
}


    /*public CustomerScreen() {
        super(TITLE);
        panel = new JPanel();
        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableCustomers);

        List<Customer> customers = fetchAllCustomers();
        listAll(customers);

        setSelectedItem(null);
        
        tableCustomers = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableCustomers);
        scrollPane.setBounds(10, 50, 880, 400);
        panel.add(scrollPane);

        buttonAdd.addActionListener(e -> {
            new NewCustomerDialog(this).setVisible(true);
        });

        buttonRemove.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                    null,
                    selectedItem.getName() + " adlı müşteriyi silmek üzeresiniz. Onaylıyor musunuz?",
                    "Müşteri Silme Onayı",
                    JOptionPane.YES_NO_OPTION
            );
            if (n == JOptionPane.YES_OPTION) {
                removeCustomer();
            }
        });

        fieldCustomerName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldCustomerName.getText());
        });
    }*/

    private void removeCustomer() {
        App.getDatabaseHelper().deleteCustomerById(selectedItem.getId());
        tableModel.remove(selectedItem);
        fieldCustomerName.setText("");
    }

    private List<Customer> fetchAllCustomers() {
        return App.getDatabaseHelper().getCustomers();
    }

    private void listAll(List<Customer> customers) {
        tableModel = new CustomerTableModel(customers);
        tableCustomers.setModel(tableModel);
        tableCustomers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCustomers.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = tableCustomers.getSelectedRow();
            if (selectedIndex < 0) {
                setSelectedItem(null);
                return;
            }
            setSelectedItem(tableModel.getItem(selectedIndex));
        });
    }

    public void setSelectedItem(Customer selectedItem) {
        this.selectedItem = selectedItem;
        if (selectedItem == null) {
            buttonRemove.setEnabled(false);
        } else {
            buttonRemove.setEnabled(true);
        }
    }

    @Override
    public void onAddCustomer(Customer newCustomer) {
        tableModel.add(newCustomer);
        fieldCustomerName.setText("");
    }
}

