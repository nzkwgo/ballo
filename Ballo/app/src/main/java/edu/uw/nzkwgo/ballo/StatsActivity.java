package edu.uw.nzkwgo.ballo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {
    private TextView age;
    private TextView bounce;
    private TextView distance;
    private TextView fed;
    private TextView highestHealth;
    private TextView highestStrength;
    private TextView lowestHealth;
    private TextView lowestStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Get Ui Views
        age = (TextView) findViewById(R.id.stats_age);
        bounce = (TextView) findViewById(R.id.stats_bounce);
        distance = (TextView) findViewById(R.id.stats_distance);
        fed = (TextView) findViewById(R.id.stats_fed);
        highestHealth = (TextView) findViewById(R.id.stats_highest_health);
        highestStrength = (TextView) findViewById(R.id.stats_highest_strength);
        lowestHealth = (TextView) findViewById(R.id.stats_lowest_health);
        lowestStrength = (TextView) findViewById(R.id.stats_lowest_strength);
    }
}
