package model.devices;

import model.devices.concreteDevices.PetFeeder.PetFeeder;
import model.devices.concreteDevices.blinds.Blinds;
import model.devices.concreteDevices.car.Car;
import model.devices.concreteDevices.dishwasher.Dishwasher;
import model.devices.concreteDevices.lights.Lights;
import model.devices.concreteDevices.sensors.TemperatureSensor;
import model.devices.concreteDevices.heatingPanel.HeatingPanel;
import model.devices.concreteDevices.washing_machine.WashingMachine;
import model.house.House;
import model.house.Room;
import model.house.RoomType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeviceFactory {

    private int lastIdCreated;
    Logger logger = LogManager.getLogger(DeviceFactory.class);

    House house;
    public DeviceFactory(House house) {
        this.lastIdCreated = 0;
        this.house = house;
    }

    /**
     * Creates a device of the given type, with the given name, in the given room.
     * @param deviceType type of device
     * @param name  name of device
     * @param room room to put device in
     * @return the created device
     */
    public AbstractDevice createDevice(DeviceType deviceType, String name, Room room) {
        logger.info("Creating device of type " + deviceType + " with name " + name + " in room " + room.getName());
        return createDeviceWithTypeNameAndRoom(deviceType, name, room);
    }

    /**
     * a switch statement that creates a device of the given type, with the given name, in the given room.
     * Not to be used outside of this class.
     * @param deviceType type of device
     * @param name name of device
     * @param room room to put device in
     * @return the created device
     */
    private AbstractDevice createDeviceWithTypeNameAndRoom(DeviceType deviceType, String name, Room room) {
        switch (deviceType) {
            case LIGHT:
                return new Lights(name, lastIdCreated++, room);
            case HEATING_PANEL:
                return new HeatingPanel(name, lastIdCreated++, room);
            case DISHWASHER:
                return new Dishwasher(name, lastIdCreated++, room);
            case WASHING_MACHINE:
                return new WashingMachine(name, lastIdCreated++, room);
            case TEMPERATURE_SENSOR:
                return new TemperatureSensor(name, lastIdCreated++, room);
            case CAR:
                if(room.getRoomType() == RoomType.GARAGE){
                    return new Car(name, lastIdCreated++, room);
                }
                else{
                    logger.error("Car can only be created in a garage");
                    System.out.println("Car can only be created in a garage");
                    return null;
                }
            case PET_FEEDER:
                return new PetFeeder(name, lastIdCreated++, room);
            case BLINDS:
                if(room.getWindow() != null){
                    return new Blinds(name, lastIdCreated++, room);
                }
                else{
                    logger.error("Blinds can only be created in a room with a window");
                    System.out.println("Blinds can only be created in a room with a window");
                    return null;
                }


            // Handle other device types as needed
            default:
                logger.error("Unsupported device type: " + deviceType);
                System.out.println("Unsupported device type: " + deviceType);
                return null;
        }
    }


    /**
     * Creates a device of the given type, with the given name, in the a random room.
     * @param deviceType type of device
     * @return the created device
     */
    public AbstractDevice generateDeviceOfTypeInRandomRoom(DeviceType deviceType){
        Room room = house.getRandomRoom();
        String nameOfDevice = room.getName() +  "_" + deviceType.toString();
        return createDevice(deviceType, nameOfDevice, room);
    }

    /**
     * Creates a device of the given type, with the given name, but does not add it to a room.
     * @param deviceType type of device
     * @return the created device
     */
    public AbstractDevice createDeviceDontAddToRoom(DeviceType deviceType, String name) {
        return createDeviceWithTypeAndName(deviceType, name);
    }



    /**
     * a switch statement that creates a device of the given type, with the given name, but does not add it to a room.
     * Not to be used outside of this class.
     * @param deviceType type of device
     * @param name name of device
     * @return the created device
     */
    private AbstractDevice createDeviceWithTypeAndName(DeviceType deviceType, String name) {

        Room room = null;
        switch (deviceType) {
            case LIGHT:
                return new Lights(name, lastIdCreated++, room);
            case HEATING_PANEL:
                return new HeatingPanel(name, lastIdCreated++, room);
            case DISHWASHER:
                return new Dishwasher(name, lastIdCreated++, room);
            case WASHING_MACHINE:
                return new WashingMachine(name, lastIdCreated++, room);
//            case TIME_SENSOR:
//                return new TimeSensor(name, lastIdCreated++,room);
//            case SENSOR:
//                return new (name, lastIdCreated++);
            // Handle other device types as needed
            default:
                logger.error("Unsupported device type: " + deviceType);
                System.out.println("Unsupported device type: " + deviceType);
                return null;
        }
    }

    public int getIdCounter() {
        return lastIdCreated;
    }

    /**
     * Creates a device of the given type, in the given room.
     * @param deviceType type of device
     * @param room room to put device in
     */
    public void createDeviceOfTypeInRoom(DeviceType deviceType, Room room) {
        String nameOfDevice = room.getName() + "_" + deviceType.toString();
        AbstractDevice device = createDevice(deviceType, nameOfDevice, room);
    }
}
