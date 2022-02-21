package com.tsai.flowsample.blank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tsai.flowsample.databinding.FragmentBlankBinding
import com.tsai.flowsample.ext.collectLatestLifeCycleFlowStarted
import com.tsai.flowsample.ext.collectLifeCycleFlowStarted
import com.tsai.flowsample.ext.getVmFactory
import com.tsai.flowsample.util.Logger


class BlankFragment : Fragment() {

    private lateinit var binding: FragmentBlankBinding

    private val viewModel by viewModels<BlankFragmentViewModel> {
        getVmFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBlankBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = BlankAdapter()
        val adapter2 = TestAdapter()

        collectLatestLifeCycleFlowStarted(viewModel.largeList) {
//            adapter.submitList(it)
            binding.apply {
                adapter2.submitList(
                    it,
                    ((rev.layoutManager) as LinearLayoutManager).findFirstVisibleItemPosition(),
                    ((rev.layoutManager) as LinearLayoutManager).findLastVisibleItemPosition()
                )
            }
        }

        binding.rev.adapter = adapter2

    }


}