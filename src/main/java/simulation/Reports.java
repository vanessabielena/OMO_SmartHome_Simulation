package simulation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Reports {

//    private final static Logger reports = Logger.getLogger(Reports.class.getName());
//    public static void Logger(String[] args) throws IOException {
//        FileHandler fh = new FileHandler("log.txt");
//        fh.setLevel(Level.ALL);
//        fh.setFormatter(new SimpleFormatter());
//        reports.addHandler(fh);
//
//        reports.setLevel(Level.ALL);

    /**
     * Generates a report for the events that happen during the simulation.
     */
    public static void generateEventReport() {
        generateReport("events.log", "Event");
    }

    /**
     * Generates a report for the house configuration.
     */
    public static void generateHouseConfigReport() {
        generateReport("houseConfig.log", "House Configuration");
    }

    /**
     * Generates a report for the activity and usage of the house.
     */
    public static void generateActivityAndUsageReport() {
        generateReport("activityAndUsage.log", "Activity and Usage");
    }

    /**
     * Generates a report for the consumption of the house.
     */
    public static void generateConsumptionReport() {
        generateReport("consumption.log", "Consumption");
    }

    /**
     * Generates a report for the events that happen during the simulation.
     * @param logFileName name of the log file
     * @param reportName name of the report
     */
    public static void generateReport(String logFileName, String reportName) {
        createNewLogFileIfNonExistent(logFileName);
        List<String> events = SimulationLogger.readLogs(logFileName);

        if (events != null) {
            // some kind of logic with events list
            System.out.println(reportName + " Report generated successfully.");
        } else {
            System.out.println("Error reading logs for " + reportName + " Report.");
        }
    }

    /**
     * Creates a new log file if it doesn't exist.
     * @param logFileName name of the log file
     */
    public static void createNewLogFileIfNonExistent(String logFileName) {
        Path logFilePath = Paths.get(logFileName);

        try {
            if (Files.notExists(logFilePath)) {
                Files.createFile(logFilePath);
                System.out.println("Created new log file: " + logFileName);
            }
        } catch (IOException e) {
            System.out.println("Error creating log file: " + logFileName);
            e.printStackTrace();
        }
    }

    public static void generateEventReportOrdered() {
        generateReport("eventsOrderedAndGrouped.log", "Ordered Event");
    }
}