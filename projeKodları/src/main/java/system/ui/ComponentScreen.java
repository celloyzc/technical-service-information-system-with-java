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
import system.model.Component;
import system.model.document.SingleDocumentListener;
import system.model.table.ComponentTableModel;
import system.ui.dialog.DecreaseComponentStockDialog;
import system.ui.dialog.IncreaseComponentStockDialog;
import system.ui.dialog.NewComponentDialog;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class ComponentScreen extends JFrame implements NewComponentDialog.AddComponentListener, IncreaseComponentStockDialog.IncreaseComponentStockListener, DecreaseComponentStockDialog.DecreaseComponentStockListener {
    private static final String TITLE = "Parça İşlemleri";

    private JTable tableComponents;
    private JButton buttonRemove;
    private JButton buttonInput;
    private JButton buttonOutput;
    private JButton buttonAdd;
    private JPanel panel;
    private JTextField fieldComponentCode;

    private Component selectedItem;
    private ComponentTableModel tableModel;

    public ComponentScreen() {
        super(TITLE);
        
        panel = new JPanel();
        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        
        tableComponents = new JTable();
     
        JScrollPane scrollPane = new JScrollPane(tableComponents);
        scrollPane.setBounds(10, 50, 880, 400);
        panel.add(scrollPane);

        buttonAdd = new JButton("Ekle");
        buttonAdd.setBounds(10, 460, 100, 30);
        panel.add(buttonAdd);

        buttonRemove = new JButton("Sil");
        buttonRemove.setBounds(120, 460, 100, 30);
        panel.add(buttonRemove);

        buttonInput = new JButton("Giriş");
        buttonInput.setBounds(230, 460, 100, 30);
        panel.add(buttonInput);

        buttonOutput = new JButton("Çıkış");
        buttonOutput.setBounds(340, 460, 100, 30);
        panel.add(buttonOutput);

        fieldComponentCode = new JTextField();
        fieldComponentCode.setBounds(450, 460, 200, 30);
        panel.add(fieldComponentCode);
        panel.setLayout(null);
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableComponents);

        List<Component> components = fetchAllComponents();
        listAll(components);

        setSelectedItem(null);

        buttonAdd.addActionListener(e -> {
            new NewComponentDialog(this).setVisible(true);
        });

        buttonRemove.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                    null,
                    selectedItem.getName() + " adlı parçayı silmek üzeresiniz. Onaylıyor musunuz?",
                    "Parça Silme Onayı",
                    JOptionPane.YES_NO_OPTION
            );
            if (n == JOptionPane.YES_OPTION) {
                removeComponent();
            }
        });

        buttonInput.addActionListener(e -> {
            new IncreaseComponentStockDialog(selectedItem, this).setVisible(true);
        });

        buttonOutput.addActionListener(e -> {
            new DecreaseComponentStockDialog(selectedItem, this).setVisible(true);
        });

        fieldComponentCode.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldComponentCode.getText());
        });
    }

    private void removeComponent() {
        App.getDatabaseHelper().deleteComponentById(selectedItem.getId());
        tableModel.remove(selectedItem);
        fieldComponentCode.setText("");
    }

    private List<Component> fetchAllComponents() {
        return App.getDatabaseHelper().getComponents();
    }

    private void listAll(List<Component> components) {
        tableModel = new ComponentTableModel(components);
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

    public void setSelectedItem(Component selectedItem) {
        this.selectedItem = selectedItem;
        if (selectedItem == null) {
            buttonRemove.setEnabled(false);
            buttonInput.setEnabled(false);
            buttonOutput.setEnabled(false);
        } else {
            buttonRemove.setEnabled(true);
            buttonInput.setEnabled(true);
            buttonOutput.setEnabled(true);

        }
    }

    @Override
    public void onAddComponent(Component component) {
        tableModel.add(component);
        fieldComponentCode.setText("");
    }

    @Override
    public void onIncreaseComponentStock(int newStock) {
        selectedItem.setStock(newStock);
        tableModel.update(selectedItem);
    }

    @Override
    public void onDecreaseComponentStock(int newStock) {
        selectedItem.setStock(newStock);
        tableModel.update(selectedItem);
    }
    
    
    
}

