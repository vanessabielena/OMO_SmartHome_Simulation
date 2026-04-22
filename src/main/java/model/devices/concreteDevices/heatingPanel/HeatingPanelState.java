package model.devices.concreteDevices.heatingPanel;

public interface HeatingPanelState {
    boolean turnOn(HeatingPanel heatingPanel);

    boolean turnOff(HeatingPanel heatingPanel);

    void increaseTemperature(HeatingPanel heatingPanel, float amount);

    void decreaseTemperature(HeatingPanel heatingPanel, float amount);

    void performAction(HeatingPanel heatingPanel);
}
