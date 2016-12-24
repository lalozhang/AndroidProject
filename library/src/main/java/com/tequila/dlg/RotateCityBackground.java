package com.tequila.dlg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.tequila.R;
import com.tequila.utils.BitmapHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2016/12/24.
 */

public class RotateCityBackground extends View {

    /** 重回消息的id */
    private static final int REDRAW_MSG_ID = 1;
    /** 旋转角度增量 ，单位度 */
    private static final int DEGREE_ADD_NUM = 1;
    /** 重绘间隔时间，单位毫秒 */
    private static final int REDRAW_TIME_DISTANCE = 100;

    /** 上下文对象 */
    private final Context context;

    private static PorterDuffXfermode duffXfermod;

    /** 城市背景 */
    private static Bitmap cityBackground;
    /** 蒙板层，用于遮挡不可见部分 */
    private static Bitmap maskLayer;

    /** 画笔 */
    private Paint paint;

    /** 旋转矩阵，通过该矩阵绘制不同角度的图片，产生旋转效果 */
    private Matrix rotateMatrix;
    /** 旋转角度 */
    private int rotateDegree;

    /** 重绘Hander */
    private ReDrawHandler reDrawHandler;
    private Timer timer;
    private boolean isDetached = false ;

    static {
        duffXfermod = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    /**
     * 构造方法
     *
     * @param context 上下文对象
     * @param attrs AttributeSet对象
     */
    public RotateCityBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initSomeObjects();

        loadBitMapResources();

        executeScheduleReDrawTask();
    }

    /**
     * 初始化一些对象
     */
    private void initSomeObjects() {
        reDrawHandler = new ReDrawHandler();

        rotateMatrix = new Matrix();

        paint = new Paint();
        // 设置抗锯齿，防抖动效果
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    /**
     * 加载位图资源
     */
    private void loadBitMapResources() {
        if (cityBackground == null) {
            cityBackground = BitmapFactory.decodeResource(getResources(), R.drawable.city_background);
        }

        if (maskLayer == null) {
            // 创建和城市背景一样大小的蒙板图片
            Bitmap srcMask = BitmapFactory.decodeResource(getResources(), R.drawable.city_background_mask);
            maskLayer = Bitmap.createScaledBitmap(srcMask, cityBackground.getWidth(), cityBackground.getHeight(),
                    false);
        }
    }

    /**
     * 执行周期的重绘制任务
     */
    private void executeScheduleReDrawTask() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new ReDrawTask(), 0, REDRAW_TIME_DISTANCE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int sc = canvas.saveLayer(0, 0, cityBackground.getWidth(), BitmapHelper.dip2pxF(context, 130), null,
                Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas
                        .FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        // 绘制背景
        canvas.drawBitmap(cityBackground, rotateMatrix, paint);

        // 绘制蒙板
        paint.setXfermode(duffXfermod);
        canvas.drawBitmap(maskLayer, 0, BitmapHelper.dip2pxF(context, 28), paint);
        paint.setXfermode(null);
        canvas.restoreToCount(sc);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(cityBackground.getWidth(), BitmapHelper.dip2px(context, 65));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDetached = true ;
        synchronized (RotateCityBackground.class) {
            if (reDrawHandler != null) {
                reDrawHandler.removeCallbacksAndMessages(null);
                reDrawHandler = null;
            }
        }
        //当当前页面不显示的时候，调用该方法，取消任务
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }

    }

    /**
     * 重绘任务类，以指定的时间间隔执行该任务，修改图片的角度，重绘
     *
     * @author chengxiang.peng
     */
    private class ReDrawTask extends TimerTask {

        @Override
        public void run() {
            if(!isDetached){
                updateRorateMatrix();
                sendReDrawMessage();
            }
        }

        /**
         * 发送重绘消息
         */
        private void sendReDrawMessage() {
            synchronized (RotateCityBackground.class){
                if(reDrawHandler != null){
                    Message message = Message.obtain();
                    message.what = REDRAW_MSG_ID;
                    reDrawHandler.sendMessage(message);
                }
            }
        }

        /**
         * 更新旋转矩阵
         */
        private void updateRorateMatrix() {
            // 更新旋转角度
            if (rotateDegree == 360) {
                rotateDegree = 0;
            }
            rotateDegree += DEGREE_ADD_NUM;
            rotateMatrix.setTranslate(0, BitmapHelper.dip2pxF(context, 18));
            rotateMatrix.preRotate(rotateDegree, cityBackground.getWidth() / 2, cityBackground.getHeight() / 2);
        }
    }

    /**
     * 处理重绘消息Hander，接收到重绘消息刷新调用OnDraw()方法重绘制
     * @author chengxiang.peng
     */
    private class ReDrawHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REDRAW_MSG_ID:
                    invalidate();
                    break;
                default:
                    break;
            }
        }
    }
}
