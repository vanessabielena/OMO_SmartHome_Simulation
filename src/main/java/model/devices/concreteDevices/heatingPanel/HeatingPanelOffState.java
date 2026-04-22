package model.devices.concreteDevices.heatingPanel;

public class HeatingPanelOffState implements HeatingPanelState {
    @Override
    public boolean turnOn(HeatingPanel heatingPanel) {
        heatingPanel.setState(new HeatingPanelOnState());
        System.out.println(heatingPanel.getName() + " is now ON.");
        return true;
    }

    @Override
    public boolean turnOff(HeatingPanel heatingPanel) {
        System.out.println(heatingPanel.getName() + " is already OFF.");
        return false;
    }

    @Override
    public void increaseTemperature(HeatingPanel heatingPanel, float amount) {
        System.out.println(heatingPanel.getName() + " is off. Turn it on before adjusting the temperature.");
    }

    @Override
    public void decreaseTemperature(HeatingPanel heatingPanel, float amount) {
        System.out.println(heatingPanel.getName() + " is off. Turn it on before adjusting the temperature.");
    }

    @Override
    public void performAction(HeatingPanel heatingPanel) {
        System.out.println(heatingPanel.getName() + " is currently off. No temperature adjustment.");
    }
}
