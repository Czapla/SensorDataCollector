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

/**
 * Klasa implementująca Fragment wyświetlający odebrane dane z brokera w postaci tabeli.
 */
public class TableFragment extends Fragment {
    /**
     * Statyczna metoda tworząca nową instancję Fragmentu.
     * @return Obiekt klasy TableFragment.
     */
    public static TableFragment newInstance() {
        TableFragment fragment = new TableFragment();
        return fragment;
    }

    /**
     * Lista obiektów klasy Data.
     */
    ArrayList<Data> dataList = new ArrayList<Data>();
    /**
     * ListAdapter, który automatycznie wypełni ListView, czyli pojemnik reprezentujący tabelę z danymi.
     */
    ListAdapter adap;

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
        return inflater.inflate(R.layout.table_fragment, container, false);
    }

    /**
     * Metoda reprezentująca moment w cyklu życia Fragmentu, zaraz po utworzeniu instancji
     * intefejsu użytkownika. Tutaj przypisywane są elementy intefejsu do pól klasy. Jest tutaj
     * również tworzona tabela(nagłówek z nazwami kolumn oraz wiersze z danymi).
     * @param view Widok, który został utworzony w cyklu życia Fragmentu.
     * @param savedInstanceState Obiekt typu Bundle, który reprezentuje stan Fragmentu.
     */
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

    /**
     * Metoda informująca adapter o potrzebie zaktualizowania tabeli o nowe dane.
     */
    public void updateTable() {
        ((DataListAdapter)adap).notifyDataSetChanged();
    }
}
