package ro.fmit.ac;

import okhttp3.MultipartBody;
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
    @POST("uploadfile")
    Call<FileModel> callUploadAPI(@Part MultipartBody.Part image);


}
