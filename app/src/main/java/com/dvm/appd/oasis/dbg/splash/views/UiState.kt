package com.dvm.appd.oasis.dbg.splash.views

sealed class UiState {

    object Login : UiState()
    object GoToMainApp : UiState()
}