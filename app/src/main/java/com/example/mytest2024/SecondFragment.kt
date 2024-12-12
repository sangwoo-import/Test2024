package com.example.mytest2024

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mytest2024.MainActivity
import com.example.mytest2024.databinding.SecondFragmentBinding

class SecondFragment : Fragment() , MainActivity.onBackPressedListner{


    private lateinit var binding: SecondFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SecondFragmentBinding.inflate(inflater)
        return binding.root // root는 contraint 전체를 뜻한다
    }



    override fun onBackPressed(){
        goToMain()
    }



    private fun goToMain(){

        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        requireActivity().supportFragmentManager.popBackStack()
    }
}
