package kr.co.decent.dpro.android.scan;

import static kr.co.decent.dpro.android.common.M3ConstantValues.SCANNER_ACTION_BARCODE;
import static kr.co.decent.dpro.android.common.M3ConstantValues.SCANNER_ACTION_IS_ENABLE_ANSWER;
import static kr.co.decent.dpro.android.common.M3ConstantValues.SCANNER_EXTRA_BARCODE_CODE_TYPE;
import static kr.co.decent.dpro.android.common.M3ConstantValues.SCANNER_EXTRA_BARCODE_DATA;
import static kr.co.decent.dpro.android.common.M3ConstantValues.SCANNER_EXTRA_BARCODE_DATA_LENGTH;
import static kr.co.decent.dpro.android.common.M3ConstantValues.SCANNER_EXTRA_BARCODE_DEC_COUNT;
import static kr.co.decent.dpro.android.common.M3ConstantValues.SCANNER_EXTRA_BARCODE_RAW_DATA;
import static kr.co.decent.dpro.android.common.M3ConstantValues.SCANNER_EXTRA_IS_ENABLE_ANSWER;
import static kr.co.decent.dpro.android.common.M3ConstantValues.SCANNER_EXTRA_MODULE_TYPE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import kr.co.decent.dpro.android.nexacro.NexacroActivityExt;

public class ScanReceiverM3 extends BroadcastReceiver {

    private String barcode;
    private String type;
    private String module;
    private byte[] rawdata;
    private int length;
    private int decCount;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(SCANNER_ACTION_BARCODE)) {
                barcode = intent.getExtras().getString(SCANNER_EXTRA_BARCODE_DATA);
                type = intent.getExtras().getString(SCANNER_EXTRA_BARCODE_CODE_TYPE);
                module = intent.getExtras().getString(SCANNER_EXTRA_MODULE_TYPE);
                try {
                    rawdata = intent.getExtras().getByteArray(SCANNER_EXTRA_BARCODE_RAW_DATA);
                } catch (Exception e) {
                    System.out.println( "onReceive scanner - null raw data");
                }
                length = intent.getExtras().getInt(SCANNER_EXTRA_BARCODE_DATA_LENGTH, 0);
                decCount = intent.getExtras().getInt(SCANNER_EXTRA_BARCODE_DEC_COUNT, 0);

                if(barcode != null){
                    if(rawdata.length > 0){
                        String strRawData = "";
                        for(int i = 0; i< rawdata.length; i++){
                            strRawData += String.format("0x%02X ", (int)rawdata[i]&0xFF);
                        }
                        System.out.println("data: " + barcode + " \ntype: " + type + " \nraw: " + strRawData);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("scanVal", barcode);
                        ((NexacroActivityExt)NexacroActivityExt.context).callMethod("scanReceiverM3", jsonObject);
                    }else{
                        System.out.println("data: " + barcode + " type: " + type);
                    }
                }else{
                    int nSymbol = intent.getExtras().getInt("symbology", -1);
                    int nValue = intent.getExtras().getInt("value", -1);

                    System.out.println("getSymbology ["+ nSymbol + "][" + nValue + "]");
                    if(nSymbol != -1){
                        System.out.println(Integer.toString(nSymbol));
                        System.out.println(Integer.toString(nValue));
                    }
                }
            }else if(intent.getAction().equals(SCANNER_ACTION_IS_ENABLE_ANSWER)){
                boolean bEnable = intent.getBooleanExtra(SCANNER_EXTRA_IS_ENABLE_ANSWER, false);
                System.out.println("is enable scanner [" + bEnable + "]");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
