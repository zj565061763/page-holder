package com.sd.lib.holder.page;

import java.util.Collection;

/**
 * 分页逻辑封装
 */
public class FPageHolder {
    /** 刷新数据页码 */
    private final int mPageForRefresh;
    /** 初始默认页码 */
    private final int mPageDefault;

    /** 当前页码 */
    private volatile int mCurrentPage;
    /** 是否有下一页数据 */
    private volatile boolean mHasNextPage = false;

    private ResultUpdater mResultUpdater = null;

    public FPageHolder() {
        this(1);
    }

    public FPageHolder(int pageForRefresh) {
        assert Integer.MIN_VALUE != pageForRefresh;
        mPageForRefresh = pageForRefresh;
        mPageDefault = pageForRefresh - 1;
        setCurrentPage(mPageDefault);
    }

    /**
     * 是否有下一页数据
     */
    public boolean hasNextPage() {
        return mHasNextPage;
    }

    /**
     * 返回当前的页码
     */
    public int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     * 返回请求数据需要的page
     */
    public int getPageForRequest(boolean isLoadMore) {
        return isLoadMore ? getPageForLoadMore() : getPageForRefresh();
    }

    /**
     * 返回刷新数据需要的page
     */
    public synchronized int getPageForRefresh() {
        checkUpdater();
        return mPageForRefresh;
    }

    /**
     * 返回下一页数据需要的page
     */
    public synchronized int getPageForLoadMore() {
        checkUpdater();
        return mCurrentPage + 1;
    }

    private void checkUpdater() {
        if (mResultUpdater != null) {
            throw new RuntimeException("You should update result before this.");
        }
    }

    /**
     * 接口成功触发
     *
     * @param isLoadMore 是否加载更多
     */
    public synchronized ResultUpdater onSuccess(boolean isLoadMore) {
        mResultUpdater = new ResultUpdater(isLoadMore);
        return mResultUpdater;
    }

    private synchronized void updatePage(ResultUpdater updater) {
        if (mResultUpdater == null || mResultUpdater != updater) {
            return;
        }

        final Boolean hasNextPage = updater._hasNextPage;
        if (hasNextPage == null) {
            throw new RuntimeException("You should call ResultUpdater.setHasNextPage() before this.");
        }

        final Boolean hasData = updater._hasData;
        if (hasData == null) {
            throw new RuntimeException("You should call ResultUpdater.setHasData() before this.");
        }

        mHasNextPage = hasNextPage;

        final Integer page = updater._page;
        if (page != null) {
            setCurrentPage(page);
        } else {
            if (updater._isLoadMore) {
                // load more
                if (hasData) {
                    setCurrentPage(mCurrentPage + 1);
                }
            } else {
                // refresh
                if (hasData) {
                    setCurrentPage(mPageForRefresh);
                } else {
                    setCurrentPage(mPageDefault);
                }
            }
        }

        // 重置
        mResultUpdater = null;
        onUpdate();
    }

    /**
     * 设置当前页码
     */
    private void setCurrentPage(int page) {
        if (page < mPageDefault) {
            page = mPageDefault;
        }
        if (mCurrentPage != page) {
            mCurrentPage = page;
        }
    }

    protected void onUpdate() {
    }

    public final class ResultUpdater {
        private final boolean _isLoadMore;
        private Boolean _hasNextPage = null;
        private Boolean _hasData = null;
        private Integer _page = null;

        ResultUpdater(boolean isLoadMore) {
            this._isLoadMore = isLoadMore;
        }

        /**
         * 设置是否有下一页
         *
         * @param hasNextPage true-是；false-否
         */
        public ResultUpdater setHasNextPage(boolean hasNextPage) {
            this._hasNextPage = hasNextPage;
            return this;
        }

        /**
         * 设置是否有返回数据
         *
         * @param hasData true-是；false-否
         */
        public ResultUpdater setHasData(boolean hasData) {
            this._hasData = hasData;
            return this;
        }

        /**
         * 设置是否有返回数据
         */
        public ResultUpdater setHasData(Collection<?> data) {
            setHasData(data != null && data.size() > 0);
            return this;
        }

        /**
         * 设置页码
         */
        public ResultUpdater setPage(Integer page) {
            this._page = page;
            return this;
        }

        /**
         * 更新page
         */
        public void update() {
            updatePage(this);
        }
    }
}
