//package com.tequila.view;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.Qunar.R;
//
///**
// * 标题栏中间区域view
// * @author shutao.xiang
// * @since 2014年2月11日下午12:04:37
// */
//public class TitleBarCenterItem extends LinearLayout {
//
//    private float titleMaxTextSize = -1;
//    private float titleMinTextSize = -1;
//    private int textColor = Color.TRANSPARENT;
//    private String content;
//    private View customView;
//    public static final int MODE_TEXT = 0;
//    public static final int MODE_CUSTOM_VIEW = 1;
//    private int mode = MODE_TEXT;
//    private int innerGravity = Gravity.NO_GRAVITY;
//
//    public TitleBarCenterItem(Context context) {
//        this(context, MODE_TEXT);
//    }
//
//    public TitleBarCenterItem(Context context, int mode) {
//        this(context, mode, null);
//    }
//
//    public TitleBarCenterItem(Context context, int mode, AttributeSet attrs) {
//        super(context, attrs);
//        this.mode = mode;
//    }
//
//    public void setInnerGravity(int innerGravity) {
//        this.innerGravity = innerGravity;
//    }
//
//    public void setTitleMaxTextSize(float titleMaxTextSize) {
//        this.titleMaxTextSize = titleMaxTextSize;
//    }
//
//    public void setTitleMinTextSize(float titleMinTextSize) {
//        this.titleMinTextSize = titleMinTextSize;
//    }
//
//    public String getContent() {
//        return this.content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public void setTextColor(int textColor) {
//        this.textColor = textColor;
//    }
//
//    public int getMode() {
//        return this.mode;
//    }
//
//	public void requestRelayout() {
//		requestRelayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//	}
//
//    /**
//     * 更新控件内容
//     * @author shutao.xiang
//     * @since 2014年2月11日下午5:32:31
//     */
//    public synchronized void requestRelayout(int widthLayoutParam,int heightLayoutParam) {
//        this.removeAllViews();
//        this.setGravity(Gravity.CENTER); // 居中显示
//        if (mode == MODE_CUSTOM_VIEW) {
//            if (customView != null) {
//				try {
//					this.removeView(customView);
//				} catch (Exception e) {
//					//
//				}
//				this.addView(customView, new LayoutParams(widthLayoutParam,heightLayoutParam));
//                this.setClickable(true);
//            }
//        } else {
//            View inflateView = inflate(getContext(), R.layout.titlebar_center_content_layout, null);
//            if (inflateView != null && inflateView instanceof AutoScaleTextView) {
//                AutoScaleTextView autoScaleTextView = (AutoScaleTextView) inflateView;
//                if (titleMaxTextSize != -1) {
//                    autoScaleTextView.setMaxTextSize(titleMaxTextSize);
//                }
//                if (titleMinTextSize != -1) {
//                    autoScaleTextView.setMinTextSize(titleMinTextSize);
//                }
//                if (TextUtils.isEmpty(content)) {
//                    return;
//                }
//                if (textColor != Color.TRANSPARENT) {
//                    autoScaleTextView.setTextColor(textColor);
//                }
//                if (innerGravity != Gravity.NO_GRAVITY) {
//                    autoScaleTextView.setGravity(innerGravity);
//                }
//                autoScaleTextView.setText(content);
//                this.addView(autoScaleTextView, new LayoutParams(LayoutParams.MATCH_PARENT,
//                        LayoutParams.MATCH_PARENT));
//
//            }
//        }
//
//    }
//
//    /**
//     * 设置自定义
//     * @param customView
//     * @author shutao.xiang
//     * @since 2014年2月11日下午12:19:07
//     */
//    public void setCustomView(View customView) {
//        this.customView = customView;
//    }
//
//}
