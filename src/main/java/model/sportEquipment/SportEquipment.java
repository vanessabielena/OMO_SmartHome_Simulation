package model.sportEquipment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Optional;

/**
 * This class represents the sport equipment available in the house.
 */
public class SportEquipment {

    private final HashMap<String, Integer> equipmentAvailabilityMap = new HashMap<>();

    Logger logger = LogManager.getLogger(SportEquipment.class);
    public enum EquipmentType {
        BIKE,
        SKIS
    }

    public SportEquipment() {
        // Initialize equipment availability to 0 for each type
        equipmentAvailabilityMap.put(EquipmentType.BIKE.toString(), 0);
        equipmentAvailabilityMap.put(EquipmentType.SKIS.toString(), 0);
    }

    /**
     * Adds a piece of equipment to the sport equipment.
     * @param equipment
     */
    public void addEquipment(EquipmentType equipment) {
        logger.info("Adding " + equipment.toString() + " to the sport equipment.");
        equipmentAvailabilityMap.put(equipment.toString(), equipmentAvailabilityMap.getOrDefault(equipment.toString(), 0) + 1);
    }

    public boolean removeEquipment(EquipmentType equipment) {
        int currentCount = equipmentAvailabilityMap.getOrDefault(equipment.toString(), 0);
        if (currentCount > 0) {
            equipmentAvailabilityMap.put(equipment.toString(), currentCount - 1);
            logger.info("Removing " + equipment.toString() + " from the sport equipment.");
            return true;
        } else {
            logger.info("No " + equipment.toString() + " available to remove.");
            return false;
        }
    }

    /**
     * Checks if any equipment is available.
     * @return true if any equipment is available, false otherwise
     */
    public boolean isAnyEquipmentAvailable() {
        // Check if any equipment type has a count greater than 0
        return equipmentAvailabilityMap.values().stream().anyMatch(integer -> integer > 0);
    }

    /**
     * Gets any available equipment.
     * @return an Optional containing the equipment type if any is available, empty otherwise
     */
    public Optional<EquipmentType> getAnyEquipment() {
        // Find the first available equipment type and return it as an Optional
        return equipmentAvailabilityMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> EquipmentType.valueOf(entry.getKey()))
                .findFirst();
    }

    public HashMap<String, Integer> getEquipmentAvailabilityMap() {
        return equipmentAvailabilityMap;
    }
}
