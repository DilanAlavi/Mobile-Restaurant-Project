package com.ucb.framework.mappers

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.ucb.domain.AuthError
import com.ucb.domain.User

// Extension para mapear FirebaseUser a User del dominio
fun FirebaseUser.toUser(): User {
    return User(
        id = uid,
        name = displayName ?: "",
        email = email ?: "",
        photoUrl = photoUrl?.toString(),
        isEmailVerified = isEmailVerified
    )
}

// Extension para mapear FirebaseAuthException a AuthError del dominio
fun FirebaseAuthException.toAuthError(): AuthError {
    return when (errorCode) {
        "ERROR_NETWORK_REQUEST_FAILED" -> AuthError.NetworkError
        "ERROR_INVALID_CREDENTIAL" -> AuthError.InvalidCredentials
        "ERROR_USER_DISABLED" -> AuthError.AccountDisabled
        "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> AuthError.AccountExistsWithDifferentCredential
        "ERROR_CREDENTIAL_ALREADY_IN_USE" -> AuthError.CredentialAlreadyInUse
        "ERROR_USER_CANCELLED" -> AuthError.UserCancelled
        else -> AuthError.Unknown(message)
    }
}