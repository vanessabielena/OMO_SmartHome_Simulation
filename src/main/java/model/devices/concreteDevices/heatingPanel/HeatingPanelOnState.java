package model.devices.concreteDevices.heatingPanel;

public class HeatingPanelOnState implements HeatingPanelState {
    @Override
    public boolean turnOn(HeatingPanel heatingPanel) {
        System.out.println(heatingPanel.getName() + " is already ON.");
        return false;
    }

    @Override
    public boolean turnOff(HeatingPanel heatingPanel) {
        heatingPanel.setState(new HeatingPanelOffState());
        System.out.println(heatingPanel.getName() + " is now OFF.");
        return true;
    }

    @Override
    public void increaseTemperature(HeatingPanel heatingPanel, float amount) {
        heatingPanel.setTemperature(heatingPanel.getTemperature() + amount);
        System.out.println(heatingPanel.getName() + " temperature increased by " + amount + "°C.");
    }

    @Override
    public void decreaseTemperature(HeatingPanel heatingPanel, float amount) {
        heatingPanel.setTemperature(heatingPanel.getTemperature() - amount);
        System.out.println(heatingPanel.getName() + " temperature decreased by " + amount + "°C.");
    }

    @Override
    public void performAction(HeatingPanel heatingPanel) {
        System.out.println(heatingPanel.getName() + " is adjusting the temperature.");
    }
}
