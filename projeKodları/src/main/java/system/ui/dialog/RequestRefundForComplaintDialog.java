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
import javax.swing.JOptionPane;
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
public class RequestRefundForComplaintDialog extends JDialog {
    private static final String TITLE = "İade İstemi Oluştur";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldId;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JTextArea textAreaDescription;

    private final RequestRefundForComplaintListener listener;

    public RequestRefundForComplaintDialog(Complaint complaint, RequestRefundForComplaintListener listener) {
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

        JLabel labelDeviceSerialNumber = new JLabel("Cihaz Seri Numarası:");
        labelDeviceSerialNumber.setBounds(10, 130, 150, 30);
        contentPane.add(labelDeviceSerialNumber);
        fieldDeviceSerialNumber.setBounds(160, 130, 210, 30);
        fieldDeviceSerialNumber.setEditable(false);
        contentPane.add(fieldDeviceSerialNumber);

        buttonOK = new JButton("Tamam");
        buttonOK.setBounds(50, 190, 100, 30);
        buttonOK.setEnabled(true);
        contentPane.add(buttonOK);

        buttonCancel = new JButton("İptal");
        buttonCancel.setBounds(200, 190, 100, 30);
        contentPane.add(buttonCancel);

        textAreaDescription = new JTextArea();
        textAreaDescription.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textAreaDescription);
        scrollPane.setBounds(10, 170, 360, 80);
        contentPane.add(scrollPane);

        fieldId.setText(String.valueOf(complaint.getId()));
        fieldComplaint.setText(complaint.getComplaint());
        fieldCustomerName.setText(complaint.getCustomer().getName());
        fieldDeviceSerialNumber.setText(complaint.getSerialNumber());

        buttonOK.addActionListener(e -> {
            String description = textAreaDescription.getText().trim();
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Açıklama kısmı boş bırakılamaz!");
                return;
            }
            onOK(complaint, description);
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

    private void onOK(Complaint complaint, String description) {
        complaint.setDescription(description);
        complaint.setStatus(ComplaintStatus.WAITING_REFUND);
        App.getDatabaseHelper().updateComplaint(complaint);
        dispose();
        listener.onRequestRefund(complaint);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface RequestRefundForComplaintListener {
        void onRequestRefund(Complaint complaint);
    }

}

