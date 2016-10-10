package com.tequila;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

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
