import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.commons.logging.LoggerFactory;
import simulation.TimeSimulation;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;


public class InputTests {
//    private static final Logger logger = LogManager.getLogger(TimeSimulation.class);

    private static Logger logger = LogManager.getLogger(TimeSimulation.class);



    @Test
    void testValidInput() {
        // Mock user input for house configuration
        String mockInput = "y" +
                "\nn" +
                "\n2" + // Number of floors
                "\n2" + // Number of rooms
                "\n4" + // Number of pets members
                "\n10" + // Number of family members
                "\n5" + // Number of sport equipment
                "\n1" + // Event frequency
                "\n1.5" + // Gas price per litre
                "\n0.2" + // Water price per litre
                "\n0.15" + // Electricity price per kWh
                "\n2"; // Simulation run time
        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        // Run the house configuration
        TimeSimulation.houseConfig();

        // Run the simulation
        TimeSimulation timeSimulation = new TimeSimulation();
        timeSimulation.startSimulation();

        // Wait for the simulation to run (adjust this based on your actual simulation duration)
        try {
            // Run the simulation for 60 seconds
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the simulation
        timeSimulation.getTimer().cancel();
    }

    @Test
    void validateNumericInput_ValidInputInRange_ReturnsInputValue() {
        System.setIn(new ByteArrayInputStream("5\n".getBytes()));
        Scanner scanner = new Scanner(System.in);
        int result = TimeSimulation.validateNumericInput(scanner, 1, 10);
        assertEquals(5, result);
    }

    @Test
    void validateNumericInput_InvalidInputBelowLowerLimit_ThrowsInputMismatchException() {
        System.setIn(new ByteArrayInputStream("0\n".getBytes()));
        Scanner scanner = new Scanner(System.in);
        assertThrows(InputMismatchException.class, () ->
                TimeSimulation.validateNumericInput(scanner, 1, 10));
    }

    @Test
    void validateNumericInput_InvalidInputAboveUpperLimit_ThrowsInputMismatchException() {
        System.setIn(new ByteArrayInputStream("15\n".getBytes()));
        Scanner scanner = new Scanner(System.in);
        assertThrows(InputMismatchException.class, () ->
                TimeSimulation.validateNumericInput(scanner, 1, 10));
    }

    @Test
    void validatePositiveNumericInput_ValidPositiveInputInRange_ReturnsInputValue() {
        System.setIn(new ByteArrayInputStream("3.5\n".getBytes()));
        Scanner scanner = new Scanner(System.in);
        float result = TimeSimulation.validatePositiveNumericInput(scanner,TimeSimulation.LOWER_FLOAT_LIMIT, 5.0f);
        assertEquals(3.5f, result);
    }

    @Test
    void validatePositiveNumericInput_InvalidNegativeInput_ThrowsInputMismatchException() {
        System.setIn(new ByteArrayInputStream("-1.0\n".getBytes()));
        Scanner scanner = new Scanner(System.in);
        assertThrows(InputMismatchException.class, () ->
                TimeSimulation.validatePositiveNumericInput(scanner,TimeSimulation.LOWER_FLOAT_LIMIT, 5.0f));
    }

    @Test
    void validatePositiveNumericInput_InvalidInputAboveUpperLimit_ThrowsInputMismatchException() {
        System.setIn(new ByteArrayInputStream("6.0\n".getBytes()));
        Scanner scanner = new Scanner(System.in);
        assertThrows(InputMismatchException.class, () ->
                TimeSimulation.validatePositiveNumericInput(scanner, TimeSimulation.LOWER_FLOAT_LIMIT,5.0f));
    }






    @Test
    void testValidJSONInput() {

        String mockInput = "y" +
                "\ny";

        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        TimeSimulation timeSimulation = new TimeSimulation();

        timeSimulation.getSimulationConfig().setJSON_FILE_PATH("JsonDataFiles/correct_simulation_data.json");
        // Run the house configuration
        TimeSimulation.houseConfig();

        // Run the simulation
        timeSimulation.startSimulation();

        // Wait for the simulation to run (adjust this based on your actual simulation duration)
        try {
            Thread.sleep((long) (timeSimulation.getSimulationConfig().getSimulationRunTime()*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the simulation
        timeSimulation.getTimer().cancel();
    }


    @Test
    void JSONInputCorrectDataLessVars() {

        String mockInput = "y" +
                "\ny";

        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        TimeSimulation timeSimulation = new TimeSimulation();

        timeSimulation.getSimulationConfig().setJSON_FILE_PATH("JsonDataFiles/correct_simulation_data_less_vars.json");
        // Run the house configuration
        TimeSimulation.houseConfig();

        // Run the simulation
        timeSimulation.startSimulation();

        // Wait for the simulation to run (adjust this based on your actual simulation duration)
        try {
            Thread.sleep((long) (timeSimulation.getSimulationConfig().getSimulationRunTime()*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the simulation
        timeSimulation.getTimer().cancel();
    }




    @Test
    void JSONInputFails1() {

        String mockInput = "y" +
                "\ny";

        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        TimeSimulation timeSimulation = new TimeSimulation();

        timeSimulation.getSimulationConfig().setJSON_FILE_PATH("JsonDataFiles/incorrect_simulation_data.json");
        // Run the house configuration

        //expect that the NoSuchElementException is thrown when the y is entered a second time
        //and the loadJSONConfig() is called and throws false since if it were found it woudln't need additional input
        assertThrows(NoSuchElementException.class, () -> TimeSimulation.houseConfig());


    }


    @Test
    void JSONInputFails2() {
        String mockInput =
                "y\n" +
                "y"; // Simulate user input for loading from JSON

        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        TimeSimulation timeSimulation = new TimeSimulation();

        timeSimulation.getSimulationConfig().setJSON_FILE_PATH("JsonDataFiles/incorrect_simulation_data2.json");
        // Run the house configuration
        //expect that the NoSuchElementException is thrown when the y is entered a second time
        //and the loadJSONConfig() is called and throws false since if it were found it woudln't need additional input
        assertThrows(NoSuchElementException.class, () -> TimeSimulation.houseConfig());

    }


    @Test
    void JSONInputFails3() {

        String mockInput = "y" +
                "\ny";

        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        TimeSimulation timeSimulation = new TimeSimulation();

        timeSimulation.getSimulationConfig().setJSON_FILE_PATH("JsonDataFiles/incorrect_simulation_data3.json");
        //expect that the NoSuchElementException is thrown when the y is entered a second time
        //and the loadJSONConfig() is called and throws false since if it were found it woudln't need additional input

        assertThrows(NoSuchElementException.class, () -> TimeSimulation.houseConfig());

    }



    @Test
    void JSONInputFails4() {

        String mockInput = "y" +
                "\ny";

        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        TimeSimulation timeSimulation = new TimeSimulation();

        timeSimulation.getSimulationConfig().setJSON_FILE_PATH("JsonDataFiles/incorrect_simulation_data4.json");

        // Run the house configuration
        //expect that the NoSuchElementException is thrown when the y is entered a second time
        //and the loadJSONConfig() is called and throws false since if it were found it woudln't need additional input
        assertThrows(NoSuchElementException.class, () -> TimeSimulation.houseConfig());

    }

    @Test
    void JSONInputFails_InputFileMissing() {

        String mockInput = "y" +
                "\ny";

        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        TimeSimulation timeSimulation = new TimeSimulation();
        timeSimulation.getSimulationConfig().setJSON_FILE_PATH("JsonDataFiles/MISSINGFILE.json");
        // Run the house configuration
        //expect that the NoSuchElementException is thrown when the y is entered a second time
        //and the loadJSONConfig() is called and throws false since if it were found it woudln't need additional input

        assertThrows(NoSuchElementException.class, () -> TimeSimulation.houseConfig());

    }



    @Test
    void JSONInputFails_WrongFileFormat() {

        String mockInput = "y" +
                "\ny";

        System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

        TimeSimulation timeSimulation = new TimeSimulation();

        timeSimulation.getSimulationConfig().setJSON_FILE_PATH("JsonDataFiles/incorrect_simulation_data.abc");
        // Run the house configuration //expect that the NoSuchElementException is thrown when the y is entered a second time
        //        //and the loadJSONConfig() is called and throws false since if it were found it woudln't need additional input
        //

        //expect that the NoSuchElementException is thrown when the y is entered a second time
        //and the loadJSONConfig() is called and throws false since if it were found it woudln't need additional input

        assertThrows(NoSuchElementException.class, () -> TimeSimulation.houseConfig());
    }

}
