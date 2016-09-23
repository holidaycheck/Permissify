package com.holidaycheck.permissify;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class PermissionDeniedInfoDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String TAG = "Permissify";
    private static final String ARG_PENDING_CALL = "pendingCall";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        PermissifyManager.PendingPermissionCall pendingCall = (PermissifyManager.PendingPermissionCall) getArguments().getSerializable(ARG_PENDING_CALL);

        return PermissifyConfig.get()
            .getDenyDialogFactory()
            .createDialog(getContext(), getDialogMessage(pendingCall.options), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getContext().getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private String getDialogMessage(PermissionCallOptions callOptions) {
        return callOptions.getDenyDialogMsg() != null ? callOptions.getDenyDialogMsg() : getString(callOptions.getDenyDialogMsgRes());
    }

    public static void showDialog(FragmentManager fragmentManager, PermissifyManager.PendingPermissionCall pendingPermissionCall) {
        if (fragmentManager.findFragmentByTag(TAG) != null) {
            Log.w(TAG, "Dialog is already on screen - rejecting show command");
            return;
        }

        Bundle args = new Bundle();
        args.putSerializable(ARG_PENDING_CALL, pendingPermissionCall);

        PermissionDeniedInfoDialogFragment dialog = new PermissionDeniedInfoDialogFragment();
        dialog.setArguments(args);
        dialog.setCancelable(false);

        fragmentManager
            .beginTransaction()
            .add(dialog, TAG)
            .commitAllowingStateLoss();
    }

    public static PermissifyConfig.AlertDialogFactory getDefaultDialogFactory() {
        return new PermissifyConfig.AlertDialogFactory() {
            @Override
            public AlertDialog createDialog(Context context, String dialogMsg, DialogInterface.OnClickListener onClickListener) {
                return new AlertDialog.Builder(context)
                    .setMessage(dialogMsg)
                    .setPositiveButton(R.string.permissify_go_to_settings, onClickListener)
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
            }
        };
    }

}
