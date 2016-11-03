package com.example.konka.workbench.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.konka.workbench.R;
import com.example.konka.workbench.domain.Project;

import java.util.HashMap;
import java.util.List;

/**
 * Created by HP on 2016-9-14.
 * project适配器
 */
public class ProjectAdapter extends ArrayAdapter<Project>{
    private int resourceId;

    public ProjectAdapter(Context context, int resource, List<Project> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Project project = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            holder=new ViewHolder();
            holder.bom = (TextView) convertView.findViewById(R.id.lv_mypro_bom_tv);
            holder.platform = (TextView) convertView.findViewById(R.id.lv_mypro_platform_tv);
            holder.projectName = (TextView) convertView.findViewById(R.id.lv_mypro_name_tv);
            holder.type = (TextView) convertView.findViewById(R.id.lv_mypro_type_tv);
            holder.screen = (TextView) convertView.findViewById(R.id.lv_mypro_screen_tv);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.projectName.setText(project.getProjectName());
        holder.type.setText(project.getType());
        holder.platform.setText(project.getPlatform());
        holder.bom.setText(project.getBom());
        holder.screen.setText(project.getWithScreen());
        return convertView;
    }
    class ViewHolder {
        TextView projectName;
        TextView type;
        TextView platform;
        TextView bom;
        TextView screen;
    }
}
