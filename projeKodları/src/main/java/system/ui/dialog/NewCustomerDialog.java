package system.ui.dialog;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import system.model.Customer;
import system.ui.App;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class NewCustomerDialog extends JDialog {
    private static final String TITLE = "Yeni Müşteri";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldName;
    private JTextField fieldPhone;
    private JTextField fieldAddress;

    private final AddCustomerListener listener;

    public NewCustomerDialog(AddCustomerListener listener) {
        this.listener = listener;

        setTitle(TITLE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);
        
        buttonOK = new JButton("Tamam");
        buttonOK.setBounds(50, 330, 100, 30);
        buttonOK.setEnabled(true);
        contentPane.add(buttonOK);

        buttonCancel = new JButton("İptal");
        buttonCancel.setBounds(200, 330, 100, 30);
        contentPane.add(buttonCancel);

        JLabel labelCode = new JLabel("Telefon Numarası:");
        labelCode.setBounds(10, 10, 310, 30);
        contentPane.add(labelCode);

        fieldPhone = new JTextField();
        fieldPhone.setBounds(10, 40, 310, 30);
        contentPane.add(fieldPhone);

        JLabel labelName = new JLabel("İsim:");
        labelName.setBounds(10, 80, 310, 30);
        contentPane.add(labelName);

        fieldName = new JTextField();
        fieldName.setBounds(10, 110, 310, 30);
        contentPane.add(fieldName);

        JLabel labelPrice = new JLabel("Adres:");
        labelPrice.setBounds(10, 150, 310, 30);
        contentPane.add(labelPrice);

        fieldAddress = new JTextField();
        fieldAddress.setBounds(10, 180, 310, 30);
        contentPane.add(fieldAddress);
        
        setSize(360, 420);

        contentPane.setLayout(null);

        buttonOK.addActionListener(e -> {
            String name = fieldName.getText().trim();
            if(name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "İsim kısmı boş bırakılamaz!");
                return;
            }
            String phone = fieldPhone.getText().trim();
            String regexPhone = "5\\d{2}-\\d{3}-\\d{4}";
            if(!Pattern.matches(regexPhone, phone)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir telefon giriniz!");
                return;
            }
            String address = fieldAddress.getText().trim();
            if(address.length() < 5) {
                JOptionPane.showMessageDialog(null, "Adres çok kısa!");
                return;
            }
            onOK(name, phone, address);
        });

        buttonCancel.addActionListener(e -> onCancel());
        
        contentPane.add(buttonOK);
        contentPane.add(buttonCancel);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private int insertCustomer(Customer customer) {
        return App.getDatabaseHelper().insertCustomer(customer);
    }

    private void onOK(String name, String phone, String address) {
        
        Customer c = new Customer(name, phone, address);
        c.setId(insertCustomer(c));
        listener.onAddCustomer(c);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public interface AddCustomerListener {
        void onAddCustomer(Customer newCustomer);
    }
    
}