import simulation.TimeSimulation;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

public class RepetitionLargeScaleTests {

    @Test
    void repeatingEndToEndTest() throws NoSuchMethodException {


        //TAKES ME ABOUT 21 SECONDS

        //BE CAREFUL ABOUT THIS TEST - WHEN WRONG VALUES ARE SET IT CAN EDIT THE SAME VALUES
        for (int i = 0; i < 10; i++) {
            // Mock user input for house configuration
            String mockInput = "n\n";  // Assuming default/random values
            System.setIn(new ByteArrayInputStream(mockInput.getBytes()));

            // Run the simulation
            TimeSimulation timeSimulation = new TimeSimulation();

            // Run the house configuration
            TimeSimulation.houseConfig();

            // Every 1/1000 seconds
            TimeSimulation.setCycleSecondInterval(0.001f);

            // Run the simulation for 2 seconds
            float simulationRunTime = 2;
            timeSimulation.startSimulation();

            // Wait for the simulation to run (adjust this based on your actual simulation duration)
            try {
                Thread.sleep((long) simulationRunTime * 1000);  // Convert seconds to milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Stop the simulation
            timeSimulation.getTimer().cancel();
        }
    }

}
