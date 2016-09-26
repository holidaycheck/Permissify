package com.holidaycheck.permissify;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Manager that handles various permission request states
 */
public class PermissifyManager {

    private static final String SAVE_INSTANCE_KEY_PENDING_PERMISSION_CALL = "pendingPermissionCalls";
    private static final String TAG = "Permissify";

    /**
     * Status that indicates the current state of permission request
     */
    public enum CallRequestStatus {
        PERMISSION_GRANTED, PERMISSION_DENIED_ONCE, PERMISSION_DENIED_FOREVER, SHOW_PERMISSION_RATIONALE
    }

    private PermissifyActivity activity;
    private LifecycleHandler lifecycleHandler = new LifecycleHandler();
    private PermissionCallDefaultInitializer callOptionsInitializer = new PermissionCallDefaultInitializer();
    private HashMap<Integer, PendingPermissionCall> pendingPermissionCalls = new HashMap<>();
    private PermissifyConfig permissifyConfig;

    PermissifyManager(PermissifyActivity activity) {
        this.activity = activity;
        this.permissifyConfig = PermissifyConfig.get();
    }

    /**
     * Requests permission using default PermissionCallOptions
     *
     * @param fragment   - fragment that implements Callback interface and where result will be delivered
     * @param callId     - unique identifier that is associated with this permission call
     * @param permission - one of the {@link android.Manifest.permission}
     */
    public <T extends Fragment & Callback> void callWithPermission(T fragment, int callId, String permission) {
        callWithPermission(fragment, callId, permission, permissifyConfig.getDefaultPermissionCallOptions());
    }

    /**
     * Requests permission using default PermissionCallOptions
     *
     * @param activity   - PermissifyActivity
     * @param callId     - unique identifier that is associated with this permission call
     * @param permission - one of the {@link android.Manifest.permission}
     */
    public void callWithPermission(PermissifyActivity activity, int callId, String permission) {
        callWithPermission(activity, callId, permission, permissifyConfig.getDefaultPermissionCallOptions());
    }

    /**
     * Requests permission using custom PermissionCallOptions
     *
     * @param fragment              - fragment that implements Callback interface and where result will be delivered
     * @param callId                - unique identifier that is associated with this permission call
     * @param permission            - one of the {@link android.Manifest.permission}
     * @param permissionCallOptions - custom permission call options
     */
    public <T extends Fragment & Callback> void callWithPermission(T fragment, int callId, String permission, PermissionCallOptions permissionCallOptions) {
        PermissionCallInternalData data = new PermissionCallInternalData();
        data.requestFromFragment = true;
        data.fragmentId = fragment.getId();
        data.callId = callId;
        data.permission = permission;

        PendingPermissionCall pendingPermissionCall = new PendingPermissionCall(permissionCallOptions, data);
        doCallWithPermission(fragment.getActivity(), fragment, pendingPermissionCall);
    }

    /**
     * Requests permission using custom PermissionCallOptions
     *
     * @param activity              - PermissifyActivity
     * @param callId                - unique identifier that is associated with this permission call
     * @param permission            - one of the {@link android.Manifest.permission}
     * @param permissionCallOptions - custom permission call options
     */
    public void callWithPermission(PermissifyActivity activity, int callId, String permission, PermissionCallOptions permissionCallOptions) {
        PermissionCallInternalData data = new PermissionCallInternalData();
        data.requestFromFragment = false;
        data.callId = callId;
        data.permission = permission;

        PendingPermissionCall pendingPermissionCall = new PendingPermissionCall(permissionCallOptions, data);
        doCallWithPermission(activity, activity, pendingPermissionCall);
    }

    /**
     * Checks whether or not app has this permission
     *
     * @param permission - one of the {@link android.Manifest.permission}
     * @return true - app has this permission, false app doesn't have this permission
     */
    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This method should be called after the user had seen permission rationale and system permission call should be now requested. Use it only in case of handling permission rationale in custom way.
     *
     * @param callId - unique identifier that is associated with this permission call
     */
    public void onRationaleConfirmed(int callId) {
        PendingPermissionCall pendingPermissionCall = pendingPermissionCalls.get(callId);
        getLifecycleHandler().onRationaleDialogConfirm(pendingPermissionCall);
    }

    LifecycleHandler getLifecycleHandler() {
        return lifecycleHandler;
    }

    private void doCallWithPermission(FragmentActivity activity, Callback callback, PendingPermissionCall pendingCall) {
        callOptionsInitializer.initializeWithDefault(activity, pendingCall.internalData.permission, pendingCall.options, permissifyConfig);

        if (hasPermission(pendingCall.internalData.permission)) {
            callback.onCallWithPermissionResult(pendingCall.internalData.callId, CallRequestStatus.PERMISSION_GRANTED);
        } else {
            pendingPermissionCalls.put(pendingCall.internalData.callId, pendingCall);

            if (pendingCall.options.isRationaleEnabled() && ActivityCompat.shouldShowRequestPermissionRationale(activity, pendingCall.internalData.permission)) {
                if (pendingCall.options.showRationaleDialog()) {
                    PermissionRationaleDialogFragment.showDialog(activity.getSupportFragmentManager(), pendingCall);
                }
                callback.onCallWithPermissionResult(pendingCall.internalData.callId, CallRequestStatus.SHOW_PERMISSION_RATIONALE);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{ pendingCall.internalData.permission }, pendingCall.internalData.callId);
            }
        }
    }

    @Nullable
    private Callback getCallback(Activity activity) {
        if (activity instanceof Callback) {
            return (Callback) activity;
        }

        return null;
    }

    @Nullable
    private Callback getCallback(Fragment fragment) {
        if (fragment instanceof Callback) {
            return (Callback) fragment;
        }

        return null;
    }

    @Nullable
    private Callback getCallback(PendingPermissionCall pendingPermissionCall) {
        if (pendingPermissionCall.internalData.requestFromFragment) {
            Fragment requestingFragment = getRequestingFragment(pendingPermissionCall);
            if (requestingFragment != null) {
                return getCallback(requestingFragment);
            }
        } else {
            return getCallback(activity);
        }

        return null;
    }

    @Nullable
    private Fragment getRequestingFragment(PendingPermissionCall permissionCall) {
        return activity.getSupportFragmentManager().findFragmentById(permissionCall.internalData.fragmentId);
    }

    /**
     * Callback that is used by PermissifyManager to deliver permission request status
     */
    public interface Callback {

        /**
         * Delivers permission request status
         *
         * @param callId - unique identifier that is associated with this permission call
         * @param status - current permission request status
         */
        void onCallWithPermissionResult(int callId, CallRequestStatus status);
    }

    private static class PermissionCallInternalData implements Serializable {
        private int callId;
        private boolean requestFromFragment;
        private int fragmentId;
        private String permission;
    }

    static class PendingPermissionCall implements Serializable {
        public final PermissionCallOptions options;
        public final PermissionCallInternalData internalData;

        public PendingPermissionCall(PermissionCallOptions options, PermissionCallInternalData internalData) {
            this.options = options;
            this.internalData = internalData;
        }
    }

    class LifecycleHandler {

        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            PendingPermissionCall pendingPermissionCall = pendingPermissionCalls.remove(requestCode);

            if (grantResults.length < 1) {
                Log.w(TAG, "Incorrect size of grant result array");
                return;
            }

            if (pendingPermissionCall == null) {
                Log.w(TAG, "Unable to find PendingPermissionCall");
                return;
            }

            boolean granted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0]);
            Callback callback = getCallback(pendingPermissionCall);

            if (callback == null) {
                Log.w(TAG, "Callback was null. Unable to dispatch permission result");
            } else {
                if (!granted && !showRationale) {
                    if (pendingPermissionCall.options.showDenyDialog()) {
                        PermissionDeniedInfoDialogFragment.showDialog(activity.getSupportFragmentManager(), pendingPermissionCall);
                    }
                }

                callback.onCallWithPermissionResult(requestCode,
                    granted ? CallRequestStatus.PERMISSION_GRANTED :
                        showRationale ? CallRequestStatus.PERMISSION_DENIED_ONCE : CallRequestStatus.PERMISSION_DENIED_FOREVER);
            }
        }

        @SuppressWarnings("unchecked")
        void onRestoreInstanceState(Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(SAVE_INSTANCE_KEY_PENDING_PERMISSION_CALL)) {
                    pendingPermissionCalls = (HashMap<Integer, PendingPermissionCall>) savedInstanceState.getSerializable(SAVE_INSTANCE_KEY_PENDING_PERMISSION_CALL);
                }
            }
        }

        void onSaveInstanceState(Bundle outState) {
            outState.putSerializable(SAVE_INSTANCE_KEY_PENDING_PERMISSION_CALL, pendingPermissionCalls);
        }

        void onRationaleDialogConfirm(PendingPermissionCall permissionCall) {
            ActivityCompat.requestPermissions(activity, new String[]{ permissionCall.internalData.permission }, permissionCall.internalData.callId);
        }

    }

}
