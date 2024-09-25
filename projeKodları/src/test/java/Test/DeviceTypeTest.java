
package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import system.model.DeviceType;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class DeviceTypeTest {
    
    public DeviceTypeTest() {
    }

    @Test
    public void testGetId() {
        DeviceType instance = new DeviceType(1, "Laptop");
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetName() {
        String name = "Laptop";
        DeviceType instance = new DeviceType(1, name);
        String expResult = name;
        String result = instance.getName();
        assertEquals(expResult, result);
    }

   

    @Test
    public void testToString() {
        DeviceType instance = new DeviceType(1, "Laptop");
        String expResult = "Laptop";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
