package com.example.a1.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a1.R
import com.example.a1.databinding.FragmentDetailDishBinding

class DetailDishFragment : Fragment() {
    private lateinit var detailBinding: FragmentDetailDishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        detailBinding = FragmentDetailDishBinding.inflate(inflater,container, false)
        return detailBinding.root
    }
}