/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.ui;

import java.awt.Dimension;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import system.model.Payment;
import system.model.table.PaymentHistoryTableModel;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class PaymentHistoryScreen extends JFrame {
    private static final String TITLE = "Ödeme Geçmişi";

    private JTable tablePaymentHistory;
    private JPanel panel;
    private JComboBox comboMonth;
    private JComboBox comboYear;

    private PaymentHistoryTableModel tableModel;

    public PaymentHistoryScreen() {
        super(TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);
        
        tablePaymentHistory = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablePaymentHistory);
        scrollPane.setPreferredSize(new Dimension(850, 400));
        panel.add(scrollPane);

        comboYear = new JComboBox<>(generateYears());
        comboYear.setSelectedItem(null);
        panel.add(comboYear);

        comboMonth = new JComboBox<>(generateMonths());
        comboMonth.setSelectedItem(null);
        panel.add(comboMonth);

        setRenderer(tablePaymentHistory);

        List<Payment> payments = fetchAllPaymentHistory();
        listAll(payments);

        comboYear.setModel(new DefaultComboBoxModel<>(generateYears()));
        comboYear.setSelectedItem(null);

        comboMonth.setModel(new DefaultComboBoxModel<>(generateMonths()));
        comboMonth.setSelectedItem(null);

        comboMonth.addItemListener(e -> {
            onChangeDate();
        });
        comboYear.addItemListener(e -> {
            onChangeDate();
        });
    }

    private String[] generateYears() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        String[] years = new String[5];
        for (int i = 0; i < 5; i++) {
            years[i] = String.valueOf(currentYear - 4 + i);
        }
        return years;
    }

    private String[] generateMonths() {
        return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    }

    private void onChangeDate() {
        String query = getSelectedDate();
        tableModel.filter(query);
    }

    private String getSelectedDate() {
        Object month = comboMonth.getSelectedItem();
        Object year = comboYear.getSelectedItem();
        if (month == null || year == null) {
            return null;
        }
        return year + "-" + month;
    }

    private List<Payment> fetchAllPaymentHistory() {
        return App.getDatabaseHelper().getPaymentHistory();
    }

    private void listAll(List<Payment> payments) {
        tableModel = new PaymentHistoryTableModel(payments);
        tablePaymentHistory.setModel(tableModel);
        tablePaymentHistory.setCellSelectionEnabled(false);
    }

}

