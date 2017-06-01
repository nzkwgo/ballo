package edu.uw.nzkwgo.ballo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity implements Ballo.Events {
    private TextView age;
    private TextView bounce;
    private TextView distance;
    private TextView fed;
    private TextView lowestHappiness;
    private TextView highestStrength;
    private TextView lowestHunger;
    private TextView lowestStrength;

    private Ballo ballo;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ballo.destroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Get Ui Views
        age = (TextView) findViewById(R.id.stats_age);
        bounce = (TextView) findViewById(R.id.stats_bounce);
        distance = (TextView) findViewById(R.id.stats_distance);
        fed = (TextView) findViewById(R.id.stats_fed);
        lowestHappiness = (TextView) findViewById(R.id.stats_lowest_happiness);
        highestStrength = (TextView) findViewById(R.id.stats_highest_strength);
        lowestHunger = (TextView) findViewById(R.id.stats_lowest_health);
        lowestStrength = (TextView) findViewById(R.id.stats_lowest_strength);

        ballo = new Ballo();
        ballo.setEventHandler(this);
        age.setText("" + ballo.getAge());
        bounce.setText("" + ballo.getTimesBounced());
        distance.setText("" + ballo.getDistanceWalked());
        fed.setText("" + ballo.getTimesFed());
        lowestHappiness.setText("" + ballo.getLowestHappiness());
        highestStrength.setText("" + ballo.getHighestStrength());
        lowestHunger.setText("" + ballo.getLowestHunger());
        lowestStrength.setText("" + ballo.getLowestStrength());
    }

    @Override
    public void onUpdate() {
        Ballo.saveBallo(this, ballo);
    }
}
