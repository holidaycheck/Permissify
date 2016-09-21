package com.holidaycheck.permissify.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.holidaycheck.permissify.PermissifyActivity;
import com.holidaycheck.permissify.PermissifyManager;
import com.holidaycheck.permissify.PermissionCallOptions;

public class SampleActivity extends PermissifyActivity {

    private static final int LOCATION_PERMISSION_REQUEST_ID = 1;
    private static final int CAMERA_PERMISSION_REQUEST_ID = 2;
    private static final int CONTACTS_PERMISSION_REQUEST_ID = 3;

    @BindView(R.id.contacts_button)
    protected Button contactsButton;

    @BindView(R.id.location_status)
    protected TextView locationStatus;

    @BindView(R.id.camera_status)
    protected TextView cameraStatus;

    @BindView(R.id.contacts_status)
    protected TextView contactsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        ButterKnife.bind(this);
    }

    @Override
    public void onCallWithPermissionResult(int callId, PermissifyManager.CallRequestStatus status) {
        super.onCallWithPermissionResult(callId, status);

        if (callId == LOCATION_PERMISSION_REQUEST_ID) {
            visualiseStatus(locationStatus, status);

            switch (status) {
                case PERMISSION_GRANTED:
                    //getUserLocation();
                    break;
                case PERMISSION_DENIED_ONCE:
                    //do some custom logic
                    break;
                case PERMISSION_DENIED_FOREVER:
                    //do some custom logic
                case SHOW_PERMISSION_RATIONALE:
                    //do some custom logic
            }
        } else if (callId == CAMERA_PERMISSION_REQUEST_ID) {
            visualiseStatus(cameraStatus, status);

            if (status == PermissifyManager.CallRequestStatus.SHOW_PERMISSION_RATIONALE) {
                showCameraRationaleSnackbar(callId);
            }

        } else if (callId == CONTACTS_PERMISSION_REQUEST_ID) {
            visualiseStatus(contactsStatus, status);

            contactsButton.setEnabled(status != PermissifyManager.CallRequestStatus.PERMISSION_DENIED_ONCE && status != PermissifyManager.CallRequestStatus.PERMISSION_DENIED_FOREVER);
        }
    }

    @OnClick(R.id.location_button)
    protected void onLocationClick() {
        //call to permissify using default dialog text & behaviour
        getPermissifyManager().callWithPermission(this, LOCATION_PERMISSION_REQUEST_ID, android.Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @OnClick(R.id.camera_button)
    protected void onCameraClick() {
        //call to permissify using default deny dialog & custom behaviour for rationale dialog (handled in onCallWithPermissionResult)
        getPermissifyManager().callWithPermission(this, CAMERA_PERMISSION_REQUEST_ID, Manifest.permission.CAMERA,
            new PermissionCallOptions.Builder()
                .withDefaultDenyDialog(true)
                .withDefaultRationaleDialog(false)
                .build()
        );
    }

    @OnClick(R.id.contacts_button)
    protected void onContactsClick() {
        //call to permissify using without dialogs, but with custom behavior (handled in onCallWithPermissionResult)
        getPermissifyManager().callWithPermission(this, CONTACTS_PERMISSION_REQUEST_ID, Manifest.permission.READ_CONTACTS,
            new PermissionCallOptions.Builder()
                .withDefaultDenyDialog(false)
                .withRationaleEnabled(false)
                .build());
    }

    private void showCameraRationaleSnackbar(final int callId) {
        Snackbar
            .make(findViewById(android.R.id.content), R.string.camera_rationale, Snackbar.LENGTH_LONG)
            .setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPermissifyManager().onRationaleConfirmed(callId);
                }
            })
            .show();
    }

    private String getStatusText(PermissifyManager.CallRequestStatus status) {
        switch (status) {
            case PERMISSION_GRANTED:
                return getString(R.string.permission_status_granted);
            case PERMISSION_DENIED_ONCE:
                return getString(R.string.permission_status_denied_once);
            case PERMISSION_DENIED_FOREVER:
                return getString(R.string.permission_status_denied_forever);
            case SHOW_PERMISSION_RATIONALE:
                return getString(R.string.permission_status_rationale);
            default:
                return "";
        }
    }

    @ColorInt
    private int getStatusColor(PermissifyManager.CallRequestStatus status) {
        switch (status) {
            case PERMISSION_GRANTED:
                return ContextCompat.getColor(this, R.color.permission_status_granted);
            case PERMISSION_DENIED_ONCE:
                return ContextCompat.getColor(this, R.color.permission_status_denied_once);
            case PERMISSION_DENIED_FOREVER:
                return ContextCompat.getColor(this, R.color.permission_status_denied_forever);
            case SHOW_PERMISSION_RATIONALE:
                return ContextCompat.getColor(this, R.color.permission_status_rationale);
            default:
                return ContextCompat.getColor(this, R.color.permission_status_rationale);
        }
    }

    private void visualiseStatus(TextView view, PermissifyManager.CallRequestStatus status) {
        view.setText(getStatusText(status));
        ((ViewGroup) view.getParent()).setBackgroundColor(getStatusColor(status));
    }
}
