package br.com.tiagoluna.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.tiagoluna.agenda.R;
import br.com.tiagoluna.agenda.persistencia.AlunoDAO;

/**
 * Created by tiago on 13/03/2017.
 */

public class SMSReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        //Get the PDU (Protocol Description Unit)
        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];
        //Get PDU format
        String format = (String) intent.getSerializableExtra("format");
        //Recover SMS content
        SmsMessage sms = SmsMessage.createFromPdu(pdu, format);
        //Get the telephone number of the SMS sender
        String phone = sms.getDisplayOriginatingAddress();

        AlunoDAO dao = new AlunoDAO(context);
        if (dao.isAluno(phone)) {
            //Show toast
            Toast.makeText(context, "Student SMS received!", Toast.LENGTH_SHORT).show();
            //Play a msg sound
            MediaPlayer player = MediaPlayer.create(context, R.raw.msg);
            player.start();
        }
    }
}
