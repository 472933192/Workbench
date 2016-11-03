package com.example.konka.workbench.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.example.konka.workbench.R;

import java.util.List;


/**
 * Created by HP on 2016-9-21.
 */
public class ProjectPopAdapter extends ArrayAdapter<String> {
    private int resourceId;

    public ProjectPopAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            holder=new ViewHolder();
            holder.checktv_title = (CheckedTextView) convertView.findViewById(R.id.pop_check_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        boolean check = ((ListView) parent).isItemChecked(position);
        holder.checktv_title.setChecked(check);
        String project=getItem(position);
        if(project!=null)
        {
            holder.checktv_title.setText(project);
        }
        else
        {
            holder.checktv_title.setText("");
        }
        return convertView;

    }
    class ViewHolder {
        CheckedTextView checktv_title;
    }
}
