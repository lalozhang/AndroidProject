package com.tequila.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tequila.dlg.ProgressDialogFragment;
import com.tequila.net.IServiceMap;
import com.tequila.net.NetworkListener;
import com.tequila.net.NetworkManager;
import com.tequila.net.NetworkParam;
import com.tequila.net.Request;
import com.tequila.utils.CheckUtils;
import com.tequila.utils.HandlerCallbacks;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;


public abstract class BaseActivity extends FragmentActivity implements NetworkListener, OnClickListener,
        OnLongClickListener, OnItemClickListener, OnItemLongClickListener, IBaseActFrag {
    protected Handler mHandler;
    protected Bundle myBundle;

//    protected TitleBarNew mTitleBar;
    protected ViewGroup mRoot;
    private FrameLayout mAndroidContent;
    private boolean blockTouch;
    protected boolean fromRestore = false;
    private boolean mIsFirstResume = true;
    private HandlerCallbacks.CommonCallback hcb;
    public static final String INTENT_TO_ACTIVITY = "intent_to";
	public static final String EXTRA_FROM_ACTIVITY = "__FROM_ACTIVITY__";

	protected ArrayList<IServiceMap> mergeServiceMapList ;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Display display = ImageUtils.getScreenDisplay(this);
//        QunarApp.screenWidth = display.getWidth();
//        QunarApp.screenHeight = display.getHeight();
//        /*
//         * 不要随意改这个值，如需更改请全面测试所有业务
//         */
//        TextDrawable.defaultTextSize = 12;
//        UpgradeActivity upg = (UpgradeActivity) QunarApp.getContext().getActiveContext(UpgradeActivity.class);
//        if (!(null == upg || upg.isBackPress || this instanceof UpgradeActivity)) {
//            Intent upgIntent = upg.getIntent();
//            upgIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//            startActivity(upgIntent);
//        } else if (needLoginRequset()) {
//            startClientLoginRequest(QunarApp.getContext());
//        }

        mHandler = new Handler(hcb = new HandlerCallbacks.ActivityCallback(this, genCallback()));
        if (savedInstanceState != null) {
            fromRestore = true;
        }
        myBundle = savedInstanceState == null ? getIntent().getExtras() : savedInstanceState;
        if (myBundle == null) {
            myBundle = new Bundle();
        }
        mIsFirstResume = myBundle.getBoolean("mIsFirstResume", true);
        blockTouch = myBundle.getBoolean("blockTouch");
		mergeServiceMapList = (ArrayList<IServiceMap>) myBundle.getSerializable("mergeServiceMapList");

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * 设置rootview，默认是linearlayout，子类可以自定义类型，使titlebar可以显示不同的样式（如浮动）
     * @return
     */
    public ViewGroup genRootView() {
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    /**
     * 设置realroot，默认是linearlayout，子类可以自定义类型
     * @return
     */
    public ViewGroup genRealRootView() {
        final FrameLayout linearLayout = new FrameLayout(this);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    public void setContentView(View view, boolean autoInject) {
        final ViewGroup realRoot = genRealRootView();
        mRoot = genRootView();
//        mTitleBar = new TitleBarNew(this);
//        mRoot.addView(mTitleBar, -1, -2);
        mRoot.addView(view, -1, -1);
        realRoot.addView(mRoot, -1, -1);
        super.setContentView(realRoot);
//        mTitleBar.setVisibility(View.GONE);
        if (autoInject) {
            ButterKnife.bind(this);
        }
    }

    public void setContentView(int layoutResID, boolean autoInject) {
        final View content = getLayoutInflater().inflate(layoutResID, null);
        setContentView(content, autoInject);
    }


    @Override
    public void setContentView(int layoutResID) {
        setContentView(layoutResID, true);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, true);
    }



    public void openSoftinput(final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 498);
    }





    @Override
    public void onNetEnd(NetworkParam param) {
        if (param.block) {
            onCloseProgress(param);
        }
    }

    @Override
    public void onNetError(final NetworkParam param, int errCode) {
        // showToast("error code :" + errCode + "\nparam:" + param.toString());
        if (param.block) {
            new AlertDialog.Builder(this).setTitle("提示")//
                    .setMessage(errCode == HandlerCallbacks.MESSAGE_ERROR ?"网络异常，请检查网络":"服务器异常，请稍后再试")//
                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Request.startRequest(param, mHandler);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            onCloseProgress(param);
        }
    }

    @Override
    public void onMsgSearchComplete(final NetworkParam param) {

    }

    @Override
    public void onNetCancel() {

    }

    @Override
    public void onNetStart(final NetworkParam param) {
        if (param.block) {
            onShowProgress(param);
        }
    }

    @Override
    public void onShowProgress(final NetworkParam networkParam) {
		String tag = MERGED_TAG;
		//目的是为了合并多个请求,期间不会多次弹出加载框
		if(mergeServiceMapList == null || !mergeServiceMapList.contains(networkParam.key)) {
			tag = networkParam.toString();
		}

        ProgressDialogFragment progressDialog = (ProgressDialogFragment) getSupportFragmentManager()
                .findFragmentByTag(tag);
		OnCancelListener cancelListener = new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				NetworkManager.getInstance().cancelTaskByParam(networkParam);
				onNetCancel();
			}
		};

        if (progressDialog == null) {
            progressDialog = ProgressDialogFragment.newInstance(networkParam.progressMessage, networkParam.cancelAble,
					cancelListener);
            progressDialog.show(getSupportFragmentManager(), tag);
        } else {
            progressDialog.setMessage(networkParam.progressMessage);
            progressDialog.setCancelable(networkParam.cancelAble);
			progressDialog.setCancelListener(cancelListener);
        }
    }

    @Override
    public void onShowProgress(String message, boolean cancelAble, OnCancelListener cancelListener) {
        ProgressDialogFragment progressDialog = ProgressDialogFragment.newInstance(message, cancelAble,
                cancelListener);
        progressDialog.show(getSupportFragmentManager(), message);
    }

    @Override
    public void onCloseProgress(NetworkParam networkParam) {
		if(mergeServiceMapList != null) {
			for(int i = 0; i < mergeServiceMapList.size() ; i++) {
				IServiceMap serviceMap = mergeServiceMapList.get(i);
				if (networkParam.key == serviceMap) {
					if(i == mergeServiceMapList.size() - 1) {
						onCloseProgress(MERGED_TAG);
						mergeServiceMapList = null;
					} else {
						if(networkParam.result == null || networkParam.result.code != 0) {
							//请求失败(可能是调用了onNetError或者onNetCancel),这时候要把block窗口关闭了
							onCloseProgress(MERGED_TAG);
						}
					}
					return;
				}
			}
		}

        onCloseProgress(networkParam.toString());
    }

    @Override
    public void onCloseProgress(String message) {
        ProgressDialogFragment progressDialog = (ProgressDialogFragment) getSupportFragmentManager()
                .findFragmentByTag(message);
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (myBundle != null) {
            outState.putAll(myBundle);
        }
        super.onSaveInstanceState(outState);
        outState.putBoolean("blockTouch", blockTouch);
        outState.putBoolean("mIsFirstResume", mIsFirstResume);
		outState.putSerializable("mergeServiceMapList",mergeServiceMapList);
    }

	/**
	 * 哪些block的请求可以合并,就调用这个方法,传入需要合并的请求,再不清楚的在群里问
	 * @param serviceMaps
	 */
	public void addMergeServiceMap(IServiceMap... serviceMaps) {
		mergeServiceMapList = new ArrayList<IServiceMap>(3);
		if(!CheckUtils.isEmpty(serviceMaps)) {
			mergeServiceMapList.addAll(Arrays.asList(serviceMaps));
		}
	}

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        blockTouch = true;
		intent.putExtra(EXTRA_FROM_ACTIVITY,getClass().getSimpleName());
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        blockTouch = true;
		intent.putExtra(EXTRA_FROM_ACTIVITY,getClass().getSimpleName());
        super.startActivityFromFragment(fragment, intent, requestCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsFirstResume) {
            onFirstResume();
        } else {
            onRegularResume();
        }

        mAndroidContent = (FrameLayout) getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mAndroidContent.setForegroundGravity(Gravity.FILL);
        if (fromRestore) {
            fromRestore = false;
        }
        blockTouch = false;
    }

    protected void onRegularResume() {
    }

    protected void onFirstResume() {
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

	@Override
	public void finish() {
		super.finish();
		// onDestroy，导致网络请求监听未被移除
		NetworkManager.getInstance().cancelTaskByHandler(mHandler);
	}

	@Override
    protected void onDestroy() {
        super.onDestroy();

        if (hcb != null) {
            hcb.removeListener();
        }
        NetworkManager.getInstance().cancelTaskByHandler(mHandler);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    /**
     * 如果想处理,网络请求以外的消息,可以实现这个方法,返回自定义的callback即可
     * @return
     */
    protected Callback genCallback() {
        return null;
    }







    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
    }

    @SuppressLint("NewApi")
    public void killCurrentProgress() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT > 7) {
            manager.killBackgroundProcesses(getPackageName());
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 关掉软键盘
     */
    public void hideSoftInput() {
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    @Override
    public BaseActivity getContext() {
        return this;
    }

    @Override
    public FragmentManager getV4FragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }


    /**
     * Return if the activity is that invoked this activity. This is who the data in setResult() will be sent to.
     * Note: if the calling activity is not expecting a result (that is it did not use the startActivityForResult form
     * that includes a request code), then this method will return false
     * @param clazz
     * @return
     */
    public boolean fromActivity(Class<? extends Activity> clazz) {
        try {
            return clazz.getName().equals(((Activity) getContext()).getCallingActivity().getClassName());
        } catch (Exception e) {
            return false;
        }
    }

	/**
	 * 获得来源Activity的名字，有可能为null
	 * @return
	 */
	public String fromActivityName(){
		return myBundle.getString(EXTRA_FROM_ACTIVITY);
	}


//    public TitleBarNew getTitleBar() {
//        return mTitleBar;
//    }

	public void onCacheHit(NetworkParam param) {
		//do nothing
	}

	/**
	 * 尝试做finish操作,先检查当前Activity是否有添加的Fragment到back栈,如果有则执行弹栈操作
	 * 之类若想处理onBackPressed事件,应在onBackPressed方法内调用此方法做一次检查
	 * @return true 表示back栈中没有添加过Fragment,back响应事件交给当前Activity false 执行弹栈操作,当前Activity不响应back事件
	 */
	public boolean tryDoBack() {
		boolean canFinish = true;
		try {
			FragmentManager fm = getSupportFragmentManager();
			if(fm.getBackStackEntryCount() > 0) {
				//判断当前back栈中有Fragment才进行弹栈操作,避免异常发生,fuck
				canFinish = !fm.popBackStackImmediate();
			}
		} catch(IllegalStateException ise) {
			;
		}
		return canFinish;
	}
}
