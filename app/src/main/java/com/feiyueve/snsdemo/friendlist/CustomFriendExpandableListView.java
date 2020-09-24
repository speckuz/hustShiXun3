package com.feiyueve.snsdemo.friendlist;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.feiyueve.snsdemo.FriendInfActivity;
import com.feiyueve.snsdemo.inf.Friend;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomFriendExpandableListView extends ExpandableListView {
    private boolean mDragMode;

    private int mStartPosition;
    private int mDragPointOffset; //用于调整拖动视图位置


    private ImageView mDragView;

    private long startTime;
    private long endTime;

    private CustomDragAndDropListener mDragAndDropListener;
    private HashMap<String,ArrayList<Friend>> friendLists;
    private ArrayList<String> groupName;
    private LayoutInflater layoutInflater;
    private Context context;


    public CustomFriendExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setDragNDropListener(CustomDragAndDropListener l) {
        mDragAndDropListener = l;
    }

    public void setFriendLists(HashMap<String,ArrayList<Friend>> friendLists){
        this.friendLists = friendLists;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getActionIndex() != 0) {
            return super.onTouchEvent(ev);
        }

        final int action = ev.getAction();
        final int x = (int) ev.getX(0);
        final int y = (int) ev.getY(0);



        //&& x > getWidth() - 80
        if (action == MotionEvent.ACTION_DOWN ) { // 只拖动该项的右边部分，可以调整
            mDragMode = true;
            startTime = System.currentTimeMillis();
        }


        if (!mDragMode) {
            return super.onTouchEvent(ev);
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartPosition = pointToPosition(x, y);
                if (mStartPosition != INVALID_POSITION) {
                    int mItemPosition = mStartPosition - getFirstVisiblePosition();
                    mDragPointOffset = y - getChildAt(mItemPosition).getTop();
                    mDragPointOffset -= ((int) ev.getRawY()) - y;

                    if(ExpandableListView.getPackedPositionType(this.getExpandableListPosition(mStartPosition)) == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
                        return super.onTouchEvent(ev);
                    }
                    startDrag(mItemPosition, y);
                    drag(0, y);// 如果需要可以把0替换成:x
                }
                break;
            case MotionEvent.ACTION_MOVE:
                drag(0, y);// 如果需要可以把0替换成:x
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();
                if(ExpandableListView.getPackedPositionType(this.getExpandableListPosition(mStartPosition)) == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
                    return super.onTouchEvent(ev);
                }
                if ((endTime - startTime) < 0.1 * 1000L){
                    int packedGroupPosForm = ExpandableListView.getPackedPositionGroup(getExpandableListPosition(mStartPosition));
                    long itemId = getItemIdAtPosition(mStartPosition);
                    if((int) packedGroupPosForm == -1){
                        return super.onTouchEvent(ev);
                    }
                    if((int) itemId == -1){
                        return super.onTouchEvent(ev);
                    }
                    Friend friend = friendLists.get(groupName.get((int) packedGroupPosForm)).get((int) itemId);
                    Intent intent = new Intent(context, FriendInfActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("friend", friend);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            default:
                mDragMode = false;
                int endPosition = pointToPosition(x, y);
                stopDrag(mStartPosition - getFirstVisiblePosition());

                if (mDragAndDropListener != null && mStartPosition != INVALID_POSITION && endPosition != INVALID_POSITION) {
                    //调用实现接口的方法
                    mDragAndDropListener.onDrop(mStartPosition, endPosition);
                }
                break;
        }

        super.onTouchEvent(ev);
        return ev.getPointerCount() <= 1 || super.onTouchEvent(ev);

    }

    /**
     * 移动视图getHeight();
     * @param x 你的小手停放X轴的位置
     * @param y 如果拖动的ItemView，Y小于50（因为上方还有标题栏高度是50），列表开始向上移动，如果Y大于getHeight()-10.列表开始向下移动
     */
    private void drag(int x, int y) {
        if (mDragView != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mDragView.getLayoutParams();
            layoutParams.x = x;
            layoutParams.y = y - mDragPointOffset;
            WindowManager mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            mWindowManager.updateViewLayout(mDragView, layoutParams);

            if (mDragAndDropListener != null) {
                //调用实现接口的方法
                mDragAndDropListener.onDrag(x, y, this);
            }
        }
    }

    private void startDrag(int itemIndex, int y) {
        stopDrag(itemIndex);

        View item = getChildAt(itemIndex);
        if (item == null)
            return;

        if (mDragAndDropListener != null) {
            //调用实现接口的方法
            mDragAndDropListener.onStartDrag(item);
        }

        WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.gravity = Gravity.TOP;
        mWindowParams.x = 0;
        mWindowParams.y = y - mDragPointOffset;

        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = 0;

        Context context = getContext();

        // 创建一个绘图缓存的副本，以便它不被回收
        ImageView v = new ImageView(context);
        item.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
        item.setDrawingCacheEnabled(false);
        v.setBackgroundResource(android.R.color.holo_blue_dark);
        v.setImageBitmap(bitmap);

        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(v, mWindowParams);
        mDragView = v;
    }

    // 销毁拖动的视图
    private void stopDrag(int itemIndex) {
        if (mDragView != null) {
            if (mDragAndDropListener != null) {
                //调用实现接口的方法
                mDragAndDropListener.onStopDrag(getChildAt(itemIndex));
            }
            mDragView.setVisibility(GONE);
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(mDragView);
            mDragView.setImageDrawable(null);
            mDragView = null;
        }
    }

    /**
     *
     * @param flatPos
     * @return
     */
    public long getItemIdAtPosition(int flatPos) {
        if(-1!=flatPos) {
            long packedPos = getExpandableListPosition(flatPos);
            if (ExpandableListView.getPackedPositionType(packedPos) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                return getExpandableListAdapter().getGroupId(ExpandableListView.getPackedPositionGroup(packedPos));
            } else {
                return getExpandableListAdapter().getChildId(ExpandableListView.getPackedPositionGroup(packedPos), ExpandableListView.getPackedPositionChild(packedPos));
            }
        }else{
            return flatPos;
        }
    }

    public ArrayList<String> getGroupName() {
        return groupName;
    }

    public void setGroupName(ArrayList<String> groupName) {
        this.groupName = groupName;
    }
}