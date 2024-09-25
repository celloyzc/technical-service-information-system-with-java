/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import system.model.Technician;
import system.model.document.SingleDocumentListener;
import system.model.table.TechnicianTableModel;
import system.ui.dialog.NewTechnicianDialog;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class TechnicianScreen extends JFrame implements NewTechnicianDialog.AddTechnicianListener {

    private static final String TITLE = "Teknisyen İşlemleri";

    private JTable tableTechnicians;
    private JTextField fieldTechnicianName;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JPanel panel;

    private Technician selectedItem;
    private TechnicianTableModel tableModel;

    public TechnicianScreen() {
        super(TITLE);
        panel = new JPanel();
        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);
        
        tableTechnicians = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableTechnicians);
        scrollPane.setBounds(10, 50, 880, 400);
        panel.add(scrollPane);

        buttonAdd = new JButton("Teknisyen Ekle");
        buttonAdd.setBounds(10, 10, 120, 30);
        panel.add(buttonAdd);

        buttonRemove = new JButton("Teknisyen Sil");
        buttonRemove.setBounds(140, 10, 120, 30);
        buttonRemove.setEnabled(false);
        panel.add(buttonRemove);

        fieldTechnicianName = new JTextField();
        fieldTechnicianName.setBounds(270, 10, 200, 30);
        panel.add(fieldTechnicianName);
        
        panel.setLayout(null);

        setRenderer(tableTechnicians);

        List<Technician> technicians = fetchAllTechnicians();
        listAll(technicians);

        setSelectedItem(null);

        buttonAdd.addActionListener(e -> {
            new NewTechnicianDialog(this).setVisible(true);
        });

        buttonRemove.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                    null,
                    selectedItem.getName() + " adlı teknisyeni silmek üzeresiniz. Onaylıyor musunuz?",
                    "Teknisyen Silme Onayı",
                    JOptionPane.YES_NO_OPTION
            );
            if (n == JOptionPane.YES_OPTION) {
                removeTechnician();
            }
        });

        fieldTechnicianName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldTechnicianName.getText());
        });
    }

    private void removeTechnician() {
        App.getDatabaseHelper().deleteTechnicianById(selectedItem.getId());
        tableModel.remove(selectedItem);
        fieldTechnicianName.setText("");
    }

    private List<Technician> fetchAllTechnicians() {
        return App.getDatabaseHelper().getTechnicians();
    }

    private void listAll(List<Technician> technicians) {
        tableModel = new TechnicianTableModel(technicians);
        tableTechnicians.setModel(tableModel);
        tableTechnicians.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTechnicians.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = tableTechnicians.getSelectedRow();
            if(selectedIndex < 0) {
                setSelectedItem(null);
                return;
            }
            setSelectedItem(tableModel.getItem(selectedIndex));
        });
    }

    public void setSelectedItem(Technician selectedItem) {
        this.selectedItem = selectedItem;
        if(selectedItem == null) {
            buttonRemove.setEnabled(false);
        } else {
            buttonRemove.setEnabled(true);
        }
    }

    @Override
    public void onAddTechnician(Technician newTechnician) {
        tableModel.add(newTechnician);
        fieldTechnicianName.setText("");
    }
}

