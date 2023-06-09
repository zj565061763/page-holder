package com.sd.demo.page_holder

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sd.lib.pholder.FPageHolder
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun testInit() {
        val holder = FPageHolder()

        assertEquals(0, holder.currentPage)
        assertEquals(1, holder.pageForRefresh)
        assertEquals(1, holder.pageForLoadMore)
        assertEquals(false, holder.hasNextPage())

        holder.onSuccess(false)
            .setHasNextPage(true)
            .setHasData(true)
            .setPage(10)
            .update()

        assertEquals(10, holder.currentPage)
    }
}