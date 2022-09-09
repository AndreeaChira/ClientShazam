package ro.fmit.ac;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadPhotoActivity extends AppCompatActivity {

    private CardView cardView;
    private ImageView imageView;
    private Button buttonPhoto;
    private Button buttonSend;
    private CharSequence[] options= { "Camera","Gallery","Cancel"};
    private String filePath;
    private TextView textViewMood;

    private String selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        cardView=findViewById(R.id.cardViwe);
        imageView=findViewById(R.id.imageView);
        buttonPhoto=findViewById(R.id.btnTakePhoto);
        buttonSend=findViewById(R.id.btnSendPhoto);
        textViewMood=findViewById(R.id.textForMood);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(UploadPhotoActivity.this);
                builder.setTitle("Select Image");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(options[i].equals("Camera"))
                        {
                            Intent takePic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            startActivityForResult(takePic,0);
                        }
                        else if(options[i].equals("Gallery"))
                        {
//                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                            intent.setType("image/*");


                            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");

//                            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
//                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                            startActivityForResult(pickIntent, 1);
//                            Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            gallery.setType("image/*");
//                            startActivityForResult(gallery,1);
                        }
                        else
                        {
                            dialogInterface.dismiss();
                        }
                    }
                });

                builder.show();
            }
        });

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filePath!=null)
                    uploadImageandgetMood();
                else
                {
                    String message= "Please select one photo";
                    Toast.makeText(UploadPhotoActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filePath.isEmpty()){
                    String message= "Please select one photo";
                    Toast.makeText(UploadPhotoActivity.this, message, Toast.LENGTH_LONG).show();
                }else
                    if(textViewMood.equals("Here will apear your mood"))
                    {
                        String message= "Please check your mood";
                        Toast.makeText(UploadPhotoActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                    else
                        uploadImageAndGetList();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= RESULT_CANCELED)
        {
            switch (requestCode)
            {
                case 0:
                    if(resultCode==RESULT_OK&& data!=null){
                        Bitmap image =(Bitmap) data.getExtras().get("data");
                        selectedImage=FileUtils.getPath(UploadPhotoActivity.this,getImageUri(UploadPhotoActivity.this,image));
                        filePath=FileUtils.getPath(UploadPhotoActivity.this,getImageUri(UploadPhotoActivity.this,image));

                        imageView.setImageBitmap(image);

                    }
                    break;
                case 1:
                    if(resultCode==RESULT_OK &&data!=null){
                        Uri image=data.getData();
                        selectedImage=FileUtils.getPath(UploadPhotoActivity.this,image);
                        filePath=FileUtils.getPath(UploadPhotoActivity.this,image);
                        Picasso.get().load(image).into(imageView);
                    }
            }
        }
    }
    public Uri getImageUri(Context context, Bitmap bitmap)
    {
        String path=MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap,"MyImage","");
        return Uri.parse(path);
    }

    public void uploadImageandgetMood(){
        File file=new File(filePath);
        Retrofit retrofit= ApiClient.getRetrofi();
        RequestBody requestBody=RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(),requestBody);

        RequestBody someData=RequestBody.create(MediaType.parse("text/plain"),"image");


        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<emotion> call=retrofitService.callUploadAPI(part,someData);
        call.enqueue(new Callback<emotion>() {
            @Override
            public void onResponse(Call<emotion> call, Response<emotion> response) {
                System.out.println("========================="+response.message());
                if(response.isSuccessful())
                {
                    emotion emotions = response.body();
                    System.out.println(emotions);
                    System.out.println(emotions.getEmotion());
                    textViewMood.setText(emotions.getEmotion());
                }
                else{
                    String message= "An error occured, please try again later";
                    Toast.makeText(UploadPhotoActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<emotion> call, Throwable t) {
                String message= t.getLocalizedMessage();
                Toast.makeText(UploadPhotoActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void uploadImageAndGetList() {
        File file=new File(filePath);
        Retrofit retrofit= ApiClient.getRetrofi();
        RequestBody requestBody=RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(),requestBody);
        RequestBody someData=RequestBody.create(MediaType.parse("text/plain"),"image");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<LoginResponse> call=retrofitService.callListSongsByMood(part,someData);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful())
                {
                    LoginResponse loginResponse = response.body();
                    startActivity(new Intent(UploadPhotoActivity.this,ListMusicActivity.class).putExtra("data", loginResponse));
                }
                else{
                    String message= "An error occured, please try again later";
                    Toast.makeText(UploadPhotoActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String message= t.getLocalizedMessage();
                Toast.makeText(UploadPhotoActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

}