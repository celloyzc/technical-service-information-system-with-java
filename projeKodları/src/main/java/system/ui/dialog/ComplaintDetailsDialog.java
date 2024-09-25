/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.ui.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import system.model.Complaint;
import system.model.UsedComponent;
import system.model.table.UsedComponentTableModel;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class ComplaintDetailsDialog extends JDialog {
    private static final String TITLE = "Şikayet Detayları";

    private JPanel contentPane;
    private JTextField fieldId;
    private JTable tableComponents;
    private JTextField fieldComponentFee;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JTextArea textAreaDescription;
    private JTextField fieldLaborFee;
    private JTextField fieldTotalFee;

    public ComplaintDetailsDialog(Complaint complaint) {
        setTitle(TITLE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(700, 550));
        pack();
        setLocationRelativeTo(null);
        contentPane.setLayout(null);
        
        JLabel labelId = new JLabel("Şikayet No:");
        labelId.setBounds(10, 10, 310, 30);
        contentPane.add(labelId);

        fieldId = new JTextField();
        fieldId.setEditable(false);
        fieldId.setBounds(10, 40, 310, 30);
        contentPane.add(fieldId);

        JLabel labelComplaint = new JLabel("Şikayet:");
        labelComplaint.setBounds(10, 80, 310, 30);
        contentPane.add(labelComplaint);

        fieldComplaint = new JTextField();
        fieldComplaint.setEditable(false);
        fieldComplaint.setBounds(10, 110, 310, 30);
        contentPane.add(fieldComplaint);

        JLabel labelCustomerName = new JLabel("Müşteri Adı:");
        labelCustomerName.setBounds(10, 150, 310, 30);
        contentPane.add(labelCustomerName);

        fieldCustomerName = new JTextField();
        fieldCustomerName.setEditable(false);
        fieldCustomerName.setBounds(10, 180, 310, 30);
        contentPane.add(fieldCustomerName);

        JLabel labelDeviceSerialNumber = new JLabel("Cihaz Seri Numarası:");
        labelDeviceSerialNumber.setBounds(10, 220, 310, 30);
        contentPane.add(labelDeviceSerialNumber);

        fieldDeviceSerialNumber = new JTextField();
        fieldDeviceSerialNumber.setEditable(false);
        fieldDeviceSerialNumber.setBounds(10, 250, 310, 30);
        contentPane.add(fieldDeviceSerialNumber);

        JLabel labelComponentFee = new JLabel("Parça Ücreti:");
        labelComponentFee.setBounds(10, 290, 310, 30);
        contentPane.add(labelComponentFee);

        fieldComponentFee = new JTextField();
        fieldComponentFee.setEditable(false);
        fieldComponentFee.setBounds(10, 320, 310, 30);
        contentPane.add(fieldComponentFee);

        JLabel labelLaborFee = new JLabel("İşçilik Ücreti:");
        labelLaborFee.setBounds(10, 360, 310, 30);
        contentPane.add(labelLaborFee);

        fieldLaborFee = new JTextField();
        fieldLaborFee.setEditable(false);
        fieldLaborFee.setBounds(10, 390, 310, 30);
        contentPane.add(fieldLaborFee);

        JLabel labelTotalFee = new JLabel("Toplam Ücret:");
        labelTotalFee.setBounds(10, 430, 310, 30);
        contentPane.add(labelTotalFee);

        fieldTotalFee = new JTextField();
        fieldTotalFee.setEditable(false);
        fieldTotalFee.setBounds(10, 460, 310, 30);
        contentPane.add(fieldTotalFee);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(330, 10, 340, 300);
        contentPane.add(scrollPane);

        tableComponents = new JTable();
        scrollPane.setViewportView(tableComponents);

        JLabel labelDescription = new JLabel("Açıklama:");
        labelDescription.setBounds(330, 320, 310, 30);
        contentPane.add(labelDescription);
        
        textAreaDescription = new JTextArea();
        textAreaDescription.setEditable(false);
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setBounds(330, 350, 340, 100);
        contentPane.add(textAreaDescription);
        
        int componentFee = calculateComponentFee(complaint);

        fieldId.setText(String.valueOf(complaint.getId()));
        fieldComplaint.setText(complaint.getComplaint());
        fieldComponentFee.setText(String.valueOf(componentFee));
        fieldCustomerName.setText(complaint.getCustomer().getName());
        fieldDeviceSerialNumber.setText(complaint.getSerialNumber());
        fieldLaborFee.setText(String.valueOf(complaint.getLaborFee()));
        fieldTotalFee.setText(String.valueOf(complaint.getTotalFee()));
        textAreaDescription.setText(complaint.getDescription());

        setRenderer(tableComponents);

        setupComponentsTable(complaint);

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

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}

