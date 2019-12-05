package com.sd.lib.holder.page;

import java.util.List;

/**
 * 分页逻辑封装
 */
public class FPageHolder
{
    private int mCurrentPage;
    private int mCurrentCount;
    private boolean mHasNextPage;

    public FPageHolder()
    {
        reset();
    }

    /**
     * 是否有下一页数据
     *
     * @return
     */
    public boolean hasNextPage()
    {
        return mHasNextPage;
    }

    /**
     * 返回当前的页数
     *
     * @return
     */
    public int getCurrentPage()
    {
        return mCurrentPage;
    }

    /**
     * 返回当前数据的数量
     *
     * @return
     */
    public int getCurrentCount()
    {
        return mCurrentCount;
    }

    /**
     * 返回当前请求需要传入的page
     *
     * @param isLoadMore 是否加载更多
     * @return
     */
    public int getPageForRequest(boolean isLoadMore)
    {
        if (isLoadMore)
        {
            return mCurrentPage + 1;
        } else
        {
            return 1;
        }
    }

    /**
     * 接口成功触发
     *
     * @param isLoadMore
     * @return
     */
    public synchronized ResultUpdater onSuccess(boolean isLoadMore)
    {
        return new ResultUpdater(isLoadMore);
    }

    private synchronized void updatePage(boolean isLoadMore, boolean hasNextPage, boolean hasData)
    {
        mHasNextPage = hasNextPage;

        if (isLoadMore)
        {
            if (hasData)
                mCurrentPage++;
        } else
        {
            if (hasData)
                mCurrentPage = 1;
            else
                mCurrentPage = 0;
        }
    }

    /**
     * 重置
     */
    public synchronized void reset()
    {
        mCurrentPage = 0;
        mCurrentCount = 0;
        mHasNextPage = false;
    }

    public final class ResultUpdater
    {
        private final boolean isLoadMore;

        private Boolean hasNextPage = null;
        private Boolean hasData = null;

        ResultUpdater(boolean isLoadMore)
        {
            this.isLoadMore = isLoadMore;
        }

        /**
         * 设置是否有下一页
         *
         * @param hasNextPage true-是；false-否
         * @return
         */
        public ResultUpdater setHasNextPage(boolean hasNextPage)
        {
            this.hasNextPage = hasNextPage;
            return this;
        }

        /**
         * 设置是否有返回数据
         *
         * @param hasData true-是；false-否
         * @return
         */
        public ResultUpdater setHasData(boolean hasData)
        {
            this.hasData = hasData;
            return this;
        }

        /**
         * 设置是否有返回数据
         *
         * @param listData
         * @return
         */
        public ResultUpdater setHasData(List<? extends Object> listData)
        {
            final boolean has = listData != null && listData.size() > 0;
            setHasData(has);
            return this;
        }

        /**
         * 更新page
         */
        public void update()
        {
            if (hasNextPage == null)
                throw new RuntimeException("you must invoke setHasNextPage() method before this");

            if (hasData == null)
                throw new RuntimeException("you must invoke setHasData() method before this");

            updatePage(isLoadMore, hasNextPage, hasData);
        }
    }
}
