package com.sd.lib.pholder

/**
 * 分页逻辑封装
 */
open class FPageHolder @JvmOverloads constructor(
    pageForRefresh: Int = 1,
) {
    /** 刷新数据页码  */
    private val _pageForRefresh: Int
    /** 初始默认页码  */
    private val _pageDefault: Int

    /** 当前页码  */
    private var _currentPage: Int
    /** 是否有下一页数据  */
    private var _hasNextPage = false

    private var _currentUpdater: ResultUpdater? = null

    init {
        require(pageForRefresh != Int.MIN_VALUE)
        _pageForRefresh = pageForRefresh
        _pageDefault = pageForRefresh - 1
        _currentPage = _pageDefault
    }

    /**
     * 是否有下一页数据
     */
    @Synchronized
    fun hasNextPage(): Boolean {
        return _hasNextPage
    }

    /**
     * 返回当前的页码
     */
    @Synchronized
    fun currentPage(): Int {
        return _currentPage
    }

    /**
     * 返回刷新数据的页码
     */
    @Synchronized
    fun pageForRefresh(): Int {
        check(_currentUpdater == null) { "You should update result before this." }
        return _pageForRefresh
    }

    /**
     * 返回获取下一页数据的页码
     */
    @Synchronized
    fun pageForLoadMore(): Int {
        check(_currentUpdater == null) { "You should update result before this." }
        return _currentPage + 1
    }

    /**
     * 接口成功时调用
     * @param isLoadMore 是否加载更多
     */
    @Synchronized
    fun onSuccess(isLoadMore: Boolean): ResultUpdater {
        return ResultUpdater(isLoadMore).also {
            _currentUpdater = it
        }
    }

    /**
     * 重置
     */
    @Synchronized
    fun reset() {
        _currentPage = _pageDefault
        _hasNextPage = false
        _currentUpdater = null
    }

    @Synchronized
    private fun updatePage(updater: ResultUpdater) {
        if (_currentUpdater !== updater) return

        _hasNextPage = updater.hasNextPage ?: error("You should call ResultUpdater.setHasNextPage() before this.")

        val page = updater.page
        if (page != null) {
            setCurrentPage(page)
        } else {
            val hasData = updater.hasData ?: error("You should call ResultUpdater.setHasData() before this.")
            if (updater.isLoadMore) {
                if (hasData) {
                    setCurrentPage(_currentPage + 1)
                }
            } else {
                if (hasData) {
                    setCurrentPage(_pageForRefresh)
                } else {
                    setCurrentPage(_pageDefault)
                }
            }
        }

        // 重置
        _currentUpdater = null
        onUpdate()
    }

    private fun setCurrentPage(page: Int) {
        val legalPage = page.coerceAtLeast(_pageDefault)
        if (_currentPage != legalPage) {
            _currentPage = legalPage
        }
    }

    protected open fun onUpdate() {}

    inner class ResultUpdater internal constructor(
        internal val isLoadMore: Boolean,
    ) {
        internal var hasNextPage: Boolean? = null
            private set

        internal var hasData: Boolean? = null
            private set

        internal var page: Int? = null
            private set

        /**
         * 设置是否有下一页
         * @param hasNextPage true-是；false-否
         */
        fun setHasNextPage(hasNextPage: Boolean) = apply {
            this.hasNextPage = hasNextPage
        }

        /**
         * 设置是否有返回数据
         * @param hasData true-是；false-否
         */
        fun setHasData(hasData: Boolean) = apply {
            this.hasData = hasData
        }

        /**
         * 设置是否有返回数据
         */
        fun setHasData(data: Collection<*>?) = apply {
            setHasData(data?.isNotEmpty() == true)
        }

        /**
         * 设置页码
         */
        fun setPage(page: Int?) = apply {
            this.page = page
        }

        /**
         * 更新page
         */
        fun update() {
            updatePage(this)
        }
    }
}