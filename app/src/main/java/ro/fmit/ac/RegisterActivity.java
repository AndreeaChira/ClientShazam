package ro.fmit.ac;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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
    }
}