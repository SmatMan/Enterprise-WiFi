# General Overview
- The way this setup works is by installing an application onto each TV that connects to the UofT/eduroam enterprise network using hardcoded credentials.

# Changing Credentials
- To change credentials, modify the file in `app/src/res/raw/preset_config.json` / create a new file at `app/src/res/raw/preset_config.json` based on `preset_config.json_example` at root dir.
- Recompilation will be required in order to create a new APK to install the new credentials. Follow below instructions

## Re-compilation (WIP)

- Set environment variable `ANDROID_HOME` to location of Android Development SDK (will expand)
- Run `./gradlew assembleDebug`.
- The compiled APK will be available in `app/build/outputs/apk/debug/app-debug.apk`


# Procedure

## ADB & Temporary Internet Setup
| In order to install the application, we need to temporarily enable developer access on the TV.

1. Go to **Settings** -> **System** -> **About**, scroll down to highlight **Android TV OS build**. Select this field multiple times until you see a popup that states "You are now a developer".
2. Go back to **Settings** -> **System** and find **Developer Options**
3. Scroll down to find **ADB debugging** and enable this.

## Installing

| To connect to the TV to install the app, we need some kind of existing connection. In the future we could potentially use a USB cable connected directly to the laptop, but currently I am using the GLINet router to connect the laptop and TV over the same network.

1. Connect the GLinet router to the TV over ethernet, and connect your laptop to the wifi network hosted by the router.
2. Identify the temporary IP address on the network in **Settings** -> **Network and Internet**
3. On the laptop, open the terminal, and run `adb connect IP_ADDRESS`.
4. On the TV, a prompt will appear asking to confirm the debugging connection. Allow this.
5. On the laptop, run `adb install app-debug.apk` to send the application to the TV.

## Connecting to the Network

1. Once the app is installed, unplug the ethernet connection to the TV. 
2. Navigate to the applications menu on the TV, and launch "Enterprise Wi-Fi for TVs".
    1. The app on first launch will prompt for location access, this should be allowed.
3. Scroll down inside the application to the **Connect To Preset** button, and select it. A popup should appear asking to connect to a Wi-Fi networks suggested by Enterprise Wifi. Press yes.
4. Navigate to **Settings** to confirm the connection. If it hasn't connected, go to **Settings** -> **System** -> **Restart** and it should be connected after the restart.

## Disabling Debug Access

| Now that the application is installed and the network has been connected, we need to disable ADB access.

1. Navigate to **Settings** -> **System** -> **Developer options**.
2. Disable **ADB debugging**, and scroll to the top and disable **Enable developer options**
3. When you exit out this menu, the developer options menu should be gone. 


# Removing the Network Configuration
1. To remove the saved network configuration, you must find the application in the apps menu and uninstall it.
