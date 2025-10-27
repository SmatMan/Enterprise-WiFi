# Preset Configuration Guide

This guide explains how to configure hardcoded preset Wi-Fi credentials in the Enterprise Wi-Fi app for Android TV.

## Overview

The preset configuration feature allows you to compile Wi-Fi credentials directly into the APK, making it easy to deploy to TVs without root access. When configured, a "Connect to Preset" button appears in the app that instantly connects to the preconfigured network.

## Security Features

- **No Credentials Exposure**: Username and password are never logged or displayed in the UI
- **Compiled into APK**: Credentials are embedded in the application binary and cannot be extracted without decompiling
- **Read-Only**: Credentials cannot be modified at runtime, requiring recompilation for changes
- **No External Files**: Works without root access or external file system access

## Configuration Steps

### 1. Locate the Configuration File

The preset configuration is stored in:
```
app/src/main/res/raw/preset_config.json
```

### 2. Edit the Configuration

Open `preset_config.json` and modify the values:

```json
{
  "enabled": true,
  "ssid": "YourNetworkName",
  "username": "your.username",
  "password": "your_password",
  "eap": "PEAP",
  "phase2": "MSCHAPV2"
}
```

**Configuration Parameters:**

- `enabled` (boolean): Set to `true` to enable the preset, `false` to disable
- `ssid` (string): The exact name of the Wi-Fi network (case-sensitive)
- `username` (string): The enterprise username/identity
- `password` (string): The enterprise password
- `eap` (string): EAP method - Options: `PEAP`, `TLS`, `TTLS`, `PWD`, `SIM`, `AKA`
- `phase2` (string): Phase 2 authentication - Options: `NONE`, `PAP`, `MSCHAP`, `MSCHAPV2`, `GTC`

### 3. Common Configurations

**University of Toronto (UofT):**
```json
{
  "enabled": true,
  "ssid": "UofT",
  "username": "your.utorid",
  "password": "your_password",
  "eap": "PEAP",
  "phase2": "MSCHAPV2"
}
```

**WPA2 Enterprise with PEAP/MSCHAPv2 (Most Common):**
```json
{
  "enabled": true,
  "ssid": "CompanyWiFi",
  "username": "employee.name",
  "password": "secure_password",
  "eap": "PEAP",
  "phase2": "MSCHAPV2"
}
```

**WPA2 Enterprise with TTLS/PAP:**
```json
{
  "enabled": true,
  "ssid": "OfficeNetwork",
  "username": "user@domain.com",
  "password": "password123",
  "eap": "TTLS",
  "phase2": "PAP"
}
```

### 4. Build the APK

After editing the configuration, build a new APK:

```bash
# Debug build (for testing)
./gradlew assembleDebug

# Release build (for production)
./gradlew assembleRelease
```

The APK will be located at:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

### 5. Install on Android TV

Transfer the APK to your Android TV and install it:

```bash
# Via ADB
adb install app/build/outputs/apk/debug/app-debug.apk

# Or use USB drive/file manager on the TV
```

## Usage

1. Launch the Enterprise Wi-Fi app on your Android TV
2. Navigate to the "Enterprise" tab
3. If a preset is configured, you'll see a "Connect to Preset" button
4. Click the button to automatically connect using the preset credentials
5. On Android 10+, confirm the network suggestion in system Wi-Fi settings

## Disabling the Preset

To disable the preset button without removing credentials:

```json
{
  "enabled": false,
  "ssid": "YourNetworkName",
  "username": "your.username",
  "password": "your_password",
  "eap": "PEAP",
  "phase2": "MSCHAPV2"
}
```

## Changing Credentials

**Important:** Each time you change the preset configuration, you must:

1. Edit `app/src/main/res/raw/preset_config.json`
2. Rebuild the APK with `./gradlew assembleDebug` or `./gradlew assembleRelease`
3. Uninstall the old version from the TV
4. Install the new APK

The credentials are compiled into the APK and cannot be changed without recompiling.

## Troubleshooting

### Preset Button Not Visible
- Check that `"enabled": true` in the configuration
- Verify the JSON syntax is correct (no trailing commas, proper quotes)
- Rebuild the APK and reinstall

### Connection Fails
- Verify SSID matches exactly (case-sensitive)
- Check username and password are correct
- Confirm EAP method and Phase 2 authentication match your network requirements
- Check the "Log" panel for error messages

### Android 10+ Network Suggestions
On Android 10 and newer, the app uses network suggestions:
1. The app will show "Suggestion added. Confirm in Wi-Fi settings"
2. Open Android TV Settings → Network & Internet → Wi-Fi
3. Find and approve the network suggestion

## Security Best Practices

1. **Unique APKs**: Build separate APKs for different networks/locations
2. **Secure Distribution**: Transfer APKs securely (encrypted USB, secure file transfer)
3. **Version Control**: Do NOT commit the actual credentials to public repositories
4. **Template File**: Keep a template with dummy credentials in version control
5. **Access Control**: Restrict who can build and distribute APKs with real credentials

## Example Workflow for Multiple Locations

If deploying to multiple locations with different credentials:

1. Keep `preset_config.json` with dummy/disabled values in Git
2. Create separate config files: `preset_office1.json`, `preset_office2.json`
3. Before building, copy the appropriate config:
   ```bash
   cp preset_office1.json app/src/main/res/raw/preset_config.json
   ./gradlew assembleRelease
   mv app/build/outputs/apk/release/app-release.apk enterprise-wifi-office1.apk
   ```
4. Distribute the correct APK to each location

## Support

For issues or questions:
- Check the in-app "Log" panel for connection details
- Review Android TV's Wi-Fi settings for network status
- Consult your network administrator for correct EAP/Phase2 settings
