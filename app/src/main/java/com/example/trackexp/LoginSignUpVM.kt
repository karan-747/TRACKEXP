package com.example.trackexp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class LoginSignUpVM:ViewModel() {
    private val TAG = "LOGINSIGNUPVIEWMODEL"

        private var repositoryRef :Repository? = null

    init{
        repositoryRef = Repository.getRepositoryInstance()
    }

    suspend fun loginUser(emailID:String , password:String): Pair<Boolean, String> {
        return repositoryRef?.loginUser(emailID,password)!!
    }

    suspend fun signInWithGoogle(account:GoogleSignInAccount): Pair<Boolean, String> {
        return repositoryRef?.loginWithGoogle(account)!!
    }

    suspend fun signUpUser(emailID:String , password:String): Pair<Boolean, String> {
        return repositoryRef?.signUpUser(emailID,password)!!
    }

    suspend fun signUpWithGoogle(account:GoogleSignInAccount): Pair<Boolean, String> {
        return repositoryRef?.signUpWithGoogle(account)!!
    }
    fun getUserLoggedInStatus():Boolean = repositoryRef?.getuserLoggedInStatus()!!


     fun checkLoginStatus(navigateToHome: () -> Unit) {

         Log.d(TAG,"MOVING TO HOME")
        repositoryRef?.checkLoginStatus(navigateToHome)
    }
}