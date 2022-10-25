package dcard.commons.component.identity.bottomsheet.viewpager2

import android.view.View
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * This class override [ViewPager2.OnPageChangeCallback] for smooth viewPager2 height change
 * animation
 *
 * @author SmallTsai
 */
class BottomSheetSmoothViewHeightAnimator(
    private val viewPager2: ViewPager2,
) : ViewPager2.OnPageChangeCallback() {

    enum class Mode {
        Default,
        FixedFirstPageHeightIfIsHighest,
    }

    private val layoutManager: LinearLayoutManager?
        get() = (viewPager2.getChildAt(0) as? RecyclerView)?.layoutManager as? LinearLayoutManager

    private var leftHeight: Int = 0
    private var rightHeight: Int = 0

    var mode: Mode = Mode.Default

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        recalculate(position, positionOffset)
    }

    private fun recalculate(position: Int, positionOffset: Float = 0f) = layoutManager?.apply {
        val leftView = findViewByPosition(position) ?: return@apply
        val rightView = findViewByPosition(position + 1)
        setMeasure(leftView, rightView, positionOffset)
    }

    private fun setMeasure(leftView: View, rightView: View?, positionOffset: Float) {
        viewPager2.apply {
            leftView.findViewTreeLifecycleOwner()?.lifecycle
            leftHeight = getMeasuredViewHeightFor(leftView)
            rightHeight = rightView?.let { getMeasuredViewHeightFor(it) } ?: return
            updateLayoutParams {
                height = when (mode) {
                    Mode.Default -> {
                        leftHeight + ((rightHeight - leftHeight) * positionOffset).toInt()
                    }
                    Mode.FixedFirstPageHeightIfIsHighest -> if (leftHeight < rightHeight) {
                        leftHeight + ((rightHeight - leftHeight) * positionOffset).toInt()
                    } else {
                        leftHeight
                    }
                }
            }
        }
    }

    private fun getMeasuredViewHeightFor(view: View): Int {
        val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
        val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(wMeasureSpec, hMeasureSpec)
        return view.measuredHeight
    }
}