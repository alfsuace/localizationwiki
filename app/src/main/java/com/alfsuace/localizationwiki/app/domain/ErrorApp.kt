package com.alfsuace.localizationwiki.app.domain

sealed class ErrorApp : Throwable() {

    class InternetErrorApp : ErrorApp() {
        private fun readResolve(): Any = InternetErrorApp()
    }

    class ServerErrorApp : ErrorApp() {
        private fun readResolve(): Any = ServerErrorApp()
    }

    class DataErrorApp : ErrorApp() {
        private fun readResolve(): Any = DataErrorApp()
    }

    class UnknownErrorApp : ErrorApp() {
        private fun readResolve(): Any = UnknownErrorApp()
    }

    class CacheExpiredErrorApp : ErrorApp() {
        private fun readResolve(): Any = CacheExpiredErrorApp()
    }
}