
package system.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import system.model.StockHistoryItem;
import system.model.table.StockHistoryTableModel;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class StockHistoryScreen extends JFrame {
    private static final String TITLE = "Stok Hareketleri";

    private JTable tableStockHistory;
    private JPanel panel;
    private JComboBox comboMonth;
    private JComboBox comboYear;

    private StockHistoryTableModel tableModel;

    public StockHistoryScreen() {
        super(TITLE);
        panel = new JPanel();
        setContentPane(panel);
        setPreferredSize(new Dimension(930, 550));
        pack();
        setLocationRelativeTo(null);
        
        tableStockHistory = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableStockHistory);
        scrollPane.setBounds(10, 50, 880, 400);
        panel.add(scrollPane);

        comboYear = new JComboBox();
        comboYear.setBounds(10, 10, 120, 30);
        panel.add(comboYear);

        comboMonth = new JComboBox();
        comboMonth.setBounds(140, 10, 120, 30);
        panel.add(comboMonth);
        
        panel.setLayout(null);
        setRenderer(tableStockHistory);

        List<StockHistoryItem> stockHistory = fetchAllStockHistory();
        listAll(stockHistory);

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
        String[] years = new String[6];
        for (int i = 0; i < 6; i++) {
            years[i] = String.valueOf(currentYear - 5 + i);
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

    private List<StockHistoryItem> fetchAllStockHistory() {
        return App.getDatabaseHelper().getStockHistory();
    }

    private void listAll(List<StockHistoryItem> stockHistory) {
        tableModel = new StockHistoryTableModel(stockHistory);
        tableStockHistory.setModel(tableModel);
        tableStockHistory.setCellSelectionEnabled(false);
    }
    
    private void setRenderer(JTable table) {
    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row % 2 == 0) {
                c.setBackground(Color.LIGHT_GRAY);
            } else {
                c.setBackground(Color.WHITE);
            }
            return c;
        }
    });
}

}
