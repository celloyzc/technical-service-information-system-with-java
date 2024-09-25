/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import system.model.Component;
import system.model.StockHistoryItem;
import system.ui.App;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class DecreaseComponentStockDialog extends JDialog {
    private static final String TITLE = "Parça Çıkışı";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldCode;
    private JTextField fieldName;
    private JTextField fieldPrice;
    private JTextField fieldStock;
    private JTextField fieldOutput;

    private final DecreaseComponentStockListener listener;

    public DecreaseComponentStockDialog(Component c, DecreaseComponentStockListener listener) {
        this.listener = listener;

        setTitle(TITLE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
         
        contentPane.setLayout(null);

        JLabel labelCode = new JLabel("Kod:");
        labelCode.setBounds(20, 20, 100, 30);
        contentPane.add(labelCode);

        fieldCode = new JTextField();
        fieldCode.setBounds(130, 20, 200, 30);
        fieldCode.setEditable(false);
        contentPane.add(fieldCode);

        JLabel labelName = new JLabel("İsim:");
        labelName.setBounds(20, 60, 100, 30);
        contentPane.add(labelName);

        fieldName = new JTextField();
        fieldName.setBounds(130, 60, 200, 30);
        fieldName.setEditable(false);
        contentPane.add(fieldName);

        JLabel labelPrice = new JLabel("Fiyat:");
        labelPrice.setBounds(20, 100, 100, 30);
        contentPane.add(labelPrice);

        fieldPrice = new JTextField();
        fieldPrice.setBounds(130, 100, 200, 30);
        fieldPrice.setEditable(false);
        contentPane.add(fieldPrice);

        JLabel labelStock = new JLabel("Stok:");
        labelStock.setBounds(20, 140, 100, 30);
        contentPane.add(labelStock);

        fieldStock = new JTextField();
        fieldStock.setBounds(130, 140, 200, 30);
        fieldStock.setEditable(false);
        contentPane.add(fieldStock);

        JLabel labelOutput = new JLabel("Çıkış Adedi:");
        labelOutput.setBounds(20, 180, 100, 30);
        contentPane.add(labelOutput);

        fieldOutput = new JTextField();
        fieldOutput.setBounds(130, 180, 200, 30);
        contentPane.add(fieldOutput);

        buttonOK = new JButton("Tamam");
        buttonOK.setBounds(50, 230, 100, 30);
        contentPane.add(buttonOK);

        buttonCancel = new JButton("İptal");
        buttonCancel.setBounds(180, 230, 100, 30);
        contentPane.add(buttonCancel);
        
        pack();
        setSize(400, 320);
        setLocationRelativeTo(null);

        fieldCode.setText(c.getCode());
        fieldName.setText(c.getName());
        fieldStock.setText(String.valueOf(c.getStock()));
        fieldPrice.setText(String.valueOf(c.getPrice()));

        buttonOK.addActionListener(e -> {
            String stockOutput = fieldOutput.getText().trim();
            String regexOutput = "\\d{1,9}";
            if (!Pattern.matches(regexOutput, stockOutput)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir parça çıkış adedi giriniz!");
                return;
            }
            if (Integer.parseInt(stockOutput) == 0) {
                JOptionPane.showMessageDialog(null, "Parça çıkış adedi 0 olamaz!");
                return;
            }
            if (c.getStock() - Integer.parseInt(stockOutput) < 0) {
                JOptionPane.showMessageDialog(null, "Stok adedinden fazla parça çıkışı yapılamaz!");
                return;
            }
            onOK(c, Integer.parseInt(stockOutput));
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

    private void onOK(Component c, int output) {
        App.getDatabaseHelper().decreaseComponentStockById(c.getId(), output);
        StockHistoryItem stockHistoryItem = new StockHistoryItem(null, c, "PARÇA ÇIKIŞI", c.getStock(), c.getStock() - output);
        // stok kaydı tut
        App.getDatabaseHelper().insertStockHistoryItem(stockHistoryItem);
        int newStock = c.getStock() - output;
        listener.onDecreaseComponentStock(newStock);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface DecreaseComponentStockListener {
        void onDecreaseComponentStock(int newStock);
    }
}
