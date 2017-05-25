package edu.uw.nzkwgo.ballo;

import com.google.gson.Gson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The home screen of the Ballo app/game. This screen shows a player their current Ballo's hunger,
 * happiness, and strength. The user may choose to exercise, feed, or play with the Ballo, or visit
 * the ballo's stats or leaderboard.
 */
public class HomeActivity extends AppCompatActivity {
    private static final String BALLO_PREFERENCE_STATE_ID = "ballo-state-pref";
    private static final String BALLO_OBJECT_ID = "ballo";
    private static final long DECAY_TIMER_MS = 1000 * 60 * 5; // 5 minutes

    private static Gson gson;

    private Ballo ballo;
    private TextView name;
    private SeekBar hunger;
    private SeekBar happiness;
    private TextView strength;
    private ImageView balloAvatar;

    private Timer decayTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get UI views
        name = (TextView) findViewById(R.id.homeName);
        hunger = (SeekBar) findViewById(R.id.hungerVal);
        happiness = (SeekBar) findViewById(R.id.happinessVal);
        strength = (TextView) findViewById(R.id.strengthVal);
        balloAvatar = (ImageView) findViewById(R.id.ballo);

        // Set so you can't set the seekbar
        hunger.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        happiness.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        // Set button onclick actions
        findViewById(R.id.feedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Implement feed action
                ballo.feed();
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
        if (ballo != null) {
            return;
        }

        // Load ballo if one exists
        ballo = new Ballo();
        final SharedPreferences pref =
                getSharedPreferences(BALLO_PREFERENCE_STATE_ID, MODE_PRIVATE);
        String balloJson = pref.getString(BALLO_OBJECT_ID, "");
        if (balloJson.length() != 0) {
            try {
                ballo = getGson().fromJson(balloJson, Ballo.class);
            } catch (Exception e) {
                Toast.makeText(this, "Couldn't load your ballo :(", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            System.out.println("Loaded ballo from storage");
        } else {
            System.out.println("A new ballo was created");
        }

        if (decayTimer != null) {
            decayTimer.cancel();
            decayTimer.purge();
            decayTimer = null;
        }

        // Set updater
        decayTimer = new Timer();
        TimerTask decayTask = new TimerTask() {
            @Override
            public void run() {
                // Decay
                ballo.decay();

                // Update display
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDisplay();
                    }
                });

                // Store updated ballo in preferences
                pref.edit().putString(BALLO_OBJECT_ID, getGson().toJson(ballo)).apply();
            }
        };
        decayTimer.schedule(decayTask, 0, DECAY_TIMER_MS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (decayTimer != null) {
            decayTimer.cancel();
            decayTimer.purge();
        }
        ballo = null;
    }

    // Updates the screen to reflect ballo's current state.
    private void updateDisplay() {
        hunger.setProgress(ballo.getHunger());
        happiness.setProgress(ballo.getHappiness());
        strength.setText(String.valueOf(ballo.getStrength()));
        name.setText(ballo.getName());
        balloAvatar.setImageResource(
                getResources().getIdentifier(ballo.getImgURL(), "drawable", getPackageName()));
    }

    private static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }
}
