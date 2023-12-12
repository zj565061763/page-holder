package com.sd.demo.page_holder

import com.sd.lib.pholder.FPageHolder
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PageUnitTest {

    @Test
    fun testInit() {
        val holder = FPageHolder()

        assertEquals(0, holder.currentPage())
        assertEquals(1, holder.pageForRefresh())
        assertEquals(1, holder.pageForLoadMore())
        assertEquals(false, holder.hasNextPage())

        holder.onSuccess(false)
            .setHasNextPage(true)
            .setHasData(true)
            .setPage(10)
            .update()

        assertEquals(10, holder.currentPage())
    }

    @Test
    fun testRefresh() {
        val holder = FPageHolder()


        // test setHasNextPage(true)
        holder.onSuccess(false)
            .setHasNextPage(true)
            .setHasData(false)
            .update()
        assertEquals(true, holder.hasNextPage())


        // test setHasNextPage(false)
        holder.onSuccess(false)
            .setHasNextPage(false)
            .setHasData(false)
            .update()
        assertEquals(false, holder.hasNextPage())


        // test setHasData(true)
        holder.onSuccess(false)
            .setHasNextPage(true)
            .setHasData(true)
            .update()
        assertEquals(1, holder.currentPage())


        // test setHasData(false)
        holder.onSuccess(false)
            .setHasNextPage(true)
            .setHasData(false)
            .update()
        assertEquals(0, holder.currentPage())


        // test setHasData(Collection)
        holder.onSuccess(false)
            .setHasNextPage(true)
            .setHasData(emptyList<Any?>())
            .update()
        assertEquals(0, holder.currentPage())


        // test setHasData(Collection)
        holder.onSuccess(false)
            .setHasNextPage(true)
            .setHasData(listOf(""))
            .update()
        assertEquals(1, holder.currentPage())


        // test setHasData(Collection)
        holder.onSuccess(false)
            .setHasNextPage(true)
            .setHasData(listOf(""))
            .update()
        assertEquals(1, holder.currentPage())
    }

    @Test
    fun testLoadMore() {
        val holder = FPageHolder()


        // test setHasNextPage(true)
        holder.onSuccess(true)
            .setHasNextPage(true)
            .setHasData(false)
            .update()
        assertEquals(true, holder.hasNextPage())


        // test setHasNextPage(false)
        holder.onSuccess(true)
            .setHasNextPage(false)
            .setHasData(false)
            .update()
        assertEquals(false, holder.hasNextPage())


        // test setHasData(true)
        holder.onSuccess(true)
            .setHasNextPage(true)
            .setHasData(true)
            .update()
        assertEquals(1, holder.currentPage())


        // test setHasData(false)
        holder.onSuccess(true)
            .setHasNextPage(true)
            .setHasData(false)
            .update()
        assertEquals(1, holder.currentPage())


        // test setHasData(Collection)
        holder.onSuccess(true)
            .setHasNextPage(true)
            .setHasData(emptyList<Any?>())
            .update()
        assertEquals(1, holder.currentPage())


        // test setHasData(Collection)
        holder.onSuccess(true)
            .setHasNextPage(true)
            .setHasData(listOf(""))
            .update()
        assertEquals(2, holder.currentPage())


        // test setHasData(Collection)
        holder.onSuccess(true)
            .setHasNextPage(true)
            .setHasData(listOf(""))
            .update()
        assertEquals(3, holder.currentPage())
    }
}