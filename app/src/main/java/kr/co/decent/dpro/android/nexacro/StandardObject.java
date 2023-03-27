package kr.co.decent.dpro.android.nexacro;

import com.nexacro.NexacroActivity;
import com.nexacro.plugin.NexacroPlugin;

import org.json.JSONObject;

import kr.co.decent.dpro.android.common.CommonConstants;

/*
    Class Name      : StandardObject
    Description     : NexacroPlugin 상속받아 앱과 nexacro 화면의 데이터를 주고 받기위해 재정의한 공통 Class
 */
public class StandardObject extends NexacroPlugin{
    private final String LOG_TAG = this.getClass().getSimpleName(); //현재 Class Name 문자열

    public static StandardObject context;
    private String mServiceId = "dpro_android";         //화면에서 callMethod 호출시 method id
    private JSONObject mParamData = null;   //화면에서 callMethod 호출시 Parameter
    private NexacroActivityExt nexaExtObj = null;

    /* 생성시 NexacroActivity 와 연결을 위한 작업    */
    public StandardObject(String objectId){
        super(objectId);
        nexaExtObj = (NexacroActivityExt) NexacroActivity.getInstance();
        nexaExtObj.setPlugin(this);
        context = this;
    }

    /* Nexacro 화면에서 new nexacro.Standard() 호출시 발생    */
    @Override
    public void init(JSONObject paramObject){

    }

    /* Nexacro 화면에서 Standard.destroy() 호출시 발생    */
    @Override
    public void release(JSONObject paramObject){
        nexaExtObj.stopGpsService();
    }

    /* Nexacro 화면에서 StandardObject 의 callMethod 를 호출시 발생    */
    @SuppressWarnings("unchecked")
    @Override
    public void execute(String method, JSONObject paramObject){
        mServiceId = "";
        mParamData = null;

        //화면에서 호출된 메소드 명이 "callMethod" 일시
        if(method.equals(CommonConstants.CALL_METHOD)){
            try{
                JSONObject params = paramObject.getJSONObject("params");   //화면에서 받은 데이터
                mServiceId = params.getString("serviceid");       //화면에서 받은 메소드 ID 문자열
                mParamData = params.getJSONObject("param");               //화면에서 받은 Json 형태 Parameter

                nexaExtObj.callMethod(mServiceId, mParamData);
            }catch(Exception e){
                e.printStackTrace();
                send(CommonConstants.CODE_ERROR, CommonConstants.CALL_METHOD + ":" + e.getMessage(), CommonConstants.ON_CALLBACK);
            }
        }
    }

    /*
        Nexacro 화면으로 데이터 반환을 위한 메소드

        reason : 화면으로 넘길 반환코드 (CODE_SUCCES, CODE_ERROR, CODE_PERMISSION_ERROR)
        retval : 화면으로 넘길 데이터
        action : 화면에서 호출될 이벤트 (ON_CALLBACK, ON_RESUME, ON_PERMISSION_RESULT)
        */
    public boolean send(int reason, Object retval, String action){
        JSONObject obj = new JSONObject();
        try{
            obj.put(CommonConstants.SVCID, mServiceId);     //callMethod 호출에 의한 callback 이 아니면 값은 없다.
            obj.put(CommonConstants.REASON, reason);
            obj.put(CommonConstants.RETVAL, retval);

            callback(action, obj);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            mServiceId = null;
            mParamData = null;
        }
        return true;
    }

    public boolean sendScan(int reason, Object retval, String action){
        JSONObject obj = new JSONObject();
        try{
            obj.put(CommonConstants.SVCID, "scan");     //callMethod 호출에 의한 callback 이 아니면 값은 없다.
            obj.put(CommonConstants.REASON, reason);
            obj.put(CommonConstants.RETVAL, retval);

            callback(action, obj);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            mServiceId = null;
            mParamData = null;
        }
        return true;
    }

    /* 예약된 이벤트명을 반환 */
    public String getActionString (String action){
        if(action.equals(CommonConstants.ON_CALLBACK)){
            return CommonConstants.ON_CALLBACK;
        }else if(action.equals(CommonConstants.ON_RESUME)) {
            return CommonConstants.ON_RESUME;
        }else if(action.equals(CommonConstants.ON_PERMISSION_RESULT)) {
            return CommonConstants.ON_PERMISSION_RESULT;
        }else if(action.equals(CommonConstants.CALL_METHOD)){
            return CommonConstants.CALL_METHOD;
        }
        return "";
    }
}
