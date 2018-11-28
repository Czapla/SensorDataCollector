package net.ddns.achouse.sensordatacollector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SettingsFragment extends Fragment {
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    private EditText etServerUrl;
    private EditText etPort;
    private EditText etUser;
    private EditText etPassword;
    private EditText etTopicTemperature;
    private EditText etTopicHumidity;
    private EditText etTopicPressure;

    private Button btnConnect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

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
                ((MainActivity)getActivity()).launchMqtt();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
