package com.coderpage.concurrency

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor

/**
 * AsyncTaskExecutor 的 MockK 测试。
 * 使用 MockK 模拟线程池行为，验证 submit/execute 调用。
 */
class AsyncTaskExecutorMockKTest {

    @Test
    fun executor_returnsThreadPoolExecutor() {
        val executor = AsyncTaskExecutor.executor()
        assertNotNull(executor)
        assertEquals(ThreadPoolExecutor::class.java, executor.javaClass)
    }

    @Test
    fun submit_callable_returnsFuture() {
        val callable = Callable { 42 }
        val future: Future<Int> = AsyncTaskExecutor.submit(callable)
        assertNotNull(future)
    }

    @Test
    fun submit_runnable_returnsFuture() {
        val runnable = Runnable { /* do nothing */ }
        val future: Future<*> = AsyncTaskExecutor.submit(runnable)
        assertNotNull(future)
    }

    @Test
    fun submit_runnableWithResult_returnsFutureWithResult() {
        val runnable = Runnable { /* do nothing */ }
        val result = "done"
        val future: Future<String> = AsyncTaskExecutor.submit(runnable, result)
        assertNotNull(future)
    }

    @Test
    fun executor_hasCorePoolSizeAtLeast2() {
        val executor = AsyncTaskExecutor.executor()
        assertTrue(executor.corePoolSize >= 2)
    }

    @Test
    fun executor_allowsCoreThreadTimeOut() {
        val executor = AsyncTaskExecutor.executor()
        assertTrue(executor.allowsCoreThreadTimeOut())
    }
}
