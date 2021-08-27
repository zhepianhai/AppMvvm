package com.gw.safty.common.base;

import androidx.lifecycle.Observer;

public class NonNullObserver<T> implements Observer<T> {
    private NonNullCall<T> call;

    public NonNullObserver(NonNullCall<T> call) {
        this.call = call;
    }

    @Override
    public void onChanged(T t) {
        if (t != null){
            call.change(t);
        }
    }
}
