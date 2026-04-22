package model.devices.concreteDevices;

import model.devices.AbstractDevice;
import model.house.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

public class Fridge extends AbstractDevice {

    private Map<String, Integer> food;
    private LocalTime lastTurnOnTimestamp;

Logger logger = LogManager.getLogger(Fridge.class);
    public Fridge(String name, int id, Room room) {
        super(name, id,room);
        this.food = new HashMap<>();
        hasTimeFunction = true;
        this.lastTurnOnTimestamp = room.getHouse().getTimeEntity().getCurrentTime().getCurrentLocalTime();

        for (int i = 0; i < 3; i++)
        {
            addRandomFood();
        }

    }


    public boolean isFoodInFridge(String foodName) {
        return food.containsKey(foodName);
    }

    public void addFood(String foodName, int quantity) {
        if (food.containsKey(foodName)) {
            food.put(foodName, food.get(foodName) + quantity);
        } else {
            food.put(foodName, quantity);
        }
    }

    public void removeFood(String foodName, int quantity) {
        if (food.containsKey(foodName)) {
            food.put(foodName, food.get(foodName) - quantity);
        }
    }

    public int getFoodQuantity(String foodName) {
        if (food.containsKey(foodName)) {
            return food.get(foodName);
        } else {
            logger.error("Food not found in fridge");
            throw new IllegalArgumentException("Food not found in fridge");
        }
    }

    public void setFoodQuantity(String foodName, int quantity) {
        if (food.containsKey(foodName)) {
            food.put(foodName, quantity);
        }
        //add food if not in fridge
        else {
            food.put(foodName, quantity);
        }
    }

    public void clearFridge() {
        food.clear();
    }

    public void printFridgeContents() {
        System.out.println("Fridge contents:");
        for (String foodName : food.keySet()) {
            System.out.println(foodName + ": " + food.get(foodName));
        }
    }

    public Map<String, Integer> getFridgeContents() {
        return food;
    }

    //get random food from fridge
    public String eatRandomFood(){
        int random = (int) (Math.random() * food.size());
        int i = 0;
        for (String foodName : food.keySet()) {
            if (i == random) {
                //remove food
                removeFood(foodName, 1);
                return foodName;

            }
            i++;
        }
        return null;
    }

    @Override
    public boolean turnOff(LocalTime time) {
        this.powerConsumption += calculatePowerConsumption(Duration.between(time, lastTurnOnTimestamp).getSeconds());
        lastTurnOnTimestamp = getRoom().getHouse().getTimeEntity().getCurrentTime().getCurrentLocalTime();
        return true;
    }


    public float calculatePowerConsumption(long durationInSeconds) {
        return abs(durationInSeconds * 0.00004f);
    }

    public void addRandomFood() {
        int random = (int) (Math.random() * 5);
        switch (random) {
            case 0:
                addFood("Apple", 1);
                break;
            case 1:
                addFood("Banana", 1);
                break;
            case 2:
                addFood("Orange", 1);
                break;
            case 3:
                addFood("Milk", 1);
                break;
            case 4:
                addFood("Cheese", 1);
                break;
            case 5:
                addFood("Bread", 1);
                break;
        }
    }
}
