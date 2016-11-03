package com.example.konka.workbench.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.konka.workbench.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-26.
 * Gridview的适配器
 */
public class ProjectFilterAdapter extends BaseAdapter {
    private List<String> selectedList;
    private List<String> list;
    private Context context;
    public ProjectFilterAdapter(Context context, List<String> selectedList, List<String> list) {
        this.selectedList=selectedList;
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null, false);
        }
        TextView tv= (TextView) convertView.findViewById(R.id.grid_item);
        tv.setText(list.get(position));
        if (selectedList.contains(list.get(position))) {
            tv.setBackgroundResource(R.drawable.blank_red);
            tv.setTextColor(Color.RED);
        }

        return convertView;
    }
}
