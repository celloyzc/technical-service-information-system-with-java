
package system.ui.dialog;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import system.model.Customer;
import system.model.document.SingleDocumentListener;
import system.model.table.CustomerTableModel;
import system.ui.App;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class SelectCustomerDialog extends JDialog {
    private static final String TITLE = "Müşteri Seç";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldName;
    private JTable tableCustomers;

    private final SelectCustomerListener listener;
    private Customer selectedCustomer;
    private CustomerTableModel tableModel;

    public SelectCustomerDialog(SelectCustomerListener listener) {
        this.listener = listener;

        setTitle(TITLE);
        
        contentPane = new JPanel();
        setContentPane(this.contentPane);
        setPreferredSize(new Dimension(630, 530));
        setModal(true);
        getRootPane().setDefaultButton(this.buttonOK);
        pack();
        setLocationRelativeTo(null);
        
        tableCustomers = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableCustomers);
        scrollPane.setBounds(10, 70, 600, 400);
        contentPane.add(scrollPane);

        buttonOK = new JButton("Tamam");
        buttonOK.setBounds(10, 30, 120, 30);
        buttonOK.setEnabled(false);
        contentPane.add(buttonOK);

        buttonCancel = new JButton("İptal");
        buttonCancel.setBounds(140, 30, 120, 30);
        contentPane.add(buttonCancel);
        
        JLabel labelDT = new JLabel("İsim Arama:");
        labelDT.setBounds(270, 10, 200, 20);
        contentPane.add(labelDT);

        fieldName = new JTextField();
        fieldName.setBounds(270, 30, 200, 30);
        contentPane.add(fieldName);

        contentPane.setLayout(null);

        setRenderer(tableCustomers);
        
        List<Customer> customers = fetchAllCustomers();
        listAll(customers);

        setSelectedCustomer(null);

        fieldName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldName.getText());
        });

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
     public static interface SelectCustomerListener {
        void onSelectCustomer(Customer param1Customer);
  }
    
    private List<Customer> fetchAllCustomers() {
        return App.getDatabaseHelper().getCustomers();
    }

    private void listAll(List<Customer> customers) {
        this.tableModel = new CustomerTableModel(customers);
        this.tableCustomers.setModel(this.tableModel);
        this.tableCustomers.setSelectionMode(0);
        this.tableCustomers.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = this.tableCustomers.getSelectedRow();
            if (selectedIndex < 0) {
                setSelectedCustomer((Customer)null);
                return;
            }
            setSelectedCustomer((Customer)this.tableModel.getItem(selectedIndex));
        });
    }

    private void onOK() {
        this.listener.onSelectCustomer(selectedCustomer);
        dispose();
    }

    private void onCancel() {
        dispose();
    }


    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        if(selectedCustomer == null) {
            this.buttonOK.setEnabled(false);
        } else {
            this.buttonOK.setEnabled(true);
        }
    }
}
