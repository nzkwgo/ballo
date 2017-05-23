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
    private String deathStatus;

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
        this.deathStatus = "";
    }

    public Ballo() {
        this("Ballo");
    }

    public void feed() {
        if (hunger < 100) {
            setHunger(hunger + 10);
        } else {
            setHappiness(happiness - 5);
        }
        timesFed++;
    }

    //TODO: Make bounce return coordinates for animation
    public void bounce() {
        if (happiness < 100) {
            setHappiness(happiness + 2);
        } else {
            setStrength(strength - 1);
        }
        timesBounced++;
    }

    public void walk() {
        setStrength(strength + 10);
        setHunger(hunger - 5);
        int placeHolderDistance = 0; //TODO: Figure out distance update
        distanceWalked += placeHolderDistance;
    }

    //returns Ballo's age in days
    public int getAge() {
        Date currentDate = new Date();
        long diff = this.birthdate.getTime() - currentDate.getTime();
        return (int) diff / (24 * 60 * 60 * 1000);
    }

    //Sets hunger. Cannot exceed 100. Ballo dies when hunger drops below 0
    public void setHunger(int hunger) {
        this.hunger = hunger;
        if (this.hunger > 100) {
            this.hunger = 100;
        } else if (this.hunger <= 0) {
            this.hunger = 0;
            kill(this.name + " starved to death. Next time feed it more often!");
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
            kill(this.name + " died of a broken heart. Next time play with it more often!");
        }

        lowestHappiness = Math.min(this.happiness, lowestHappiness);
    }

    //Sets strength. Ballo dies when strength drops below 0
    public void setStrength(int strength) {
        this.strength = strength;

        if (this.strength <= 0) {
            this.strength = 0;
            kill(this.name + " got too weak and died. Next time walk it more often!");
        }

        highestStrength = Math.max(this.strength, highestStrength);
        lowestStrength = Math.min(this.strength, lowestStrength);
    }

    //Kills ballo, setting his death message to
    public void kill(String status) {
        this.deathStatus = status;
    }

    //returns whether or not ballo is dead
    public boolean isDead() {
        return !deathStatus.isEmpty();
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
