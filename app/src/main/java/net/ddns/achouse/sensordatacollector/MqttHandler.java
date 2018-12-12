package net.ddns.achouse.sensordatacollector;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Klasa typu Handler dla MQTT Client. Zdefiniowane tutaj metody służą do obsługi połączenia z MQTT oraz jego ustawień,
 * zdefiniowania zachowań klienta MQTT.
 */
public class MqttHandler {

    /**
     * Instancja obiektu klasy mqttAndroidClient z biblioteki Paho Android
     */
    public MqttAndroidClient mqttAndroidClient;
    /**
     * ID klienta widoczny dla brokera(serwera MQTT)
     */
    String clientId = "SensorDataCollector";
    /**
     * URI brokera. Uniform Resource Identifier (z ang. Ujednolicony Identyfikator Zasobów)
     * definiujący nie tylko lokalizację zasobów, ale również sposób komunikacji. W przypadku
     * MQTT jest to protokół TCP
     */
    String serverUrl = "";
    /**
     * Nazwa użytkownika, która używana jest przy autoryzacji połączenia do brokera.
     */
    String username = "";
    /**
     * Hasło użytkownika, które używane jest przy autoryzacji połączenia do brokera.
     */
    String password = "";
    /**
     * Temat, w ramach którego są publikowane wiadomości z czujnika temperatury, a w ramach którego
     * aplikacja będzie subskrybowała i odbierała pomiary.
     */
    String topicTemperature = "";
    /**
     * Temat, w ramach którego są publikowane wiadomości z czujnika wilgotności, a w ramach którego
     * aplikacja będzie subskrybowała i odbierała pomiary.
     */
    String topicHumidity = "";
    /**
     * Temat, w ramach którego są publikowane wiadomości z czujnika ciśnienia, a w ramach którego
     * aplikacja będzie subskrybowała i odbierała pomiary.
     */
    String topicPressure = "";
    /**
     * Zmienna logiczna wskazująca czy aplikacja jest połączona z brokerem.
     */
    boolean connected = false;

    /**
     * Konstruktor klasy MqttHandler, w którym przypisywane są przekazane argumenty do pól klasy
     * oraz tworzony jest obiekt klasy MqttAndroidClient wraz z definiowanym MqttCallbackExtended.
     *
     * @param context Jest to kontekst definujący stan aplikacji, obiektu. Dzięki niemu nowo utworzone
     *                obiekty wiedzą co się aktualnie dzieje w aplikacji.
     * @param serverUrl ID klienta widoczny dla brokera(serwera MQTT)
     * @param username Nazwa użytkownika, która używana jest przy autoryzacji połączenia do brokera.
     * @param password Hasło użytkownika, które używane jest przy autoryzacji połączenia do brokera.
     * @param topicTemperature Temat, w ramach którego są publikowane wiadomości z czujnika wilgotności, a w ramach którego
     *                         aplikacja będzie subskrybowała i odbierała pomiary.
     * @param topicHumidity Temat, w ramach którego są publikowane wiadomości z czujnika ciśnienia, a w ramach którego
     *                      aplikacja będzie subskrybowała i odbierała pomiary.
     * @param topicPressure Temat, w ramach którego są publikowane wiadomości z czujnika ciśnienia, a w ramach którego
     *                      aplikacja będzie subskrybowała i odbierała pomiary.
     */
    public MqttHandler(Context context, String serverUrl, String username, String password, String topicTemperature, String topicHumidity, String topicPressure){
        this.serverUrl = serverUrl;
        this.username = username;
        this.password = password;
        this.topicTemperature = topicTemperature;
        this.topicHumidity = topicHumidity;
        this.topicPressure = topicPressure;

        mqttAndroidClient = new MqttAndroidClient(context, serverUrl, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            /**
             * Callback wywoływany, gdy połączenie z brokerem zostanie nawiązane.
             *
             * @param reconnect Zmienna logiczna informująca, czy połączenie jest wykonywane po raz
             *                  pierwszy, czy też jest to ponowna próba połączenia z brokerem.
             * @param ServerURI Adres URI serwera, z którym połączenie zostało nawiązane.
             * @see MqttHandler#serverUrl
             */
            @Override
            public void connectComplete(boolean reconnect, String ServerURI) {
                if (reconnect) {
                    Log.w("Debug","Connecting Complete!");
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                } else {
                    Log.w("Debug","Connecting Complete!");
                }
            }

            /**
             * Callback wywoływany, gdy połączenie z brokerem zostanie utracone.
             *
             * @param throwable Obiekt klasy Throwable, który może być wyjątkiem.
             */
            @Override
            public void connectionLost(Throwable throwable) {

            }

            /**
             * Callback wywoływany, gdy zostanie odebrana wiadomość z brokera, w związku z
             * subskrybowanym tematem.
             *
             * @param topic Subskrybowany temat, w ramach którego została odebrana wiadomość.
             * @param mqttMessage Odebrana wiadomość, w subskrybowanego tematu.
             * @throws Exception
             */
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Mqtt", mqttMessage.toString());
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
        connect();
    }

    /**
     * Metoda pozwalająca ustawić własny zmodyfikowany zestaw callback'ów.
     *
     * @param callback Obiekt reprezentujący zestaw callback'ów na zdarzenia związane
     *                 z połączeniem, z brokerem.
     */
    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    /**
     * Metoda służąca do nawiązania połączenia z brokerem. Stworzy ona obiekt klasy
     * MqttConnectOpions, którego metody określają ustawienia związane z połączeniem MQTT.
     *
     * @throws MqttException
     */
    private void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                /**
                 * Callback wywoływany, gdy nawiązanie połączenia zakończy się sukcesem oraz
                 * definiuje ustawienia, warunki potencjalnego utracenia połączenia.
                 *
                 * @param asyncActionToken Token połączenia MQTT.
                 */
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                /**
                 * Callback wywoływany, gdy nawiązanie połączenia zakończy się niepowodzeniem.
                 *
                 * @param asyncActionToken Token połączenia MQTT.
                 * @param exception Wyjątek, wskazujący powód niepowodzenia nawiązania
                 *                  połączenia.
                 */
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Failed to connect to: " + serverUrl + exception.toString());
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }


    /**
     * Metoda, w której definiowane są subskrybcje w ramach danego tematu. W przypadku tej
     * aplikacji są to 3 tematy.
     * @see MqttHandler#topicTemperature
     * @see MqttHandler#topicHumidity
     * @see MqttHandler#topicPressure
     * @throws MqttException
     */
    private void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(topicTemperature, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });
            mqttAndroidClient.subscribe(topicHumidity, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });
            mqttAndroidClient.subscribe(topicPressure, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }
}

