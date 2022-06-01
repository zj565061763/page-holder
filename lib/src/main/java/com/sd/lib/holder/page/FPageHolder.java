package com.sd.lib.holder.page;

import java.util.Collection;

/**
 * 分页逻辑封装
 */
public class FPageHolder {
    private final int mPageForRefresh;
    private volatile int mCurrentPage;
    private boolean mHasNextPage = false;

    public FPageHolder() {
        this(1);
    }

    public FPageHolder(int pageForRefresh) {
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
     * 返回当前的页数
     */
    public int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     * 返回当前请求需要传入的page
     *
     * @param isLoadMore 是否加载更多
     */
    public int getPageForRequest(boolean isLoadMore) {
        if (isLoadMore) {
            return getPageForLoadMore();
        } else {
            return getPageForRefresh();
        }
    }

    /**
     * 返回刷新数据需要的page
     */
    public int getPageForRefresh() {
        return mPageForRefresh;
    }

    /**
     * 返回下一页数据需要的page
     */
    public int getPageForLoadMore() {
        return mCurrentPage + 1;
    }

    /**
     * 接口成功触发
     *
     * @param isLoadMore 是否加载更多
     */
    public ResultUpdater onSuccess(boolean isLoadMore) {
        return new ResultUpdater(isLoadMore);
    }

    private synchronized void updatePage(boolean isLoadMore, boolean hasNextPage, boolean hasData) {
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
    }

    public final class ResultUpdater {
        private final boolean _isLoadMore;
        private Boolean _hasNextPage = null;
        private Boolean hasData = null;

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
            this.hasData = hasData;
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
            if (_hasNextPage == null) {
                throw new RuntimeException("you must invoke setHasNextPage() method before this");
            }
            if (hasData == null) {
                throw new RuntimeException("you must invoke setHasData() method before this");
            }
            updatePage(_isLoadMore, _hasNextPage, hasData);
        }
    }
}
