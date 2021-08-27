package com.baiiu.filter.interfaces;

import java.util.HashMap;

/**
 * author: baiiu
 * date: on 16/1/21 23:30
 * description:
 */
public interface OnFilterDoneListener {
    void onFilterDone(int position, String positionTitle, String urlValue);

    void onFilterCancel();

    void onFilterSubmit(HashMap<String, String> map);
}