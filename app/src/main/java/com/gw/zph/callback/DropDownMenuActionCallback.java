package com.gw.zph.callback;

import java.util.HashMap;

public interface DropDownMenuActionCallback {
    void onFilterCancel();

    void onFilterSubmit(HashMap<String, String> map);
}
