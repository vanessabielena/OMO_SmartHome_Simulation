package model.devices.concreteDevices.blinds;

public class BlindsStateOpen implements BlindsState {
    @Override
    public boolean open(Blinds blinds) {
        System.out.println(blinds.getName() + "are already open.");
        return false;
    }

    @Override
    public boolean close(Blinds blinds) {
        System.out.println(blinds.getName() + "are now closed.");
        return true;
    }
}
