//package model.devices.concreteDevices.garage_door;
//
//// GarageDoorOpenState.java
//public class GarageDoorOpenState implements GarageDoorState {
//    @Override
//    public void open(GarageDoor garageDoor) {
//        System.out.println(garageDoor.getName() + " is already OPEN.");
//    }
//
//    @Override
//    public void close(GarageDoor garageDoor) {
//        garageDoor.setState(new GarageDoorClosedState());
//        System.out.println(garageDoor.getName() + " is now CLOSED.");
//    }
//
//    @Override
//    public void performAction(GarageDoor garageDoor) {
//        System.out.println(garageDoor.getName() + " is currently open.");
//    }
//}
