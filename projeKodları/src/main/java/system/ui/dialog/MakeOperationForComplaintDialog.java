/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.ui.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.ListSelectionModel;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import system.model.Complaint;
import system.model.ComplaintStatus;
import system.model.Component;
import system.model.StockHistoryItem;
import system.model.UsedComponent;
import system.model.table.UsedComponentTableModel;
import system.ui.App;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class MakeOperationForComplaintDialog extends JDialog implements SelectComponentDialog.SelectComponentListener {
    private static final String TITLE = "İşlem Ekle";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldId;
    private JTable tableComponents;
    private JButton buttonAddComponent;
    private JTextField fieldComponentFee;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JButton buttonRemoveComponent;

    private final MakeOperationListener listener;
    private UsedComponentTableModel tableModel;
    private UsedComponent selectedItem;
    private int totalFee;

    public MakeOperationForComplaintDialog(Complaint complaint, MakeOperationListener listener) {
        this.listener = listener;

        setTitle(TITLE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setPreferredSize(new Dimension(940, 650));
        pack();
        setLocationRelativeTo(null);
        
        fieldId = new JTextField();
        fieldComplaint = new JTextField();
        fieldComponentFee = new JTextField();
        fieldCustomerName = new JTextField();
        fieldDeviceSerialNumber = new JTextField();
        
        JScrollPane scrollPaneTable = new JScrollPane();
        scrollPaneTable.setBounds(10, 80, 880, 400);
        contentPane.add(scrollPaneTable);
        tableComponents = new JTable();
        scrollPaneTable.setViewportView(tableComponents);

        JLabel labelId = new JLabel("Şikayet ID:");
        labelId.setBounds(10, 10, 80, 20);
        contentPane.add(labelId);
        fieldId.setBounds(10, 40, 80, 30);
        fieldId.setEditable(false);
        contentPane.add(fieldId);

        JLabel labelComplaint = new JLabel("Şikayet:");
        labelComplaint.setBounds(100, 10, 100, 20);
        contentPane.add(labelComplaint);
        fieldComplaint.setBounds(100, 40, 150, 30);
        fieldComplaint.setEditable(false);
        contentPane.add(fieldComplaint);

        JLabel labelComponentFee = new JLabel("Toplam Parça Ücreti:");
        labelComponentFee.setBounds(260, 10, 150, 20);
        contentPane.add(labelComponentFee);
        fieldComponentFee.setBounds(260, 40, 150, 30);
        fieldComponentFee.setEditable(false);
        contentPane.add(fieldComponentFee);

        JLabel labelCustomerName = new JLabel("Müşteri Adı:");
        labelCustomerName.setBounds(420, 10, 100, 20);
        contentPane.add(labelCustomerName);
        fieldCustomerName.setBounds(420, 40, 200, 30);
        fieldCustomerName.setEditable(false);
        contentPane.add(fieldCustomerName);


        JLabel labelDeviceSerialNumber = new JLabel("Cihaz Seri Numarası:");
        labelDeviceSerialNumber.setBounds(630, 10, 150, 20);
        contentPane.add(labelDeviceSerialNumber);
        fieldDeviceSerialNumber.setBounds(630, 40, 150, 30);
        fieldDeviceSerialNumber.setEditable(false);
        contentPane.add(fieldDeviceSerialNumber);

        buttonAddComponent = new JButton("Parça Ekle");
        buttonAddComponent.setBounds(10, 500, 150, 30);
        contentPane.add(buttonAddComponent);

        buttonRemoveComponent = new JButton("Parça Sil");
        buttonRemoveComponent.setBounds(170, 500, 150, 30);
        buttonRemoveComponent.setEnabled(false);
        contentPane.add(buttonRemoveComponent);



        buttonOK = new JButton("Tamam");
        buttonOK.setBounds(330, 500, 100, 30);
        buttonOK.setEnabled(true);
        contentPane.add(buttonOK);

        buttonCancel = new JButton("İptal");
        buttonCancel.setBounds(440, 500, 100, 30);
        contentPane.add(buttonCancel);

        fieldId.setText(String.valueOf(complaint.getId()));
        fieldComplaint.setText(complaint.getComplaint());
        fieldComponentFee.setText("0");
        fieldCustomerName.setText(complaint.getCustomer().getName());
        fieldDeviceSerialNumber.setText(complaint.getSerialNumber());
        
        contentPane.setLayout(null);

        setRenderer(tableComponents);

        setupComponentsTable();

        setSelectedItem(null);
        
        buttonAddComponent.addActionListener(e -> {
        new SelectComponentDialog(this).setVisible(true);
        });

        buttonRemoveComponent.addActionListener(e -> {
            removeComponent();
        });

        buttonOK.addActionListener(e -> onOK(complaint));

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
   

    private void setupComponentsTable() {
        tableModel = new UsedComponentTableModel(new ArrayList<>());
        tableComponents.setModel(tableModel);
        tableComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableComponents.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = tableComponents.getSelectedRow();
            if (selectedIndex < 0) {
                setSelectedItem(null);
                return;
            }
            setSelectedItem(tableModel.getItem(selectedIndex));
        });
    }
    
     private void setupEventListeners(Complaint complaint) {
        buttonAddComponent.addActionListener(e -> new SelectComponentDialog(this).setVisible(true));
        buttonRemoveComponent.addActionListener(e -> removeComponent());
        buttonOK.addActionListener(e -> onOK(complaint));
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void removeComponent() {
        setTotalFee(totalFee - selectedItem.getComponent().getPrice() * selectedItem.getPiece());
        tableModel.remove(selectedItem);
    }

    public void setSelectedItem(UsedComponent selectedItem) {
        this.selectedItem = selectedItem;
        if (selectedItem == null) {
            buttonRemoveComponent.setEnabled(false);
        } else {
            buttonRemoveComponent.setEnabled(true);
        }
    }

    private void onOK(Complaint complaint) {
        List<UsedComponent> usedComponents = tableModel.getAllItems();
        if (checkIfSufficientStock(usedComponents)) {
            update(complaint, usedComponents);
        } else {
            insufficientStock(complaint, usedComponents);
        }
    }

    private void insufficientStock(Complaint complaint, List<UsedComponent> usedComponents) {
        int result = JOptionPane.showConfirmDialog(
                null,
                "Kullanılan parçalarda stoğu yeterli olmayan parça/parçalar mevcut! Parçaları beklemeye alabilirsiniz. Onaylıyor musunuz?",
                "Stok Yetersizliği",
                JOptionPane.YES_NO_OPTION
        );
        if (result == JOptionPane.OK_OPTION) {
            complaint.setStatus(ComplaintStatus.WAITING_COMPONENT);
            /**/
            App.getDatabaseHelper().updateComplaint(complaint);
            insertUsedComponents(complaint, usedComponents);
            /**/
            listener.onMakeOperation(complaint);
            dispose();
            JOptionPane.showMessageDialog(null, complaint.getId() + " nolu şikayet için stoğu yetersiz parçalar beklenmekte!");
        }
    }

    private void update(Complaint complaint, List<UsedComponent> usedComponents) {
        complaint.setStatus(ComplaintStatus.IN_OPERATION);
        /**/
        App.getDatabaseHelper().updateComplaint(complaint);
        insertUsedComponents(complaint, usedComponents);
        for (UsedComponent usedComponent : usedComponents) {
            Component c = usedComponent.getComponent();
            // stok azalt
            App.getDatabaseHelper().decreaseComponentStockById(c.getId(), usedComponent.getPiece());
            StockHistoryItem stockHistoryItem = new StockHistoryItem(null, c, "PARÇA ÇIKIŞI", c.getStock(), c.getStock() - usedComponent.getPiece());
            // stok kaydı tut
            App.getDatabaseHelper().insertStockHistoryItem(stockHistoryItem);
        }
        /**/
        listener.onMakeOperation(complaint);
        dispose();
        JOptionPane.showMessageDialog(null, complaint.getId() + " nolu şikayet işleme alındı!");
    }

    private void insertUsedComponents(Complaint complaint, List<UsedComponent> usedComponents) {
        App.getDatabaseHelper().insertUsedComponentsToComplaint(complaint.getId(), usedComponents);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    @Override
    public void onSelectComponent(Component component, int piece) {
        List<UsedComponent> items = tableModel.getAllItems();
        UsedComponent usedComponent = findComponent(items, component);
        if (usedComponent == null) {
            tableModel.add(new UsedComponent(component, piece));
        } else {
            usedComponent.setPiece(usedComponent.getPiece() + piece);
            tableModel.update(usedComponent);
        }
        setTotalFee(totalFee + component.getPrice() * piece);
    }

    private UsedComponent findComponent(List<UsedComponent> items, Component component) {
        for (UsedComponent item : items) {
            if (item.getComponent().getCode().equals(component.getCode())) {
                return item;
            }
        }
        return null;
    }

    private boolean checkIfSufficientStock(List<UsedComponent> items) {
        for (UsedComponent item : items) {
            if (item.getComponent().getStock() < item.getPiece()) {
                return false;
            }
        }
        return true;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
        fieldComponentFee.setText(String.valueOf(totalFee));
    }

    public interface MakeOperationListener {
        void onMakeOperation(Complaint complaint);
    }

}
