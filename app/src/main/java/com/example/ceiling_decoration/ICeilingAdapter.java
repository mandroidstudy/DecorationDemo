package com.example.ceiling_decoration;

import android.text.TextUtils;

public interface ICeilingAdapter {

    default  boolean isGroupHeader(int position) {
        if (position == 0) {
            return true;
        } else {
            String currentGroupName = getGroupName(position);
            String preGroupName = getGroupName(position - 1);
            return !TextUtils.equals(currentGroupName,preGroupName);
        }
    }

    abstract String getGroupName(int position);
}
