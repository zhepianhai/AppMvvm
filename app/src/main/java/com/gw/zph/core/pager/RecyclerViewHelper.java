package com.gw.zph.core.pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gw.safty.common.network.PagerWrapper;

import java.util.List;

public class RecyclerViewHelper<RequestParams extends IPagerParams, Model> {
    public static final int DEFAULT_PAGE_SIZE = 50;

    private RvHelperLoadingCallback<RequestParams> callback;
    private IPagerAdapter<Model> adapter;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private RequestParams params;
    private int totalPages = 0;
    private boolean isLoading = false;
    private boolean isLoadingMore = false;

    public void attach() {
        if (params == null) throw new IllegalArgumentException("需要先设置请求参数");
        if (recyclerView == null) throw new IllegalArgumentException("需要先设置RecyclerView");
        if (adapter == null) throw new IllegalArgumentException("需要先设置RecyclerView 的 Adapter");
        if (refreshLayout != null) refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            loadFirstPage();
        });
        recyclerView.addOnScrollListener(scrollListener);
        loadFirstPage();
    }

    public void loadFirstPage() {
        this.isLoading = true;
        this.isLoadingMore = false;
        params.setCurrentPage(1);
        if (callback != null) {
            callback.onLoadingPages(params);
        }
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!isLoading && totalPages > params.getCurrentPage() && !recyclerView.canScrollVertically(1)) { //正数向上滚，复数相反
                isLoading = true;
                isLoadingMore = true;
                if (callback != null) {
                    callback.onLoadingPages(params);
                }
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    /**
     * 需要主动调用
     *
     * @param models 传入网络请求得到的数据
     */
    public void setResult(@Nullable PagerWrapper<Model> models) {
        this.isLoading = false;
        if (models == null || models.getList() == null) {
            return;
        }
        if (isLoadingMore) {
            adapter.appendList(models.getList());
        } else {
            adapter.setList(models.getList());
        }
        if (models.getList().size() > 0) {
            params.setCurrentPage(params.getCurrentPage() + 1);
        }
        this.totalPages = models.getPages();
        this.isLoadingMore = false;
    }





    public RecyclerViewHelper setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public RecyclerViewHelper setRefreshLayout(SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        return this;
    }

    public RecyclerViewHelper setCallback(RvHelperLoadingCallback<RequestParams> callback) {
        this.callback = callback;
        return this;
    }

    public RecyclerViewHelper setAdapter(IPagerAdapter<Model> adapter) {
        this.adapter = adapter;
        return this;
    }

    public RecyclerViewHelper setParams(RequestParams params) {
        this.params = params;
        return this;
    }
}
