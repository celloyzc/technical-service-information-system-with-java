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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
public class RefundForComplaintDialog extends JDialog {
    private static final String TITLE = "İade İşlemi Yap";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldId;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JTextArea textAreaDescription;

    private final RefundForComplaintListener listener;

    public RefundForComplaintDialog(Complaint complaint, RefundForComplaintListener listener) {
        this.listener = listener;

        setTitle(TITLE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setPreferredSize(new Dimension(400, 250));
        pack();
        setLocationRelativeTo(null);
        
        fieldId = new JTextField();
        fieldComplaint = new JTextField();
        fieldCustomerName = new JTextField();
        textAreaDescription = new JTextArea();
        fieldDeviceSerialNumber = new JTextField();

        JLabel labelId = new JLabel("Şikayet ID:");
        labelId.setBounds(10, 10, 100, 30);
        contentPane.add(labelId);
        fieldId.setBounds(120, 10, 250, 30);
        fieldId.setEditable(false);
        contentPane.add(fieldId);

        JLabel labelComplaint = new JLabel("Şikayet:");
        labelComplaint.setBounds(10, 50, 100, 30);
        contentPane.add(labelComplaint);
        fieldComplaint.setBounds(120, 50, 250, 30);
        fieldComplaint.setEditable(false);
        contentPane.add(fieldComplaint);

        JLabel labelCustomerName = new JLabel("Müşteri Adı:");
        labelCustomerName.setBounds(10, 90, 100, 30);
        contentPane.add(labelCustomerName);
        fieldCustomerName.setBounds(120, 90, 250, 30);
        fieldCustomerName.setEditable(false);
        contentPane.add(fieldCustomerName);

        JLabel labelDescription = new JLabel("Açıklama:");
        labelDescription.setBounds(10, 130, 100, 30);
        contentPane.add(labelDescription);
        textAreaDescription.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textAreaDescription);
        scrollPane.setBounds(120, 130, 250, 80);
        contentPane.add(scrollPane);

        JLabel labelDeviceSerialNumber = new JLabel("Cihaz Seri Numarası:");
        labelDeviceSerialNumber.setBounds(10, 220, 150, 30);
        contentPane.add(labelDeviceSerialNumber);
        fieldDeviceSerialNumber.setBounds(160, 220, 210, 30);
        fieldDeviceSerialNumber.setEditable(false);
        contentPane.add(fieldDeviceSerialNumber);

        buttonOK = new JButton("Tamam");
        buttonOK.setBounds(50, 280, 100, 30);
        buttonOK.setEnabled(true);
        contentPane.add(buttonOK);

        buttonCancel = new JButton("İptal");
        buttonCancel.setBounds(200, 280, 100, 30);
        contentPane.add(buttonCancel);


        fieldId.setText(String.valueOf(complaint.getId()));
        fieldComplaint.setText(complaint.getComplaint());
        fieldCustomerName.setText(complaint.getCustomer().getName());
        textAreaDescription.setText(String.valueOf(complaint.getDescription()));
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
        listener.onRefund(complaint);
    }

    private void pay(Complaint complaint) {
        App.getDatabaseHelper().updateComplaint(complaint);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface RefundForComplaintListener {
        void onRefund(Complaint complaint);
    }

}
