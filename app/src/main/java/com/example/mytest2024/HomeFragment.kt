package com.example.mytest2024

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.example.mytest2024.RecyclerView.RecyclerViewActivity
import com.example.mytest2024.databinding.HomeFragment3Binding

class HomeFragment : Fragment() {


    private lateinit var binding: HomeFragment3Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragment3Binding.inflate(inflater)


        binding.menu9.isEnabled = false


        /* 프래그먼트에서 sharedprefernces 받기 */
        val pref: SharedPreferences = requireContext().getSharedPreferences("test", MODE_PRIVATE)
        val name = pref.getString("test1", "text")


        /* 경조사 조회 */
        binding.menu1.setOnClickListener {
            val intent = Intent(requireActivity(), FamilyEventActivity::class.java)
            intent.apply {
                startActivity(intent)
            }
        }

        // 자유 토론방

        binding.menu4.setOnClickListener {
            val intent = Intent(requireActivity(), FreeTalkActivity::class.java)
            intent.apply {
                startActivity(intent)
            }
        }

        // 편지 목록 조회

        binding.menu5.setOnClickListener {
            val intent = Intent(requireActivity(), LetterActivity::class.java)
            intent.apply {
                startActivity(intent)
            }
        }

        binding.menu6.setOnClickListener {
            val intent = Intent(requireActivity(), SurveyActivity::class.java)
            intent.apply {
                startActivity(intent)
            }
        }

        /* 설정 기능 */
        binding.menu8.setOnClickListener {
            val intent = Intent(requireActivity(), SettingActivity::class.java)
            intent.apply {
                startActivity(intent)
            }
        }





        return binding.root // root는 contraint 전체를 뜻한다 ,프래그먼트에서는 onCreateView에서 UI 초기화와 관련된 코드를 작성하며, return 문은 항상 마지막에 위치해야 합니다.
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context
    }
}