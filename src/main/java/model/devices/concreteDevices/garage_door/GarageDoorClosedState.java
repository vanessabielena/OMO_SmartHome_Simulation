//package model.devices.concreteDevices.garage_door;
//
//// GarageDoorClosedState.java
//public class GarageDoorClosedState implements GarageDoorState {
//    @Override
//    public void open(GarageDoor garageDoor) {
//        garageDoor.setState(new GarageDoorOpenState());
//        System.out.println(garageDoor.getName() + " is now OPEN.");
//    }
//
//    @Override
//    public void close(GarageDoor garageDoor) {
//        System.out.println(garageDoor.getName() + " is already CLOSED.");
//    }
//
//    @Override
//    public void performAction(GarageDoor garageDoor) {
//        // Additional logic for when the garage door is closed
//        System.out.println(garageDoor.getName() + " is currently closed.");
//    }
//}
