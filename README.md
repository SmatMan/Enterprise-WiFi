## Quick Links
<img src='https://github.com/ITVlab/Enterprise-Wi-Fi/blob/master/promo/banner.png?raw=true' />

[Download on Google Play](https://play.google.com/store/apps/details?id=com.felkertech.ussenterprise)

# Enterprise Wi-Fi
Android TV does not by default have a user interface for setting up Enterprise Wi-Fi networks. These networks, use the [Extensible Authentication Protocol (EAP)](https://en.wikipedia.org/wiki/Extensible_Authentication_Protocol), a framework for authenticating your device with the Wi-Fi network by providing both a username and password.

These types of networks are common at businesses, where only particular users should be accessing the network, or at universities where content is audited.

As stated above, though there is no user interface for connecting to these networks, the system has APIs for programmatically connecting. To aid those in the situations above, this app provides a useful interface for making connections.

There is another app that fulfills this need on Android TV, although I have found a number of issues with how it works.

## Features
* Connection details can be saved and updated
* Automatically fills in fields where possible
* Shows logs of connection status to show you progress is happening
* **Preset Configuration**: Compile Wi-Fi credentials directly into the APK for easy deployment (see [PRESET_CONFIGURATION.md](PRESET_CONFIGURATION.md))

<img src='https://github.com/ITVlab/Enterprise-Wi-Fi/blob/master/promo/device-2016-09-07-012808.png?raw=true' />

<img src='https://github.com/ITVlab/Enterprise-Wi-Fi/blob/master/promo/device-2016-09-07-012756.png?raw=true' />

### University of Toronto preset
When the SSID **UofT** is selected, the app automatically configures the connection using PEAP and MSCHAPv2 for WPA & WPA2 Enterprise authentication. No CA certificate is required for this network.

### Android 10 and newer
Starting with Android 10, third-party apps can no longer directly add Wi‑Fi configurations. On these versions the app now uses **network suggestions**. After you press Connect you will be asked to confirm the suggestion in the system Wi‑Fi settings.

## Preset Configuration
For deploying to multiple TVs with the same network credentials, you can configure a preset network that is compiled into the APK. This provides:
* One-click connection via "Connect to Preset" button
* Secure credential storage (no external files needed)
* No root access required
* Easy recompilation for credential updates

See [PRESET_CONFIGURATION.md](PRESET_CONFIGURATION.md) for detailed instructions on configuring and building APKs with preset credentials.

## Building
To build with recent Android Studio, update the Gradle wrapper and plugin. This repo now targets SDK 34 and uses Gradle plugin 8.1.0. Run `./gradlew assembleDebug` to create an APK.

