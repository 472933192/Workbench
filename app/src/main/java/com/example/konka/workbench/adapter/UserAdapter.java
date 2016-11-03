package com.example.konka.workbench.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.konka.workbench.R;
import com.example.konka.workbench.domain.User;

import java.util.List;

/**
 * Created by HP on 2016-9-26.
 * 用户适配器，显示项目用户
 */
public class UserAdapter extends ArrayAdapter<User> {
    private  int  resourceID;
    public UserAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
        } else {
            view=convertView;
        }
        TextView userForPro =(TextView) view.findViewById(R.id.member_item_tv);
        userForPro.setText(user.getUsername());
        return view;
    }
}
