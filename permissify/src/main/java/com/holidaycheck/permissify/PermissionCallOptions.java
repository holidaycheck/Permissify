package com.holidaycheck.permissify;

import android.support.annotation.StringRes;

import java.io.Serializable;

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

    public boolean isRationaleEnabled() {
        return rationaleEnabled;
    }

    void setRationaleDialogMsgRes(int rationaleDialogMsgRes) {
        this.rationaleDialogMsgRes = rationaleDialogMsgRes;
    }

    void setDenyDialogMsgRes(int denyDialogMsgRes) {
        this.denyDialogMsgRes = denyDialogMsgRes;
    }

    /**
     * Builder for easily creating instances of PermissionCallOptions
     */
    public static class Builder {

        private PermissionCallOptions buildObj;

        public Builder() {
            buildObj = new PermissionCallOptions();
        }

        public PermissionCallOptions build() {
            return buildObj;
        }

        public Builder withRationaleDialogMsg(@StringRes int rationaleMsgRes) {
            buildObj.rationaleDialogMsgRes = rationaleMsgRes;
            buildObj.showRationaleDialog = true;

            return this;
        }

        public Builder withRationaleDialogMsg(String rationaleMsg) {
            buildObj.rationaleDialogMsg = rationaleMsg;
            buildObj.showRationaleDialog = true;

            return this;
        }

        public Builder withDefaultRationaleDialog(boolean useDefault) {
            buildObj.showRationaleDialog = useDefault;

            return this;
        }

        public Builder withRationaleEnabled(boolean enabled) {
            buildObj.rationaleEnabled = enabled;

            return this;
        }

        public Builder withDenyDialogMsg(@StringRes int denyDialogMsgRes) {
            buildObj.denyDialogMsgRes = denyDialogMsgRes;
            buildObj.showDenyDialog = true;

            return this;
        }

        public Builder withDenyDialogMsg(String denyDialogMsg) {
            buildObj.denyDialogMsg = denyDialogMsg;
            buildObj.showDenyDialog = true;

            return this;
        }

        public Builder withDefaultDenyDialog(boolean useDefault) {
            buildObj.showDenyDialog = useDefault;

            return this;
        }
    }

}
