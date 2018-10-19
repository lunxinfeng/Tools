package com.lxf.recyclerhelper

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

import com.lxf.recyclerhelper.animation.BaseAnimation
import com.lxf.recyclerhelper.animation.ScaleInAnimation

import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

typealias OnItemClickListener = (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
typealias OnItemChildClickListener = (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
typealias OnItemLongClickListener = (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Boolean
typealias OnItemChildLongClickListener = (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Boolean

abstract class BaseQuickAdapter<T, VH : BaseViewHolder>(
        @LayoutRes private val layoutId: Int,
        var data: MutableList<T>? = null
) : RecyclerView.Adapter<VH>() {

    //-------------------------anim---------------------------
    /**
     * item显示的时候是否开启动画
     */
    private var animShow: Boolean = false
    /**
     * 是否只有在第一次item显示的时候开启动画
     */
    private var animFirstOnly: Boolean = false
    /**
     * 具体动画
     */
    private var animation: BaseAnimation? = null
    /**
     * 动画时长
     */
    private var animDuration: Long = 300
    /**
     * 动画插值器
     */
    private var animInterpolator: Interpolator = LinearInterpolator()
    /**
     * 列表展示的最后一条item位置
     */
    private var lastPosition = -1

    //-------------------------emptyView----------------------
    /**
     * 数据为空时展示的布局
     */
    private var emptyView: View? = null
    //-------------------------errorView----------------------
    /**
     * 加载失败时展示的布局
     */
    private var errorView: View? = null
    /**
     * 是否展示失败布局
     */
    private var loadError: Boolean = false

    var onItemClickListener: OnItemClickListener? = null
    var onItemChildClickListener: OnItemChildClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    var onItemChildLongClickListener: OnItemChildLongClickListener? = null

    val headerLayoutCount: Int
        get() = 0

    companion object {
        internal val HEADER_VIEW = 0x00000111
        internal val FOOTER_VIEW = 0x00000222
        internal val EMPTY_VIEW = 0x00000333
        internal val ERROR_VIEW = 0x00000444
    }

    init {
        this.data = data ?: mutableListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val holder: VH
        when (viewType) {
            EMPTY_VIEW -> holder = createBaseViewHolder(emptyView)
            ERROR_VIEW -> holder = createBaseViewHolder(errorView)
            else -> {
                holder = createBaseViewHolder(view)
                bindViewClickListener(holder)
            }
        }

        holder.setAdapter(this)
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val viewType = holder.itemViewType
        when (viewType) {
            HEADER_VIEW -> {
            }
            EMPTY_VIEW -> {
            }
            FOOTER_VIEW -> {
            }
            ERROR_VIEW -> {
            }
            else -> convert(holder, data!![position])
        }
    }

    override fun getItemCount(): Int {
        return if (loadError || hasEmptyView()) 1 else data!!.size
    }

    override fun getItemViewType(position: Int): Int {
        if (loadError)
            return ERROR_VIEW
        return if (hasEmptyView()) {
            EMPTY_VIEW
        } else super.getItemViewType(position)
    }

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        val viewType = holder.itemViewType
        if (viewType != EMPTY_VIEW && viewType != ERROR_VIEW)
            addAnimation(holder)

        loadError = false
    }

    protected abstract fun convert(holder: VH, item: T)

    fun setNewData(data: MutableList<T>?) {
        this.data = data ?: mutableListOf()
        lastPosition = -1
        notifyDataSetChanged()
    }

    fun addData(data: List<T>?) {
        if (data == null) return
        this.data!!.addAll(data)
        notifyDataSetChanged()
    }

    fun loadDataFail() {
        loadError = true
        notifyDataSetChanged()
    }

    fun showWithAnimation(anim: Boolean) {
        animShow = anim
    }

    fun showWithAnimation(anim: Boolean, animation: BaseAnimation) {
        animShow = anim
        this.animation = animation
    }

    fun showAnimOnlyFirst(first: Boolean) {
        animFirstOnly = first
    }

    /**
     * 是否显示空布局
     */
    fun hasEmptyView(): Boolean {
        return emptyView != null && data!!.size == 0
    }

    fun setEmptyView(@LayoutRes layoutId: Int, viewGroup: ViewGroup) {
        emptyView = LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }

    fun setErrorView(@LayoutRes layoutId: Int, viewGroup: ViewGroup) {
        errorView = LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }

    private fun bindViewClickListener(baseViewHolder: BaseViewHolder?) {
        if (baseViewHolder == null) {
            return
        }
        val view = baseViewHolder.itemView ?: return
        view.setOnClickListener { v -> onItemClickListener?.invoke(this@BaseQuickAdapter, v, baseViewHolder.layoutPosition) }
    }

    /**
     * 为item展现的时候添加动画
     */
    private fun addAnimation(holder: RecyclerView.ViewHolder) {
        if (animShow) {
            if (!animFirstOnly || holder.layoutPosition > lastPosition) {
                if (animation == null) {
                    animation = ScaleInAnimation()
                }
                for (anim in animation!!.getAnimators(holder.itemView)) {
                    anim.setDuration(animDuration).start()
                    anim.interpolator = animInterpolator
                }
                lastPosition = holder.layoutPosition
            }
        }
    }

    fun setAnimDuration(animDuration: Long) {
        this.animDuration = animDuration
    }

    fun setAnimInterpolator(animInterpolator: Interpolator) {
        this.animInterpolator = animInterpolator
    }

    /**
     * if you want to use subclass of BaseViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @param view view
     * @return new ViewHolder
     */
    protected fun createBaseViewHolder(view: View?): VH {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp)
            temp = temp.superclass
        }
        val k: VH?
        // 泛型擦除会导致z为null
        k = if (z == null) {
            BaseViewHolder(view) as VH
        } else {
            createGenericKInstance(z, view)
        }
        return k ?: BaseViewHolder(view) as VH
    }

    /**
     * try to create Generic VH instance
     */
    private fun createGenericKInstance(z: Class<*>, view: View?): VH? {
        try {
            val constructor: Constructor<*>
            // inner and unstatic class
            if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                constructor.isAccessible = true
                return constructor.newInstance(this, view) as VH
            } else {
                constructor = z.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                return constructor.newInstance(view) as VH
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * get generic parameter VH
     */
    private fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
        val type = z.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (temp in types) {
                if (temp is Class<*>) {
                    if (BaseViewHolder::class.java.isAssignableFrom(temp)) {
                        return temp
                    }
                } else if (temp is ParameterizedType) {
                    val rawType = temp.rawType
                    if (rawType is Class<*> && BaseViewHolder::class.java.isAssignableFrom(rawType)) {
                        return rawType
                    }
                }
            }
        }
        return null
    }
}
