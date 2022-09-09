package ro.fmit.ac;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitService {


    @POST("/users/login")
    Call<LoginResponse>loginUser(@Body LoginRequest loginRequest);


    @POST("/users/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);


    @Multipart
    @POST("/detect/trackMood")
    Call<emotion> callUploadAPI(@Part MultipartBody.Part image, @Part("somedata") RequestBody requestBody);

    @Multipart
    @POST("detect/tracksByMood")
    Call<List<Song>> callListSongsByMood(@Part MultipartBody.Part image, @Part("somedata") RequestBody requestBody);

}
