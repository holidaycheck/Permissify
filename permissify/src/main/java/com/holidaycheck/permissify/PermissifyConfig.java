package com.holidaycheck.permissify;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.HashMap;

/**
 * Config for Permissify Library
 */
public class PermissifyConfig {

    private static PermissifyConfig sInstance;

    private PermissionCallOptions defaultPermissionCallOptions;
    private HashMap<String, DialogText> defaultTextForPermissions;
    private DialogText permissionTextFallback;
    private AlertDialogFactory rationaleDialogFactory;
    private AlertDialogFactory denyDialogFactory;

    static PermissifyConfig get() {
        if (sInstance == null) {
            throw new RuntimeException("Permissify is not initialized");
        }

        return sInstance;
    }

    private PermissifyConfig() {
    }

    /**
     * Builder for creating PermissifyConfig instance
     */
    public static class Builder {

        private PermissifyConfig instance = new PermissifyConfig();

        /**
         * Sets options that are used by default when requesting for permission
         *
         * @param callOptions
         */
        public Builder withDefaultPermissionCallOptions(PermissionCallOptions callOptions) {
            instance.defaultPermissionCallOptions = callOptions;
            return this;
        }

        /**
         * Sets map that matches every permission group {@link android.Manifest.permission_group} that is used in the app with texts that is used in dialogs
         */
        public Builder withDefaultTextForPermissions(HashMap<String, DialogText> wording) {
            instance.defaultTextForPermissions = wording;
            return this;
        }

        /**
         * Sets dialog texts that will be used in case no match in map {@link #withDefaultTextForPermissions}
         */
        public Builder withPermissionTextFallback(DialogText dialogText) {
            instance.permissionTextFallback = dialogText;
            return this;
        }

        /**
         * Sets custom AlertDialogFactory that can be used to customize Rationale dialog
         */
        public Builder withDialogRationaleDialogFactory(AlertDialogFactory factory) {
            instance.rationaleDialogFactory = factory;
            return this;
        }

        /**
         * Sets custom AlertDialogFactory that can be used to customize Deny dialog
         */
        public Builder withDenyDialogFactory(AlertDialogFactory factory) {
            instance.denyDialogFactory = factory;
            return this;
        }

        /**
         * Builds instance of PermissifyConfig
         */
        public PermissifyConfig build() {
            if (instance.denyDialogFactory == null) {
                instance.denyDialogFactory = PermissionDeniedInfoDialogFragment.getDefaultDialogFactory();
            }

            if (instance.rationaleDialogFactory == null) {
                instance.rationaleDialogFactory = PermissionRationaleDialogFragment.getDefaultDialogFactory();
            }

            if (instance.defaultPermissionCallOptions == null) {
                instance.defaultPermissionCallOptions = new PermissionCallOptions.Builder()
                    .withDefaultDenyDialog(true)
                    .withDefaultRationaleDialog(true)
                    .build();
            }

            if (instance.permissionTextFallback == null) {
                instance.permissionTextFallback = new DialogText(R.string.permissify_no_text_fallback, R.string.permissify_no_text_fallback);
            }

            return instance;
        }
    }

    /**
     * Initializes Permissify config
     *
     * @param permissifyConfig - instance of PermissifyConfig that is returned from {@link Builder}
     */
    public static void initDefault(PermissifyConfig permissifyConfig) {
        sInstance = permissifyConfig;
    }

    PermissionCallOptions getDefaultPermissionCallOptions() {
        return defaultPermissionCallOptions;
    }

    DialogText getPermissionTextFallback() {
        return permissionTextFallback;
    }

    HashMap<String, DialogText> getDefaultTextForPermissions() {
        return defaultTextForPermissions;
    }

    AlertDialogFactory getRationaleDialogFactory() {
        return rationaleDialogFactory;
    }

    AlertDialogFactory getDenyDialogFactory() {
        return denyDialogFactory;
    }

    /**
     * Class that produces instances of AlertDialogs that are used as a content for Rationale & Deny dialogs {@link com.holidaycheck.permissify.DialogText}
     */
    public interface AlertDialogFactory {

        /**
         * Creates alert dialogs
         *
         * @param context         - app context
         * @param dialogMsg       - message that is related with requested permission
         * @param onClickListener - listener that should be attaches to dialog buttons
         * @return - custom AlertDialog
         */
        AlertDialog createDialog(Context context, String dialogMsg, DialogInterface.OnClickListener onClickListener);
    }

}
