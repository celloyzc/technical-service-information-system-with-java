
package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import system.model.DeviceBrand;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class DeviceBrandTest {
    
    public DeviceBrandTest() {
    }

    @Test
    public void testGetId() {
        DeviceBrand instance = new DeviceBrand(1, "Apple");
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetName() {
        String name = "Apple";
        DeviceBrand instance = new DeviceBrand(1, name);
        String expResult = name;
        String result = instance.getName();
        assertEquals(expResult, result);
    }   

    @Test
    public void testToString() {
        DeviceBrand instance = new DeviceBrand(1, "Apple");
        String expResult = "Apple";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
