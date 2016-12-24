package com.tequila.dlg;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tequila.R;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by admin on 2016/12/24.
 */

public class ProgressDialogFragment extends DialogFragment implements Observer {

    public static final String TAG = ProgressDialogFragment.class.getSimpleName();


    private TextView tvMessage;
    private ImageButton btnCancel;
    private DialogInterface.OnCancelListener mCancelListener;
    private final ObservedString mMessage = new ObservedString();

    public static ProgressDialogFragment newInstance(String message, boolean cancelable,
                                                      DialogInterface.OnCancelListener cancelListener) {
        ProgressDialogFragment frag = new ProgressDialogFragment();
        frag.setCancelable(cancelable);
        frag.setCancelListener(cancelListener);
        Bundle args = new Bundle();
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch(IllegalStateException ise) {
            Log.e(TAG,ise.getMessage());
            try {
                FragmentTransaction ft = manager.beginTransaction();
                ft.commitAllowingStateLoss();
            } catch(IllegalStateException e) {
                Log.e(TAG,e.getMessage());
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMessage.addObserver(this);
        Bundle myBundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        if (myBundle == null) {
            myBundle = new Bundle();
        }
        String msg = myBundle.getString("message");
        mMessage.setText(msg == null ? getString(R.string.state_loading) : msg);
    }

    @Override
    public void onDestroy() {
        mMessage.deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public int getTheme() {
        return R.style.Theme_Dialog_Router;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog onCreateDialog = super.onCreateDialog(savedInstanceState);
        onCreateDialog.setContentView(R.layout.loading_dialog);
        tvMessage = (TextView) onCreateDialog.findViewById(android.R.id.message);
        btnCancel = (ImageButton) onCreateDialog.findViewById(android.R.id.button2);
        if (isCancelable()) {
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getDialog() != null) {
                        getDialog().cancel();
                    }
                }
            });
        } else {
            btnCancel.setVisibility(View.INVISIBLE);
        }
        return onCreateDialog;
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        if (btnCancel == null) {
            return;
        }
        if (cancelable) {
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getDialog() != null) {
                        getDialog().cancel();
                    }
                }
            });
        } else {
            btnCancel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        DialogInterface.OnCancelListener cancelListener = getCancelListener();
        if (cancelListener != null) {
            cancelListener.onCancel(dialog);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", mMessage.toString());
    }

    public void setMessage(CharSequence message) {
        mMessage.setText(message);
    }

    public DialogInterface.OnCancelListener getCancelListener() {
        return mCancelListener;
    }

    public void setCancelListener(DialogInterface.OnCancelListener cancelListener) {
        this.mCancelListener = cancelListener;
    }

    private static class ObservedString extends Observable implements CharSequence {

        private CharSequence text;

        @Override
        public CharSequence subSequence(int start, int end) {
            return getText().subSequence(start, end);
        }

        @Override
        public int length() {
            return getText().length();
        }

        @Override
        public char charAt(int index) {
            return getText().charAt(index);
        }

        CharSequence getText() {
            return text;
        }

        void setText(CharSequence text) {
            this.text = text;
            setChanged();
            notifyObservers(text);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (this.text == null ? 0 : this.text.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ObservedString other = (ObservedString) obj;
            if (this.text == null) {
                if (other.text != null) {
                    return false;
                }
            } else if (!this.text.equals(other.text)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return text.toString();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (tvMessage != null) {
            tvMessage.setText((CharSequence) data);
        }
    }


}
