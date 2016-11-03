package com.example.konka.workbench.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.example.konka.workbench.R;
import com.example.konka.workbench.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * Created by HP on 2016-9-22.
 * 未加入项目的成员列表适配器
 */
public class UserNumAdapter extends ArrayAdapter<User> {

private  int  resourceID;
    public UserNumAdapter(Context context, int resource,List<User> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        ViewHolder holder=null;
        if (convertView == null) {
            convertView= LayoutInflater.from(getContext()).inflate(resourceID, null);
            holder=new ViewHolder();
            holder.checktv_title = (CheckedTextView) convertView.findViewById(R.id.user_Num_cho);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.checktv_title.setText(user.getUsername());
        boolean check = ((ListView) parent).isItemChecked(position);
        holder.checktv_title.setChecked(check);
        return convertView;
    }
    class ViewHolder {
        CheckedTextView checktv_title;
    }
}
