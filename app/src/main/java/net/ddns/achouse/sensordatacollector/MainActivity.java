package net.ddns.achouse.sensordatacollector;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    private MqttHandler mqttHandler;

    //private TextView mTextMessage;
    private  TextView tvStatus;
    private EditText etServerUrl;
    private EditText etPort;
    private EditText etUser;
    private EditText etPassword;
    private EditText etTopicTemperature;
    private EditText etTopicHumidity;
    private EditText etTopicPressure;

    private Button btnConnect;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_table:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_graph:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        tvStatus = (TextView) findViewById(R.id.connectionStatus);

        //Settings
        etServerUrl = (EditText) findViewById(R.id.serverUrl);
        etPort = (EditText) findViewById(R.id.port);
        etUser = (EditText) findViewById(R.id.user);
        etPassword = (EditText) findViewById(R.id.password);
        etTopicTemperature = (EditText) findViewById(R.id.topicTemperature);
        etTopicHumidity = (EditText) findViewById(R.id.topicHumidity);
        etTopicPressure = (EditText) findViewById(R.id.topicPressure);
        btnConnect = (Button) findViewById(R.id.connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                launchMqtt();
            }
        });
        //Navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //launchMqtt();
    }

    private void launchMqtt(){
        mqttHandler = new MqttHandler(getApplicationContext(), etServerUrl.getText() + ":" + etPort.getText(), etUser.getText().toString(), etPassword.getText().toString(), etTopicTemperature.getText().toString(), etTopicHumidity.getText().toString(), etTopicPressure.getText().toString());
        mqttHandler.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Debug","Connecting Complete!");
                tvStatus.setTextColor(Color.parseColor("#00cc00"));
                tvStatus.setText("Connected");
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
                //mTextMessage.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}
