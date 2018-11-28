package net.ddns.achouse.sensordatacollector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TableFragment extends Fragment {
    public static TableFragment newInstance() {
        TableFragment fragment = new TableFragment();
        return fragment;
    }

    ArrayList<Data> dataList = new ArrayList<Data>();
    ListAdapter adap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.table_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ListView lv = (ListView) view.findViewById(R.id.data_list);

        // header
        LayoutInflater inflater = getLayoutInflater();
        View header = getLayoutInflater().inflate(R.layout.table_header, null);
        lv.addHeaderView(header);
        dataList = ((MainActivity)getActivity()).dataList;
        adap = new DataListAdapter(view.getContext(), dataList);
        lv.setAdapter(adap);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
