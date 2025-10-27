package com.felkertech.ussenterprise.model;

import android.content.Context;
import android.util.Log;

import com.felkertech.ussenterprise.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Loads and manages preset Wi-Fi configuration from a JSON resource file.
 * The preset is compiled into the APK and cannot be accessed or modified at runtime,
 * providing secure storage of enterprise Wi-Fi credentials.
 */
public class PresetConfig {
    private static final String TAG = PresetConfig.class.getSimpleName();
    private static final String KEY_ENABLED = "enabled";
    private static final String KEY_SSID = "ssid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EAP = "eap";
    private static final String KEY_PHASE2 = "phase2";

    private boolean enabled = false;
    private String ssid = "";
    private String username = "";
    private String password = "";
    private String eapMethod = "PEAP";
    private String phase2Method = "MSCHAPV2";

    /**
     * Load preset configuration from raw resource file
     */
    public static PresetConfig loadFromResource(Context context) {
        PresetConfig config = new PresetConfig();
        
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.preset_config);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            inputStream.close();

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            config.enabled = jsonObject.optBoolean(KEY_ENABLED, false);
            config.ssid = jsonObject.optString(KEY_SSID, "");
            config.username = jsonObject.optString(KEY_USERNAME, "");
            config.password = jsonObject.optString(KEY_PASSWORD, "");
            config.eapMethod = jsonObject.optString(KEY_EAP, "PEAP");
            config.phase2Method = jsonObject.optString(KEY_PHASE2, "MSCHAPV2");

            // Only log non-sensitive information
            if (config.enabled) {
                Log.d(TAG, "Preset configuration loaded for SSID: " + config.ssid);
            } else {
                Log.d(TAG, "Preset configuration is disabled");
            }
        } catch (Exception e) {
            Log.w(TAG, "No preset configuration found or failed to load: " + e.getMessage());
            config.enabled = false;
        }

        return config;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getSsid() {
        return ssid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getEapMethodValue() {
        // Map string to WifiEnterpriseConfig.Eap constants
        switch (eapMethod.toUpperCase()) {
            case "PEAP":
                return Eap.METHOD_PEAP;
            case "TLS":
                return Eap.METHOD_TLS;
            case "TTLS":
                return Eap.METHOD_TTLS;
            case "PWD":
                return Eap.METHOD_PWD;
            case "SIM":
                return Eap.METHOD_SIM;
            case "AKA":
                return Eap.METHOD_AKA;
            default:
                return Eap.METHOD_PEAP;
        }
    }

    public int getPhase2MethodValue() {
        // Map string to WifiEnterpriseConfig.Phase2 constants
        switch (phase2Method.toUpperCase()) {
            case "NONE":
                return Phase2.AUTHENTICATION_NONE;
            case "PAP":
                return Phase2.AUTHENTICATION_PAP;
            case "MSCHAP":
                return Phase2.AUTHENTICATION_MSCHAP;
            case "MSCHAPV2":
                return Phase2.AUTHENTICATION_MSCHAPV2;
            case "GTC":
                return Phase2.AUTHENTICATION_GTC;
            default:
                return Phase2.AUTHENTICATION_MSCHAPV2;
        }
    }

    /**
     * Converts preset configuration to EnterpriseWifiConnection
     */
    public EnterpriseWifiConnection toWifiConnection() {
        if (!enabled) {
            throw new IllegalStateException("Preset is not enabled");
        }
        
        return new EnterpriseWifiConnection.Builder()
                .setSsid(ssid)
                .setIdentity(username)
                .setPassword(password)
                .setEap(getEapMethodValue())
                .setPhase2(getPhase2MethodValue())
                .build();
    }
}
