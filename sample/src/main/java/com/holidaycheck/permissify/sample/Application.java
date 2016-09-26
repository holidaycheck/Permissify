package com.holidaycheck.permissify.sample;

import android.Manifest;
import com.holidaycheck.permissify.DialogText;
import com.holidaycheck.permissify.PermissifyConfig;

import java.util.HashMap;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PermissifyConfig permissifyConfig = new PermissifyConfig.Builder()
            .withDefaultTextForPermissions(new HashMap<String, DialogText>() {{
                put(Manifest.permission_group.LOCATION, new DialogText(R.string.location_rationale, R.string.location_deny_dialog));
                put(Manifest.permission_group.CAMERA, new DialogText(R.string.camera_rationale, R.string.camera_deny_dialog));
            }})
            .build();

        PermissifyConfig.initDefault(permissifyConfig);
    }

}
