package com.zgs.library.base.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.luck.picture.lib.utils.ToastUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.util.SmartGlideImageLoader
import com.ruffian.library.widget.RImageView
import com.zgs.library.base.R
import com.zgs.library.base.ext.*
import com.zgs.library.base.ext.view.bindData

/**
 * 图片上传UI组件
 */
class ImageUploader @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttr) {

    private var addImage: Drawable?
    private var delImage: Drawable?
    var delImageMargin = 0
    var imageRadius = 0
    var spanCount = 3
    var hSpace = context.dp2px(10f) //横向间距
    var vSpace = context.dp2px(10f)
    var maxImages = 9
    private var add_button = "_add_button_"
    var paths = arrayListOf<String>(add_button)

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.ImageUploader)
        imageRadius = ta.getDimensionPixelSize(R.styleable.ImageUploader_imageRadius, imageRadius)
        delImageMargin =
            ta.getDimensionPixelSize(R.styleable.ImageUploader_delImageMargin, delImageMargin)
        addImage = ta.getDrawable(R.styleable.ImageUploader_addImageRes)
        delImage = ta.getDrawable(R.styleable.ImageUploader_delImageRes)
        spanCount = ta.getInt(R.styleable.ImageUploader_spanCount, spanCount)
        maxImages = ta.getInt(R.styleable.ImageUploader_maxImages, maxImages)
        hSpace = ta.getDimensionPixelSize(R.styleable.ImageUploader_hSpace, hSpace)
        vSpace = ta.getDimensionPixelSize(R.styleable.ImageUploader_vSpace, vSpace)

        ta.recycle()

        if (addImage == null) addImage = drawable(R.mipmap._ktx_ic_add_image)
        if (delImage == null) delImage = drawable(R.mipmap._ktx_ic_image_del)

        layoutManager = FlexboxLayoutManager(context)
        setupData()
    }

    fun setupData(urls: ArrayList<String>? = null) {
        if (!urls.isNullOrEmpty()) {
            paths.clear()
            paths.addAll(urls)
            paths.add(add_button)
        }
        bindData(paths, R.layout._ktx_adapter_image_uploader, bindFn = { holder, t, position ->
            val itemWidth = (measuredWidth - (spanCount - 1) * hSpace) / spanCount
            //计算每个条目的宽度
            holder.itemView.widthAndHeight(itemWidth, itemWidth)
            val isRowStart = (position) % spanCount == 0
            holder.itemView.margin(
                leftMargin = if (isRowStart) 0 else hSpace,
                bottomMargin = vSpace
            )
            holder.getView<ImageView>(R.id.close).setImageDrawable(delImage)
            if (delImageMargin > 0) {
                holder.getView<ImageView>(R.id.close)
                    .margin(rightMargin = delImageMargin, topMargin = delImageMargin)
            }
            if (t == add_button) {
                holder.getView<ImageView>(R.id.close).gone()
                holder.getView<RImageView>(R.id.image).apply {
                    setImageDrawable(addImage)
                    corner = 0f
                    singleClick { addButtonClickAction?.invoke() }
                }
            } else {
                holder.getView<ImageView>(R.id.close).visible()
                holder.getView<RImageView>(R.id.image).apply {
                    load(t)
                    corner = imageRadius.toFloat()
                    singleClick {
                        XPopup.Builder(context)
                            .asImageViewer(it as ImageView, t, SmartGlideImageLoader()).show()
                    }
                }
            }
            holder.getView<ImageView>(R.id.close).singleClick {
                paths.remove(t)
                if (paths.size < maxImages && !paths.contains(add_button)) {
                    paths.add(add_button)
                }
                adapter?.notifyDataSetChanged()
            }
        })
    }

    var addButtonClickAction: (() -> Unit)? = null

    fun addImage(path: String) {
        if (paths.size >= (maxImages + 1)) {
            //达到最大数量
            "最多添加${maxImages}张图片".toast()
            return
        }
        val index = paths.indexOf(add_button)
        paths.add(index, path)
        if (paths.size == (maxImages + 1)) {
            paths.remove(add_button)
        }
        adapter?.notifyDataSetChanged()
    }

    fun addImages(images: List<String>) {
        if (images.isNullOrEmpty()) return
        if (paths.size >= (maxImages + 1) ||
            (paths.size + images.size) > (maxImages + 1)
        ) {
            //达到最大数量
            "最多添加${maxImages}张图片".toast()
            return
        }
        val index = paths.indexOf(add_button)
        paths.addAll(index, images)
        if (paths.size == (maxImages + 1)) {
            paths.remove(add_button)
        }
        adapter?.notifyDataSetChanged()
    }

    fun getImagePaths() = paths.subList(0, paths.size - 1)
}