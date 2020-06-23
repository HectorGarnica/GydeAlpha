package com.example.gydealpha.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.gydealpha.R
import com.example.gydealpha.model.UserModel
import com.example.gydealpha.model.ParentModelFindMentor
import com.example.gydealpha.util.MyPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_find_mentors.*


class FindMentorsFragment : Fragment(){

    val auth = FirebaseAuth.getInstance()
    val user_id = auth.currentUser?.uid
    val storage = Firebase.storage
    val db = Firebase.firestore
    val userCollectionRef = db.collection("users")
    val docRef = db.collection("users").document("/$user_id")


    companion object {
        private val ARG_CAUGHT = "FindMentorsFragment_caught"

        fun newInstance():FindMentorsFragment{
            val fragment = FindMentorsFragment()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //this.requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_mentors, container, false)
    }


    override fun onStart() {
        super.onStart()

        var listOfrows  =  arrayListOf<ParentModelFindMentor>()
        var myAdapter = ParentAdapterFindMentor(listOfrows)
        rv_parent_findMentors.apply {
            layoutManager = LinearLayoutManager(requireActivity(),RecyclerView.VERTICAL,false)
            adapter = myAdapter
        }


        docRef.get().addOnSuccessListener {

            val myName = it.get("fname").toString()+" "+it.get("lname").toString() //reading logged on user
            val myPreference = MyPreference(requireActivity())
            myPreference.setUserName(myName)
            var topics = it.get("seeking") as ArrayList<String>     //subject user is interested in
            val university = it.get("institution")

            for(topicString:String in topics){

                val mentorsQuery = userCollectionRef            //fetch good matches for user
                    .whereEqualTo("institution",university)
                    .whereEqualTo("isAMentor",true)
                    .whereArrayContains("expertise",topicString)
                    .orderBy("lastActive", Query.Direction.DESCENDING)

                mentorsQuery.get().addOnCompleteListener { task ->

                    if (task.isSuccessful){         //add userObject to list to be displayed in ui

                        var mentorsInSubject = arrayListOf<UserModel>()

                        for(userDocument: QueryDocumentSnapshot in task.result!!){
                            val userModelInDoc: UserModel = userDocument.toObject(UserModel::class.java)
                            mentorsInSubject.add(userModelInDoc)
                        }
                        if (mentorsInSubject.isNotEmpty()){
                            var row = ParentModelFindMentor(topicString,mentorsInSubject)
                            listOfrows.add(row)
                            myAdapter.notifyDataSetChanged()
                        }
                    }
                } } }




    }



}
