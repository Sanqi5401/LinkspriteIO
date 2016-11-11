package com.linksprite.io.network;

import com.linksprite.io.activity.AddDeviceActivity;
import com.linksprite.io.device.activity.led.LEDResponse;
import com.linksprite.io.network.model.AddDeviceRequest;
import com.linksprite.io.network.model.CreateDevRequest;
import com.linksprite.io.network.model.BaseDevRespone;
import com.linksprite.io.network.model.BaseRespone;
import com.linksprite.io.network.model.BaseUpdateRequest;
import com.linksprite.io.network.model.DevListRespone;
import com.linksprite.io.network.model.LedUpdateRequest;
import com.linksprite.io.network.model.LoginRequest;
import com.linksprite.io.network.model.LoginResponse;
import com.linksprite.io.network.model.ResetPasswordRequest;
import com.linksprite.io.network.model.UpdateRespone;
import com.linksprite.io.network.model.WeatherRespone;
import com.linksprite.io.utils.Constant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/2.
 */
public class ApiManager {
    private static com.squareup.okhttp.OkHttpClient getOkHttpClient() {
        com.squareup.okhttp.OkHttpClient okHttpClient = new com.squareup.okhttp.OkHttpClient();
        okHttpClient.setConnectTimeout(100000, TimeUnit.SECONDS);
        return okHttpClient;
    }

    private static Retrofit builder = new Retrofit.Builder()
            .baseUrl(Constant.URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();


    public interface LoginService {
        @Headers({"Content-Type: application/json"})
        @POST(Constant.LOGIN)
        Observable<LoginResponse> Login(@Body LoginRequest request);
    }

    public interface RegisterService {
        @Headers({"Content-Type: application/json"})
        @POST(Constant.REGISTER)
        Observable<LoginResponse> Register(@Body LoginRequest request);
    }

    public interface PasswordService {
        @Headers({"Content-Type: application/json"})
        @POST(Constant.PASSWORD)
        Observable<BaseRespone> ResetPassword(
                @Header("Authorization") String jwt,
                @Body ResetPasswordRequest resetPasswordRequest);
    }


    public interface CreateDevService {
        @POST(Constant.DEV)
        Observable<BaseDevRespone> createrDev(@Header("Authorization") String jwt,
                                              @Body CreateDevRequest addDevRequest);
    }


    public interface AddDeviceService {
        @POST(Constant.DEV + Constant.ADD)
        Observable<BaseDevRespone> addDev(@Header("Authorization") String jwt,
                                          @Body AddDeviceRequest addDevRequest);
    }

    public interface DevListService {
        @GET(Constant.DEV)
        Observable<List<DevListRespone>> getDevList(@Header("Authorization") String jwt);
    }

    public interface DeviceInfoService {
        @GET(Constant.DEV + "/{id}")
        Observable<BaseDevRespone<WeatherRespone>> getWeaterInfo(
                @Header("Authorization") String jwt,
                @Path("id") String id);
        @GET(Constant.DEV + "/{id}")
        Observable<BaseDevRespone<LEDResponse>> getLedInfo(
                @Header("Authorization") String jwt,
                @Path("id") String id);

    }

    public interface LedUpdateService {
        @Headers({"Content-Type: application/json"})
        @POST(Constant.UPDATE)
        Observable<UpdateRespone> updateLed(@Body BaseUpdateRequest<LEDResponse> request);
    }

    private static LoginService LOGIN = builder.create(LoginService.class);
    private static RegisterService REGISTER = builder.create(RegisterService.class);
    private static PasswordService RESETPASSWORD = builder.create(PasswordService.class);
    private static DevListService DEVLISTSERVICE = builder.create(DevListService.class);
    private static DeviceInfoService DEVINFO = builder.create(DeviceInfoService.class);
    private static CreateDevService CREATEDEIVCE = builder.create(CreateDevService.class);
    private static AddDeviceService ADDDEVICE = builder.create(AddDeviceService.class);

    private static LedUpdateService UPDATE = builder.create(LedUpdateService.class);

    public static LoginService getLoginService() {
        return LOGIN;
    }

    public static RegisterService getRegisterService() {
        return REGISTER;
    }

    public static PasswordService getPasswordService() {
        return RESETPASSWORD;
    }

    public static DevListService getDevListService() {
        return DEVLISTSERVICE;
    }

    public static DeviceInfoService getDevInfoService() {
        return DEVINFO;
    }

    public static CreateDevService getCreateDevService() {
        return CREATEDEIVCE;
    }

    public static AddDeviceService getAddDeviceService() {
        return ADDDEVICE;
    }

    public static LedUpdateService getDevUpdateService() {
        return UPDATE;
    }
}
