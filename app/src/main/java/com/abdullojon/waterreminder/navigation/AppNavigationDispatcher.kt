package com.abdullojon.waterreminder.navigation

import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigationDispatcher @Inject constructor() : AppNavigator, AppNavigationHandler {

    private val _navigationFlow = MutableSharedFlow<AppNavigationParam>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val navigationFlow: SharedFlow<AppNavigationParam> = _navigationFlow

    private fun navigate(param: AppNavigationParam) {
        _navigationFlow.tryEmit(param)
    }

    override fun navigateTo(screen: Screen) = navigate {
        push(screen)
    }

    override fun back() = navigate {
        pop()
    }

    override fun replace(screen: Screen) = navigate {
        replace(screen)
    }

    override fun replaceAll(screen: Screen) = navigate {
        replaceAll(screen)
    }
}
