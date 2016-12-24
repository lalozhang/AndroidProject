package com.tequila.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import com.tequila.net.IServiceMap;

/**
 * Created by admin on 2016/10/2.
 */
public interface IBaseActFrag {
    String MERGED_TAG = "MERGED_DIALOG_TAG";

    Context getContext();

    FragmentManager getV4FragmentManager();

    Handler getHandler();

    void onShowProgress(String message, boolean cancelAble, DialogInterface.OnCancelListener cancelListener);

    void onCloseProgress(String message);

    void addMergeServiceMap(IServiceMap... serviceMaps);
}
