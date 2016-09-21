# Permissify - Simplifying Android Permissions

Permissify is an Android library that makes requesting permissions at runtime much easier.

Android Marshmallow added a new functionality to let users grant permissions when running an app instead of granting them all when installing it. This approach gives the user more control but requires developers to add lots of boilerplate code to support it.

Overview
-----------
* Tested in production - over 1000 000 installs in the [Holidaycheck app](https://play.google.com/store/apps/details?id=com.holidaycheck)
* Handles showing Rationale & Permission denied dialogs
* Consistent API across android versions
* Perfectly fine with configuration changes
* API for activity and fragment
* Works great without hacking android
* No memory leaks guaranteed
* Supports Android 15+

Screenshots
-----------
![Demo screenshot](./art/sample_record.gif)

Add it to your project
----------------------

Include the library in your ``build.gradle``

```groovy
dependencies{
    compile 'com.holidaycheck.permissify:1.0.0'
}
```

or to your ``pom.xml`` if you are using Maven

```xml
<dependency>
    <groupId>com.holidaycheck</groupId>
    <artifactId>permissify</artifactId>
    <version>1.0.0</version>
    <type>aar</type>
</dependency>

```

Configuration
-----
To start using the library you need to extend your activity from ``PermissifyActivity`` and configure library in your custom ``android.app.Application``.

Minimum configuration to run is presented below. You basically need to provide translations for every [Permission group](https://developer.android.com/guide/topics/security/permissions.html) that is used in your app.
```java
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PermissifyConfig permissifyConfig = new PermissifyConfig.Builder()
            .setDefaultTextForPermissions(new HashMap<String, DialogText>() {{
                 put(Manifest.permission_group.LOCATION, new DialogText(R.string.location_rationale, R.string.location_deny_dialog));
                 put(Manifest.permission_group.CAMERA, new DialogText(R.string.camera_rationale, R.string.camera_deny_dialog));
                }})
              .build();

        PermissifyConfig.initDefault(permissifyConfig);
    }
}
```

Full configuration looks like this, but mostly you will use minimum config. You can customize default options for every permission call, and dialogs appearance. See javadoc for more details.
```java
PermissifyConfig permissifyConfig = new PermissifyConfig.Builder()
    .setDefaultPermissionCallOptions(
        new PermissionCallOptions.Builder()
            .withDefaultDenyDialog(true)
            .withDefaultRationaleDialog(true)
            .withDefaultRationaleDialog(R.string.default_rationale_dialog_text)
            .withDenyDialogMsg(R.string.default_deny_dialog_text)
            .withRationaleEnabled(true)
            .build()
    )
    .setDefaultTextForPermissions(new HashMap<String, DialogText>() {{
        put(Manifest.permission_group.LOCATION, new DialogText(R.string.location_rationale, R.string.location_deny_dialog));
        put(Manifest.permission_group.CAMERA, new DialogText(R.string.camera_rationale, R.string.camera_deny_dialog));
    }})
    .setPermissionTextFallback(new DialogText(R.string.fallback_rationale_dialog_text, R.string.fallback_deny_dialog_text))
    .setDialogRationaleDialogFactory(new PermissifyConfig.AlertDialogFactory() {
        @Override
        public AlertDialog createDialog(Context context, String dialogMsg, DialogInterface.OnClickListener onClickListener) {
            return new CustomRationaleDialog(context, dialogMsg, onClickListener);
        }
    })
    .setDenyDialogFactory(new PermissifyConfig.AlertDialogFactory() {
        @Override
        public AlertDialog createDialog(Context context, String dialogMsg, DialogInterface.OnClickListener onClickListener) {
            return new CustomDenyDialog(context, dialogMsg, onClickListener);
        }
    })
    .build();

PermissifyConfig.initDefault(permissifyConfig);
```

Usage
-----
To request specific permission you need make a call to ``PermissifyManager``. Every call is associated with your unique RequestId.

```java
getPermissifyManager().callWithPermission(this, LOCATION_PERMISSION_REQUEST_ID, android.Manifest.permission.ACCESS_FINE_LOCATION);
```

the result and the current permission status is provided via
```java
@Override
public void onCallWithPermissionResult(int callId, PermissifyManager.CallRequestStatus status) {
    if (callId == LOCATION_PERMISSION_REQUEST_ID) {
        switch (status) {
            case PERMISSION_GRANTED:
                getUserLocation();
            break
            case PERMISSION_DENIED_ONCE:
            //do some custom logic
            break;
            case PERMISSION_DENIED_FOREVER:
            //do some custom logic
            case SHOW_PERMISSION_RATIONALE:
            //do some custom logic
        }
    }
}
```

If you want to request permission from a fragment make sure it implements ``PermissifyManager.Callback``

There are two types of dialogs:
* Rationale dialog - is shown in situations where the user might need an explanation why the app needs this permission
* Deny dialog - is shown when the user permanently denied requested permission

There are number of options that you can change when requesting for permission using additional parameter:
```java
getPermissifyManager().callWithPermission(this, LOCATION_PERMISSION_REQUEST_ID, android.Manifest.permission.ACCESS_FINE_LOCATION,
            new PermissionCallOptions.Builder()
                .withDefaultDenyDialog(false)
                .withRationaleEnabled(false)
                .build());
```

Current limitations
-----
* Multiple permission request at once is not currently supported (will be added soon)

Do you want to contribute?
-----
Feel free to add any cool and useful feature to the library.

License
-------
    The MIT License (MIT)

    Copyright (c) 2016 HolidayCheck

    Permission is hereby granted, free of charge, to any person obtaining a copy of
    this software and associated documentation files (the "Software"), to deal in
    the Software without restriction, including without limitation the rights to
    use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
    the Software, and to permit persons to whom the Software is furnished to do so,
    subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
    FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
    COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
    IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
    CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

