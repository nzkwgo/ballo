package edu.uw.nzkwgo.ballo;

import android.support.v7.app.AppCompatActivity;

import java.util.Date;

/**
 * Created by Noah on 5/22/17.
 */

public class Ballo {

    private String name;
    private double hunger;
    private double happiness;
    private double strength;
    private final Date birthdate;
    private double distanceWalked;
    private int timesFed;
    private int timesBounced;
    private double lowestHappiness;
    private double lowestStrength;
    private double lowestHunger;
    private double highestStrength;
    private String deathStatus;
    private String imgURL;

    //Bouncing Animation Variables
    public float cy;

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
        this.imgURL = "basic_ballo";
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
        updateImg();
    }

    //Changes Ballo's stats based on a quarter mile's worth of walking.
    //Should be called after walking a quarter mile
    public void walk() {
        setStrength(strength + 10);
        setHunger(hunger - 5);
        distanceWalked += 0.25;
    }

    //Depreciates the stats while Ballo is alive
    //Should be called every 15 minutes
    public void depreciateStats() {
        if (!this.isDead()) {
            setHappiness(happiness - 1);
            setHunger(hunger - 0.5);
            setStrength(strength - 0.25);
        }
    }

    //returns whether or not Ballo is dead
    public boolean isDead() {
        return !deathStatus.isEmpty();
    }

    public int getHunger() {
        return (int) hunger;
    }

    public int getHappiness() {
        return (int) happiness;
    }

    public int getStrength() {
        return (int) strength;
    }

    public String getName() {
        return name;
    }

    //To get the R.drawable version of the url, call getResources().getIdentifier(ballo.getImgURL() , "drawable", getPackageName());
    public String getImgURL() {
        return imgURL;
    }

    //Returns the sprite to be used while Ballo is on a walk. See comment on getImgURL
    public String getExerciseURL() {
        if (this.strength >= 50) {
            return "healthy_exercise_ballo";
        } else {
            return "unhealthy_exercise_ballo";
        }
    }

    //Stat getter methods
        public int getTimesFed() {
            return timesFed;
        }

        public int getTimesBounced() {
            return timesBounced;
        }

        public int getLowestHappiness() {
            return (int) lowestHappiness;
        }

        public int getLowestStrength() {
            return (int) lowestStrength;
        }

        public int getLowestHunger() {
            return (int) lowestHunger;
        }

        public int getHighestStrength() {
            return (int) highestStrength;
        }

        public double getDistanceWalked() {
            return Math.round(distanceWalked * 100) / 100.0;
        }

        //returns Ballo's age in days
        public int getAge() {
            Date currentDate = new Date();
            long diff = this.birthdate.getTime() - currentDate.getTime();
            return (int) diff / (24 * 60 * 60 * 1000);
        }

    //Getter and Setter methods for ObjectAnimator

        public float getCy() {
            return cy;
        }

        public void setCy(float cy) {
            this.cy = cy;
        }

    //Sets hunger. Cannot exceed 100. Ballo dies when hunger drops below 0
    private void setHunger(double hunger) {
        this.hunger = hunger;
        if (this.hunger > 100) {
            this.hunger = 100;
        } else if (this.hunger <= 0) {
            this.hunger = 0;
            kill(this.name + " starved to death. \nTip: Feed your Ballo so it doesn't starve!");
        } else if (this.hunger < 50) {
            updateImg();
        }

        lowestHunger = Math.min(this.hunger, lowestHunger);
    }

    //Sets happiness. Cannot exceed 100. Ballo dies when happiness drops below 0
    private void setHappiness(double happiness) {
        this.happiness = happiness;

        if (this.happiness > 100) {
            this.happiness = 100;
        } else if (this.happiness <= 0) {
            this.happiness = 0;
            kill(this.name + " died of a broken heart. \nTip: Play with your Ballo to raise its happiness!");
        } else if (this.happiness < 50) {
            updateImg();
        }

        lowestHappiness = Math.min(this.happiness, lowestHappiness);
    }

    //Sets strength. Ballo dies when strength drops below 0
    private void setStrength(double strength) {
        this.strength = strength;

        if (this.strength <= 0) {
            this.strength = 0;
            kill(this.name + " got too weak and died. Next time walk it more often!");
        } else if (this.strength < 50) {
            updateImg();
        }

        highestStrength = Math.max(this.strength, highestStrength);
        lowestStrength = Math.min(this.strength, lowestStrength);
    }

    //Kills ballo, setting his death message to the passed string
    private void kill(String status) {
        this.deathStatus = status;
        imgURL = "dead_ballo";
    }

    //Updates Ballo's sprite to reflect his lowest stat under 50
    private void updateImg() {
        if (hunger > 50 && happiness > 50 && strength > 50) {
            imgURL = "basic_ballo";
        } else if (hunger < 50 && hunger <= happiness && hunger <= strength) {
            imgURL = "hungry_ballo";
        } else if (happiness < 50 && happiness <= hunger && happiness <= strength) {
            imgURL = "sad_ballo";
        } else if (strength < 50 && strength <= hunger && strength <= happiness) {
            imgURL = "unhealthy_ballo";
        }
    }
}
