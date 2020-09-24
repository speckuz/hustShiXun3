package com.feiyueve.snsdemo.friendlist;


import android.view.View;
import android.widget.ListView;


public interface CustomDragAndDropListener {
    /**
     * 开始拖动时调用
     *
     * @param itemView 要拖动的视图
     */
    void onStartDrag(View itemView);

    /**
     * 当拖动时调用
     *
     * @param x        横坐标的位移事件。
     * @param y        垂直坐标的位移事件。
     * @param listView
     */
    void onDrag(int x, int y, ListView listView);

    /**
     * 当拖动停止时调用
     *
     * @param itemView 要拖动的视图
     */
    void onStopDrag(View itemView);

    /**
     * 当停止拖动时被调用，松开拖动
     *
     * @param flatPosFrom
     * @param flatPosTo
     */
    void onDrop(int flatPosFrom, int flatPosTo);
}
