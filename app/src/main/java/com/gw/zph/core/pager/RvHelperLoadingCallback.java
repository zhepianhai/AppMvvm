package com.gw.zph.core.pager;

import androidx.annotation.NonNull;

public interface RvHelperLoadingCallback<Params> {
    void onLoadingPages(@NonNull Params params);
}
