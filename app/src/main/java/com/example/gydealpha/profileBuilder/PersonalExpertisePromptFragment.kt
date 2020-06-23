package com.example.gydealpha.profileBuilder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.gydealpha.R
import kotlinx.android.synthetic.main.fragment_mentor_skill_prompt.*
import kotlinx.android.synthetic.main.fragment_name_prompt.*
import kotlinx.android.synthetic.main.fragment_personal_expertise_prompt.*
import kotlinx.android.synthetic.main.fragment_personal_expertise_prompt.mentor_skill_gridView_personal
import kotlinx.android.synthetic.main.grid_view_topic_item.view.*


class PersonalExpertisePromptFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var fname: String
    lateinit var lname: String
    lateinit var institution: String
    lateinit var major : String
    lateinit var mentorRequirements : String

    var mentoringTopics : String=""
    var topicBubbles = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fname = arguments!!.getString("firstNameArg").toString()
        lname = arguments!!.getString("lastNameArg").toString()
        institution = arguments!!.getString("institutionArg").toString()
        major = arguments!!.getString("major1Arg").toString()
        mentorRequirements =arguments!!.getString("mentorSkillArg").toString()
        //topicBubbles = mentorRequirements.split(",") as ArrayList<String>

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_expertise_prompt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Grid View Setup
        val topicsBasic = resources.getStringArray(R.array.topics_basic)
        val images : IntArray = intArrayOf(R.drawable.nutrition,
            R.drawable.physical_wellness,R.drawable.spirituality,
            R.drawable.arts,R.drawable.student_life,R.drawable.personal_finance)

        val gvAdapter = TopicsGridViewAdapter(requireActivity(),images,topicsBasic)
        mentor_skill_gridView_personal.adapter = gvAdapter

        //textBubble
        val bAdapter = TopicTextBubbleAdapter(requireActivity(),topicBubbles)
        mentor_topic_recent_bubbles_personal.adapter = bAdapter

        mentor_skill_gridView_personal.setOnItemClickListener { _, view, position, _ ->

            if(view.checked.visibility == View.GONE) {
                view.checked.visibility = View.VISIBLE
                topicBubbles.add(0,topicsBasic.get(position).toString())
                bAdapter.notifyDataSetChanged()
            }
            else {
                view.checked.visibility = View.GONE
                topicBubbles.remove(topicsBasic.get(position).toString())
                bAdapter.notifyDataSetChanged()
            }

        }

        //search
        input_topic_searchView_personal.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false

            }
        })


        navController = Navigation.findNavController(view)

        button5.setOnClickListener {

            if(topicBubbles.isNotEmpty()){
                mentoringTopics = ""
                for (item : String in topicBubbles){
                    mentoringTopics+= item +","
                }
            }
            val bundle = bundleOf(
                "firstNameArg" to fname,
                "lastNameArg" to lname,
                "institutionArg" to institution,
                "major1Arg" to major,
                "mentorSkillArg" to mentorRequirements,
                "personalSkillArg" to mentoringTopics)

            navController.navigate(R.id.action_personalExpertisePromptFragment_to_profilePicturePrompt,bundle)
        }
    }

}