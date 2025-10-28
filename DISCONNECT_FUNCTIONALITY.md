# Disconnect Functionality

## Overview
The app includes a "Disconnect" button that allows users to remove Wi-Fi network suggestions/configurations without uninstalling the app. This feature addresses the different network management APIs across Android versions.

## How It Works

### Android 11+ (API 30+)
On Android 11 and newer versions, the disconnect functionality can remove **all network suggestions** added by the app, including those from previous app sessions.

**API Used:** `WifiManager.getNetworkSuggestions()`
- Retrieves all network suggestions previously added by this app
- Works across app sessions (persists after app restart)
- Removes all suggestions when disconnect is pressed

**User Experience:**
- Press "Disconnect" button
- All network suggestions added by this app are removed
- Toast message shows: "Removed X network suggestion(s)"

### Android 10 (API 29)
On Android 10, the disconnect functionality has **platform limitations** and can only remove network suggestions from the current app session.

**Limitation:** The `getNetworkSuggestions()` API is not available on Android 10
- Can only track suggestions added during the current app session
- Cannot retrieve suggestions from previous sessions
- The `lastSuggestion` field tracks the most recent suggestion

**User Experience:**
- Press "Disconnect" button after connecting in the same session: Suggestion is removed
- Press "Disconnect" button after app restart: Shows "No network suggestion to remove (session-only tracking on Android 10)"
- **To remove old suggestions:** User must uninstall/reinstall the app or manually remove from Android Wi-Fi settings

### Android 9 and Earlier (API 28 and below)
On Android 9 and earlier, the app uses the legacy Wi-Fi configuration API.

**API Used:** `WifiManager.removeNetwork()` + `saveConfiguration()`
- Removes networks by matching SSID
- Works with `getConfiguredNetworks()` to find networks
- Removes network configuration and saves changes

**User Experience:**
- Press "Disconnect" button
- Network matching the last connected SSID is removed
- Toast message shows: "Network removed: [SSID]"

## Platform Limitations Summary

| Android Version | API Level | Can Remove Previous Session Suggestions | Method |
|----------------|-----------|---------------------------------------|--------|
| Android 11+    | 30+       | ✅ Yes                                | `getNetworkSuggestions()` |
| Android 10     | 29        | ❌ No (session-only)                  | `removeNetworkSuggestions()` with tracked suggestion |
| Android 9-     | 28-       | ✅ Yes                                | `removeNetwork()` |

## Why Android 10 Has This Limitation

Android 10 introduced the Network Suggestions API to improve security and privacy. However, the `getNetworkSuggestions()` method to retrieve all suggestions wasn't added until Android 11 (API 30). This creates a gap where:

1. Apps cannot use the old `getConfiguredNetworks()` API (returns null for non-system apps on API 29+)
2. Apps cannot use `getNetworkSuggestions()` yet (only available on API 30+)
3. Apps must track suggestions themselves, but only for the current session

This is a **known Android platform limitation** and not a bug in this app.

## Workarounds for Android 10

If you're on Android 10 and need to remove network suggestions from previous sessions:

1. **Uninstall and reinstall the app** - This clears all app data including suggestions
2. **Manually remove from Android Settings:**
   - Go to Settings → Network & Internet → Wi-Fi
   - Tap on the connected network
   - Select "Forget" or remove the network
3. **Clear app data:**
   - Go to Settings → Apps → Enterprise Wi-Fi
   - Tap "Storage" → "Clear Data"

## Technical Implementation

The disconnect button routes to the appropriate removal method based on Android version:

```java
public void disconnect() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        disconnectSuggestion();  // Android 10+: Uses Network Suggestions API
    } else {
        disconnectNetwork();     // Android 9-: Uses legacy WifiConfiguration API
    }
}
```

### For Android 11+ (disconnectSuggestion):
```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    // Get ALL suggestions from all sessions
    List<WifiNetworkSuggestion> allSuggestions = wifiManager.getNetworkSuggestions();
    // Remove them
    wifiManager.removeNetworkSuggestions(allSuggestions);
}
```

### For Android 10 (disconnectSuggestion):
```java
else if (lastSuggestion != null) {
    // Can only remove suggestion from current session
    wifiManager.removeNetworkSuggestions(Collections.singletonList(lastSuggestion));
}
```

### For Android 9- (disconnectNetwork):
```java
List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
for (WifiConfiguration config : list) {
    if (config.SSID.equals("\"" + ssid + "\"")) {
        wifiManager.removeNetwork(config.networkId);
        wifiManager.saveConfiguration();
    }
}
```

## Future Considerations

- **Minimum SDK bump**: If the app's minimum SDK is raised to API 30+ in the future, the Android 10 limitation would no longer apply
- **Persistent storage**: An alternative approach would be to persist suggestion details (SSID, EAP config) to SharedPreferences and reconstruct suggestions, but `removeNetworkSuggestions()` requires the exact original suggestion object, making this approach ineffective
