package model.devices.concreteDevices.lights;

public interface LightsState {
    boolean turnOn(Lights light);

    boolean turnOff(Lights light);

    void setBrightness(Lights light, int brightness);

    void performAction(Lights light);
}
