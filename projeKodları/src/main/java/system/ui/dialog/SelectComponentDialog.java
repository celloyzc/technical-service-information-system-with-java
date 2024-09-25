/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.ui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import system.model.Component;
import system.model.document.SingleDocumentListener;
import system.model.table.ComponentTableModel;
import system.ui.App;
import static system.util.TableRendererUtils.setRenderer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class SelectComponentDialog extends JDialog {
    private static final String TITLE = "Parça Seç";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldCode;
    private JTable tableComponents;
    private JTextField fieldPiece;

    private final SelectComponentListener listener;
    private Component selectedComponent;
    private ComponentTableModel tableModel;

    public SelectComponentDialog(SelectComponentListener listener) {
        this.listener = listener;

        setTitle(TITLE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setModal(true);
        initializeUIComponents();
        layoutComponents();
 
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableComponents);

        List<Component> components = fetchAllComponents();
        listAll(components);

        setSelectedComponent(null);
        
        fieldPiece.setText("1");

        fieldCode.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterComponents();
            }

            public void removeUpdate(DocumentEvent e) {
                filterComponents();
            }

            public void changedUpdate(DocumentEvent e) {
                filterComponents();
            }

            private void filterComponents() {
                tableModel.filter(fieldCode.getText());
            }
        });

        setupEventListeners();

        pack();
        setLocationRelativeTo(null);

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
    private void initializeUIComponents() {
        contentPane = new JPanel(new GridBagLayout());
        buttonOK = new JButton("OK");
        buttonCancel = new JButton("İptal");
        fieldCode = new JTextField(20);
        tableComponents = new JTable();
        fieldPiece = new JTextField(5);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
    }

    private void layoutComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(new JLabel("Parça Kodu:"), gbc);
        gbc.gridx = 1;
        contentPane.add(fieldCode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        contentPane.add(new JScrollPane(tableComponents), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPane.add(new JLabel("Adet:"), gbc);
        gbc.gridx = 1;
        contentPane.add(fieldPiece, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPane.add(buttonOK, gbc);
        gbc.gridx = 1;
        contentPane.add(buttonCancel, gbc);
    }

    private void setupEventListeners() {
        buttonOK.addActionListener(e -> {
            String piece = fieldPiece.getText().trim();
            if (!isValidPiece(piece)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir adet giriniz!");
                return;
            }
            onOK(Integer.parseInt(piece));
        });

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
    private boolean isValidPiece(String piece) {
        return Pattern.matches("\\d{1,9}", piece) && Integer.parseInt(piece) > 0;
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
                setSelectedComponent(null);
                return;
            }
            setSelectedComponent(tableModel.getItem(selectedIndex));
        });
    }

    private void onOK(int piece) {
        listener.onSelectComponent(selectedComponent, piece);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface SelectComponentListener {
        void onSelectComponent(Component component, int piece);
    }

    public void setSelectedComponent(Component selectedComponent) {
        this.selectedComponent = selectedComponent;
        if(selectedComponent == null) {
            buttonOK.setEnabled(false);
        } else {
            buttonOK.setEnabled(true);
        }
    }
}

