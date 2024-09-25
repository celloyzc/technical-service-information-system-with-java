/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.ui.dialog;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import system.model.Complaint;
import system.model.ComplaintStatus;
import system.ui.App;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class PayForComplaintDialog extends JDialog {
    private static final String TITLE = "Ödeme Yap";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldId;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JTextField fieldTotalFee;

    private final PayForComplaintListener listener;

    public PayForComplaintDialog(Complaint complaint, PayForComplaintListener listener) {
        this.listener = listener;

        setTitle(TITLE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setPreferredSize(new Dimension(400, 300));
        pack();
        setLocationRelativeTo(null);
        
        contentPane.setLayout(null);
        
        JLabel labelId = new JLabel("Şikayet ID:");
        labelId.setBounds(10, 10, 80, 30);
        contentPane.add(labelId);

        fieldId = new JTextField();
        fieldId.setBounds(100, 10, 270, 30);
        fieldId.setEditable(false);
        contentPane.add(fieldId);

        JLabel labelComplaint = new JLabel("Şikayet:");
        labelComplaint.setBounds(10, 50, 80, 30);
        contentPane.add(labelComplaint);

        fieldComplaint = new JTextField();
        fieldComplaint.setBounds(100, 50, 270, 30);
        fieldComplaint.setEditable(false);
        contentPane.add(fieldComplaint);

        JLabel labelCustomerName = new JLabel("Müşteri Adı:");
        labelCustomerName.setBounds(10, 90, 80, 30);
        contentPane.add(labelCustomerName);

        fieldCustomerName = new JTextField();
        fieldCustomerName.setBounds(100, 90, 270, 30);
        fieldCustomerName.setEditable(false);
        contentPane.add(fieldCustomerName);

        JLabel labelDeviceSerialNumber = new JLabel("Cihaz Seri No:");
        labelDeviceSerialNumber.setBounds(10, 130, 100, 30);
        contentPane.add(labelDeviceSerialNumber);

        fieldDeviceSerialNumber = new JTextField();
        fieldDeviceSerialNumber.setBounds(100, 130, 270, 30);
        fieldDeviceSerialNumber.setEditable(false);
        contentPane.add(fieldDeviceSerialNumber);

        JLabel labelTotalFee = new JLabel("Toplam Ücret:");
        labelTotalFee.setBounds(10, 170, 100, 30);
        contentPane.add(labelTotalFee);

        fieldTotalFee = new JTextField();
        fieldTotalFee.setBounds(100, 170, 270, 30);
        fieldTotalFee.setEditable(false);
        contentPane.add(fieldTotalFee);

        buttonOK = new JButton("Ödeme Yap");
        buttonOK.setBounds(50, 210, 120, 30);
        contentPane.add(buttonOK);

        buttonCancel = new JButton("İptal");
        buttonCancel.setBounds(230, 210, 120, 30);
        contentPane.add(buttonCancel);

        fieldId.setText(String.valueOf(complaint.getId()));
        fieldComplaint.setText(complaint.getComplaint());
        fieldCustomerName.setText(complaint.getCustomer().getName());
        fieldTotalFee.setText(String.valueOf(complaint.getTotalFee()));
        fieldDeviceSerialNumber.setText(complaint.getSerialNumber());

        buttonOK.addActionListener(e -> onOK(complaint));

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

    private void onOK(Complaint complaint) {
        complaint.setStatus(ComplaintStatus.COMPLETED);
        pay(complaint);
        dispose();
        listener.onPay(complaint);
    }

    private void pay(Complaint complaint) {
        App.getDatabaseHelper().updateComplaint(complaint);
        App.getDatabaseHelper().insertPayment(complaint);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface PayForComplaintListener {
        void onPay(Complaint complaint);
    }

}

