
package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import system.model.Customer;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class CustomerTest {
    
    public CustomerTest() {
    }

    @Test
    public void testGetId() {
        Customer instance = new Customer(1, null, null, null);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetName() {
        String name = "John Doe";
        Customer instance = new Customer(1, name, null, null);
        String expResult = name;
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetPhone() {
        String phone = "123456789";
        Customer instance = new Customer(1, null, phone, null);
        String expResult = phone;
        String result = instance.getPhone();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAddress() {
        String address = "123 Main St";
        Customer instance = new Customer(1, null, null, address);
        String expResult = address;
        String result = instance.getAddress();
        assertEquals(expResult, result);
    }


    @Test
    public void testToString() {
        Customer instance = new Customer(1, "John Doe", "123456789", "123 Main St");
        String expResult = "Customer{id='1'name='John Doe'phone='123456789'address='123 Main St'}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
