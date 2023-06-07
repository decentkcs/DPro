package kr.co.decent.dpro.android.common;

import kr.co.decent.dpro.android.BuildConfig;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class CommonConstants {


    /**
     * 서버설정
     */
    public static final String APP_VERSION = BuildConfig.VERSION_NAME;
    public static final String PROJECT_NAME = BuildConfig.PROJECT_NAME;
    public static final String BASE_URL = BuildConfig.SERVER;
    public static final String MOBILE_PATH = "/ams/ams_mobile/";
    public static final String START_FILE = "start_android.json";
    public static final String GPS_RUL = BASE_URL + "/ams/restfulApiController/saveGps.do";

    /**
     * Nexacro
     */
    //nexacro 로 반환될 이벤트 속성명
    public static final String SVCID    = "svcid";
    public static final String REASON   = "reason";
    public static final String RETVAL   = "returnvalue";

    public static final int CODE_SUCCESS    = 0; //처리결과 코드(성공)
    public static final int CODE_ERROR      = -1; //처리결과 코드(실패)
    public static final int CODE_PERMISSION_ERROR  = -9; //앱 권한관련 에러코드

    public static final String ON_CALLBACK = "_oncallback";
    public static final String ON_RESUME = "_onresume";
    public static final String ON_PERMISSION_RESULT = "_onpermissionresult";
    public static final String CALL_METHOD = "callMethod";


    /**
     * GPS
     */
    public static final String TAG = "BackgroundLocationUpdateService";
    public static final String TAG_LOCATION = "TAG_LOCATION";

    /**
     * APP 권한
     */
    public interface REQUEST_PERMISSION {
        int CAMERA          = 101;
        int LOCATION        = 102;
        int STORAGE         = 103;
        int IGNORE_BATTERY  = 104;
        int LOCATION_ONE    = 105;
        int CALL_PHONE      = 106;
    }

    /**
     * API
     */
    public interface ApiInterface {
        @POST(GPS_RUL)
        Call<String> requestLocation(@Query("lat") String lat, @Query("lon") String lon, @Query("urkey") String urkey);
    }

    /**
     * TEST
     */
    public static final boolean IS_TEST = false;
}
