package com.example.android.flowercalendar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class LoginActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkBiometricSupport();
        authenticateUser();
        createNotificationChannel();
    }

    //Metoda służąca do komunikacji z użytkownikiem za pomocą toasta
    private void notifyUser(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

    private void checkBiometricSupport() {

        //KeyguardManager sprawdza, czy ekran można odblokować za pomocą alternatywnej metody,
        // innej niż odcisk palca
        KeyguardManager keyguardManager =
                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        //PackageManager sprawdza czy fingerprint authentication jest dostępne na tym urządzeniu
        PackageManager packageManager = this.getPackageManager();

        assert keyguardManager != null;
        if (!keyguardManager.isKeyguardSecure()) {
            notifyUser("Lock screen security not enabled in Settings");
            return;
        }

        //Sprawdza czy została udzielona zgoda na użycie biometric
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.USE_BIOMETRIC) !=
                PackageManager.PERMISSION_GRANTED) {

            notifyUser("Fingerprint authentication permission not enabled");
            return;
        }

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
        }

    }

    //Metoda dająca użytkownikowi zwrotną informację o tym, czy jego odcisk palca
    // został rozpoznany przez urządzenie
    private android.hardware.biometrics.BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {

        return new android.hardware.biometrics.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              CharSequence errString) {
                notifyUser("Authentication error: " + errString);
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode,
                                             CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(
                    android.hardware.biometrics.BiometricPrompt.AuthenticationResult result) {
               launchMainActivity();
                super.onAuthenticationSucceeded(result);
            }
        };
    }

    //Zatrzymywanie procesu autentyfikacji
    private CancellationSignal getCancellationSignal() {
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                notifyUser("Cancelled via signal");
            }
        });
        return cancellationSignal;
    }

    //Metoda odpowiedzialna za uwierzytelnienie użytkownika za pomocą odcisku palca
    public void authenticateUser() {
        android.hardware.biometrics.BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Fingerprint authentication")
                .setSubtitle("Authentication is required to continue")
                .setDescription("This app uses biometric authentication to protect your data.")
                .setNegativeButton("Cancel", this.getMainExecutor(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notifyUser("Authentication cancelled");
                            }
                        })
                .build();

        biometricPrompt.authenticate(getCancellationSignal(), getMainExecutor(),
                getAuthenticationCallback());
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("ObsoleteSdkInt")
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "alarmClock";
            String description = "wakeUpCall";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
