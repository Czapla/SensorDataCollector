package net.ddns.achouse.sensordatacollector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DataListAdapter extends BaseAdapter {
    private ArrayList<Data> listData;
    private LayoutInflater layoutInflater;

    public DataListAdapter(Context aContext, ArrayList<Data> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.table_row, null);
            holder = new ViewHolder();
            holder.uType = (TextView) v.findViewById(R.id.type);
            holder.uTimestamp = (TextView) v.findViewById(R.id.timestamp);
            holder.uValue = (TextView) v.findViewById(R.id.value);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.uType.setText(listData.get(position).getType());
        holder.uTimestamp.setText(listData.get(position).getTimestamp());
        holder.uValue.setText(listData.get(position).getValue());
        return v;
    }

    static class ViewHolder {
        TextView uType;
        TextView uTimestamp;
        TextView uValue;
    }
}
