package model.devices.concreteDevices.blinds;

public class BlindsStateClosed implements BlindsState{
    @Override
    public boolean open(Blinds blinds) {
        System.out.println(blinds.getName() + "are now open.");
        blinds.setState(new BlindsStateOpen());
        return true;

    }

    @Override
    public boolean close(Blinds blinds) {
        System.out.println(blinds.getName() + "are already closed.");
        return false;

    }
}
