package com.sd.lib.holder.page;

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
     * 刷新接口请求成功后，更新当前分页
     *
     * @param hasNextPage 是否有下一页，true-是；false-否
     * @param hasData     接口是否有返回数据
     */
    public synchronized void onSuccessRefresh(boolean hasNextPage, boolean hasData)
    {
        mHasNextPage = hasNextPage;

        if (hasData)
            mCurrentPage = 1;
        else
            mCurrentPage = 0;
    }

    /**
     * 加载更多接口请求成功后，更新当前分页
     *
     * @param hasNextPage 是否有下一页，true-是；false-否
     * @param hasData     接口是否有返回数据
     */
    public synchronized void onSuccessLoadMore(boolean hasNextPage, boolean hasData)
    {
        mHasNextPage = hasNextPage;

        if (hasData)
            mCurrentPage++;
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
}
