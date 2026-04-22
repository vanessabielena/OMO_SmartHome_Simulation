package model.devices.concreteDevices.lights;

// LightOffState.java
public class LightsOffState implements LightsState {
    @Override
    public boolean turnOn(Lights light) {
        light.setState(new LightsOnState());
        System.out.println(light.getName() + " is now ON.");
        return true;

    }

    @Override
    public boolean turnOff(Lights light) {
        System.out.println(light.getName() + " is already OFF.");
        return false;
    }

    @Override
    public void setBrightness(Lights light, int brightness) {
        System.out.println(light.getName() + " is off. Set brightness after turning it on.");
    }

    @Override
    public void performAction(Lights light) {
        // Additional logic for when the light is off
        System.out.println(light.getName() + " is currently off. No light emitted.");
    }
}
