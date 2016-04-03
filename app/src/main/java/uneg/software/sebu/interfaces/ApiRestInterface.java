package uneg.software.sebu.interfaces;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import uneg.software.sebu.models.Alarma;
import uneg.software.sebu.models.BaseResponse;
import uneg.software.sebu.models.LoginResponse;

/**
 * Created by Jhonny on 18/8/2015.
 */
public interface ApiRestInterface {
    @FormUrlEncoded
    @POST("/login")
    void doLogin(
            @Field("username") String username,
            @Field("password") String password,
            Callback<LoginResponse> callback);


    @FormUrlEncoded
    @POST("/alarma/subirFoto")
    void subirFoto(
            @Field("foto") String foto,
            Callback<BaseResponse> callback);

    @FormUrlEncoded
    @POST("/alarma")
    void sendAlarma(
            @Field("usuario") String usuario,
            @Field("descripcion") String descripcion,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("area") String area,
            @Field("fotos") String[] fotos,
            Callback<Alarma> callback);

    @FormUrlEncoded
    @POST("/recuperar")
    void solicitarCambioPass(
            @Field("correo") String correo,
            Callback<BaseResponse> callback);
}