package edu.uw.nzkwgo.ballo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayActivity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = "Play";

    private DrawingSurfaceView view;

    // variables for shake detection
    private static final int SHAKE_THRESHOLD = 3; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;
    private SensorManager mSensorMgr;
    private Ballo ballo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ballo = Ballo.getBallo(this);

        view = (DrawingSurfaceView)findViewById(R.id.drawingView);

        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Listen for shakes

        Sensor accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;

                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    //Shook
                    bounceAnim();
                    ballo.bounce();
                    Ballo.saveBallo(this, ballo);
                }
            }
        }
    }

    public void bounceAnim() {
        view.ballo.setImgURL("excited_ballo");

        ObjectAnimator upAnim = ObjectAnimator.ofFloat(view.ballo, "Cy", 500);
        upAnim.setDuration(500);
        ObjectAnimator downAnim = ObjectAnimator.ofFloat(view.ballo, "Cy", view.getHeight() - 400);
        downAnim.setDuration(400);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(upAnim, downAnim);
        set.start();
        view.ballo.updateImg();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ignore
    }
}
