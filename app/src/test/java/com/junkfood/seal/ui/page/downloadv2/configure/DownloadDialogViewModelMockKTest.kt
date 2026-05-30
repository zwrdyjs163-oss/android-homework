package com.junkfood.seal.ui.page.downloadv2.configure

import com.junkfood.seal.download.DownloaderV2
import com.junkfood.seal.download.Task
import com.junkfood.seal.util.DownloadUtil.DownloadPreferences
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DownloadDialogViewModelMockKTest {

    @Test
    fun downloadWithPreset_enqueuesOneTaskForEachUrlAndHidesSheet() {
        val downloader = mockk<DownloaderV2>()
        val taskSlot = slot<Task>()
        every { downloader.enqueue(capture(taskSlot)) } just runs
        val viewModel = DownloadDialogViewModel(downloader)

        viewModel.postAction(
            DownloadDialogViewModel.Action.DownloadWithPreset(
                urlList = listOf("https://example.com/one", "https://example.com/two"),
                preferences = DownloadPreferences.EMPTY,
            )
        )

        verify(exactly = 2) { downloader.enqueue(any<Task>()) }
        verify { downloader.enqueue(match { it.url == "https://example.com/one" && it.preferences == DownloadPreferences.EMPTY }) }
        verify { downloader.enqueue(match { it.url == "https://example.com/two" && it.preferences == DownloadPreferences.EMPTY }) }
        assertEquals("https://example.com/two", taskSlot.captured.url)
        assertTrue(
            viewModel.sheetValueFlow.value is DownloadDialogViewModel.SheetValue.Hidden
        )
    }
}
