package kr.co.decent.dpro.android.common;

/**
 * Created by Jayden on 2018-04-03.
 */

public class M3ConstantValues {

    public static final String SCANNER_ACTION_SETTING_CHANGE = "com.android.server.scannerservice.settingchange";
    public static final String LRSCANNER_ACTION_SETTING_CHANGE = "com.android.server.lrscannerservice.settingchange";
    public static final String SCANNER_ACTION_PARAMETER = "android.intent.action.SCANNER_PARAMETER";
    public static final String SCANNER_ACTION_ENABLE = "com.android.server.scannerservice.m3onoff";
    public static final String LRSCANNER_ACTION_ENABLE = "com.android.server.lrscannerservice.m3onoff";
    public static final String SCANNER_EXTRA_ENABLE = "scanneronoff";

    public static final String SCANNER_ACTION_START = "android.intent.action.M3SCANNER_BUTTON_DOWN";
    public static final String SCANNER_ACTION_CANCEL = "android.intent.action.M3SCANNER_BUTTON_UP";
    public static final String LRSCANNER_ACTION_START = "android.intent.action.LRSCANNER_BUTTON_DOWN";
    public static final String LRSCANNER_ACTION_CANCEL = "android.intent.action.LRSCANNER_BUTTON_UP";

    public static final String SCANNER_ACTION_BARCODE = "com.android.server.scannerservice.broadcast";

    public static final String SCANNER_EXTRA_BARCODE_DATA = "m3scannerdata";
    public static final String SCANNER_EXTRA_BARCODE_CODE_TYPE = "m3scanner_code_type";
    public static final String SCANNER_EXTRA_MODULE_TYPE = "m3scanner_module_type";
    public static final String SCANNER_EXTRA_BARCODE_RAW_DATA = "m3scannerdata_raw";	// add 2017-01-17	after ScanEmul 1.3.0
    public static final String SCANNER_EXTRA_BARCODE_DATA_LENGTH = "m3scannerdata_length";	// add 2017-01-31	after ScanEmul 1.3.0
    public static final String SCANNER_EXTRA_BARCODE_DEC_COUNT = "m3scanner_dec_count"; // add 2018-10-08   after ScanEmul 2.2.3

    public static final String SCANNER_ACTION_IS_ENABLE = "com.android.server.scannerservice.m3onoff.ison";
    public static final String SCANNER_ACTION_IS_ENABLE_ANSWER = "com.android.server.scannerservice.m3onoff.ison.answer";
    public static final String SCANNER_EXTRA_IS_ENABLE_ANSWER = "ison";

    // add 20190730 after ScanEmul 2.4.6
    public static final String SCANNER_ACTION_STATUS = "scanemul.action.status";
    public static final String SCANNER_EXTRA_STATUS = "scanemul.extra.status";
    public static final int SCANNER_STATUS_NO_ERROR = 0;
    public static final int SCANNER_STATUS_SCANNER_OPEN_FAIL = 1;
    public static final int SCANNER_STATUS_SCANNER_CLOSE_FAIL = 2;
    public static final int SCANNER_STATUS_SCANNER_OPEN_SUCCESS = 4;
    public static final int SCANNER_STATUS_SCANNER_CLOSE_SUCCESS = 8;

    // add 20200226 imageCapture
    public static final String SCANNER_ACTION_TAKE_PICTURE = "android.intent.action.SCANNER_TAKE_PICTURE";
    public static final String SCANNER_ACTION_TAKE_PICTURE_PATH = "android.intent.action.SCANNER_TAKE_PICTURE_PATH";
    public static final String SCANNER_EXTRA_TAKE_PICTURE_PATH = "take_picture_path";
}
