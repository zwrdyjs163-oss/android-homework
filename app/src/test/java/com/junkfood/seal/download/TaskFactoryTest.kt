package com.junkfood.seal.download

import com.junkfood.seal.download.Task.DownloadState.Idle
import com.junkfood.seal.download.Task.TypeInfo.Playlist
import com.junkfood.seal.util.DownloadUtil.DownloadPreferences
import com.junkfood.seal.util.PlaylistEntry
import com.junkfood.seal.util.PlaylistResult
import com.junkfood.seal.util.Thumbnail
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskFactoryTest {

    @Test
    fun createWithPlaylistResult_mapsSelectedEntriesToIdlePlaylistTasks() {
        val playlist =
            PlaylistResult(
                title = "Course playlist",
                channel = "Seal Channel",
                entries =
                    listOf(
                        PlaylistEntry(
                            url = "https://example.com/one",
                            title = "First lesson",
                            duration = 61.6,
                            uploader = "Teacher",
                            thumbnails = listOf(Thumbnail("https://example.com/one.jpg")),
                        ),
                        PlaylistEntry(url = "https://example.com/two", title = null, duration = 9.2),
                    ),
            )

        val tasks =
            TaskFactory.createWithPlaylistResult(
                playlistUrl = "https://example.com/playlist",
                indexList = listOf(1, 2),
                playlistResult = playlist,
                preferences = DownloadPreferences.EMPTY,
            )

        assertEquals(2, tasks.size)
        assertEquals("https://example.com/playlist", tasks[0].task.url)
        assertEquals(Playlist(1), tasks[0].task.type)
        assertEquals(Idle, tasks[0].state.downloadState)
        assertEquals("First lesson", tasks[0].state.viewState.title)
        assertEquals(62, tasks[0].state.viewState.duration)
        assertEquals("Teacher", tasks[0].state.viewState.uploader)
        assertEquals("https://example.com/one.jpg", tasks[0].state.viewState.thumbnailUrl)

        assertEquals(Playlist(2), tasks[1].task.type)
        assertEquals("Course playlist - 2", tasks[1].state.viewState.title)
        assertEquals("Seal Channel", tasks[1].state.viewState.uploader)
    }
}
