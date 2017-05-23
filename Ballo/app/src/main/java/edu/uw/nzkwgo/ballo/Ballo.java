package edu.uw.nzkwgo.ballo;

import java.util.Date;

/**
 * Created by iguest on 5/22/17.
 */

public class Ballo {

    private String name;
    private int hunger;
    private int happiness;
    private int strength;
    private final Date birthdate;
    private double distanceWalked;
    private int timesFed;
    private int timesBounced;
    private int lowestHealth;
    private int lowestStrength;
    private int lowestHunger;
    private int highestStrength;

    public Ballo(String name) {
        this.name = name;
        this.hunger = 100;
        this.happiness = 100;
        this.strength = 100;
        this.birthdate = new Date();
        this.distanceWalked = 0;
        this.timesFed = 0;
        this.timesBounced = 0;
        this.lowestHealth = 100;
        this.lowestStrength = 100;
        this.lowestHunger = 100;
        this.lowestHealth = 100;
        this.highestStrength = 100;
    }

    public Ballo() {
        this("Ballo");
    }

    public void killBallo() {

    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
        if (this.hunger > 100) {
            this.hunger = 100;
        } else if (this.hunger <= 0) {
            killBallo();
        }
    }

    public Date getAge() {

        return birthdate;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public double getDistanceWalked() {
        return distanceWalked;
    }

    public void setDistanceWalked(double distanceWalked) {
        this.distanceWalked = distanceWalked;
    }

    public int getTimesFed() {
        return timesFed;
    }

    public void setTimesFed(int timesFed) {
        this.timesFed = timesFed;
    }

    public int getTimesBounced() {
        return timesBounced;
    }

    public void setTimesBounced(int timesBounced) {
        this.timesBounced = timesBounced;
    }

    public String getName() {
        return name;
    }
}
