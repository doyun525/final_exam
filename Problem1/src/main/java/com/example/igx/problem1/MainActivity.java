package com.example.igx.problem1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.model.people.Person;

import java.util.List;

import static android.hardware.Sensor.TYPE_ALL;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener /* implements Something1, Something2 */ {
    private static final int SEND_TYPE_LOCATION = 0;
    private static final int SEND_TYPE_SENSOR = 1;

    Location location;

    GoogleApiClient googleApiClient;

    SensorManager sensorManager;
    Sensor Gyroscope_sensor; //자이로
    Sensor Accelerometer_sensor; //가속도
    Sensor Temperature_sensor; //온도
    Sensor Proximity_sensor; //근접

    String data;

    int sendType;

    SenseorValue senseorValue;

    public class SenseorValue{
        public float GyroscopeX, GyroscopeY, GyroscopeZ;
        public float AccelerometerX, AccelerometerY, AccelerometerZ;
        public float Temperature;
        public float Proximity;
    }


    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                senseorValue.GyroscopeX = event.values[0];
                senseorValue.GyroscopeY = event.values[1];
                senseorValue.GyroscopeZ = event.values[2];

                Log.d("test" , "자이로 센서 " + event.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, Gyroscope_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                senseorValue.AccelerometerX = event.values[0];
                senseorValue.AccelerometerY = event.values[1];
                senseorValue.AccelerometerZ = event.values[2];

                Log.d("test" , "가속도 센서 " + event.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, Accelerometer_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                senseorValue.Temperature = event.values[0];

                Log.d("aa" , "온도 센서 " + event.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, Temperature_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                senseorValue.Proximity = event.values[0];

                Log.d("aa" , "근접 센서 " + event.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, Proximity_sensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    protected void onPause() {
        super.onPause();
        googleApiClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            recreate();
        }

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Gyroscope_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Accelerometer_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Temperature_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        Proximity_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        senseorValue = new SenseorValue();

        Button btn_getLocation = (Button) findViewById(R.id.btn_getLocation);
        Button btn_getSensors = (Button) findViewById(R.id.btn_getSensors);
        Button btn_sendMessage = (Button) findViewById(R.id.btn_sendMessage);

        final TextView text_selectedData = (TextView) findViewById(R.id.text_selectedData);
        final TextView text_selectedType = (TextView) findViewById(R.id.text_selectedType);
        final EditText edit_phoneNumber = (EditText) findViewById(R.id.edit_phoneNumber);

        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                data = "Latitude : "+ location.getLatitude()+ "\n"
                        + "Longitude : " + location.getLongitude() + "\n"
                        + "Altitude : " + location.getAltitude() + "\n"
                        + "Accuracy : " + location.getAccuracy();
                text_selectedType.setText("Location");
                text_selectedData.setText(data);

                sendType = SEND_TYPE_LOCATION;
            }
        });



        btn_getSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_selectedType.setText("Sensor");
                data = "Gyroscope X:" + senseorValue.GyroscopeX + " Y:" + senseorValue.GyroscopeY+" Z:"+senseorValue.GyroscopeZ + "\n"
                        + "Accelerometer X:" + senseorValue.AccelerometerX + " Y:" + senseorValue.AccelerometerY+" Z:"+senseorValue.AccelerometerZ + "\n"
                        + "Temperature : " + senseorValue.Temperature + "\n"
                        + "Proximity : " + senseorValue.Proximity;
                text_selectedData.setText(data);
                sendType = SEND_TYPE_SENSOR;
            }
        });

        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonNumber = edit_phoneNumber.getText().toString();
                if(phonNumber==null){
                    Toast.makeText(MainActivity.this,"전화번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
