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
import system.model.Technician;
import system.ui.App;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class NewTechnicianDialog extends JDialog {
    private static final String TITLE = "Yeni Teknisyen";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldName;
    private JTextField fieldPhone;

    private final AddTechnicianListener listener;

    public NewTechnicianDialog(AddTechnicianListener listener) {
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
            onOK(name, phone);
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

    private int insertTechnician(Technician technician) {
        return App.getDatabaseHelper().insertTechnician(technician);
    }

    private void onOK(String name, String phone) {
        Technician t = new Technician(name, phone);
        t.setId(insertTechnician(t));
        listener.onAddTechnician(t);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static interface AddTechnicianListener {
        void onAddTechnician(Technician newTechnician);
    }

}
