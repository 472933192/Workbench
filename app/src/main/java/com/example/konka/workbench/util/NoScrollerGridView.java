package com.example.konka.workbench.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by ChenXiaotao on 2016-10-25.
 */
public class NoScrollerGridView extends GridView {
    public NoScrollerGridView(Context context) {
        super(context);
        }
    public NoScrollerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
        }
}
