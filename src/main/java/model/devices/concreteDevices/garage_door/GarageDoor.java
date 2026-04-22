//package model.devices.concreteDevices.garage_door;
//
//import model.devices.AbstractDevice;
//import model.devices.concreteDevices.lights.LightsOffState;
//import model.devices.concreteDevices.lights.LightsOnState;
//import model.house.Room;
//
//import java.time.LocalTime;
//import java.util.Locale;
//
//public class GarageDoor extends AbstractDevice {
//    private boolean isOpen;
//    private GarageDoorState state;
//    private long lastTurnOnTimestamp;
//
//    public GarageDoor(String name, int id, Room room) {
//        super(name,id,room);
//        this.isOpen = false; // Default to closed
//        this.state = new GarageDoorClosedState();
//    }
//
//    public void setState(GarageDoorState state) {
//        this.state = state;
//    }
//
//    public boolean isOpen() {
////        return isOpen;
//        return state instanceof GarageDoorOpenState;
//    }
//
//    public void open() {
//        isOpen = true;
//        this.turnOn();
//        System.out.println(getName() + " is now OPEN.");
//    }
//
//    public void close() {
//        isOpen = false;
//        this.turnOff();
//        System.out.println(getName() + " is now CLOSED.");
//    }
//
//    @Override
//    public void turnOn(LocalTime time) {
//        if (state instanceof LightsOffState) {
//            lastTurnOnTimestamp = System.currentTimeMillis();
//        }
//        state.open(this);
//    }
//
//    @Override
//    public void turnOff(LocalTime time) {
//        if (state instanceof LightsOnState) {
//            //calculate duration in seconds
//            long durationInSeconds = (System.currentTimeMillis() - lastTurnOnTimestamp) / 1000;
//            //calculate power consumption based on duration
//            float powerConsumed = calculatePowerConsumption(durationInSeconds);
//            setPowerConsumption(getPowerConsumption() + powerConsumed);
//            //maybe should log something? idk
//        }
//        state.close(this);
//    }
//
////    @Override
////    public void performAction() {
////        System.out.println(getName() + " is moving.");
////    }
//
//
//
//    public float calculatePowerConsumption(long durationInSeconds) {
//        return durationInSeconds * 0.02f;
//    }
//
//}
