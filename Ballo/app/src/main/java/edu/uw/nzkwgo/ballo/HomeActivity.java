package edu.uw.nzkwgo.ballo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The home screen of the Ballo app/game. This screen shows a player their current Ballo's hunger,
 * happiness, and strength. The user may choose to exercise, feed, or play with the Ballo, or visit
 * the ballo's stats or leaderboard.
 */
public class HomeActivity extends AppCompatActivity implements Ballo.Events {
    private Ballo ballo;
    private TextView name;
    private ProgressBar hunger;
    private ProgressBar happiness;
    private TextView strength;
    private ImageView balloAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get UI views
        name = (TextView) findViewById(R.id.homeName);
        hunger = (ProgressBar) findViewById(R.id.hungerVal);
        happiness = (ProgressBar) findViewById(R.id.happinessVal);
        strength = (TextView) findViewById(R.id.strengthVal);
        balloAvatar = (ImageView) findViewById(R.id.ballo);

        // Set so you can't set the seekbar
//        hunger.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return true;
//            }
//        });
//        happiness.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return true;
//            }
//        });

        // Set button onclick actions
        findViewById(R.id.feedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Implement feed action
                ballo.feed();
                ProgressBar hungerBar = (ProgressBar)findViewById(R.id.hungerVal);
                hungerBar.setProgress(ballo.getHunger());
            }
        });
        findViewById(R.id.walkBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, WalkActivity.class));
            }
        });
        findViewById(R.id.playBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PlayActivity.class));
            }
        });

        // Don't re-instantiate if ballo already exists (can occur if the screen orientation
        // changes, the phone is put to sleep, etc).
        if (ballo == null) {
            ballo = Ballo.getBallo(this);
        }

        // Load ballo if one exists
        ballo.setEventHandler(this);
        onUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ballo.clearEventHandler();
        ballo.destroy();
        ballo = null;
    }

    // Updates the screen to reflect ballo's current state.
    @Override
    public void onUpdate() {
        if (ballo == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hunger.setProgress(ballo.getHunger());
                happiness.setProgress(ballo.getHappiness());
                strength.setText(String.valueOf(ballo.getStrength()));
                name.setText(ballo.getName());
                balloAvatar.setImageResource(getResources()
                        .getIdentifier(ballo.getImgURL(), "drawable", getPackageName()));
            }
        });
        Ballo.saveBallo(this, ballo);
    }
}
