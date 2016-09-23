package com.holidaycheck.permissify;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class PermissionRationaleDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String TAG = "Permissify";
    private static final String ARG_PENDING_CALL = "pendingCall";

    private PermissifyManager.PendingPermissionCall pendingCall;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        pendingCall = (PermissifyManager.PendingPermissionCall) getArguments().getSerializable(ARG_PENDING_CALL);

        return PermissifyConfig.get()
            .getRationaleDialogFactory()
            .createDialog(getContext(), getDialogMessage(pendingCall.options), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        PermissifyActivity activity = (PermissifyActivity) getActivity();

        activity.getPermissifyManager().getLifecycleHandler().onRationaleDialogConfirm(pendingCall);
    }

    public static void showDialog(FragmentManager fragmentManager, PermissifyManager.PendingPermissionCall pendingPermissionCall) {
        if (fragmentManager.findFragmentByTag(TAG) != null) {
            Log.w(TAG, "Dialog is already on screen - rejecting show command");
            return;
        }

        Bundle args = new Bundle();
        args.putSerializable(ARG_PENDING_CALL, pendingPermissionCall);

        PermissionRationaleDialogFragment dialog = new PermissionRationaleDialogFragment();
        dialog.setArguments(args);
        dialog.setCancelable(false);

        fragmentManager
            .beginTransaction()
            .add(dialog, TAG)
            .commitAllowingStateLoss();
    }

    private String getDialogMessage(PermissionCallOptions callOptions) {
        return callOptions.getRationaleDialogMsg() != null ? callOptions.getRationaleDialogMsg() : getString(callOptions.getRationaleDialogMsgRes());
    }

    public static PermissifyConfig.AlertDialogFactory getDefaultDialogFactory() {
        return new PermissifyConfig.AlertDialogFactory() {
            @Override
            public AlertDialog createDialog(Context context, String dialogMsg, DialogInterface.OnClickListener onClickListener) {
                return new AlertDialog.Builder(context)
                    .setTitle(R.string.permissify_permission_rationale_title)
                    .setMessage(dialogMsg)
                    .setPositiveButton(android.R.string.ok, onClickListener)
                    .create();
            }
        };
    }
}
