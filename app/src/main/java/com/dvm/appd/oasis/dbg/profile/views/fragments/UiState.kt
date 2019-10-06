package com.dvm.appd.oasis.dbg.profile.views.fragments

sealed class UiState {
    object MoveToLogin : UiState()
    object ShowLoading: UiState()
    object ShowIdle: UiState()
}