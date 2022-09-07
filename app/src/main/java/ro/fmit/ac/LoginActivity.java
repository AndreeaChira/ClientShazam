package ro.fmit.ac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextInputEditText edEmail, edPasswd;
    TextView noAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin=findViewById(R.id.btnLogin);
        edEmail=findViewById(R.id.et_email);
        edPasswd=findViewById(R.id.edEmails);
        noAcc=findViewById(R.id.tvCreateAcc);

        noAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edEmail.getText().toString())|| TextUtils.isEmpty(edPasswd.getText().toString()))
                    Toast.makeText(LoginActivity.this, "Fill all inputs", Toast.LENGTH_SHORT).show();
                else
                {
                    LoginRequest loginRequest=new LoginRequest();
                    loginRequest.setEmail(edEmail.getText().toString());
                    loginRequest.setPass(edPasswd.getText().toString());
                    loginUser(loginRequest);
                }

            }
        });
    }

    public void loginUser(LoginRequest  loginRequest){
        Call<LoginResponse> loginResponseCall=ApiClient.getservice().loginUser(loginRequest);
        loginResponseCall.enqueue((new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
            if(response.isSuccessful()){
                LoginResponse loginResponse =response.body();
                startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtra("data",loginResponse));
                finish();
            }else
                {
                     String message= "An error occured, please try again later";
                     Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String message= t.getLocalizedMessage();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }));
    }


}