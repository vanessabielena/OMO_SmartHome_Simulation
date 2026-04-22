import model.devices.AbstractDevice;
import simulation.TimeSimulation;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FunctionalTests {

    //test if making a house and rooms and devices works


    @Test
    void testHouseCreationAndSimulation() throws NoSuchMethodException {
        // Mock user input for house configuration
        String mockInput = "n\n";  // Assuming default/random values
        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        // Run the house configuration
        TimeSimulation.houseConfig();

        // Run the simulation
        TimeSimulation timeSimulation = new TimeSimulation();
        timeSimulation.startSimulation();

        // Wait for the simulation to run (adjust this based on your actual simulation duration)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the simulation
        timeSimulation.getTimer().cancel();

        // Check if the house and family were created successfully
        assertNotNull(TimeSimulation.getHouse());
        assertNotNull(TimeSimulation.getFamily());
        assertFalse(TimeSimulation.getFamily().isEmpty());

        // Check if the rooms, devices, and people were generated
        assertNotNull(TimeSimulation.getHouse().getRooms());
        assertFalse(TimeSimulation.getHouse().getRooms().isEmpty());
        assertNotNull(TimeSimulation.getHouse().getDevices());
        assertFalse(TimeSimulation.getHouse().getDevices().isEmpty());
        assertNotNull(TimeSimulation.getHouse().getPeople());
        assertFalse(TimeSimulation.getHouse().getPeople().isEmpty());
    }

    @Test
    void testHouseCreationAndSimulationWithManualInput() throws NoSuchMethodException {
        // Mock user input for house configuration
        String mockInput =
                "\ny" +
                "\nn"+
                "\n2"   + //floors,
                "\n2"   + //rooms,
                "\n1"   + //pets
                "\n3"   + //family members
                "\n5"   + //sport equipment
                "\n1"   + //frequency
                "\n1.5" + //gas price
                "\n0.2" + //water price
                "\n0.15"+ //electricity price
                "\n1";  //simulation run time


        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        // Run the house configuration
        TimeSimulation.houseConfig();

        // Run the simulation
        TimeSimulation timeSimulation = new TimeSimulation();
        timeSimulation.startSimulation();

        // Wait for the simulation to run (adjust this based on your actual simulation duration)
        try {
            // Run the simulation for a longer duration, for example, 60 seconds
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the simulation
        timeSimulation.getTimer().cancel();

        // Check if the house and family were created successfully
        assertNotNull(TimeSimulation.getHouse());
        assertNotNull(TimeSimulation.getFamily());
        assertFalse(TimeSimulation.getFamily().isEmpty());

        // Check if the manually entered values for rooms, devices, and people match
        assertEquals(2, TimeSimulation.getHouse().getRooms().size());
        assertEquals(3, TimeSimulation.getHouse().getPeople().size());
        System.out.println(TimeSimulation.getHouse().getPets());
        assertEquals(1, TimeSimulation.getHouse().getPets().size());


    }





    @Test
    void testConsumptionReport() throws NoSuchMethodException {
        // Mock user input for house configuration
        String mockInput = "n\n";  // Assuming default/random values
        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        // Run the house configuration
        TimeSimulation.houseConfig();

        // Run the simulation
        TimeSimulation timeSimulation = new TimeSimulation();
        timeSimulation.startSimulation();

        // Wait for the simulation to run (adjust this based on your actual simulation duration)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the simulation
        timeSimulation.getTimer().cancel();

        // Check if the file exists and isn't empty

        File f = new File("consumption.log");

        assertTrue(f.exists() && !f.isDirectory());

        assertTrue(f.length() > 0);
    }



}
