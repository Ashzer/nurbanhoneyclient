package org.devjj.platform.nurbanhoney.core.platform

import android.util.Log

class DataLoadController<T> {

    private var nextDataSet: MutableList<T> = mutableListOf()
    private var _initialize: () -> Unit = {}
    private var _getNext: () -> Unit = {}
    private var _loadNext: () -> List<T> = { listOf() }

    constructor(initialize: () -> Unit, getNext: () -> Unit, loadNext: () -> List<T>) {
        _initialize = initialize
        _getNext = getNext
        _loadNext = loadNext
        log("constructor")
    }

    fun initialize(){
        _initialize()
        log("initData")
    }

    fun getNext(currentList: List<T>?) {
        _getNext()
        log("getNext")
    }
    fun loadNext() : List<T>{
        log("loadNext")
        return _loadNext()
    }

    private fun log(msg: String) {
        Log.d("Controller_check__", msg)
    }

}