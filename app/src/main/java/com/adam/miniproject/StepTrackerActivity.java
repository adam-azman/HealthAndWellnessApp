package com.adam.miniproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StepTrackerActivity extends AppCompatActivity implements SensorEventListener {

    private TextView stepCountTextView, motivationTextView;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private boolean isSensorPresent;
    private int stepCount = 0;
    private int dailyStepGoal = 10000; // Default goal is 10,000 steps

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_tracker);

        stepCountTextView = findViewById(R.id.stepCountTextView);
        motivationTextView = findViewById(R.id.motivationTextView);
        Button setGoalButton = findViewById(R.id.setGoalButton);


        setGoalButton.setOnClickListener(v -> {
            dailyStepGoal = 8000;
            Toast.makeText(this, "Daily step goal set to " + dailyStepGoal, Toast.LENGTH_SHORT).show();
        });


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent = stepCounterSensor != null;
        }

        if (!isSensorPresent) {
            stepCountTextView.setText("Step Counter Sensor not available!");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            stepCount = (int) event.values[0];
            stepCountTextView.setText("Steps: " + stepCount);

            updateMotivationalMessage();
        }
    }

    private void updateMotivationalMessage() {
        if (stepCount >= dailyStepGoal) {
            motivationTextView.setText("Amazing! You reached your goal!");
        } else if (stepCount >= dailyStepGoal * 0.75) {
            motivationTextView.setText("Almost there! Keep going!");
        } else if (stepCount >= dailyStepGoal * 0.5) {
            motivationTextView.setText("Halfway there! You can do it!");
        } else {
            motivationTextView.setText("Keep moving! You're doing great!");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }
}
