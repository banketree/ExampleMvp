package com.example.base_fun.bean


/**
 * @author banketree
 * @time 2020/1/17 15:25
 * @description
 * 针对分页数据封装
 */
class PageManager<T> {
    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
    }

    var data = ArrayList<T>() //列表
    var pageSize = DEFAULT_PAGE_SIZE //每页大小 默认10
    var totalPage = 1 //总页数
    var pageIndex = 0 //当前页
    var totalCount: Int = 0 //总记录数
    var keyword: String = "" //关键字

    val isEmptyData: Boolean
        get() = data.isEmpty()

    fun reset() {
        data.clear()
        pageSize = DEFAULT_PAGE_SIZE
        totalPage = 1
        pageIndex = 0
        totalCount = 0
    }

    fun increasePageIndex() {
        pageIndex++
    }


    fun declineCount() {
        totalCount--
        if (totalCount < 0)
            totalCount = 0
    }

    fun hasMore(): Boolean {
        //数据已加载完毕
        if (!data.isEmpty() && totalCount != 0 && data.size >= totalCount)
            return false
        //当前页 小于 最大页
        return pageIndex < totalPage

    }

    fun clone(): PageManager<T> {
        val pageManger = PageManager<T>()
        pageManger.pageSize = pageSize
        pageManger.totalPage = totalPage
        pageManger.pageIndex = pageIndex
        pageManger.totalCount = totalCount
        pageManger.data.addAll(data)
        return pageManger
    }

    fun release() {
        reset()
    }
}

