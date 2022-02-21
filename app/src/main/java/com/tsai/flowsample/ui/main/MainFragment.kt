package com.tsai.flowsample.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tsai.flowsample.NavigationDirections
import com.tsai.flowsample.databinding.MainFragmentBinding
import com.tsai.flowsample.ext.collectLatestLifeCycleFlowStarted
import com.tsai.flowsample.ext.collectLifeCycleFlowStarted
import com.tsai.flowsample.ext.getVmFactory
import com.tsai.flowsample.util.Logger

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    private val viewModel by viewModels<MainViewModel> {
        getVmFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Logger.d("Fragment onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("Fragment onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Logger.d("Fragment onCreateView")

        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        collectLifeCycleFlowStarted(viewModel.navToBlankFrag) {
            findNavController().navigate(NavigationDirections.navToBlank())
        }

        collectLatestLifeCycleFlowStarted(viewModel.titleString) {
            binding.message.text = it
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.d("Fragment onViewCreated")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val testString = savedInstanceState?.get("tsai")
        Logger.d("Fragment onViewStateRestored GetString-> $testString")
    }

    override fun onStart() {
        super.onStart()
        Logger.d("Fragment onStart")
    }

    override fun onResume() {
        super.onResume()
        Logger.d("Fragment onResume")
    }

    override fun onPause() {
        super.onPause()
        Logger.d("Fragment onPause")
    }

    override fun onStop() {
        super.onStop()
        Logger.d("Fragment onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("tsai", "string from onSaveInstanceState")
        Logger.d("Fragment onSaveInstanceState")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logger.d("Fragment onDestroyView")

    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("Fragment onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Logger.d("Fragment onDetach")
    }

}