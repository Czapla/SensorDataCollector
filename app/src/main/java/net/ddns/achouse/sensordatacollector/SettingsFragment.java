package net.ddns.achouse.sensordatacollector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Klasa implementująca Fragment wyświetlający formularz z polami do wypełnienia
 * potrzebnymi do połączenia z brokerem.
 */
public class SettingsFragment extends Fragment {
    /**
     * Statyczna metoda tworząca nową instancję Fragmentu.
     * @return Zwraca obiekt klasy SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    /**
     * Pole edycyjne przechowujące URL Brokera.
     */
    private EditText etServerUrl;
    /**
     * Pole edycyjne przechowujące port, na którym nasłuchuje Broker.
     */
    private EditText etPort;
    /**
     * Pole edycyjne przechowujące nazwę użytkownika.
     */
    private EditText etUser;
    /**
     * Pole edycyjne przechowujące hasło użytkownika.
     */
    private EditText etPassword;
    /**
     * Pole edycyjne przechowujące temat(Temperatura).
     */
    private EditText etTopicTemperature;
    /**
     * Pole edycyjne przechowujące temat(Wilgotność).
     */
    private EditText etTopicHumidity;
    /**
     * Pole edycyjne przechowujące temat(Ciśnienie).
     */
    private EditText etTopicPressure;
    /**
     * Przycisk, który odpowiada za potwierdzenie wypełnienia formularza.
     */
    private Button btnConnect;

    /**
     * Metoda reprezentująca moment w cyklu życia Fragmentu, zaraz po jego utworzeniu.
     * @param savedInstanceState Obiekt typu Bundle, który reprezentuje stan Fragmentu.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Metoda reprezentująca moment w cyklu życia Fragmentu, zaraz po utworzeniu instancji widoku
     * w interfejsie użytkownika.
     * @param inflater Inflater, do którego zostanie dowiązany XML fragmentu.
     * @param container ViewGroup, który jest pojemnikiem na obiekty interfejsu.
     * @param savedInstanceState Obiekt typu Bundle, który reprezentuje stan Fragmentu.
     * @return zwraca inflater powiązany z XML fragmentu.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    /**
     * Metoda reprezentująca moment w cyklu życia Fragmentu, zaraz po utworzeniu instancji
     * intefejsu użytkownika. Tutaj przypisywane są elementy intefejsu do pól klasy. Jest tutaj
     * również implementowane zachowanie wywoływane naciśnięciem klawisza. W tym przypadku
     * zebrane dane są przenoszone do pól klasy MainActivity.
     * @param view Widok, który został utworzony w cyklu życia Fragmentu.
     * @param savedInstanceState Obiekt typu Bundle, który reprezentuje stan Fragmentu.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Settings
        etServerUrl = (EditText) view.findViewById(R.id.serverUrl);
        etPort = (EditText) view.findViewById(R.id.port);
        etUser = (EditText) view.findViewById(R.id.user);
        etPassword = (EditText) view.findViewById(R.id.password);
        etTopicTemperature = (EditText) view.findViewById(R.id.topicTemperature);
        etTopicHumidity = (EditText) view.findViewById(R.id.topicHumidity);
        etTopicPressure = (EditText) view.findViewById(R.id.topicPressure);
        btnConnect = (Button) view.findViewById(R.id.connect);

        // Define button listener
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).setSettingsInActivity(etServerUrl.getText().toString(), etPort.getText().toString(), etUser.getText().toString(),
                        etPassword.getText().toString(), etTopicTemperature.getText().toString(), etTopicHumidity.getText().toString(), etTopicPressure.getText().toString());
                if(((MainActivity)getActivity()).mqttHandler != null) {
                    Log.w("Debug", "Connected");
                } else {
                    ((MainActivity)getActivity()).launchMqtt();
                    Log.w("Debug", "Not connected");
                }
            }
        });
    }
}
