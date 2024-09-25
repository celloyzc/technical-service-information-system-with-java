/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.ui;

import java.awt.Dimension;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import system.model.Complaint;
import system.model.ComplaintStatus;
import system.model.Customer;
import system.model.DeviceBrand;
import system.model.DeviceType;
import system.model.Technician;
import system.ui.dialog.SelectCustomerDialog;
import system.ui.dialog.SelectTechnicianDialog;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class ReportComplaintScreen extends JFrame implements SelectCustomerDialog.SelectCustomerListener, SelectTechnicianDialog.SelectTechnicianListener {
    private static final String TITLE = "Yeni Şikayet";

    private JPanel panel;
    private JButton buttonSelectTechnician;
    private JButton buttonOK;
    private JComboBox comboDeviceType;
    private JTextArea textAreaComplaint;
    private JButton buttonSelectCustomer;
    private JTextField fieldCustomerName;
    private JTextField fieldCustomerPhone;
    private JTextField fieldCustomerAddress;
    private JTextField fieldTechnicianName;
    private JTextField fieldTechnicianPhone;
    private JComboBox comboDeviceBrand;
    private JTextField fieldDeviceSerialNumber;

    private Customer selectedCustomer;
    private Technician selectedTechnician;

    public ReportComplaintScreen() {
        super(TITLE);
        panel = new JPanel();
        setContentPane(panel);
        setPreferredSize(new Dimension(700, 500));
        pack();
        setLocationRelativeTo(null);
        
        comboDeviceType = new JComboBox();
        textAreaComplaint = new JTextArea();
        buttonSelectCustomer = new JButton();
        fieldCustomerName = new JTextField();
        fieldCustomerPhone = new JTextField();
        fieldCustomerAddress = new JTextField();
        fieldTechnicianName = new JTextField();
        fieldTechnicianPhone = new JTextField();
        comboDeviceBrand = new JComboBox();
        fieldDeviceSerialNumber = new JTextField();
        buttonSelectTechnician = new JButton();
        buttonOK = new JButton();
        
        panel.setLayout(null);
        
        JLabel labelDT = new JLabel("Cihaz Tipi:");
        labelDT.setBounds(100, 100, 200, 20);
        panel.add(labelDT);

        comboDeviceType.setModel(new DefaultComboBoxModel<>(fetchDeviceTypes().toArray(new DeviceType[0])));
        comboDeviceType.setSelectedItem(null);
        comboDeviceType.setBounds(100, 120, 200, 30);
        panel.add(comboDeviceType);
        
        JLabel labelta = new JLabel("Şikayet Metni:");
        labelta.setBounds(100, 160, 200, 20);
        panel.add(labelta);
        
        textAreaComplaint.setBounds(100, 180, 500, 70);
        panel.add(textAreaComplaint);

        buttonSelectCustomer.setText("Müşteri Seç");
        buttonSelectCustomer.setBounds(400, 70, 200, 30);
        panel.add(buttonSelectCustomer);
        
        JLabel labelName = new JLabel("Müşteri İsmi:");
        labelName.setBounds(100, 0, 200, 20);
        panel.add(labelName);

        fieldCustomerName.setEditable(false);
        fieldCustomerName.setBounds(100, 20, 200, 30);
        panel.add(fieldCustomerName);
        
        JLabel labelPhone = new JLabel("Müşteri Telefon Numarası:");
        labelPhone.setBounds(400, 0, 200, 20);
        panel.add(labelPhone);

        fieldCustomerPhone.setEditable(false);
        fieldCustomerPhone.setBounds(400, 20, 200, 30);
        panel.add(fieldCustomerPhone);
        
        JLabel labelAddress = new JLabel("Müşteri Adresi:");
        labelAddress.setBounds(100, 50, 200, 20);
        panel.add(labelAddress);

        fieldCustomerAddress.setEditable(false);
        fieldCustomerAddress.setBounds(100, 70, 200, 30);
        panel.add(fieldCustomerAddress);
        
        JLabel labelDB = new JLabel("Cihaz Markası:");
        labelDB.setBounds(400, 100, 200, 20);
        panel.add(labelDB);

        comboDeviceBrand.setModel(new DefaultComboBoxModel<>(fetchDeviceBrands().toArray(new DeviceBrand[0])));
        comboDeviceBrand.setSelectedItem(null);
        comboDeviceBrand.setBounds(400, 120, 200, 30);
        panel.add(comboDeviceBrand);
        
        JLabel labelDs = new JLabel("Cihaz Seri Numarası:");
        labelDs.setBounds(100, 250, 200, 20);
        panel.add(labelDs);
        
        fieldDeviceSerialNumber.setBounds(100, 270, 200, 30);
        panel.add(fieldDeviceSerialNumber);

        buttonSelectTechnician.setText("Teknisyen Seç");
        buttonSelectTechnician.setBounds(400, 270, 200, 30);
        panel.add(buttonSelectTechnician);
        
        JLabel labeltn = new JLabel("Teknisyen Adı:");
        labeltn.setBounds(100, 320, 200, 20);
        panel.add(labeltn);
        
        fieldTechnicianName.setEditable(false);
        fieldTechnicianName.setBounds(100, 340, 200, 30);
        panel.add(fieldTechnicianName);
        
        JLabel labeltp = new JLabel("Teknisyen Telefon Numarası:");
        labeltp.setBounds(400, 320, 200, 20);
        panel.add(labeltp);

        fieldTechnicianPhone.setEditable(false);
        fieldTechnicianPhone.setBounds(400, 340, 200, 30);
        panel.add(fieldTechnicianPhone);

        buttonOK.setText("Tamam");
        buttonOK.setBounds(275, 380, 150, 50);
        panel.add(buttonOK);

        buttonSelectCustomer.addActionListener(e -> {
            new SelectCustomerDialog(this).setVisible(true);
        });

        buttonSelectTechnician.addActionListener(e -> {
        new SelectTechnicianDialog(this).setVisible(true);
        });

        buttonOK.addActionListener(e -> {
            if (selectedCustomer == null) {
                JOptionPane.showMessageDialog(null, "Müşteri seçmeniz gerekmekte!");
                return;
            }
            if (selectedTechnician == null) {
                JOptionPane.showMessageDialog(null, "Teknisyen seçmeniz gerekmekte!");
                return;
            }
            DeviceType deviceType = getSelectedDeviceType();
            if (deviceType == null) {
                JOptionPane.showMessageDialog(null, "Cihaz türünü seçmeniz gerekmekte!");
                return;
            }
            DeviceBrand brand = getSelectedDeviceBrand();
            if (brand == null) {
                JOptionPane.showMessageDialog(null, "Cihaz markasını seçmeniz gerekmekte!");
                return;
            }
            String serialNumber = fieldDeviceSerialNumber.getText().trim();
            if (serialNumber.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Cihaz seri numarası boş bırakılamaz!");
                return;
            }
            String complaint = textAreaComplaint.getText().trim();
            if (complaint.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Şikayet metni boş bırakılamaz!");
                return;
            }
            onOK(selectedCustomer, selectedTechnician, deviceType, brand, serialNumber, complaint);
        });


    }

    private List<DeviceType> fetchDeviceTypes() {
        return App.getDatabaseHelper().getDeviceTypes();
    }

    private List<DeviceBrand> fetchDeviceBrands() {
        return App.getDatabaseHelper().getDeviceBrands();
    }

    private DeviceBrand getSelectedDeviceBrand() {
        return (DeviceBrand) comboDeviceBrand.getSelectedItem();
    }

    private DeviceType getSelectedDeviceType() {
        return (DeviceType) comboDeviceType.getSelectedItem();
    }

    private void onOK(Customer customer, Technician technician, DeviceType deviceType, DeviceBrand brand, String serialNumber, String complaint) {
        Complaint c = new Complaint(customer, technician, complaint, deviceType, brand, serialNumber, ComplaintStatus.WAITING_OPERATION, 0, 0, "", null);
        int id = App.getDatabaseHelper().insertComplaint(c);
        dispose();
        JOptionPane.showMessageDialog(null, id + " nolu şikayet alındı!");
    }

    @Override
    public void onSelectCustomer(Customer customer) {
        this.selectedCustomer = customer;
        this.fieldCustomerName.setText(customer.getName());
        this.fieldCustomerPhone.setText(customer.getPhone());
        this.fieldCustomerAddress.setText(customer.getAddress());
    }

    @Override
    public void onSelectTechnician(Technician technician) {
        this.selectedTechnician = technician;
        this.fieldTechnicianName.setText(technician.getName());
        this.fieldTechnicianPhone.setText(technician.getPhone());
    }
}

