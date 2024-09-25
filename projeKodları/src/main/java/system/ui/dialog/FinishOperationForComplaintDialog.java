/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.ui.dialog;

import java.awt.Dimension;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import system.model.Complaint;
import system.model.ComplaintStatus;
import system.model.UsedComponent;
import system.model.document.SingleDocumentListener;
import system.model.table.UsedComponentTableModel;
import system.ui.App;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class FinishOperationForComplaintDialog extends JDialog {
    private static final String TITLE = "İşlem Ekle";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldId;
    private JTable tableComponents;
    private JTextField fieldComponentFee;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JTextArea textAreaDescription;
    private JTextField fieldLaborFee;
    private JTextField fieldTotalFee;

    private final FinishOperationListener listener;
    private int totalFee;
    private int componentFee;

    public FinishOperationForComplaintDialog(Complaint complaint, FinishOperationListener listener) {
        this.listener = listener;

        setTitle(TITLE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setPreferredSize(new Dimension(700, 550));
        pack();
        setLocationRelativeTo(null);
        contentPane.setLayout(null);
        fieldId = new JTextField();
        fieldId.setEditable(false);
        fieldId.setBounds(10, 10, 310, 30);
        contentPane.add(fieldId);

        JLabel labelComplaint = new JLabel("Şikayet:");
        labelComplaint.setBounds(10, 50, 310, 30);
        contentPane.add(labelComplaint);

        fieldComplaint = new JTextField();
        fieldComplaint.setEditable(false);
        fieldComplaint.setBounds(10, 80, 310, 30);
        contentPane.add(fieldComplaint);

        JLabel labelCustomerName = new JLabel("Müşteri Adı:");
        labelCustomerName.setBounds(10, 120, 310, 30);
        contentPane.add(labelCustomerName);

        fieldCustomerName = new JTextField();
        fieldCustomerName.setEditable(false);
        fieldCustomerName.setBounds(10, 150, 310, 30);
        contentPane.add(fieldCustomerName);

        JLabel labelDeviceSerialNumber = new JLabel("Cihaz Seri Numarası:");
        labelDeviceSerialNumber.setBounds(10, 190, 310, 30);
        contentPane.add(labelDeviceSerialNumber);

        fieldDeviceSerialNumber = new JTextField();
        fieldDeviceSerialNumber.setEditable(false);
        fieldDeviceSerialNumber.setBounds(10, 220, 310, 30);
        contentPane.add(fieldDeviceSerialNumber);

        JLabel labelComponentFee = new JLabel("Parça Ücreti:");
        labelComponentFee.setBounds(10, 260, 310, 30);
        contentPane.add(labelComponentFee);

        fieldComponentFee = new JTextField();
        fieldComponentFee.setEditable(false);
        fieldComponentFee.setBounds(10, 290, 310, 30);
        contentPane.add(fieldComponentFee);

        JLabel labelLaborFee = new JLabel("İşçilik Ücreti:");
        labelLaborFee.setBounds(10, 330, 310, 30);
        contentPane.add(labelLaborFee);

        fieldLaborFee = new JTextField();
        fieldLaborFee.setBounds(10, 360, 310, 30);
        contentPane.add(fieldLaborFee);

        JLabel labelTotalFee = new JLabel("Toplam Ücret:");
        labelTotalFee.setBounds(10, 400, 310, 30);
        contentPane.add(labelTotalFee);

        fieldTotalFee = new JTextField();
        fieldTotalFee.setEditable(false);
        fieldTotalFee.setBounds(10, 430, 310, 30);
        contentPane.add(fieldTotalFee);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(330, 10, 340, 340);
        contentPane.add(scrollPane);

        tableComponents = new JTable();
        scrollPane.setViewportView(tableComponents);

        JLabel labelDescription = new JLabel("Yapılan İşlemler:");
        labelDescription.setBounds(330, 360, 310, 30);
        contentPane.add(labelDescription);

        textAreaDescription = new JTextArea();
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setBounds(330, 390, 340, 70);
        contentPane.add(textAreaDescription);

        buttonOK = new JButton("Tamam");
        buttonOK.setBounds(50, 470, 100, 30);
        contentPane.add(buttonOK);

        buttonCancel = new JButton("İptal");
        buttonCancel.setBounds(200, 470, 100, 30);
        contentPane.add(buttonCancel);

        
        componentFee = calculateComponentFee(complaint);

        fieldId.setText(String.valueOf(complaint.getId()));
        fieldComplaint.setText(complaint.getComplaint());
        fieldComponentFee.setText(String.valueOf(componentFee));
        fieldCustomerName.setText(complaint.getCustomer().getName());
        fieldDeviceSerialNumber.setText(complaint.getSerialNumber());

        setRenderer(tableComponents);

        setupComponentsTable(complaint);

        fieldLaborFee.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            try {
                String laborFee = fieldLaborFee.getText().trim();
                setTotalFee(componentFee + Integer.parseInt(laborFee));
            } catch (Exception ignored) {
            }
        });

        fieldLaborFee.setText("0");

        buttonOK.addActionListener(e -> {
            String laborFee = fieldLaborFee.getText().trim();
            String regexFee = "\\d{1,9}";
            if (!Pattern.matches(regexFee, laborFee)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir işçilik ücreti giriniz!");
                return;
            }
            if(Integer.parseInt(laborFee) == 0) {
                JOptionPane.showMessageDialog(null, "İşçilik ücreti 0 olamaz!");
                return;
            }
            String description = textAreaDescription.getText().trim();
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Yapılan işlemler kısmı boş bırakılamaz!");
                return;
            }
            onOK(complaint, Integer.parseInt(laborFee), totalFee, description);
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

    private int calculateComponentFee(Complaint complaint) {
        int sum = 0;
        for (UsedComponent usedComponent : complaint.getUsedComponents()) {
            sum += usedComponent.getComponent().getPrice() * usedComponent.getPiece();
        }
        return sum;
    }

    private void setupComponentsTable(Complaint complaint) {
        UsedComponentTableModel tableModel = new UsedComponentTableModel(complaint.getUsedComponents());
        tableComponents.setModel(tableModel);
        tableComponents.setCellSelectionEnabled(false);
    }

    private void onOK(Complaint complaint, int laborFee, int totalFee, String description) {
        complaint.setLaborFee(laborFee);
        complaint.setTotalFee(totalFee);
        complaint.setDescription(description);
        complaint.setStatus(ComplaintStatus.WAITING_PAYMENT);
        App.getDatabaseHelper().updateComplaint(complaint);
        dispose();
        listener.onFinishOperation(complaint);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
        fieldTotalFee.setText(String.valueOf(totalFee));
    }

    public interface FinishOperationListener {
        void onFinishOperation(Complaint complaint);
    }

}

