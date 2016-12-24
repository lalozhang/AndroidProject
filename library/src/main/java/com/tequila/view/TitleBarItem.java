//package com.tequila.view;
//
//import android.content.Context;
//import android.content.res.ColorStateList;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import com.tequila.R;
//import com.tequila.utils.BitmapHelper;
//
//
///**
// * TitleBar item
// * @author steven
// */
//public class TitleBarItem extends LinearLayout {
//
//    private static final int DEFAULT_TEXT_SIZE = 20;
//
//    public TitleBarItem(Context context) {
//        super(context);
//
//    }
//
//    public TitleBarItem(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    /**
//     * 设置文本类型的Item
//     * @param resId
//     */
//    public void setTextTypeItem(int resId) {
//        this.removeAllViews();
//        this.setTextTypeItem(getContext().getString(resId));
//    }
//
//    /**
//     * 设置文本类型的Item
//     * @param res
//     */
//    public void setTextTypeItem(String res) {
//        this.removeAllViews();
//        this.setGravity(Gravity.CENTER); // 居中显示
//        TextView textView = new TextView(this.getContext());
//        textView.setTextColor(getResources().getColorStateList(R.drawable.titlebar_title_color_selector));
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, BitmapHelper.dip2px(this.getContext(), DEFAULT_TEXT_SIZE));
//        textView.setText(res);
//        textView.setPadding(BitmapHelper.dip2px(getContext(), 5), 0, BitmapHelper.dip2px(getContext(), 5), 0);
//        this.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        this.setClickable(true);
//    }
//
//    /**
//     * 设置SingleLine文本类型的Item
//     * @param res
//     */
//    public void setTextTypeItem(String res, int maxLength) {
//        this.removeAllViews();
//        this.setGravity(Gravity.CENTER); // 居中显示
//        TextView textView = new TextView(this.getContext());
//        textView.setTextColor(getResources().getColorStateList(R.drawable.titlebar_title_color_selector));
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, BitmapHelper.dip2px(this.getContext(), DEFAULT_TEXT_SIZE));
//        textView.setText(res.length() > maxLength ? res.substring(0, 4) : res);
//        textView.setSingleLine(true);
//        int padding = BitmapHelper.dip2px(getContext(), 5);
//        textView.setPadding(padding, 0, padding, 0);
//        this.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        if (res.length() < 3) {
//            this.setLayoutParams(new LayoutParams(BitmapHelper.dip2px(getContext(), 50),
//                    LayoutParams.MATCH_PARENT));
//        } else {
//            this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//                    LayoutParams.MATCH_PARENT));
//        }
//        this.setClickable(true);
//    }
//
//    /**
//     * 设置图片类型的Item
//     * @param resId
//     */
//    public void setImageTypeItem(int resId) {
//		setImageTypeItem(resId,50);
//    }
//
//	/**
//	 * 设置图片类型的Item
//	 * @param resId 资源id
//	 * @param marginLeft 左边距
//	 * @param marginRight 右边距
//	 */
//	public void setImageTypeItem(int resId,int marginLeft,int marginRight) {
//		this.removeAllViews();
//		this.setGravity(Gravity.CENTER); // 居中显示
//		ImageView imageView = new ImageView(this.getContext());
//		imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT));
//		imageView.setBackgroundResource(resId);
//		this.addView(imageView);
//		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.MATCH_PARENT);
//		layoutParams.setMargins(BitmapHelper.dip2px(getContext(),marginLeft),0,
//								BitmapHelper.dip2px(getContext(),marginRight),0);
//		this.setLayoutParams(layoutParams);
//		this.setClickable(true);
//	}
//
//	/**
//	 * 设置图片类型的Item
//	 * @param resId
//	 * @param width 图片类型的宽度 <= 0 为 WRAP_CONTENT
//	 */
//	public void setImageTypeItem(int resId,int width) {
//		this.removeAllViews();
//		this.setGravity(Gravity.CENTER); // 居中显示
//		ImageView imageView = new ImageView(this.getContext());
//		imageView.setBackgroundResource(resId);
//		this.addView(imageView, new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT));
//		if(width <= 0){
//			this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//					LayoutParams.MATCH_PARENT));
//		}else{
//			this.setLayoutParams(new LayoutParams(BitmapHelper.dip2px(getContext(),width),
//					LayoutParams.MATCH_PARENT));
//		}
//		this.setClickable(true);
//	}
//
//    /**
//     * 设置图片文字Item（图片在上，文字在下）
//     * @param textId
//     * @param iconId
//     */
//    public void setTextImageItem(int textId, int iconId) {
//        setTextImageItem(getResources().getString(textId), iconId);
//    }
//
//	/**
//	 * 设置图片文字Item（图片在上，文字在下）
//	 * @param textId
//	 * @param iconId
//	 */
//	public void setTextImageItem(String res, int iconId) {
//		setTextImageItem(res,iconId,50);
//	}
//
//    /**
//     * 设置图片文字Item（图片在上，文字在下）
//     * @param textId
//     * @param iconId
//	 * @param width 宽度 <= 0 为 WRAP_CONTENT
//     */
//    public void setTextImageItem(String res, int iconId,int width) {
//        this.removeAllViews();
//        this.setGravity(Gravity.CENTER); // 居中显示
//        TextView textView = new TextView(this.getContext());
//        textView.setTextColor(getResources().getColorStateList(R.drawable.titlebar_title_color_selector));
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, BitmapHelper.dip2px(this.getContext(), 10));
//        textView.setText(res);
//        textView.setCompoundDrawablesWithIntrinsicBounds(0, iconId, 0, 0);
//        this.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//		if(width <= 0){
//			this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//					LayoutParams.MATCH_PARENT));
//		}else{
//			this.setLayoutParams(new LayoutParams(BitmapHelper.dip2px(getContext(),width),
//					LayoutParams.MATCH_PARENT));
//		}
//        this.setClickable(true);
//    }
//
//    /**
//     * 设置自定义类型view的item
//     * @param view
//     */
//    public void setCustomViewTypeItem(View view) {
//        this.removeAllViews();
//        this.setGravity(Gravity.CENTER); // 居中显示
//        this.addView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.MATCH_PARENT));
//        this.setClickable(true);
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public void setEnabled(boolean enabled) {
//        for (int i = 0; i < this.getChildCount(); i++) {
//            View child = this.getChildAt(i);
//            if (child instanceof ImageView) {
//                ImageView iv = (ImageView) child;
//                Drawable drawable = iv.getBackground();
//                drawable.setAlpha(enabled ? 0 : 128);
//                iv.setBackgroundDrawable(drawable);
//                // ((ImageView) child).setEnabled(enabled);
//            } else if (child instanceof TextView) {
//                ((TextView) child).setEnabled(enabled);
//            }
//        }
//        super.setEnabled(enabled);
//    }
//
//	public void setEnabledByFixed(boolean enabled){
//		for (int i = 0; i < this.getChildCount(); i++) {
//			View child = this.getChildAt(i);
//			if (child instanceof ImageView) {
//				ImageView iv = (ImageView) child;
//				Drawable drawable = iv.getBackground();
//				drawable.setAlpha(enabled ? 255 : 128);
//				iv.setBackgroundDrawable(drawable);
//				// ((ImageView) child).setEnabled(enabled);
//			} else if (child instanceof TextView) {
//				((TextView) child).setEnabled(enabled);
//			}
//		}
//		super.setEnabled(enabled);
//	}
//
//    /**
//     * 设置文本类型的Item, 使item不可用时置灰
//     * @param res
//     */
//    public void setTextTypeItem(int resId, ColorStateList list) {
//        this.removeAllViews();
//        this.setGravity(Gravity.CENTER); // 居中显示
//        TextView textView = new TextView(this.getContext());
//        // textView.setTextColor(getResources().getColorStateList(com.Qunar.R.drawable.titlebar_title_color_selector));
//        textView.setTextColor(list);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, BitmapHelper.dip2px(this.getContext(), DEFAULT_TEXT_SIZE));
//        textView.setText(getContext().getString(resId));
//        int paddingValue = BitmapHelper.dip2px(getContext(), 10);
//        textView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
//        this.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        this.setClickable(true);
//    }
//
//}
