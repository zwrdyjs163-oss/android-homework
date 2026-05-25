package com.coderpage.lib.update

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * DefaultVersionComparator 的 MockK + Robolectric 测试。
 * 使用 MockK mock PackageManager 来模拟版本比较场景。
 */
@RunWith(RobolectricTestRunner::class)
class DefaultVersionComparatorMockKTest {

    @Test
    fun compare_currentLowerThanLatest_returnsTrue() {
        val context = mockk<Context>()
        val packageManager = mockk<PackageManager>()
        val apkModel = mockk<ApkModel>()
        val packageInfo = PackageInfo()

        packageInfo.versionCode = 10
        every { context.packageName } returns "com.test.app"
        every { context.packageManager } returns packageManager
        every { packageManager.getPackageInfo("com.test.app", 0) } returns packageInfo
        every { apkModel.buildCode } returns 20L

        val comparator = DefaultVersionComparator()
        assertTrue(comparator.compare(context, apkModel))
    }

    @Test
    fun compare_currentEqualToLatest_returnsFalse() {
        val context = mockk<Context>()
        val packageManager = mockk<PackageManager>()
        val apkModel = mockk<ApkModel>()
        val packageInfo = PackageInfo()

        packageInfo.versionCode = 20
        every { context.packageName } returns "com.test.app"
        every { context.packageManager } returns packageManager
        every { packageManager.getPackageInfo("com.test.app", 0) } returns packageInfo
        every { apkModel.buildCode } returns 20L

        val comparator = DefaultVersionComparator()
        assertFalse(comparator.compare(context, apkModel))
    }

    @Test
    fun compare_currentHigherThanLatest_returnsFalse() {
        val context = mockk<Context>()
        val packageManager = mockk<PackageManager>()
        val apkModel = mockk<ApkModel>()
        val packageInfo = PackageInfo()

        packageInfo.versionCode = 30
        every { context.packageName } returns "com.test.app"
        every { context.packageManager } returns packageManager
        every { packageManager.getPackageInfo("com.test.app", 0) } returns packageInfo
        every { apkModel.buildCode } returns 20L

        val comparator = DefaultVersionComparator()
        assertFalse(comparator.compare(context, apkModel))
    }

    @Test
    fun compare_packageNotFound_returnsFalse() {
        val context = mockk<Context>()
        val packageManager = mockk<PackageManager>()
        val apkModel = mockk<ApkModel>()

        every { context.packageName } returns "com.test.app"
        every { context.packageManager } returns packageManager
        every { packageManager.getPackageInfo("com.test.app", 0) } throws
                PackageManager.NameNotFoundException()

        every { apkModel.buildCode } returns 20L

        val comparator = DefaultVersionComparator()
        assertFalse(comparator.compare(context, apkModel))
    }
}
