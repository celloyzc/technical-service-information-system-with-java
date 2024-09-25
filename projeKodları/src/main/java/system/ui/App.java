
package system.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import system.DatabaseHelper;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class App extends JFrame {

    private static final String TITLE = "Teknik Servis Sistemi";

    private JPanel panelMain;
    private JButton buttonReportComplaint;
    private JButton buttonListComplaints;
    private JButton buttonStock;
    private JButton buttonCustomers;
    private JButton buttonTechnicians;
    private JButton buttonStockHistory;
    private JButton buttonPayments;

    private static final DatabaseHelper databaseHelper = new DatabaseHelper().connect();

    public App() {
        super(TITLE);

        panelMain = new JPanel(new BorderLayout());
        setContentPane(panelMain);

        // Üst kısım
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new GridLayout(1, 0, 10, 10));

        buttonReportComplaint = new JButton("Yeni Şikayet");
        buttonListComplaints = new JButton("Şikayet Listesi");
        buttonStock = new JButton("Stok");
        Dimension buttonSize = new Dimension(180, 70);
        buttonReportComplaint.setPreferredSize(buttonSize);
        buttonListComplaints.setPreferredSize(buttonSize);
        buttonStock.setPreferredSize(buttonSize);
        panelTop.add(buttonReportComplaint);
        panelTop.add(buttonListComplaints);
        panelTop.add(buttonStock);

        // Alt kısım
        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new GridLayout(1, 0, 10, 10));

        buttonCustomers = new JButton("Müşteri İşlemleri");
        buttonTechnicians = new JButton("Teknisyenler");
        buttonStockHistory = new JButton("Stok Geçmişi");
        buttonPayments = new JButton("Ödemeler");
        buttonCustomers.setPreferredSize(buttonSize);
        buttonTechnicians.setPreferredSize(buttonSize);
        buttonStockHistory.setPreferredSize(buttonSize);
        buttonPayments.setPreferredSize(buttonSize);
        panelBottom.add(buttonCustomers);
        panelBottom.add(buttonTechnicians);
        panelBottom.add(buttonStockHistory);
        panelBottom.add(buttonPayments);

        panelMain.add(panelTop, BorderLayout.NORTH);
        panelMain.add(panelBottom, BorderLayout.SOUTH);
        JLabel label = new JLabel("Teknik Servis Bilgi Sistemi", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panelMain.add(label, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);

        buttonReportComplaint.addActionListener(e -> {
            new ReportComplaintScreen().setVisible(true);
        });
        buttonListComplaints.addActionListener(e -> {
            new ComplaintScreen().setVisible(true);
        });
        buttonStock.addActionListener(e -> {
            new ComponentScreen().setVisible(true);
        });
        buttonCustomers.addActionListener(e -> {
            new CustomerScreen().setVisible(true);
        });
        buttonTechnicians.addActionListener(e -> {
            new TechnicianScreen().setVisible(true);
        });
        buttonStockHistory.addActionListener(e -> {
            new StockHistoryScreen().setVisible(true);
        });
        buttonPayments.addActionListener(e -> {
            new PaymentHistoryScreen().setVisible(true);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                databaseHelper.disconnect();
            }
        });
    }

    public static void main(String[] args) {
        new App().setVisible(true);
    }

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

}
