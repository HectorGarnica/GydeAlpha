package com.example.gydealpha.startup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.gydealpha.R
import com.example.gydealpha.profileBuilder.ProfileBuilderActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.startActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        Log.i("goku", "In SignUp Activity")

        auth = FirebaseAuth.getInstance()


        btn_sign_up.setOnClickListener{
            Log.i("goku", "SignUpActivity -> Register button pressed")
            signUpUser()


        }

        back_to_login.setOnClickListener{
            Log.i("goku", "sign up -> redirect text pressed")
            startActivity<SignInActivity>()
            finish()
        }

    }

    private fun signUpUser() {

        if (signup_username.text.toString().isEmpty()) {
            signup_username.error = "Please enter email"
            signup_username.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(signup_username.text.toString()).matches()) {
            signup_username.error = "Please enter email"
            signup_username.requestFocus()
            return
        }

        if (signup_password.text.toString().isEmpty()) {
            signup_password.error = "Please enter password"
            signup_password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(
            signup_username.text.toString(),
            signup_password.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val auth = FirebaseAuth.getInstance()
                    auth.currentUser

                    startActivity<ProfileBuilderActivity>()
                    finish()

                } else {
                    Toast.makeText(
                        baseContext, "Registration failed. Try again after some time",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

}
