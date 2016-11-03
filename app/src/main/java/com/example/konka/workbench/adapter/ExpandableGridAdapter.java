package com.example.konka.workbench.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.example.konka.workbench.R;
import com.example.konka.workbench.util.NoScrollerGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-24.
 */
public class ExpandableGridAdapter implements ExpandableListAdapter {
    private Context context;
    private String[] type = new String[]{"机型","平台","BOM","配屏"};
    /*private String[][] item=new String[][]{
            {"机型1","机型2","机型3","机型4","机型5"},
            {"平台1","平台2","平台3","平台4","平台5","平台6"},
            {"BOM1","BOM2","BOM3","BOM4","BOM5"},
            {"配屏1","配屏2","配屏3","配屏4","配屏5"}
    };*/
    private List<ArrayList<String>> list;
    private List<ArrayList<String>> selectedData;

    public ExpandableGridAdapter(Context context, List<ArrayList<String>> list,List<ArrayList<String>> selectedData) {
            this.context=context;
            this.list=list;
            this.selectedData=selectedData;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return type.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return type[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        TextView tv = new TextView(context);
        tv.setText(getGroup(i).toString());
        return tv;
    }

    @Override
    public View getChildView(final int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        }
        NoScrollerGridView gridView = (NoScrollerGridView) view.findViewById(R.id.GridView_toolbar);
        ProjectFilterAdapter adapter=new ProjectFilterAdapter(context,selectedData.get(i), list.get(i));
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv= (TextView) view.findViewById(R.id.grid_item);
                if (selectedData.get(i).contains(list.get(i).get(position))){
                    selectedData.get(i).remove(list.get(i).get(position));
                    tv.setBackgroundResource(R.drawable.blank_bn1);
                    tv.setTextColor(Color.BLACK);
                } else {
                    selectedData.get(i).add(list.get(i).get(position));
                    tv.setBackgroundResource(R.drawable.blank_red);
                    tv.setTextColor(Color.RED);
                }
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }
    class ViewHolder{
        NoScrollerGridView gridView;
    }
}
