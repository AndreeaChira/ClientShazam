package ro.fmit.ac;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class UploadPhotoActivity extends AppCompatActivity {

    private CardView cardView;
    private ImageView imageView;
    private Button buttonPhoto;
    private Button buttonSend;
    private CharSequence[] options= { "Camera","Gallery","Cancel"};

    private String selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        cardView=findViewById(R.id.cardViwe);
        imageView=findViewById(R.id.imageView);
        buttonPhoto=findViewById(R.id.btnTakePhoto);
        buttonSend=findViewById(R.id.btnSendPhoto);

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
                            Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(gallery,1);
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
                        imageView.setImageBitmap(image);
                    }
                    break;
                case 1:
                    if(resultCode==RESULT_OK &&data!=null){
                        Uri image=data.getData();
                        selectedImage=FileUtils.getPath(UploadPhotoActivity.this,image);
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
}