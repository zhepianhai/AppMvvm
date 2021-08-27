package com.gw.zph.core.pager;

import java.util.List;

public interface IPagerAdapter<Model> {
    void setList(List<Model> models);
    void appendList(List<Model> models);
}
