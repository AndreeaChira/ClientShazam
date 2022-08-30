package ro.fmit.ac;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService {


    @POST("/")
    Call<LoginResponse>loginUser(@Body LoginRequest loginRequest);


    @POST("")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

}
