package com.gw.zph.core.pager;

public interface IPagerParams {
    /**
     * @return 返回当前请求第几页
     */
    int getCurrentPage();

    /**
     * @param page 用于设置当前请求的页
     */
    void setCurrentPage(int page);
}
