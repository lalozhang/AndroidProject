//package com.tequila.view;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
///**
// * 标题栏（替代TitleBar）
// */
//public class TitleBarNew extends LinearLayout {
//
//    private OnClickListener mBackListener;
//    private boolean hasBackBtn;
//    public TitleBarCenterItem barCenterItem;
//    private TitleBarItem[] rightBarItems;
//    private TitleBarItem[] leftBarItems;
//    public static final int STYLE_NORMAL = 0;
//    public static final int STYLE_BLUE = STYLE_NORMAL + 1;
//    public static final int STYLE_GRAY = STYLE_BLUE + 1;
//    public static final int STYLE_TRANSPARENT = STYLE_GRAY + 1;
//
//    /** 0-normal,1-blue ,2-gray */
//    private int style = STYLE_NORMAL;
//
//    /***
//     * 帧布局根部
//     */
//    @From(R2.id.frame_root)
//    private FrameLayout frameRoot;
//
//    /***
//     * 子标题
//     */
//    @From(R.id.tv_sub_title)
//    private TextView subTitle;
//
//    /***
//     * 左侧区域
//     */
//    @From(R.id.llLeftArea)
//    private LinearLayout llLeftArea; // 左侧按钮
//    /***
//     * 左侧返回按钮区域
//     */
//    @From(R.id.ll_left_area)
//    private LinearLayout llBackArea; // 左侧返回按钮
//
//    /***
//     * 左侧功能区域区域
//     */
//    @From(R.id.llBarItemsArea)
//    private LinearLayout llBarItemsArea;
//
//    /***
//     * 返回按钮
//     */
//    @From(R.id.icon_back)
//    public ImageView iconBack;
//
//    /***
//     * 右侧区域
//     */
//    @From(R.id.ll_right_area)
//    private LinearLayout llRight; // 右侧区域
//
//    /***
//     * 右侧功能区域
//     */
//    @From(R.id.ll_right_function_area)
//    private LinearLayout llRightFunctionArea;
//
//    /********************* 搜索专用begin *************************/
//    /***
//     * 搜索区域
//     */
//    @From(R.id.ll_right_search_area)
//    private LinearLayout llRightSearchArea;
//    /***
//     * 搜索筐
//     */
//    @From(R.id.title_etSearch)
//    private EditText etSearch; // 搜索筐
//    /***
//     * 搜索框删除按钮
//     */
//    @From(R.id.title_ivDelete)
//    private ImageView ivDelete; // 搜索框删除按钮
//    /***
//     * 搜索按钮
//     */
//    @From(R.id.title_btnSearch)
//    private Button btnSearch; // 搜索按钮
//	public Button getBtnSearch(){
//		return btnSearch;
//	}
//    /***
//     * searching view hint
//     */
//    @From(R.id.title_progressCircle)
//    private ProgressBar waitingProgressBar;// searching view hint
//
//    /********************* 搜索专用end ************************/
//
//    public TitleBarNew(Context context) {
//        this(context, null);
//    }
//
//    public TitleBarNew(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initView(context);
//    }
//
//    private void initView(Context context) {
//        LayoutInflater.from(context).inflate(R.layout.title_bar_layout, this, true);
//        Injector.inject(this);
//    }
//
//    /***
//     * @param hasBackBtn
//     * @param barItems
//     * @author shutao.xiang
//     * @since 2014年1月6日上午11:13:24
//     */
//    public void setTitleBar(boolean hasBackBtn, TitleBarCenterItem barCenterItem, TitleBarItem... barItems) {
//		setTitleBar(hasBackBtn, null, barCenterItem, barItems);
//    }
//
//	public void setTitleBar(boolean hasBackBtn,TitleBarItem[]  leftBarItems, TitleBarCenterItem barCenterItem, TitleBarItem... rightBarItems) {
//		this.hasBackBtn = hasBackBtn;
//		this.barCenterItem = barCenterItem;
//		this.leftBarItems = leftBarItems;
//		this.rightBarItems = rightBarItems;
//		llRightSearchArea.setVisibility(GONE);
//		llRightFunctionArea.setVisibility(VISIBLE);
//		reLayout();
//	}
//
//    public void setBackButtonClickListener(OnClickListener listener) {
//        mBackListener = listener;
//        if (mBackListener != null) {
//            llBackArea.setOnClickListener(mBackListener);
//        } else {
//            llBackArea.setOnClickListener(new QOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					((BaseActivity) v.getContext()).onBackPressed();
//				}
//			}));
//        }
//    }
//
//    /**
//     * <pre>
//     * 设置自定义titleBarStyle
//     * for booking.com旗舰店
//     * </pre>
//     * @param style 0-normal,1-blue
//     */
//    public void setTitleBarStyle(int style) {
//        this.style = style;
//        reLayout();
//    }
//
//    /**
//     * 设置标题栏(用于搜索)
//     * @param listener for btnSearch && ivDelete
//     */
//    public TitleBarNew setTitleBarForSearch(OnClickListener listener, boolean hasBackBtn, boolean hasSearchBtn) {
//        this.hasBackBtn = hasBackBtn;
//        llRightSearchArea.setVisibility(VISIBLE);
//        llRightFunctionArea.setVisibility(GONE);
//        btnSearch.setOnClickListener(listener);
//        ivDelete.setOnClickListener(listener);
//        if (!hasSearchBtn) {
//            btnSearch.setVisibility(GONE);
//        }
//        reLayout();
//        return this;
//    }
//
//    /**
//     * 返回输入框
//     * @return
//     */
//    public EditText getSearchEditText() {
//        return etSearch;
//    }
//
//    public ImageView getDeleteButton() {
//        return ivDelete;
//    }
//
//    public void reLayout() {
//        int background = getResources().getColor(R.color.titlebar_background_color);
//        if (style == STYLE_BLUE) {
//            background = getResources().getColor(R.color.titlebar_background_color_blue);
//        } else if (style == STYLE_GRAY) {
//            background = getResources().getColor(R.color.titlebar_background_color_gray);
//        } else if (style == STYLE_TRANSPARENT) {
//            background = getResources().getColor(R.color.titlebar_background_color_transparent);
//        } else {
//            background = getResources().getColor(R.color.titlebar_background_color);
//        }
//        setBackgroundColor(background);
//        // 左边区域
//		// 移除左边区域功能区域
//		llBarItemsArea.removeAllViews();
//        if (hasBackBtn) {
//			llBackArea.setVisibility(VISIBLE);
//			setBackButtonClickListener(mBackListener);
//        }else{
//			llBackArea.setVisibility(GONE);
//		}
//		if (!QArrays.isEmpty(leftBarItems)) { // 右侧有操作按钮
//			for (int i = 0; i < leftBarItems.length; i++) {
//				llBarItemsArea.addView(leftBarItems[i]);
//			}
//		}
//
//		ViewUtils.setOrGone(llBarItemsArea,llBarItemsArea.getChildCount() > 0);
//
//        // 右边区域
//		// 移除右边区域功能区域
//		llRightFunctionArea.removeAllViews();
//        if (rightBarItems != null && rightBarItems.length > 0) { // 右侧有操作按钮
//            int barItemsLength = rightBarItems.length;
//            for (int i = 0; i < barItemsLength; i++) {
//                llRightFunctionArea.addView(rightBarItems[i], i);
//            }
//            llRightFunctionArea.setVisibility(VISIBLE);
//        } else {// 右侧没有操作按钮
//            llRightFunctionArea.setVisibility(GONE);
//        }
//
//		llLeftArea.post(new Runnable() {
//			@Override
//			public void run() {
//				// 移除index=1的区域
//				try {
//					frameRoot.removeViewAt(1);
//				} catch (Exception e) {
//					// QLog.e(QLog.TAO, "frameRoot.removeView(1) ", e);
//				}
//				// 有中间区域
//				if (barCenterItem != null) {
//					int maxWidth = computeMaxLeftRightWidth();
//					int width = frameRoot.getWidth();
//					// QLog.d(QLog.TAO, "frameRoot.getWidth() = " + width);
//					FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width - 2 *
//							maxWidth, FrameLayout.LayoutParams.MATCH_PARENT);
//					layoutParams.gravity = Gravity.CENTER;
//					frameRoot.addView(barCenterItem, layoutParams);
//				}
//			}
//		});
//    }
//
//    private int computeMaxLeftRightWidth() {
//        int widthOfLeft = llLeftArea.getWidth();
//        int widthOfRight = llRightFunctionArea.getWidth();
//        if (llRightSearchArea.getVisibility() == VISIBLE) {
//            widthOfRight = llRightSearchArea.getWidth();
//        }
//        QLog.d(QLog.TAO, "computeMaxLeftRightWidth widthOfLeft = " + widthOfLeft + ",widthOfRight = " + widthOfRight);
//
//        return Math.max(widthOfLeft, widthOfRight);
//    }
//
//    public void showProgress() {
//        waitingProgressBar.setVisibility(View.VISIBLE);
//    }
//
//    public void closeProgress() {
//        waitingProgressBar.setVisibility(View.GONE);
//    }
//
//    public void setTitle(String title) {
//        if (barCenterItem != null && barCenterItem.getMode() == TitleBarCenterItem.MODE_TEXT) {
//            barCenterItem.setContent(title);
//            barCenterItem.requestRelayout();
//        }
//    }
//
//    public void setSmallTitle(String smallTitle) {
//        this.subTitle.setVisibility(View.VISIBLE);
//        this.subTitle.setText(smallTitle);
//    }
//
//    public ImageView getBackImageView() {
//        return iconBack;
//    }
//
//    public TitleBarCenterItem getBarCenterItem() {
//        return this.barCenterItem;
//    }
//
//}
