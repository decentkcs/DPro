package kr.co.decent.dpro.android.nexacro;

import kr.co.decent.dpro.android.common.CommonConstants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    public static Retrofit getInstance(){
        if( retrofit == null )
        {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl( CommonConstants.BASE_URL);
            builder.addConverterFactory(ScalarsConverterFactory.create());
            builder.addConverterFactory( GsonConverterFactory.create() );  // 받아오는 Json 구조의 데이터를 객체 형태로 변환
            retrofit = builder.build();
        }
        return retrofit;
    }
}
