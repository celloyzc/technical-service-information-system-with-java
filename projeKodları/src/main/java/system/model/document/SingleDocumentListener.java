package system.model.document;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public interface SingleDocumentListener extends DocumentListener {
  void onChange(DocumentEvent paramDocumentEvent);
  
  default void insertUpdate(DocumentEvent e) {
    onChange(e);
  }
  
  default void removeUpdate(DocumentEvent e) {
    onChange(e);
  }
  
  default void changedUpdate(DocumentEvent e) {
    onChange(e);
  }
}
