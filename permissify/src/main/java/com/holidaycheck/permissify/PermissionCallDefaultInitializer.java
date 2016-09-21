package com.holidaycheck.permissify;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

class PermissionCallDefaultInitializer {

    private static final String TAG = "Permissify";

    void initializeWithDefault(@NonNull Context context, @NonNull String permission, @NonNull PermissionCallOptions callOptions, PermissifyConfig permissifyConfig) {
        DialogText text = getPermissionDefaultText(context, permission, permissifyConfig);
        setDefaultDenyMessageIfNeeded(callOptions, text);
        setDefaultRationalMessageIfNeeded(callOptions, text);
    }

    @NonNull
    private DialogText getPermissionDefaultText(Context context, String permission, PermissifyConfig permissifyConfig) {
        String permissionGroup = getPermissionGroup(context, permission);
        DialogText text = null;

        if (permissionGroup != null) {
            text = permissifyConfig.getDefaultTextForPermissions().get(permissionGroup);
        }

        return text != null ? text : permissifyConfig.getPermissionTextFallback();
    }

    private void setDefaultDenyMessageIfNeeded(PermissionCallOptions callOptions, DialogText dialogText) {
        if (callOptions.showDenyDialog() && callOptions.getDenyDialogMsg() == null && callOptions.getDenyDialogMsgRes() == 0) {
            callOptions.setDenyDialogMsgRes(dialogText.denyDialogMsgRes);
        }
    }

    private void setDefaultRationalMessageIfNeeded(PermissionCallOptions callOptions, DialogText dialogText) {
        if (callOptions.showRationaleDialog() && callOptions.getRationaleDialogMsg() == null && callOptions.getRationaleDialogMsgRes() == 0) {
            callOptions.setRationaleDialogMsgRes(dialogText.rationaleDialogMsgRes);
        }
    }

    private String getPermissionGroup(Context context, String permission) {
        try {
            return context.getPackageManager().getPermissionInfo(permission, 0).group;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to get permission group", e);
            return null;
        }
    }

}
