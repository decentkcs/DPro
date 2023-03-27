package kr.co.decent.dpro.android.scan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import device.common.ScanConst;
import kr.co.decent.dpro.android.nexacro.NexacroActivityExt;

public class ScanReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (ScanConst.INTENT_EVENT.equals(intent.getAction())) {

                boolean result              = intent.getBooleanExtra(ScanConst.EXTRA_EVENT_DECODE_RESULT, false);
                int decodeBytesLength       = intent.getIntExtra(ScanConst.EXTRA_EVENT_DECODE_LENGTH, 0);
                byte[] decodeBytesValue     = intent.getByteArrayExtra(ScanConst.EXTRA_EVENT_DECODE_VALUE);
                String decodeValue          = new String(decodeBytesValue, 0, decodeBytesLength);
                int decodeLength            = decodeValue.length();
                String symbolName           = intent.getStringExtra(ScanConst.EXTRA_EVENT_SYMBOL_NAME);
                byte symbolId               = intent.getByteExtra(ScanConst.EXTRA_EVENT_SYMBOL_ID, (byte) 0);
                int symbolType              = intent.getIntExtra(ScanConst.EXTRA_EVENT_SYMBOL_TYPE, 0);
                byte letter                 = intent.getByteExtra(ScanConst.EXTRA_EVENT_DECODE_LETTER, (byte) 0);
                byte modifier               = intent.getByteExtra(ScanConst.EXTRA_EVENT_DECODE_MODIFIER, (byte) 0);
                int decodingTime            = intent.getIntExtra(ScanConst.EXTRA_EVENT_DECODE_TIME, 0);

                System.out.println("LOG == " + "1. result: " + result);
                System.out.println("LOG == " + "2. bytes length: " + decodeBytesLength);
                System.out.println("LOG == " + "3. bytes value: " + decodeBytesValue);
                System.out.println("LOG == " + "4. decoding length: " + decodeLength);
                System.out.println("LOG == " + "5. decoding value: " + decodeValue);
                System.out.println("LOG == " + "6. symbol name: " + symbolName);
                System.out.println("LOG == " + "7. symbol id: " + symbolId);
                System.out.println("LOG == " + "8. symbol type: " + symbolType);
                System.out.println("LOG == " + "9. decoding letter: " + letter);
                System.out.println("LOG == " + "10.decoding modifier: " + modifier);
                System.out.println("LOG == " + "11.decoding time: " + decodingTime);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("scanVal", decodeValue);
                ((NexacroActivityExt)NexacroActivityExt.context).callMethod("scan", jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
