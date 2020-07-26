package com.example.ceiling_decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;


/**
  *
  * @Description:     吸顶效果得装饰器
  * @Author:         maoweiyi
  * @CreateDate:     2020/7/26
  * @Version:        1.0
 */
public class CeilingDecoration extends RecyclerView.ItemDecoration {
    private int groupHeaderHeight;
    private Paint headPaint;
    private Paint textPaint;
    private Paint dividerPaint;
    private Rect textRect;

    private boolean isEnableDivider=true;
    private int dividerHeight=4;
    private int headerTextGravity= TextGravity.LEFT;

    public CeilingDecoration(Context context) {
        groupHeaderHeight = dp2px(context, 100);

        headPaint = new Paint();
        headPaint.setAntiAlias(true);
        headPaint.setColor(Color.RED);

        textPaint = new Paint();
        textPaint.setTextSize(50);
        textPaint.setColor(Color.WHITE);
        textRect = new Rect();

        dividerPaint=new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setColor(Color.LTGRAY);
    }

    public CeilingDecoration disableDivider(){
        isEnableDivider=false;
        return this;
    }

    public CeilingDecoration setHeaderTextGravity(@TextGravity int headerTextGravity) {
        this.headerTextGravity = headerTextGravity;
        return this;
    }

    public CeilingDecoration setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
        return this;
    }

    public CeilingDecoration setGroupHeaderHeight(Context context, int dpValue){
        groupHeaderHeight = dp2px(context, dpValue);
        return this;
    }

    public CeilingDecoration setHeadPaintColor(int color){
        headPaint.setColor(color);
        return this;
    }

    public CeilingDecoration setHeadTextColor(int color){
        textPaint.setColor(color);
        return this;
    }

    public CeilingDecoration setTextPaint(Paint paint){
        textPaint=paint;
        return this;
    }

    public CeilingDecoration setDividerPaint(Paint paint){
        dividerPaint=paint;
        return this;
    }

    public CeilingDecoration setHeadTextSize(float textSize){
        textPaint.setTextSize(textSize);
        return this;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (parent.getAdapter() instanceof ICeilingAdapter) {
            ICeilingAdapter adapter = (ICeilingAdapter) parent.getAdapter();
            int count = parent.getChildCount();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            for (int i = 0; i < count; i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildLayoutPosition(view);
                boolean isGroupHeader = adapter.isGroupHeader(position);
                if (isGroupHeader && view.getTop() - groupHeaderHeight - parent.getPaddingTop() >= 0) {
                    c.drawRect(left, view.getTop() - groupHeaderHeight, right, view.getTop(), headPaint);
                    String groupName = adapter.getGroupName(position);
                    textPaint.getTextBounds(groupName, 0, groupName.length(), textRect);
                    int x;
                    if (headerTextGravity== TextGravity.LEFT){
                        x=left + 20;
                    }else if (headerTextGravity== TextGravity.RIGHT){
                        x=right - 20;
                    }else {
                        x=left+(right-left)/2-textRect.width()/2;
                    }
                    c.drawText(groupName, x, view.getTop() -
                            groupHeaderHeight / 2 + textRect.height() / 2, textPaint);
                } else if (view.getTop() - groupHeaderHeight - parent.getPaddingTop() >= 0&&isEnableDivider) {
                    drawDividerLine(c, view,left,right);
                }

            }
        }
    }

    /**
     * 默认绘制分割线得方法
     */
    protected void drawDividerLine(Canvas c,View view, int left,int right) {
        c.drawRect(left, view.getTop() - dividerHeight, right, view.getTop(), dividerPaint);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (parent.getAdapter() instanceof ICeilingAdapter) {
            ICeilingAdapter adapter = (ICeilingAdapter) parent.getAdapter();
            int position = ((LinearLayoutManager) Objects.requireNonNull(parent.getLayoutManager())).findFirstVisibleItemPosition();
            View itemView = Objects.requireNonNull(parent.findViewHolderForAdapterPosition(position)).itemView;
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = parent.getPaddingTop();
            boolean isGroupHeader = adapter.isGroupHeader(position + 1);
            if (isGroupHeader) {
                int bottom = Math.min(groupHeaderHeight, itemView.getBottom() - parent.getPaddingTop());
                c.drawRect(left, top, right, top + bottom, headPaint);
                String groupName = adapter.getGroupName(position);
                textPaint.getTextBounds(groupName, 0, groupName.length(), textRect);
                int x;
                if (headerTextGravity== TextGravity.LEFT){
                    x=left + 20;
                }else if (headerTextGravity== TextGravity.RIGHT){
                    x=right - 20;
                }else {
                    x=left+(right-left)/2-textRect.width()/2;
                }
                c.drawText(groupName, x, top + bottom - groupHeaderHeight / 2 + textRect.height() / 2, textPaint);
            } else {
                c.drawRect(left, top, right, top + groupHeaderHeight, headPaint);
                String groupName = adapter.getGroupName(position);
                textPaint.getTextBounds(groupName, 0, groupName.length(), textRect);
                int x;
                if (headerTextGravity== TextGravity.LEFT){
                    x=left + 20;
                }else if (headerTextGravity== TextGravity.RIGHT){
                    x=right - 20;
                }else {
                    x=left+(right-left)/2-textRect.width()/2;
                }
                c.drawText(groupName, x, top + groupHeaderHeight / 2 + textRect.height() / 2, textPaint);
            }

        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getAdapter() instanceof ICeilingAdapter) {
            ICeilingAdapter adapter = (ICeilingAdapter) parent.getAdapter();
            int position = parent.getChildLayoutPosition(view);
            boolean isGroupHeader = adapter.isGroupHeader(position);
            if (isGroupHeader) {
                outRect.set(0, groupHeaderHeight, 0, 0);
            } else {
                if (isEnableDivider){
                    outRect.set(0, dividerHeight, 0, 0);
                }
            }
        }
    }

    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale * 0.5f);
    }

    @IntDef({TextGravity.LEFT,TextGravity.CENTER,TextGravity.RIGHT})
    public @interface TextGravity{
        int LEFT=0, CENTER =1,RIGHT=2;
    }
}
