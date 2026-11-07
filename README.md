# Clock (com.clock.android)

A simple Android clock-style app with an explicit, confirmed root action to clear Android logcat buffers.

## Build
1. Open this folder in Android Studio (Giraffe or newer recommended).
2. Allow Gradle sync. Use JDK 17 and Android SDK Platform 35.
3. Build > Build APK(s).

## Use
Launch Clock, tap **CLEAR LOGCAT BUFFERS**, and confirm. Magisk/KernelSU/APatch may prompt for root.

The app runs `su -c "logcat -c"`. This clears current logcat buffers only. It does not clear an app's private audit database, Android package-manager state, or package uninstall history. The operation is visible and requires confirmation.

Package/application ID: `com.clock.android`
App label: `Clock`
