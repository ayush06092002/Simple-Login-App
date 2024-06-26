package com.who.simplelogin.screens.login

data class LoadingState(val status: Status, val message: String? = null) {
    companion object {
        val IDLE = LoadingState(Status.IDLE)
        val LOADING = LoadingState(Status.LOADING)
        val SUCCESS = LoadingState(Status.SUCCESS)
        val ERROR = LoadingState(Status.ERROR)
    }

    enum class Status {
        IDLE,
        LOADING,
        SUCCESS,
        ERROR
    }

}
