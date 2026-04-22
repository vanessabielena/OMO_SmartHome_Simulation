package simulation;
import model.devices.AbstractDevice;
import model.events.Event;
import model.house.House;

import java.util.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * This class is responsible for logging the events that happen during the simulation.
 */
public class SimulationLogger {

    public static List<Event> events = new ArrayList<>();

    public static void logEventOrdered(Event event) {
        events.add(event);

    }

    public static void logGroupedEvents(String fileName, Map<String, Map<String, Map<String, List<Event>>>> orderedEvents) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, false))) {
            writer.println("            ORDERED EVENTS           ");
            writer.println("----------------------------------------");
            writer.println("----------------------------------------");
            writer.println("----------------------------------------");
            writer.println("\n \n \n");

            for (Map.Entry<String, Map<String, Map<String, List<Event>>>> entry : orderedEvents.entrySet()) {
                writer.println("\n \n EVENT: \n \n");
                writer.println("    Description: " );
                writer.println("        " +entry.getKey());
                writer.println("        ----------------- \n");

                for (Map.Entry<String, Map<String, List<Event>>> sourceEntry : entry.getValue().entrySet()) {
                    writer.println("    Source: ");

                    writer.println("        " +sourceEntry.getKey());
                    writer.println("        ----------------- \n");

                    for (Map.Entry<String, List<Event>> targetEntry : sourceEntry.getValue().entrySet()) {
                        writer.println("    Target: " );

                        writer.println("        " +targetEntry.getKey());
                        writer.println("        ----------------- \n");

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void finishLogging() {
        // Group events by description, source, and target

        // Order events alphabetically by description and then by source and target names
        Map<String, Map<String, Map<String, List<Event>>>> orderedEvents = events.stream()
                .collect(Collectors.groupingBy(Event::getDescription,
                        TreeMap::new, // Use TreeMap for alphabetical ordering
                        Collectors.groupingBy(e -> e.getEventSource().toString(),
                                TreeMap::new, // Use TreeMap for alphabetical ordering
                                Collectors.groupingBy(e -> e.getEventTarget().toString(),
                                        TreeMap::new, // Use TreeMap for alphabetical ordering
                                        Collectors.toList()))));

        // Log the ordered events
        logGroupedEvents("eventsOrderedAndGrouped.log", orderedEvents);





    }


    /**
     * Logs an event to the events log file.
     * @param message message to be logged
     */
    public static void logEvent(String message) {
        log("events.log", message);
    }

    /**
     * Logs the house configuration to the house configuration log file.
     * @param message message to be logged
     */
    public static void logHouseConfig(String message) {
        log("houseConfig.log", message);
    }

    /**
     * Logs the activity and usage of the house to the activity and usage log file.
     * @param message message to be logged
     */
    public static void logActivityAndUsage(String message) {
        log("activityAndUsage.log", message);
    }

    /**
     * Logs the consumption of the house to the consumption log file.
     * @param message message to be logged
     */
    public static void logConsumption(String message) {
        log("consumption.log", message);
    }

    /**
     * Logs a message to a file.
     * @param fileName name of the log file
     * @param message message to be logged
     */
    public static void log(String fileName, String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new log file if it doesn't exist.
     * @param logFileName name of the log file
     * @return list of strings with the logs
     */
    public static List<String> readLogs(String logFileName) {
        try {
            return Files.lines(Paths.get(logFileName))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Clears all the logs except for the app log.
     */
    public static void clearSimulationLogs() {
        clearLogFile("events.log");
        clearLogFile("houseConfig.log");
        clearLogFile("activityAndUsage.log");
        clearLogFile("consumption.log");
        clearLogFile("eventsOrdered.log");

    }

    /**
     * Clears the log file.
     * @param fileName name of the log file
     */
    private static void clearLogFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            try {
                Files.write(Paths.get(fileName), "".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Logs the start time of the simulation in all the logs.
     */
    public static void logStartTimeInAllLogs() {
        LocalDateTime now = LocalDateTime.now();
        logEvent("Simulation started at: " + now);
        logHouseConfig("Simulation started at: " + now);
        logActivityAndUsage("Simulation started at: " + now);
        logConsumption("Simulation started at: " + now);
    }
}
