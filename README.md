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

## GitHub Actions build
A workflow is included at `.github/workflows/build-apk.yml`. Push to GitHub or run it manually from **Actions → Build Clock APK → Run workflow**. When it completes, open the workflow run and download the `Clock-debug-apk` artifact. The APK is a debug build.

The clear action additionally cleans only Clock's own private cache directory (`getCacheDir()`). It does not scan or delete arbitrary root folders or other apps' package history.

## CI SDK note
The workflow uses `android-actions/setup-android@v4` and explicitly installs platform-tools, Android API 35, and Build Tools 35.0.0. This avoids the retired SDK package named `tools` that caused the prior workflow to fail during SDK setup.
