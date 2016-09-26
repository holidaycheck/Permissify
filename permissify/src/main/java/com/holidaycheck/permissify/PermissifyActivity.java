package com.holidaycheck.permissify;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**
 * Base activity for an application that uses Permissify library. It provides PermissifyManager that handles various permission request states.
 */
public class PermissifyActivity extends AppCompatActivity implements PermissifyManager.Callback {

    private PermissifyManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionManager = new PermissifyManager(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        permissionManager.getLifecycleHandler().onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        permissionManager.getLifecycleHandler().onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        permissionManager.getLifecycleHandler().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCallWithPermissionResult(int callId, PermissifyManager.CallRequestStatus status) {

    }

    /**
     * Gets PermissifyManager that is associated with this activity
     *
     * @return PermissifyManager
     */
    public PermissifyManager getPermissifyManager() {
        return permissionManager;
    }

}
