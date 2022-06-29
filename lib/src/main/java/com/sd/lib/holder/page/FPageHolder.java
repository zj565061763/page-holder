package com.sd.lib.holder.page;

import java.util.Collection;

/**
 * 分页逻辑封装
 */
public class FPageHolder {
    /** 刷新数据需要的page */
    private final int mPageForRefresh;
    /** 当前的page */
    private volatile int mCurrentPage;
    /** 是否有下一页数据 */
    private volatile boolean mHasNextPage = false;

    private volatile ResultUpdater mResultUpdater = null;

    public FPageHolder() {
        this(1);
    }

    public FPageHolder(int pageForRefresh) {
        assert Integer.MIN_VALUE != pageForRefresh;
        mPageForRefresh = pageForRefresh;
        mCurrentPage = pageForRefresh - 1;
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
     * 设置当前页码
     */
    public synchronized void setCurrentPage(int currentPage) {
        final int minPage = mPageForRefresh - 1;
        if (currentPage < minPage) {
            currentPage = minPage;
        }
        if (mCurrentPage != currentPage) {
            mCurrentPage = currentPage;
            // 如果页码被直接修改，则重置为null
            mResultUpdater = null;
        }
    }

    /**
     * 返回刷新数据需要的page
     */
    public int getPageForRefresh() {
        checkUpdater();
        return mPageForRefresh;
    }

    /**
     * 返回下一页数据需要的page
     */
    public int getPageForLoadMore() {
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
        if (mResultUpdater == null) {
            mResultUpdater = new ResultUpdater();
        }
        mResultUpdater._isLoadMore = isLoadMore;
        return mResultUpdater;
    }

    private synchronized void updatePage(ResultUpdater updater) {
        if (mResultUpdater == null || mResultUpdater != updater) {
            return;
        }

        final Boolean isLoadMore = updater._isLoadMore;
        assert isLoadMore != null;

        final Boolean hasNextPage = updater._hasNextPage;
        if (hasNextPage == null) {
            throw new RuntimeException("You should call ResultUpdater.setHasNextPage() before this.");
        }

        final Boolean hasData = updater._hasData;
        if (hasData == null) {
            throw new RuntimeException("You should call ResultUpdater.setHasData() before this.");
        }

        mHasNextPage = hasNextPage;
        if (isLoadMore) {
            if (hasData) {
                mCurrentPage++;
            }
        } else {
            if (hasData) {
                mCurrentPage = mPageForRefresh;
            } else {
                mCurrentPage = mPageForRefresh - 1;
            }
        }

        // 重置为null
        mResultUpdater = null;
        onUpdate();
    }

    protected void onUpdate() {
    }

    public final class ResultUpdater {
        private Boolean _isLoadMore = null;
        private Boolean _hasNextPage = null;
        private Boolean _hasData = null;

        ResultUpdater() {
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
         * 更新page
         */
        public void update() {
            updatePage(this);
        }
    }
}
