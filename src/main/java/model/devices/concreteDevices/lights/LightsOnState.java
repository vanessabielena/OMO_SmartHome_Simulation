package model.devices.concreteDevices.lights;

// LightOnState.java
public class LightsOnState implements LightsState {
    @Override
    public boolean turnOn(Lights light) {
        System.out.println(light.getName() + " is already ON.");
        return false;

    }

    @Override
    public boolean turnOff(Lights light) {
        light.setState(new LightsOffState());
        System.out.println(light.getName() + " is now OFF.");
        return true;

    }

    @Override
    public void setBrightness(Lights light, int brightness) {
        light.setBrightness(brightness);
        System.out.println(light.getName() + " brightness set to " + brightness + "%.");
    }

    @Override
    public void performAction(Lights light) {
        System.out.println(light.getName() + " is emitting light.");
    }
}
