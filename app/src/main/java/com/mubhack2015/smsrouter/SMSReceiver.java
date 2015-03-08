package com.mubhack2015.smsrouter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import android.util.Base64;

/**
 * Created by sahaj on 07/03/2015.
 */
public class SMSReceiver extends BroadcastReceiver {

    final SmsManager smsManager = SmsManager.getDefault();
    public static String DEBUG_TAG = "SMSReceiver";
    private Context context;
    @Override
    public void onReceive(Context c, Intent intent) {
        context = c;
        SmsMessage messages[] = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        String body = "";

        for (SmsMessage msg : messages) {
            body += msg.getDisplayMessageBody();
        }
        Log.e("SMSReceiver", body);
        if(body.startsWith("<SMSRouter>")) {
            body = body.substring("<SMSRouter>".length());
            body = decrypt(body);
            if (body.startsWith("<from>")) {
                String rest = body.substring(body.indexOf("</from>")+"</from>".length());
                String from = body.substring(body.indexOf("<from>")+"<from>".length(),
                        body.indexOf("</from>"));
                //TODO do something about it
            } else if (body.startsWith("<num>")) {
                String rest = body.substring(body.indexOf("</num>")+"</num>".length());
                String to = body.substring(body.indexOf("<num>")+"<num>".length(),
                        body.indexOf("</num>"));
                smsManager.sendTextMessage(to, null, rest, null, null);
            }
        }
    }

    public String decrypt(String enc) {
        String decrypted = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            PrivateKey sKey = loadPrivateKey(context.getResources().openRawResource(R.raw.private1));
            cipher.init(Cipher.DECRYPT_MODE, sKey);
            byte[] encBytes = Base64.decode(enc, Base64.DEFAULT);
            decrypted = new String(cipher.doFinal(encBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }

    public PrivateKey loadPrivateKey(InputStream is)
            throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {

        byte[] encodedPrivateKey = new byte[is.available()];
        while (is.read(encodedPrivateKey) != -1) {}
        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                encodedPrivateKey);
        return keyFactory.generatePrivate(privateKeySpec);
    }


}
