package ro.fmit.ac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button btnSignup;
    EditText edUser,edEmail,edPass,edCfPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnSignup=findViewById(R.id.btnSingUp);
        edUser=findViewById(R.id.edUsername);
        edEmail=findViewById(R.id.edEmail);
        edPass=findViewById(R.id.edEmails);
        edCfPass=findViewById(R.id.edCPassword);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edEmail.getText().toString().isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Adresa de email nu a fost completata", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(edUser.getText().toString().isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Numele nu a fost completat", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(edPass.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Parola nu a fost completata", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!edPass.getText().toString().equals(edCfPass.getText().toString()))
                {
                    Toast.makeText(RegisterActivity.this, "Parolele nu corespund", Toast.LENGTH_SHORT).show();
                    return;
                }
                RegisterRequest registerRequest =new RegisterRequest();
                registerRequest.setEmail(edEmail.getText().toString());
                registerRequest.setName(edUser.getText().toString());
                registerRequest.setPass(edPass.getText().toString());
                registerUser(registerRequest);
            }
        });
    }
    
    public void registerUser(RegisterRequest registerRequest){
        Call<RegisterResponse> registerResponseCall =ApiClient.getservice().registerUser(registerRequest);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful()){
                    String message= "Succesful register";
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                }else{
                    String message= "An error occured, please try again later";
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                String message= t.getLocalizedMessage();
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}