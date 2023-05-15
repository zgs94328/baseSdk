package com.zgs.myapplication

import android.annotation.SuppressLint
import androidx.arch.core.internal.FastSafeIterableMap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

/**
 * 文件名：LifeCyclerRegistry <br/>
 * 描述：
 *
 * @author zgs
 * @since 2023/05/11 14:02
 */
@SuppressLint("RestrictedApi")
class LifeCyclerRegistry(provider: LifecycleOwner) : Lifecycle() {
    private var mState: State
    private val mLifecycleOwner: WeakReference<LifecycleOwner> = WeakReference(provider)

    init {
        mState = State.INITIALIZED
    }

    private val mObserverMap =
        FastSafeIterableMap<LifecycleObserver, ObserverWithState>()

    override fun addObserver(observer: LifecycleObserver) {
        val initialState = if (mState == State.DESTROYED) State.DESTROYED else State.INITIALIZED
        val statefulObserver = ObserverWithState(observer as LifecycleEventObserver, initialState)
        val provious = mObserverMap.putIfAbsent(observer, statefulObserver)
        if (provious != null)
            return
        val lifecycleOwner = mLifecycleOwner.get()
        if (mLifecycleOwner.get() == null) {
            return
        }
        while (statefulObserver.mState < mState) {
            val event = Event.upFrom(statefulObserver.mState)
            statefulObserver.dispatchEvent(lifecycleOwner!!, event!!)
        }
    }

    override fun removeObserver(observer: LifecycleObserver) {
    }

    override fun getCurrentState(): State {
        return mState
    }

    class ObserverWithState(var observer: LifecycleEventObserver, var initalState: State) {
        var mState: State = initalState

        @SuppressLint("RestrictedApi")
        private var mLifecycleObserver: LifecycleEventObserver = observer
        fun dispatchEvent(owner: LifecycleOwner, event: Event) {
            mLifecycleObserver.onStateChanged(owner, event)
            mState = event.targetState
        }
    }
}