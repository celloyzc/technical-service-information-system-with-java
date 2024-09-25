
package system.ui;

import java.awt.Dimension;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import system.model.Complaint;
import static system.model.ComplaintStatus.COMPLETED;
import static system.model.ComplaintStatus.IN_OPERATION;
import static system.model.ComplaintStatus.WAITING_COMPONENT;
import static system.model.ComplaintStatus.WAITING_OPERATION;
import static system.model.ComplaintStatus.WAITING_PAYMENT;
import static system.model.ComplaintStatus.WAITING_REFUND;
import system.model.document.SingleDocumentListener;
import system.model.table.ComplaintTableModel;
import system.ui.dialog.ComplaintDetailsDialog;
import system.ui.dialog.FinishOperationForComplaintDialog;
import system.ui.dialog.MakeOperationForComplaintDialog;
import system.ui.dialog.PayForComplaintDialog;
import system.ui.dialog.RefundForComplaintDialog;
import system.ui.dialog.RequestRefundForComplaintDialog;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class ComplaintScreen extends JFrame implements PayForComplaintDialog.PayForComplaintListener, RefundForComplaintDialog.RefundForComplaintListener, MakeOperationForComplaintDialog.MakeOperationListener, RequestRefundForComplaintDialog.RequestRefundForComplaintListener, FinishOperationForComplaintDialog.FinishOperationListener {
    private static final String TITLE = "Şikayet Sorgula";

    private JTextField filedCustomerName;
    private JTable tableComplaints;
    private JPanel panel;
    private JButton buttonRefund;
    private JButton buttonPay;
    private JButton buttonFinishOperation;
    private JButton buttonMakeOperation;
    private JButton buttonRequestRefund;
    private JButton buttonDetails;

    private Complaint selectedItem;
    private ComplaintTableModel tableModel;

    public ComplaintScreen() {
        super(TITLE);
        panel = new JPanel();
        setContentPane(panel);
        setPreferredSize(new Dimension(940, 550));
        pack();
        setLocationRelativeTo(null);        

        tableComplaints = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableComplaints);
        scrollPane.setBounds(10, 50, 880, 400);
        panel.add(scrollPane);

        buttonDetails = new JButton("Detaylar");
        buttonDetails.setBounds(10, 10, 120, 30);
        panel.add(buttonDetails);

        buttonPay = new JButton("Ödeme Yap");
        buttonPay.setBounds(140, 10, 120, 30);
        panel.add(buttonPay);

        buttonRefund = new JButton("İade Yap");
        buttonRefund.setBounds(270, 10, 100, 30);
        panel.add(buttonRefund);

        buttonMakeOperation = new JButton("İşlem Yap");
        buttonMakeOperation.setBounds(380, 10, 100, 30);
        panel.add(buttonMakeOperation);

        buttonRequestRefund = new JButton("İade Talebi Oluştur");
        buttonRequestRefund.setBounds(490, 10, 160, 30);
        panel.add(buttonRequestRefund);

        buttonFinishOperation = new JButton("İşlemi Bitir");
        buttonFinishOperation.setBounds(660, 10, 100, 30);
        panel.add(buttonFinishOperation);
        
        filedCustomerName = new JTextField();
        filedCustomerName.setBounds(770, 10, 120, 30);
        panel.add(filedCustomerName);
        
        panel.setLayout(null);
        
        setRenderer(tableComplaints);

        List<Complaint> complaints = fetchAllComplaints();
        listAll(complaints);

        setSelectedItem(null);

        buttonDetails.addActionListener(e -> {
            new ComplaintDetailsDialog(selectedItem).setVisible(true);
        });
        buttonPay.addActionListener(e -> {
            new PayForComplaintDialog(selectedItem, this).setVisible(true);
        });
        buttonRefund.addActionListener(e -> {
            new RefundForComplaintDialog(selectedItem, this).setVisible(true);
        });
        buttonMakeOperation.addActionListener(e -> {
            new MakeOperationForComplaintDialog(selectedItem, this).setVisible(true);
        });
        buttonRequestRefund.addActionListener(e -> {
            new RequestRefundForComplaintDialog(selectedItem, this).setVisible(true);
        });
        buttonFinishOperation.addActionListener(e -> {
            new FinishOperationForComplaintDialog(selectedItem, this).setVisible(true);
        });

        filedCustomerName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(filedCustomerName.getText());
        });
    }

    private List<Complaint> fetchAllComplaints() {
        return App.getDatabaseHelper().getComplaints();
    }

    private void listAll(List<Complaint> complaints) {
        tableModel = new ComplaintTableModel(complaints);
        tableComplaints.setModel(tableModel);
        tableComplaints.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableComplaints.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = tableComplaints.getSelectedRow();
            if (selectedIndex < 0) {
                setSelectedItem(null);
                return;
            }
            setSelectedItem(tableModel.getItem(selectedIndex));
        });
    }

    private void setSelectedItem(Complaint complaint) {
        this.selectedItem = complaint;

        buttonMakeOperation.setEnabled(false);
        buttonFinishOperation.setEnabled(false);
        buttonPay.setEnabled(false);
        buttonRefund.setEnabled(false);
        buttonRequestRefund.setEnabled(false);
        buttonDetails.setEnabled(true);

        if (complaint == null) {
            buttonDetails.setEnabled(false);
            return;
        }

        switch (complaint.getStatus()) {
            case WAITING_OPERATION:
                buttonMakeOperation.setEnabled(true);
                buttonRequestRefund.setEnabled(true);
                break;
            case WAITING_COMPONENT:
                break;
            case IN_OPERATION:
                buttonFinishOperation.setEnabled(true);
                break;
            case WAITING_PAYMENT:
                buttonPay.setEnabled(true);
                break;
            case WAITING_REFUND:
                buttonRefund.setEnabled(true);
                break;
            case COMPLETED:
                break;
        }
    }

    @Override
    public void onPay(Complaint complaint) {
        setSelectedItem(selectedItem);
        tableModel.update(complaint);
        JOptionPane.showMessageDialog(null, complaint.getCustomer().getName() + " adlı müşteriden " + complaint.getTotalFee() + " miktarında ödeme alındı!");
    }

    @Override
    public void onRefund(Complaint complaint) {
        setSelectedItem(selectedItem);
        tableModel.update(complaint);
        JOptionPane.showMessageDialog(null, complaint.getCustomer().getName() + " adlı müşteriye cihaz iade edildi!");
    }

    @Override
    public void onMakeOperation(Complaint complaint) {
        setSelectedItem(selectedItem);
        tableModel.update(complaint);
    }

    @Override
    public void onRequestRefund(Complaint complaint) {
        setSelectedItem(selectedItem);
        tableModel.update(complaint);
        JOptionPane.showMessageDialog(null, complaint.getId() + " numaralı şikayet için, " + complaint.getCustomer().getName() + " adlı müşteriye iade talebi oluşturuldu!");
    }

    @Override
    public void onFinishOperation(Complaint complaint) {
        setSelectedItem(selectedItem);
        tableModel.update(complaint);
        JOptionPane.showMessageDialog(null, complaint.getId() + " numaralı şikayet için " + complaint.getTotalFee() + " tutarında ödeme yapılması için bekleniyor!");
    }
}

