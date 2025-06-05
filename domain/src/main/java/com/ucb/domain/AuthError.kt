package com.ucb.domain

sealed class AuthError : Exception() {
    object NetworkError : AuthError()
    object InvalidCredentials : AuthError()
    object AccountDisabled : AuthError()
    object AccountExistsWithDifferentCredential : AuthError()
    object CredentialAlreadyInUse : AuthError()
    object UserCancelled : AuthError()
    data class Unknown(override val message: String?) : AuthError()
}