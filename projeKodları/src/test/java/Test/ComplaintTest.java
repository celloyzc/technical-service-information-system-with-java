
package Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import system.model.Complaint;
import system.model.ComplaintStatus;
import system.model.Component;
import system.model.Customer;
import system.model.DeviceBrand;
import system.model.DeviceType;
import system.model.Technician;
import system.model.UsedComponent;

/**
 *
 * @author AHMET CELAL YAZICI
 */
public class ComplaintTest {
    
    public ComplaintTest() {
    }

    @Test
    public void testGetId() {
        Complaint instance = new Complaint(1, null, null, null, null, null, null, null, 0, 0, null, null);
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCustomer() {
        Customer customer = new Customer(1, "John", "Doe", "john@example.com");
        Complaint instance = new Complaint(1, customer, null, null, null, null, null, null, 0, 0, null, null);
        Customer expResult = customer;
        Customer result = instance.getCustomer();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTechnician() {
        Technician technician = new Technician(1, "Mike", "Smith");
        Complaint instance = new Complaint(1, null, technician, null, null, null, null, null, 0, 0, null, null);
        Technician expResult = technician;
        Technician result = instance.getTechnician();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetComplaint() {
        String complaint = "Device is not working";
        Complaint instance = new Complaint(1, null, null, complaint, null, null, null, null, 0, 0, null, null);
        String expResult = complaint;
        String result = instance.getComplaint();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDeviceType() {
        DeviceType deviceType = new DeviceType(1, "Laptop");
        Complaint instance = new Complaint(1, null, null, null, deviceType, null, null, null, 0, 0, null, null);
        DeviceType expResult = deviceType;
        DeviceType result = instance.getDeviceType();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetBrand() {
        DeviceBrand brand = new DeviceBrand(1, "HP");
        Complaint instance = new Complaint(1, null, null, null, null, brand, null, null, 0, 0, null, null);
        DeviceBrand expResult = brand;
        DeviceBrand result = instance.getBrand();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSerialNumber() {
        String serialNumber = "123456";
        Complaint instance = new Complaint(1, null, null, null, null, null, serialNumber, null, 0, 0, null, null);
        String expResult = serialNumber;
        String result = instance.getSerialNumber();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetLaborFee() {
        int laborFee = 100;
        Complaint instance = new Complaint(1, null, null, null, null, null, null, null, laborFee, 0, null, null);
        int expResult = laborFee;
        int result = instance.getLaborFee();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTotalFee() {
        int totalFee = 500;
        Complaint instance = new Complaint(1, null, null, null, null, null, null, null, 0, totalFee, null, null);
        int expResult = totalFee;
        int result = instance.getTotalFee();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDescription() {
        String description = "Device needs repair";
        Complaint instance = new Complaint(1, null, null, null, null, null, null, null, 0, 0, description, null);
        String expResult = description;
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetUsedComponents() {
        List<UsedComponent> usedComponents = new ArrayList<>();
        usedComponents.add(new UsedComponent(new Component(1, "Component", "Model", 50, 10), 2));
        Complaint instance = new Complaint(1, null, null, null, null, null, null, null, 0, 0, null, usedComponents);
        List<UsedComponent> expResult = usedComponents;
        List<UsedComponent> result = instance.getUsedComponents();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetId() {
        int id = 2;
        Complaint instance = new Complaint(1, null, null, null, null, null, null, null, 0, 0, null, null);
        instance.setId(id);
        assertEquals(id, instance.getId());
    }


    @Test
    public void testSetUsedComponents() {
        List<UsedComponent> usedComponents = new ArrayList<>();
        usedComponents.add(new UsedComponent(new Component(1, "Component", "Model", 50, 10), 2));
        Complaint instance = new Complaint(1, null, null, null, null, null, null, null, 0, 0, null, null);
        instance.setUsedComponents(usedComponents);
        assertEquals(usedComponents, instance.getUsedComponents());
    }

    @Test
    public void testSetLaborFee() {
        int laborFee = 150;
        Complaint instance = new Complaint(1, null, null, null, null, null, null, null, 100, 0, null, null);
        instance.setLaborFee(laborFee);
        assertEquals(laborFee, instance.getLaborFee());
    }

    @Test
    public void testSetTotalFee() {
        int totalFee = 600;
        Complaint instance = new Complaint(1, null, null, null, null, null, null, null, 0, 500, null, null);
        instance.setTotalFee(totalFee);
        assertEquals(totalFee, instance.getTotalFee());
    }

    @Test
    public void testSetDescription() {
        String description = "Device repaired successfully";
        Complaint instance = new Complaint(1, null, null, null, null, null, null, null, 0, 0, null, null);
        instance.setDescription(description);
        assertEquals(description, instance.getDescription());
    }
    
    
}
