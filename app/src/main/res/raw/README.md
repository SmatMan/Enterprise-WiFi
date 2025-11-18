# Certificate Files for UofT WiFi

This directory should contain the CA certificate files for UofT WiFi authentication on Android 12+.

## Required Files

### Root CA Certificate
**Filename:** `uoft_root_ca.cer`
**Format:** PEM (Base64 encoded X.509)
**Source:** Sectigo Public Server Authentication Root R46

Place your `root.cer` file here and rename it to `uoft_root_ca.cer`.

### Intermediate CA Certificate (Optional)
**Filename:** `uoft_intermediate_ca.cer` 
**Format:** PEM (Base64 encoded X.509)
**Source:** Sectigo Public Server Authentication CA OV R36

This is optional but recommended if the RADIUS server doesn't provide the full certificate chain.

## File Format

The certificate files must be in PEM format, which looks like:
```
-----BEGIN CERTIFICATE-----
MIIFpDCCA4ygAwIBAgIQOc...
...
-----END CERTIFICATE-----
```

## How It Works

When connecting to the "UofT" SSID on Android 12+:
1. The app first tries to load certificates from these bundled files
2. If bundled certificates are not found, it falls back to using all system certificates
3. Both root and intermediate certificates (if provided) are used together for validation

This approach ensures the app works even on Android TV where manual certificate installation is difficult.
