package net.ddns.achouse.sensordatacollector;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity /*implements SettingsFragment.OnItemSelectedListener*/{

    public MqttHandler mqttHandler;
    private  TextView tvStatus;
    private String serverUri, port, user, password, topicTemperature, topicHumidity, topicPressure;
    public ArrayList<Data> dataList;

    private SettingsFragment settingsFragment;
    private TableFragment tableFragment;
    private GraphFragment graphFragment;

    Fragment selectedFragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            settingsFragment = SettingsFragment.newInstance();
            tableFragment = TableFragment.newInstance();
            //graphFragment = GraphFragment.newInstance();
        }
        tvStatus = (TextView) findViewById(R.id.connectionStatus);

        dataList = new ArrayList<Data>();

        //Navigation
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        //mTextMessage.setText(R.string.title_home);
                        //selectedFragment = SettingsFragment.newInstance();
                        displaySettingsFragment();
                        break;
                    case R.id.navigation_table:
                        //mTextMessage.setText(R.string.title_dashboard);
                        //selectedFragment = TableFragment.newInstance();
                        displayTableFragment();
                        break;
                    case R.id.navigation_graph:
                        //mTextMessage.setText(R.string.title_notifications);
                }
                //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //transaction.replace(R.id.frame_layout, selectedFragment);
                //transaction.commit();
                return true;
            }
        };
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        //Manually displaying the first fragment - one time only
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SettingsFragment.newInstance());
        transaction.commit();*/
        displaySettingsFragment();
        navigation.getMenu().getItem(2).setChecked(true);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("status", tvStatus.getText().toString());
        savedInstanceState.putParcelableArrayList("dataList", dataList);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        tvStatus.setText(savedInstanceState.getString("status"));
        dataList = savedInstanceState.getParcelableArrayList("dataList");
    }

    public void displaySettingsFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (settingsFragment.isAdded()) { // if the fragment is already in container
            ft.show(settingsFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.frame_layout, settingsFragment, "Settings");
        }
        // Hide fragment B
        if (tableFragment.isAdded()) { ft.hide(tableFragment); }
        // Hide fragment C
        //if (graphFragment.isAdded()) { ft.hide(graphFragment); }
        // Commit changes
        ft.commit();
    }

    private void displayTableFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (tableFragment.isAdded()) { // if the fragment is already in container
            ft.show(tableFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.frame_layout, tableFragment, "Table");
        }
        // Hide fragment B
        if (settingsFragment.isAdded()) { ft.hide(settingsFragment); }
        // Hide fragment C
        //if (graphFragment.isAdded()) { ft.hide(graphFragment); }
        // Commit changes
        ft.commit();
    }
/*
    private void displayGraphFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (graphFragment.isAdded()) { // if the fragment is already in container
            ft.show(graphFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.frame_layout, graphFragment, "Graph");
        }
        // Hide fragment B
        if (tableFragment.isAdded()) { ft.hide(tableFragment); }
        // Hide fragment C
        if (settingsFragment.isAdded()) { ft.hide(settingsFragment); }
        // Commit changes
        ft.commit();
    }*/

    public void launchMqtt(){
        mqttHandler = new MqttHandler(getApplicationContext(), "tcp://"+serverUri + ":" + port, user, password, topicTemperature, topicHumidity, topicPressure);
        mqttHandler.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Debug","Connecting Complete!");
                tvStatus.setTextColor(Color.parseColor("#00cc00"));
                tvStatus.setText("Connected");
                mqttHandler.connected = true;
            }

            @Override
            public void connectionLost(Throwable throwable) {
                Log.w("Debug","Connection Lost!!");
                tvStatus.setTextColor(Color.parseColor("#ff0000"));
                tvStatus.setText("Not Connected");
                mqttHandler.connected = false;
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
                Date timestamp = new Date();
                DateFormat timestampFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", new Locale("pl", "PL"));
                String strTimestamp = timestampFormat.format(timestamp);
                if(topic.equals(topicTemperature)) {
                    Data data = new Data("Temperature", strTimestamp, mqttMessage.toString());
                    dataList.add(data);
                } else if(topic.equals(topicHumidity)) {
                    Data data = new Data("Humidity", strTimestamp, mqttMessage.toString());
                    dataList.add(data);
                } else if(topicPressure.equals(topicPressure)) {
                    Data data = new Data("Pressure", strTimestamp, mqttMessage.toString());
                    dataList.add(data);
                }
                Log.w("Debug",Integer.toString(dataList.size()));
                // Display to terminal arraylist
                /*for(int i = 0; i < dataList.size(); i++) {
                    Log.w("dataList",dataList.get(i).getValue());
                }*/
                tableFragment.updateTable();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
    public void setSettingsInActivity(String serverUri, String port, String user, String password, String topicTemperature, String topicHumidity, String topicPressure) {
        this.serverUri = serverUri;
        this.port = port;
        this.user = user;
        this.password = password;
        this.topicTemperature = topicTemperature;
        this.topicHumidity = topicHumidity;
        this.topicPressure = topicPressure;
        Log.w("Debug","Settings received from Settings Fragment and has been set!");
    }
}
