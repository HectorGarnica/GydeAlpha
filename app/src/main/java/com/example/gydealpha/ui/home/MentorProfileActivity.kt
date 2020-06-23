package com.example.gydealpha.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gydealpha.R
import com.example.gydealpha.model.UserModel
import com.example.gydealpha.ui.home.chat.ChatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_mentor_profile.*
import org.jetbrains.anko.startActivity
import java.io.Serializable

class MentorProfileActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_profile)

        val user = intent.getSerializableExtra("extra_user") as? UserModel

        back_button_to_findAMentor.setOnClickListener {
            onBackPressed()
        }

        dialog_chat_button.setOnClickListener {
            val intent = Intent(this,ChatActivity::class.java)
            intent.putExtra("extra_user", user as Serializable)
            intent.putExtra("extra_imAMentee",true)
            startActivity(intent,null)
        }



        //dialog_findAMentor_profileImage.setImageResource()

        if (user != null) {
            val path : String? = user.userImageUrl
            Picasso.get().load(path).fit().centerCrop().into(dialog_findAMentor_profileImage)
            dialog_name_textView.text = user.fname
            dialog_bio_textView.text = user.bio
            var expertiseStringResult: String  = ""
            for(elem:String in user.expertise!!)
                expertiseStringResult+=elem+"\n"
            dialog_expertise_textView.text = expertiseStringResult
        }


    }



}
