package com.holidaycheck.permissify;

import android.support.annotation.StringRes;

public class DialogText {

    final int rationaleDialogMsgRes;
    final int denyDialogMsgRes;

    public DialogText(@StringRes int rationaleDialogMsgRes, @StringRes int denyDialogMsgRes) {
        this.rationaleDialogMsgRes = rationaleDialogMsgRes;
        this.denyDialogMsgRes = denyDialogMsgRes;
    }
}
