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

/**
 * Klasa reprezentująca główną aktywność(jedyną) aplikacji. Odpowiedzialna za generowanie,
 * wyświetlenie interfejsu użytkownika oraz jego logike.
 */
public class MainActivity extends AppCompatActivity /*implements SettingsFragment.OnItemSelectedListener*/{
    /**
     * Handler do obsługi połączenia z brokerem oraz odbieraniem danych.
     */
    public MqttHandler mqttHandler;
    /**
     * TextView wyświetlający tekst z informacją o statusie połączenia(Connected | Not Connected)
     */
    private  TextView tvStatus;
    /**
     * Łańcuchy znakowe posiadające odpowiednio informacje o: URL brokera, porcie, nazwie użytkownika,
     * haśle użytkownika, subskrybowanych tematach.
     */
    private String serverUri, port, user, password, topicTemperature, topicHumidity, topicPressure;
    /**
     * Lista obiektów klasy Data.
     */
    public ArrayList<Data> dataList;
    /**
     * Obiekt reprezentujący fragment z danymi potrzebnymi do połączenia z brokerem.
     */
    private SettingsFragment settingsFragment;
    /**
     * Obiekt reprezentujący fragment z odebranymi danymi w postaci tabelki.
     */
    private TableFragment tableFragment;
    /**
     * Obiekt reprezentujący fragment z odebranymi danymi w postaci wykresów.
     */
    private GraphFragment graphFragment;
    /**
     * Listener określający działania wykonywane w odpowiedzi na zdarzenie wyboru elementu z menu
     * nawigacyjnego.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    /**
     * Metoda reprezentująca moment w cyklu życia Aktywności, zaraz po jej utworzeniu.
     * Tworzona są tutaj instancje fragmentów(o ile nie zostały wcześniej utworzone). Łączone są
     * elementy intefejsu z obiektami odpowiednich klas. Implementowany jest tutaj Listener menu
     * nawigacyjnego, wyświetlający odpowiedni fragment.
     * @param savedInstanceState Obiekt typu Bundle, który reprezentuje stan Fragmentu.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            settingsFragment = SettingsFragment.newInstance();
            tableFragment = TableFragment.newInstance();
            graphFragment = GraphFragment.newInstance();
        }
        tvStatus = (TextView) findViewById(R.id.connectionStatus);

        dataList = new ArrayList<Data>();

        //Navigation
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        displaySettingsFragment();
                        break;
                    case R.id.navigation_table:
                        displayTableFragment();
                        break;
                    case R.id.navigation_graph:
                        displayGraphFragment();
                }
                return true;
            }
        };
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        displaySettingsFragment();
        navigation.getMenu().getItem(1).setChecked(true);
    }

    /**
     * Metoda wywoływana, gdy niszczona jest Aktywność. Zapisuje stan TextView wyświetlający
     * stan połączenia oraz zapisuje listę z odebranymi danymi.
     * @param savedInstanceState Obiekt typu Bundle, który reprezentuje stan Fragmentu.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("status", tvStatus.getText().toString());
        savedInstanceState.putParcelableArrayList("dataList", dataList);
    }

    /**
     * Metoda wywoływana, gdy aktywnośc jest przywracana. Odczytuje stan TextView wyświetlający
     * stan połączenia oraz zapisuje listę z odebranymi danymi.
     * @param savedInstanceState Obiekt typu Bundle, który reprezentuje stan Fragmentu.
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        tvStatus.setText(savedInstanceState.getString("status"));
        dataList = savedInstanceState.getParcelableArrayList("dataList");
    }

    /**
     * Wyświetla fragment z formularzem do wypełnenia(ustawienia połączenia) oraz ukrywa pozostałe
     * dwa fragmentu interfejsu(tabela, wykres).
     */
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
        if (graphFragment.isAdded()) { ft.hide(graphFragment); }
        // Commit changes
        ft.commit();
    }

    /**
     * Wyświetla fragment z danymi w postaci tabelki oraz ukrywa pozostałe
     * dwa fragmentu interfejsu(formularz, wykres).
     */
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
        if (graphFragment.isAdded()) { ft.hide(graphFragment); }
        // Commit changes
        ft.commit();
    }

    /**
     * Wyświetla fragment z danymi w postaci wykresów oraz ukrywa pozostałe
     * dwa fragmentu interfejsu(tabela, formularz).
     */
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
    }

    /**
     * Metoda łączy aplikację z brokerem MQTT za pomocą mqttHandler'a oraz określa callback'i na
     * wydarzenia związane z połączeniem do brokera oraz odebraniem wiadomości na subskrybowane
     * tematy.
     */
    public void launchMqtt(){
        mqttHandler = new MqttHandler(getApplicationContext(), "tcp://"+serverUri + ":" + port, user, password, topicTemperature, topicHumidity, topicPressure);
        mqttHandler.setCallback(new MqttCallbackExtended() {
            /**
             * Po połączeniu z brokerem, zmienia TextView ze statusem połączenia oraz ustawia
             * zmienną boolean connected Handlera na true.
             * @param reconnect Zmienna logiczna informująca, czy połączenie jest wykonywane po raz
             *                  pierwszy, czy też jest to ponowna próba połączenia z brokerem.
             * @param ServerURI Adres URI serwera, z którym połączenie zostało nawiązane.
             */
            @Override
            public void connectComplete(boolean reconnect, String ServerURI) {
                /*if (reconnect) {
                    Log.w("Debug","Connecting Complete!");
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                } else {
                    Log.w("Debug","Connecting Complete!");
                }*/
                tvStatus.setTextColor(Color.parseColor("#00cc00"));
                tvStatus.setText("Connected");
                mqttHandler.connected = true;
            }

            /**
             * Callback wywoływany, gdy połączenie z brokerem zostanie utracone. Zmienia TextView
             * ze statusem połączenia oraz ustawia zmienną boolean connected Handlera na false.
             * @param throwable Obiekt klasy Throwable, który może być wyjątkiem.
             */
            @Override
            public void connectionLost(Throwable throwable) {
                Log.w("Debug","Connection Lost!!");
                tvStatus.setTextColor(Color.parseColor("#ff0000"));
                tvStatus.setText("Not Connected");
                mqttHandler.connected = false;
            }

            /**
             * Callback wywoływany, gdy zostanie odebrana wiadomość z brokera, w związku z
             * subskrybowanym tematem. Dodaje do listy z odebranymi danymi obiekt klasy Data, oraz
             * dodaje punkt na odpowiednim wykresie i aktualizuje tabelę.
             * @param topic Subskrybowany temat, w ramach którego została odebrana wiadomość.
             * @param mqttMessage Odebrana wiadomość, w subskrybowanego tematu.
             * @throws Exception
             */
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                Log.w("Debug",mqttMessage.toString());
                Date timestamp = new Date();
                DateFormat timestampFormatTable = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", new Locale("pl", "PL"));
                String strTimestampTable = timestampFormatTable.format(timestamp);
                if(topic.equals(topicTemperature)) {
                    Data data = new Data("Temperature", strTimestampTable, mqttMessage.toString());
                    dataList.add(data);
                    graphFragment.addDataPoint(Float.valueOf(mqttMessage.toString()), "Temperature");
                } else if(topic.equals(topicHumidity)) {
                    Data data = new Data("Humidity", strTimestampTable, mqttMessage.toString());
                    dataList.add(data);
                    graphFragment.addDataPoint(Float.valueOf(mqttMessage.toString()), "Humidity");
                } else if(topicPressure.equals(topicPressure)) {
                    Data data = new Data("Pressure", strTimestampTable, mqttMessage.toString());
                    dataList.add(data);
                    graphFragment.addDataPoint(Float.valueOf(mqttMessage.toString()), "Pressure");
                }
                tableFragment.updateTable();
            }
            /**
             * Callback wywoływany, gdy odbierzemy od brokera potwierdzenie dostarczonej wiadomości.
             * Nieużywana w ramach tej aplikacji.
             * @param iMqttDeliveryToken Obiekt reprezentujący token "przyklejany" do każdej
             *                           wiadomości.
             */
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    /**
     * Metoda ustawia pola klasy na wartości z przekazanych argumentów.
     *
     * @param serverUri ID klienta widoczny dla brokera(serwera MQTT)
     * @param user Nazwa użytkownika, która używana jest przy autoryzacji połączenia do brokera.
     * @param password Hasło użytkonika, które używane jest przy autoryzacji połączenia do brokera.
     * @param topicTemperature Temat, w ramach którego są publikowane wiadomości z czujnika wilgotności, a w ramach którego
     *                         aplikacja będzie subskrybowała i odbierała pomiary.
     * @param topicHumidity Temat, w ramach którego są publikowane wiadomości z czujnika ciśnienia, a w ramach którego
     *                      aplikacja będzie subskrybowała i odbierała pomiary.
     * @param topicPressure Temat, w ramach którego są publikowane wiadomości z czujnika ciśnienia, a w ramach którego
     *                      aplikacja będzie subskrybowała i odbierała pomiary.
     */
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
