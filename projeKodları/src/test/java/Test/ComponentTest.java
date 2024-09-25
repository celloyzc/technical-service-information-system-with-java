
package Test;

import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import system.model.Component;


public class ComponentTest {
    
    public ComponentTest() {
    }

    @Test
    public void testGetId() {
        Component instance = new Component(1, null, null, 0, 0);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCode() {
        String code = "ABC123";
        Component instance = new Component(1, code, null, 0, 0);
        String expResult = code;
        String result = instance.getCode();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetName() {
        String name = "Component";
        Component instance = new Component(1, null, name, 0, 0);
        String expResult = name;
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetStock() {
        int stock = 10;
        Component instance = new Component(1, null, null, stock, 0);
        int expResult = stock;
        int result = instance.getStock();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetPrice() {
        int price = 50;
        Component instance = new Component(1, null, null, 0, price);
        int expResult = price;
        int result = instance.getPrice();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetCode() {
        String code = "XYZ456";
        Component instance = new Component(1, null, null, 0, 0);
        instance.setCode(code);
        assertEquals(code, instance.getCode());
    }

    @Test
    public void testSetStock() {
        int stock = 20;
        Component instance = new Component(1, null, null, 0, 0);
        instance.setStock(stock);
        assertEquals(stock, instance.getStock());
    }

    @Test
    public void testSetId() {
        int id = 2;
        Component instance = new Component(1, null, null, 0, 0);
        instance.setId(id);
        assertEquals(id, instance.getId());
    }

  

    @Test
    public void testToString() {
        Component instance = new Component(1, "ABC123", "Component", 10, 50);
        String expResult = "Component{id=1, code='ABC123', name='Component', stock=10, price=50}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
