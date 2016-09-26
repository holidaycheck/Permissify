package com.holidaycheck.permissify;

import android.support.annotation.StringRes;

/**
 * Contains texts for Rationale and Deny Dialogs
 *
 * There are two types of dialogs:
 * Rationale dialog - is shown in situations where the user might need an explanation why the app needs this permission
 * Deny dialog - is shown when the user permanently denied requested permission
 */
public class DialogText {

    final int rationaleDialogMsgRes;
    final int denyDialogMsgRes;

    public DialogText(@StringRes int rationaleDialogMsgRes, @StringRes int denyDialogMsgRes) {
        this.rationaleDialogMsgRes = rationaleDialogMsgRes;
        this.denyDialogMsgRes = denyDialogMsgRes;
    }
}
