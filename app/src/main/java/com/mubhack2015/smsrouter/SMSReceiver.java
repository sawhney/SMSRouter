package com.mubhack2015.smsrouter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sahaj on 07/03/2015.
 */
public class SMSReceiver extends BroadcastReceiver {

    final SmsManager smsManager = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage messages[] = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for (SmsMessage msg : messages) {
            smsManager.sendTextMessage(msg.getOriginatingAddress(), null,
                    msg.getDisplayMessageBody(), null, null);
        }
    }
}
