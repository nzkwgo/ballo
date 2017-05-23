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
    private int lowestHappiness;
    private int lowestStrength;
    private int lowestHunger;
    private int highestStrength;
    private boolean isDead;

    public Ballo(String name) {
        this.name = name;
        this.hunger = 100;
        this.happiness = 100;
        this.strength = 100;
        this.birthdate = new Date();
        this.distanceWalked = 0;
        this.timesFed = 0;
        this.timesBounced = 0;
        this.lowestHappiness = 100;
        this.lowestStrength = 100;
        this.lowestHunger = 100;
        this.highestStrength = 100;
        this.isDead = false;
    }

    public Ballo() {
        this("Ballo");
    }

    public void killBallo() {
        this.isDead = true;
    }

    public void feed() {

    }

    public void bounce() {

    }

    public void walk() {

    }

    public Date getAge() {

        return birthdate;
    }

    //Sets hunger. Cannot exceed 100. Ballo dies when hunger drops below 0
    public void setHunger(int hunger) {
        this.hunger = hunger;
        if (this.hunger > 100) {
            this.hunger = 100;
        } else if (this.hunger <= 0) {
            this.hunger = 0;
            killBallo();
        }

        lowestHunger = Math.min(this.hunger, lowestHunger);
    }

    //Sets happiness. Cannot exceed 100. Ballo dies when happiness drops below 0
    public void setHappiness(int happiness) {
        this.happiness = happiness;

        if (this.happiness > 100) {
            this.happiness = 100;
        } else if (this.happiness <= 0) {
            this.happiness = 0;
            killBallo();
        }

        lowestHappiness = Math.min(this.happiness, lowestHappiness);
    }

    //Sets strength. Ballo dies when strength drops below 0
    public void setStrength(int strength) {
        this.strength = strength;

        if (this.strength <= 0) {
            this.strength = 0;
            killBallo();
        }

        highestStrength = Math.max(this.strength, highestStrength);
        lowestStrength = Math.min(this.strength, lowestStrength);
    }

    public int getHunger() {
        return hunger;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getStrength() {
        return strength;
    }

    public double getDistanceWalked() {
        return distanceWalked;
    }

    public int getTimesFed() {
        return timesFed;
    }

    public int getTimesBounced() {
        return timesBounced;
    }

    public String getName() {
        return name;
    }
}
