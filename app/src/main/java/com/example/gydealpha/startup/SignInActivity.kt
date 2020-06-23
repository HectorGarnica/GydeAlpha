package com.example.gydealpha.startup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.example.gydealpha.MainActivity
import com.example.gydealpha.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.startActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        Log.i("goku", "In SignInActivity")

        auth = FirebaseAuth.getInstance()

        signin_button.setOnClickListener {
            Log.i("goku", "SignIn ->  sign in btn pressed")
            doLogin()


        }

        signin_create_account_button.setOnClickListener{
            Log.i("goku", "SignIn -> Create New account pressed")
            startActivity<SignUpActivity>()
            finish()
        }

    }

    private fun doLogin() {
        if(signin_username.text.toString().isEmpty()){
            signin_username.error = "Please enter email"
            signin_username.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(signin_username.text.toString()).matches()){
            signin_username.error = "Please enter email"
            signin_username.requestFocus()
            return
        }

        if(signin_password.text.toString().isEmpty()){
            signin_password.error = "Please enter password"
            signin_password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(signin_username.text.toString(),signin_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)

                }

            }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser!= null){
            startActivity<MainActivity>()
            finish()

        } else{
            Toast.makeText(baseContext,"Please Login.",Toast.LENGTH_SHORT).show()
        }

    }

}
