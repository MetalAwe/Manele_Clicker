package org.example;

public class GameLogic {
    // Game state
    public int counter = 0;
    public int autoClickerPrice = 10;
    public int autoClickersOwned = 0;
    public int factoryPrice = 25;
    public int factoriesOwned = 0;
    public double heat = 0.0;
    public final double MAX_HEAT = 100.0;
    public final double HEAT_THRESHOLD = 50.0; // Music starts above this

    // This will be called by a timer to make heat drop over time
    public void decayHeat() {
        if (heat > 0) {
            heat -= 2.0; // Drop heat every tick
        }
        if (heat < 0) heat = 0;
    }

    // The math for manual clicks
    public void doClick() {
        counter++;
        heat = Math.min(MAX_HEAT, heat + 10.0); // Each click adds 10 heat
    }

    // Logic for buying an auto-clicker
    public boolean buyAutoClicker() {
        if (counter >= autoClickerPrice) {
            counter -= autoClickerPrice;
            autoClickersOwned++;
            autoClickerPrice += 5;
            return true; // Success!
        }
        return false; // Not enough money
    }

    // Logic for buying a factory
    public boolean buyFactory() {
        if (counter >= factoryPrice) {
            counter -= factoryPrice;
            factoriesOwned++;
            factoryPrice += 15;
            return true;
        }
        return false;
    }

    // Calculate total Manele Per Second
    public int getMPS() {
        return autoClickersOwned + (factoriesOwned * 2);
    }

    // This is what the Timer will call every second
    public void processAutoClick() {
        counter += getMPS();
    }
}