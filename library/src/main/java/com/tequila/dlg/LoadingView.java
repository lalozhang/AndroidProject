package com.tequila.dlg;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tequila.R;

/**
 * 网络加载指示器
 *
 */
public class LoadingView extends FrameLayout {
    /** 行走的骆驼 */
    private ImageView workingCamel;

    /**
     * 构造方法
     * @param context 上行下对象
     * @param attrs AttributeSet对象
     */
    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);

		if(!isInEditMode()){
			inflaterNetWorkLoadingIndicatorLayout(context);

			startWorkingCamelAnimation();
		}
    }

    /**
     * 解析网络加载视图的布局
     * @param context
     *            上下文对象
     */
    private void inflaterNetWorkLoadingIndicatorLayout(Context context) {
        // 解析加载视图布局
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.loading_view, this);

        // 获取控件
        workingCamel = (ImageView) findViewById(R.id.loading_view_camel);
    }

    /**
     * 启动行走的骆驼动画
     */
    public void startWorkingCamelAnimation() {
        // 启动行走骆驼帧动画
        final AnimationDrawable animationDrawable = (AnimationDrawable) workingCamel.getBackground();
		workingCamel.post(new Runnable() {
			public void run() {
				animationDrawable.start();
			}
		});
    }
}
