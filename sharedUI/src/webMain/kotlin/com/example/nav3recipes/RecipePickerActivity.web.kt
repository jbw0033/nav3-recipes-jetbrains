package com.example.nav3recipes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.SceneInfo
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import com.github.terrakok.navigation3.browser.HierarchicalBrowserNavigation
import com.github.terrakok.navigation3.browser.buildBrowserHistoryFragment

@Composable
internal actual fun BrowserIntegration() {
    val dispatcher = LocalNavigationEventDispatcherOwner.current?.navigationEventDispatcher ?: return
    val history by dispatcher.history.collectAsState()
    HierarchicalBrowserNavigation(
        currentDestination = remember { derivedStateOf { history.currentIndex.takeIf { it >= 0 } ?: 0 } },
        currentDestinationName = { i ->
            history.mergedHistory.getOrNull(i)?.let { info ->
                val key = if (info is SceneInfo<*>) {
                    info.scene.entries.lastOrNull()?.contentKey.toString()
                } else {
                    info::class.simpleName
                }
                val name = key?.lowercase()?.replace(" ", "_").orEmpty()
                buildBrowserHistoryFragment(name)
            }
        }
    )
}