package com.holidaycheck.permissify;

import android.support.annotation.StringRes;

import java.io.Serializable;

/**
 * Options that are associated with Permission Request
 */
public class PermissionCallOptions implements Serializable {

    private boolean showRationaleDialog;
    private String rationaleDialogMsg;
    private int rationaleDialogMsgRes;
    private boolean rationaleEnabled = true;

    private boolean showDenyDialog;
    private String denyDialogMsg;
    private int denyDialogMsgRes;

    boolean showRationaleDialog() {
        return showRationaleDialog;
    }

    String getRationaleDialogMsg() {
        return rationaleDialogMsg;
    }

    int getRationaleDialogMsgRes() {
        return rationaleDialogMsgRes;
    }

    boolean showDenyDialog() {
        return showDenyDialog;
    }

    String getDenyDialogMsg() {
        return denyDialogMsg;
    }

    int getDenyDialogMsgRes() {
        return denyDialogMsgRes;
    }

    boolean isRationaleEnabled() {
        return rationaleEnabled;
    }

    void setRationaleDialogMsgRes(int rationaleDialogMsgRes) {
        this.rationaleDialogMsgRes = rationaleDialogMsgRes;
    }

    void setDenyDialogMsgRes(int denyDialogMsgRes) {
        this.denyDialogMsgRes = denyDialogMsgRes;
    }

    /**
     * Builder for creating PermissionCallOptions instance
     */
    public static class Builder {

        private PermissionCallOptions buildObj;

        public Builder() {
            buildObj = new PermissionCallOptions();
        }

        /**
         * Builds instance of PermissifyConfig
         */
        public PermissionCallOptions build() {
            return buildObj;
        }

        /**
         * Sets rationale dialog message
         *
         * @param rationaleMsgRes - string resource
         */
        public Builder withRationaleDialogMsg(@StringRes int rationaleMsgRes) {
            buildObj.rationaleDialogMsgRes = rationaleMsgRes;
            buildObj.showRationaleDialog = true;

            return this;
        }

        /**
         * Sets rationale dialog message
         *
         * @param rationaleMsg - string message
         */
        public Builder withRationaleDialogMsg(String rationaleMsg) {
            buildObj.rationaleDialogMsg = rationaleMsg;
            buildObj.showRationaleDialog = true;

            return this;
        }

        /**
         * Sets whether or not default rationale dialog should be used.
         * This should be used when you want to disable default dialog and handle rationale in custom way.
         * To disable rationale completely use {@link #withRationaleEnabled}
         *
         * @param useDefault - true - show rationale dialog
         */
        public Builder withDefaultRationaleDialog(boolean useDefault) {
            buildObj.showRationaleDialog = useDefault;

            return this;
        }

        /**
         * Sets whether or not rationale is enabled (custom or default).
         *
         * @param enabled - true rationale is enabled
         */
        public Builder withRationaleEnabled(boolean enabled) {
            buildObj.rationaleEnabled = enabled;

            return this;
        }

        /**
         * Sets deny dialog message
         *
         * @param denyDialogMsgRes - string resource
         */
        public Builder withDenyDialogMsg(@StringRes int denyDialogMsgRes) {
            buildObj.denyDialogMsgRes = denyDialogMsgRes;
            buildObj.showDenyDialog = true;

            return this;
        }

        /**
         * Sets deny dialog message
         *
         * @param denyDialogMsg - string message
         */
        public Builder withDenyDialogMsg(String denyDialogMsg) {
            buildObj.denyDialogMsg = denyDialogMsg;
            buildObj.showDenyDialog = true;

            return this;
        }

        /**
         * Sets whether or not default deny dialog should be used.
         *
         * @param useDefault - true - show deny dialog
         */
        public Builder withDefaultDenyDialog(boolean useDefault) {
            buildObj.showDenyDialog = useDefault;

            return this;
        }
    }

}
