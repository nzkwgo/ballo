package edu.uw.nzkwgo.ballo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //startActivity(new Intent(this, LeaderboardActivity.class));
        Ballo ballo = new Ballo();

        ImageView balloView = (ImageView) findViewById(R.id.ballo);
        balloView.setImageResource(getResources().getIdentifier(ballo.getImgURL(), "drawable", getPackageName()));
        startActivity(new Intent(this, PlayActivity.class));
    }
}
