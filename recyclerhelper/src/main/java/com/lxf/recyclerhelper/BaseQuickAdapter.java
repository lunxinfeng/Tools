package com.lxf.recyclerhelper;

import android.animation.Animator;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.lxf.recyclerhelper.animation.BaseAnimation;
import com.lxf.recyclerhelper.animation.ScaleInAnimation;
import com.lxf.recyclerhelper.listener.OnItemChildClickListener;
import com.lxf.recyclerhelper.listener.OnItemChildLongClickListener;
import com.lxf.recyclerhelper.listener.OnItemClickListener;
import com.lxf.recyclerhelper.listener.OnItemLongClickListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseQuickAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {
    /**
     * 数据
     */
    private List<T> data;
    /**
     * item布局id
     */
    private int layoutId;

    //-------------------------anim---------------------------
    /**
     * item显示的时候是否开启动画
     */
    private boolean animShow;
    /**
     * 是否只有在第一次item显示的时候开启动画
     */
    private boolean animFirstOnly;
    /**
     * 具体动画
     */
    private BaseAnimation animation;
    /**
     * 动画时长
     */
    private long animDuration = 300;
    /**
     * 动画插值器
     */
    private Interpolator animInterpolator = new LinearInterpolator();
    /**
     * 列表展示的最后一条item位置
     */
    private int lastPosition = -1;

    //-------------------------emptyView----------------------
    /**
     * 数据为空时展示的布局
     */
    private View emptyView;
    //-------------------------errorView----------------------
    /**
     * 加载失败时展示的布局
     */
    private View errorView;
    /**
     * 是否展示失败布局
     */
    private boolean loadError;


    private static final int HEADER_VIEW = 0x00000111;
    private static final int FOOTER_VIEW = 0x00000222;
    private static final int EMPTY_VIEW = 0x00000333;
    private static final int ERROR_VIEW = 0x00000444;

    private OnItemClickListener onItemClickListener;
    private OnItemChildClickListener onItemChildClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemChildLongClickListener onItemChildLongClickListener;

    public BaseQuickAdapter(@LayoutRes int layoutId, @Nullable List<T> data) {
        this.data = data == null ? new ArrayList<T>() : data;
        this.layoutId = layoutId;
    }

    public int getHeaderLayoutCount() {
        return 0;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        VH holder;
        switch (viewType) {
            case EMPTY_VIEW:
                holder = createBaseViewHolder(emptyView);
                break;
            case ERROR_VIEW:
                holder = createBaseViewHolder(errorView);
                break;
            default:
                holder = createBaseViewHolder(view);
                bindViewClickListener(holder);
                break;
        }

        holder.setAdapter(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case HEADER_VIEW:
                break;
            case EMPTY_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            case ERROR_VIEW:
                break;
            default:
                convert(holder, data.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (loadError || hasEmptyView())
            return 1;
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (loadError)
            return ERROR_VIEW;
        if (hasEmptyView()) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        int viewType = holder.getItemViewType();
        if (viewType != EMPTY_VIEW && viewType != ERROR_VIEW)
            addAnimation(holder);

        loadError = false;
    }

    protected abstract void convert(VH holder, T item);

    public void setNewData(@Nullable List<T> data) {
        this.data = data == null ? new ArrayList<T>() : data;
        lastPosition = -1;
        notifyDataSetChanged();
    }

    public void addData(@Nullable List<T> data) {
        if (data == null) return;
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void loadDataFail() {
        loadError = true;
        notifyDataSetChanged();
    }

    public void showWithAnimation(boolean anim) {
        animShow = anim;
    }

    public void showWithAnimation(boolean anim, BaseAnimation animation) {
        animShow = anim;
        this.animation = animation;
    }

    public void showAnimOnlyFirst(boolean first) {
        animFirstOnly = first;
    }

    /**
     * 是否显示空布局
     */
    public boolean hasEmptyView() {
        return emptyView != null && data.size() == 0;
    }

    public void setEmptyView(@LayoutRes int layoutId, ViewGroup viewGroup) {
        emptyView = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }

    public void setErrorView(@LayoutRes int layoutId, ViewGroup viewGroup) {
        errorView = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }

    private void bindViewClickListener(final BaseViewHolder baseViewHolder) {
        if (baseViewHolder == null) {
            return;
        }
        final View view = baseViewHolder.itemView;
        if (view == null) {
            return;
        }
        if (onItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(BaseQuickAdapter.this, v, baseViewHolder.getLayoutPosition());
                }
            });
        }
    }

    /**
     * 为item展现的时候添加动画
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (animShow) {
            if (!animFirstOnly || holder.getLayoutPosition() > lastPosition) {
                if (animation == null) {
                    animation = new ScaleInAnimation();
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    anim.setDuration(animDuration).start();
                    anim.setInterpolator(animInterpolator);
                }
                lastPosition = holder.getLayoutPosition();
            }
        }
    }

    public List<T> getData() {
        return data;
    }

    public OnItemChildClickListener getOnItemChildClickListener() {
        return onItemChildClickListener;
    }

    public OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return onItemChildLongClickListener;
    }

    public void setAnimDuration(long animDuration) {
        this.animDuration = animDuration;
    }

    public void setAnimInterpolator(Interpolator animInterpolator) {
        this.animInterpolator = animInterpolator;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener) {
        this.onItemChildLongClickListener = onItemChildLongClickListener;
    }

    /**
     * if you want to use subclass of BaseViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @param view view
     * @return new ViewHolder
     */
    protected VH createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        VH k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (VH) new BaseViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (VH) new BaseViewHolder(view);
    }

    /**
     * try to create Generic VH instance
     */
    @SuppressWarnings("unchecked")
    private VH createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get generic parameter VH
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && BaseViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

}
