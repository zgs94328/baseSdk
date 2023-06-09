package com.zgs.library.base.ext.view

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.zgs.library.base.ext.dp2px
import com.zgs.library.base.widget.decoration.RecyclerViewDivider
import java.util.*

/**
 * Description: RecyclerView扩展
 * Create by lxj, at 2018/12/25
 */

/**
 * 设置分割线
 * @param color 分割线的颜色，默认是#DEDEDE
 * @param size 分割线的大小，默认是1px
 * @param isReplace 是否覆盖之前的ItemDecoration，默认是true
 *
 */
fun RecyclerView.divider(
    color: Int = Color.parseColor("#f5f5f5"),
    size: Int = this.dp2px(1f),
    isReplace: Boolean = true
): RecyclerView {
    val decoration = RecyclerViewDivider(context, orientation)
    decoration.setDrawable(GradientDrawable().apply {
        setColor(color)
        shape = GradientDrawable.RECTANGLE
        setSize(size, size)
    })
    if (isReplace && itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
    addItemDecoration(decoration)
    return this
}

/**
 * 流式布局
 *
 * @return
 */
fun RecyclerView.flexbox(): RecyclerView {
    layoutManager = FlexboxLayoutManager(context)
    return this
}

fun RecyclerView.vertical(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount)
    }
    if (isStaggered) {
        layoutManager =
            StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }
    return this
}

/**
 * 设置方向
 *
 * @param spanCount
 * @param isStaggered 瀑布流
 * @return
 */
fun RecyclerView.horizontal(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    if (spanCount != 0) {
        layoutManager =
            GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
    }
    if (isStaggered) {
        layoutManager =
            StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
    }
    return this
}


inline val RecyclerView.data
    get() = (adapter as BaseQuickAdapter<*, *>).data

inline val RecyclerView.orientation
    get() = if (layoutManager == null) -1 else layoutManager.run {
        when (this) {
            is LinearLayoutManager -> orientation
            is GridLayoutManager -> orientation
            is StaggeredGridLayoutManager -> orientation
            else -> -1
        }
    }


/**
 * 无需创建adapter
 */
fun <T> RecyclerView.bindData(
    data: List<T>, layoutId: Int,
    bindFn: (holder: BaseViewHolder, t: T, position: Int) -> Unit,
): RecyclerView {
    adapter = object : BaseQuickAdapter<T, BaseViewHolder>(layoutId) {
        override fun convert(holder: BaseViewHolder, item: T) {
            bindFn(holder, item, holder.layoutPosition)
        }
    }.apply {
        setList(data)
    }
    return this
}

fun <T> RecyclerView.diffUpdate(diffCallback: DiffCallback<T>) {
    val adapter = adapter as? BaseQuickAdapter<T, BaseViewHolder>? ?: return
    DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(adapter)
}

/**
 * 必须在bindData之后调用，并且需要hasHeaderOrFooter为true才起作用
 */
fun RecyclerView.addHeader(headerView: View): RecyclerView {
    adapter?.apply {
        (this as BaseQuickAdapter<*, *>).addHeaderView(headerView)
    }
    return this
}

/**
 * 必须在bindData之后调用，并且需要hasHeaderOrFooter为true才起作用
 */
fun RecyclerView.addFooter(footerView: View): RecyclerView {
    adapter?.apply {
        (this as BaseQuickAdapter<*, *>).addFooterView(footerView)
    }
    return this
}




fun RecyclerView.smoothScrollToEnd() {
    if (adapter != null && adapter!!.itemCount > 0) {
        smoothScrollToPosition(adapter!!.itemCount - 1)
    }
}

fun RecyclerView.scrollToEnd() {
    if (adapter != null && adapter!!.itemCount > 0) {
        scrollToPosition(adapter!!.itemCount - 1)
    }
}

/**
 * 滚动置顶，只支持线性布局
 */
fun RecyclerView.scrollTop(position: Int) {
    if (layoutManager is LinearLayoutManager) {
        (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
    }
}

/**
 * 启用条目拖拽，必须在设置完adapter之后调用
 * @param isDisableLast 是否禁用最后一个拖拽
 */
fun RecyclerView.enableItemDrag(
    isDisableLast: Boolean = false,
    onDragFinish: (() -> Unit)? = null
) {
    ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            if (adapter == null) return 0
            if (isDisableLast && viewHolder.adapterPosition == (adapter!!.itemCount - 1)) return 0
            return if (recyclerView.layoutManager is GridLayoutManager) {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                val swipeFlags = 0
                makeMovementFlags(dragFlags, swipeFlags)
            } else {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = 0
                makeMovementFlags(dragFlags, swipeFlags)
            }
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            if (adapter == null) return false
            //得到当拖拽的viewHolder的Position
            val fromPosition = viewHolder.adapterPosition
            //拿到当前拖拽到的item的viewHolder
            val toPosition = target.adapterPosition
            if (isDisableLast && toPosition == (adapter!!.itemCount - 1)) return false
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap((adapter as BaseQuickAdapter<*, *>).data, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap((adapter as BaseQuickAdapter<*, *>).data, i, i - 1)
                }
            }
            recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            onDragFinish?.invoke()
        }

    }).attachToRecyclerView(this)
}

fun RecyclerView.disableItemAnimation(): RecyclerView {
    (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    return this
}

/**
 * 边界模糊
 */
fun RecyclerView.fadeEdge(
    length: Int = 25,
    isHorizontal: Boolean = false
): RecyclerView {
    if (isHorizontal) isHorizontalFadingEdgeEnabled = true
    else isVerticalFadingEdgeEnabled = true
    overScrollMode = View.OVER_SCROLL_ALWAYS
    setFadingEdgeLength(length)
    return this
}

/**
 * 示例代码如下：
 * class UserDiffCallback(var oldData: List<User>?, var newData: List<User>?) : DiffUtil.Callback() {
override fun getOldListSize() = oldData?.size ?: 0
override fun getNewListSize() = newData?.size ?: 0

override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
if(oldData.isNullOrEmpty() || newData.isNullOrEmpty()) return false
return oldData!![oldItemPosition].id == newData!![newItemPosition].id
}

override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
return oldData!![oldItemPosition].name == newData!![newItemPosition].name
}

//局部更新 areItemsTheSame==true && areContentsTheSame==false 调用
override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
val oldItem = oldData!![oldItemPosition]
val newItem = newData!![newItemPosition]
val bundle = Bundle()
if(oldItem.name != newItem.name){
bundle.putString("name", newItem.name)
}
return bundle
}
}
 *
 */
open class DiffCallback<T>(var oldData: List<T>?, var newData: List<T>?) : DiffUtil.Callback() {
    override fun getOldListSize() = oldData?.size ?: 0
    override fun getNewListSize() = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldData.isNullOrEmpty() || newData.isNullOrEmpty()) return false
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldData.isNullOrEmpty() || newData.isNullOrEmpty()) return false
        return false
    }

    //局部更新
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? = null
}
