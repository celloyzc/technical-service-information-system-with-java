
package system.ui.dialog;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import system.model.Component;
import system.ui.App;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class NewComponentDialog extends JDialog {
    private static final String TITLE = "Yeni Parça";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldCode;
    private JTextField fieldName;
    private JTextField fieldPrice;
    private JTextField fieldStock;
    private JCheckBox checkBoxGenerateCode;

    private final AddComponentListener listener;

    public NewComponentDialog(AddComponentListener listener) {
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

        JLabel labelCode = new JLabel("Kod:");
        labelCode.setBounds(10, 10, 310, 30);
        contentPane.add(labelCode);

        fieldCode = new JTextField();
        fieldCode.setBounds(10, 40, 310, 30);
        contentPane.add(fieldCode);

        JLabel labelName = new JLabel("İsim:");
        labelName.setBounds(10, 80, 310, 30);
        contentPane.add(labelName);

        fieldName = new JTextField();
        fieldName.setBounds(10, 110, 310, 30);
        contentPane.add(fieldName);

        JLabel labelPrice = new JLabel("Fiyat:");
        labelPrice.setBounds(10, 150, 310, 30);
        contentPane.add(labelPrice);

        fieldPrice = new JTextField();
        fieldPrice.setBounds(10, 180, 310, 30);
        contentPane.add(fieldPrice);

        JLabel labelStock = new JLabel("Stok:");
        labelStock.setBounds(10, 220, 310, 30);
        contentPane.add(labelStock);

        fieldStock = new JTextField();
        fieldStock.setBounds(10, 250, 310, 30);
        contentPane.add(fieldStock);

        checkBoxGenerateCode = new JCheckBox("Otomatik Kod Oluştur");
        checkBoxGenerateCode.setBounds(10, 290, 310, 30);
        contentPane.add(checkBoxGenerateCode);

        
        setSize(350, 420);

        contentPane.setLayout(null);

        checkBoxGenerateCode.addChangeListener(e -> {
            if (checkBoxGenerateCode.isSelected()) {
                fieldCode.setEnabled(false);
                fieldCode.setText("");
            } else {
                fieldCode.setEnabled(true);
            }
        });

        buttonOK.addActionListener(e -> {
            boolean generateCode = checkBoxGenerateCode.isSelected();
            String code = generateCode ? null : fieldCode.getText().trim();
            if (!generateCode && code.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Kod kısmı boş bırakılamaz!");
                return;
            }
            String name = fieldName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "İsim kısmı boş bırakılamaz!");
                return;
            }
            String price = fieldPrice.getText().trim();
            String regexPrice = "\\d{1,9}";
            if (!Pattern.matches(regexPrice, price)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir fiyat giriniz!");
                return;
            }
            String stock = fieldStock.getText().trim();
            if (!Pattern.matches(regexPrice, stock)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir stok giriniz!");
                return;
            }
            onOK(code, name, Integer.parseInt(stock), Integer.parseInt(price));
        });

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

    private String insertComponent(Component c) {
        return App.getDatabaseHelper().insertComponent(c);
    }

    private void onOK(String code, String name, int stock, int price) {
        Component c = new Component(code, name, stock, price);
        c.setCode(insertComponent(c));
        listener.onAddComponent(c);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface AddComponentListener {
        void onAddComponent(Component component);
    }

}
