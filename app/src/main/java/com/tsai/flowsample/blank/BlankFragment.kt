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
import com.tsai.flowsample.ext.getVmFactory
import com.tsai.flowsample.viewpager.PageType
import dcard.commons.component.identity.bottomsheet.viewpager2.BottomSheetSmoothViewHeightAnimator
import dcard.commons.component.identity.bottomsheet.viewpager2.IdentityBottomSheetViewPager2Adapter


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
        setupViewPager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = TestAdapter()
        collectLatestLifeCycleFlowStarted(viewModel.largeList) {
            binding.apply {
                adapter.submitList(
                    it,
                    ((rev.layoutManager) as LinearLayoutManager).findFirstVisibleItemPosition(),
                    ((rev.layoutManager) as LinearLayoutManager).findLastVisibleItemPosition()
                )
            }
        }
        binding.rev.adapter = adapter
    }

    private fun setupViewPager() {
        binding.viewPager2.apply {
            adapter = IdentityBottomSheetViewPager2Adapter(
                fragment = this@BlankFragment,
                items = PageType.values().toList()
            )
            registerOnPageChangeCallback(
                BottomSheetSmoothViewHeightAnimator(viewPager2 = this).apply {
                    mode = BottomSheetSmoothViewHeightAnimator.Mode.FixedFirstPageHeightIfIsHighest
                }
            )
            isUserInputEnabled = true
        }
    }
}