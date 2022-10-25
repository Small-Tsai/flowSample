package dcard.commons.component.identity.bottomsheet.viewpager2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tsai.flowsample.viewpager.PageType
import com.tsai.flowsample.viewpager.ViewPagerOneFragment
import com.tsai.flowsample.viewpager.ViewPagerThreeFragment
import com.tsai.flowsample.viewpager.ViewPagerTwoFragment

/**
 * @author SmallTsai
 */
class IdentityBottomSheetViewPager2Adapter(
    fragment: Fragment,
    private val items: List<PageType>
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = PageType.values().size

    override fun createFragment(position: Int): Fragment = when (getItemType(position)) {
        PageType.TypeOne -> ViewPagerOneFragment()
        PageType.TypeTwo -> ViewPagerTwoFragment()
    }

    fun getItemType(position: Int): PageType = items[position]
}