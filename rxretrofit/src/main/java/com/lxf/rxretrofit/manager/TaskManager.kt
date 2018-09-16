package com.lxf.rxretrofit.manager

import io.reactivex.disposables.Disposable


internal object TaskManager {
    private val tasks = HashMap<String,Disposable>()

    fun cancel(tag:String){
        if (hasTask(tag)){
            tasks[tag]?.dispose()
            tasks.remove(tag)
        }
    }

    fun cancelAll(){
        tasks.forEach { _, u ->
            u.dispose()
        }
        tasks.clear()
    }

    fun addTask(tag: String,disposable: Disposable){
        if (hasTask(tag))
            throw RuntimeException("already have a task which tag is $tag")
        else
            tasks[tag] = disposable
    }

    fun hasTask(tag:String) = tasks.containsKey(tag)
}