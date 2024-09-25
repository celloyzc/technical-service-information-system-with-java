/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.ui.dialog;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import system.model.Technician;
import system.model.document.SingleDocumentListener;
import system.model.table.TechnicianTableModel;
import system.ui.App;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class SelectTechnicianDialog extends JDialog {
    private static final String TITLE = "Teknisyen Seç";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tableTechnicians;
    private JTextField fieldName;


    private final SelectTechnicianListener listener;
    private Technician selectedTechnician;
    private TechnicianTableModel tableModel;

    public SelectTechnicianDialog(SelectTechnicianListener listener) {
        this.listener = listener;

        setTitle(TITLE);
        contentPane =  new JPanel();
        setContentPane(contentPane);
        setPreferredSize(new Dimension(630, 530));
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);
        
        tableTechnicians = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableTechnicians);
        scrollPane.setBounds(10, 70, 600, 400);
        contentPane.add(scrollPane);
       
        buttonOK = new JButton("Tamam");
        buttonOK.setBounds(10, 30, 120, 30);
        buttonOK.setEnabled(false);
        contentPane.add(buttonOK);

        buttonCancel = new JButton("İptal");
        buttonCancel.setBounds(140, 30, 120, 30);
        contentPane.add(buttonCancel);
        
        JLabel labelDT = new JLabel("İsim Arama:");
        labelDT.setBounds(270, 10, 200, 20);
        contentPane.add(labelDT);

        fieldName = new JTextField();
        fieldName.setBounds(270, 30, 200, 30);
        contentPane.add(fieldName);
        
        contentPane.setLayout(null);

        setRenderer(tableTechnicians);

        List<Technician> technicians = fetchAllTechnicians();
        listAll(technicians);

        setSelectedTechnician(null);

        fieldName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldName.getText());
        });

        buttonOK.addActionListener(e -> onOK());

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

    private List<Technician> fetchAllTechnicians() {
        return App.getDatabaseHelper().getTechnicians();
    }

    private void listAll(List<Technician> technicians) {
        this.tableModel = new TechnicianTableModel(technicians);
        this.tableTechnicians.setModel(this.tableModel);
        this.tableTechnicians.setSelectionMode(0);
        this.tableTechnicians.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = tableTechnicians.getSelectedRow();
            if (selectedIndex < 0) {
                setSelectedTechnician((Technician)null);
                return;
            }
            setSelectedTechnician((Technician)this.tableModel.getItem(selectedIndex));
        });
    }

    private void onOK() {
        listener.onSelectTechnician(selectedTechnician);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface SelectTechnicianListener {
        void onSelectTechnician(Technician technician);
    }

    public void setSelectedTechnician(Technician selectedTechnician) {
        this.selectedTechnician = selectedTechnician;
        if(selectedTechnician == null) {
            this.buttonOK.setEnabled(false);
        } else {
            this.buttonOK.setEnabled(true);
        }
    }
}
