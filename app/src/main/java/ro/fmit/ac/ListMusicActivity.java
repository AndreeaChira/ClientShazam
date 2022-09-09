package ro.fmit.ac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class ListMusicActivity extends AppCompatActivity {


    ListView listView;
    String[] items;
    LoginResponse loginResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);

        //Assigning Address of the Android Materials
        listView = (ListView) findViewById(R.id.list);

        //Calling Method for asking permission
        runTimePermission();
        Intent intent=getIntent();
        if(intent.getExtras()!=null)
        {
            loginResponse=(LoginResponse) intent.getSerializableExtra("data");
            if(loginResponse!=null)
            {
                displaySongs(loginResponse);
            }
            //System.out.println(loginResponse.getSongList());


            List<Song> songsRecived= (List<Song>) intent.getSerializableExtra("uploadSongs");
            if(songsRecived!=null)
            {
                loginResponse.setSongList(songsRecived);
                loginResponse.setEmail("Andreea@gmail.com");
                loginResponse.setName("andreea");
                displaySongs(loginResponse);
            }


        }

    }

    //Method To Ask access for the external storeage
    public void runTimePermission() {
        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        //Method To Display songs
                        displaySong();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        //Keeps asking for external storage permission
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }


    public ArrayList<File> findSong(File file) {

        //ArrayList to store all songs
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();


        for (File singleFile : files) {

            //Adding the directory to arrayList if it is not hidden
            if (singleFile.isDirectory() && !singleFile.isHidden()) {

                arrayList.addAll(findSong(singleFile));

            } else {
                //Adding the single music file to ArrayList
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                    arrayList.add(singleFile);
                }
            }
        }

        return arrayList;
    }

    public void displaySong() {

        final ArrayList<File> mySongs = findSong(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music"));
        items = new String[mySongs.size()];

        //Adding all the music file without extensions to ArrayList
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }

        //Calling the adapter and setting it to ListView
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        //Implementing onClickListener for ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String songName = (String) listView.getItemAtPosition(i);

                //Calling the next intent and sending the Required Details to play the songs
                Intent intent = new Intent(getApplicationContext(), PlayMusicActivity.class);
                intent.putExtra("songs", mySongs);
                intent.putExtra("songname", songName);
                intent.putExtra("pos", i);
                startActivity(intent);
            }
        });
    }
    public void displaySongs(LoginResponse loginResponse){
        List<Song> songs=loginResponse.getSongList();
        for(Song song:songs){
            System.out.println(song.getName());
        }
        OnlineAdapter customAdapter=new OnlineAdapter(songs);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) listView.getItemAtPosition(i);
                NameSong songName = new NameSong();
                songName.setNameSong(song.getSubtitle()+" - "+song.getName());
                System.out.println("++++++++++++++++++++++++++"+i);
                //Call retrofit service to get song
                Retrofit retrofit=ApiClient.getRetrofi();
                RetrofitService service=retrofit.create(RetrofitService.class);
                Call call=service.callGetLinkByNameSong(songName);
                call.enqueue(new retrofit2.Callback() {
                    @Override
                    public void onResponse(Call call, retrofit2.Response response) {
                        NameSong link= (NameSong) response.body();
                        //download from link song

                        System.out.println("++++++++++++++++++++++++++"+link);
                        Intent intent = new Intent(getApplicationContext(), PlayMusicActivity.class);
                        intent.putExtra("link_online", link.getNameSong());
                        intent.putExtra("songname_online", song.getName());
                        intent.putExtra("pos", i);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.d("Error",t.getMessage());
                    }
                });

            }
        });
    }



    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //Returning the count of total songs in an ArrayList
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Inflating all the single music files in a Layout File
            View view = getLayoutInflater().inflate(R.layout.song_name_layout, null);
            TextView txtSong = view.findViewById(R.id.SongName);
            txtSong.setSelected(true);
            txtSong.setText(items[position]);
            return view;
        }
    }

    class OnlineAdapter extends BaseAdapter{
        private  List<Song> songs;
        public OnlineAdapter(List<Song> songs){
            this.songs=songs;
        }

        @Override
        public int getCount() {
            return songs.size();
        }

        @Override
        public Object getItem(int position) {
            return songs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.song_name_layout, null);
            TextView txtSong = view.findViewById(R.id.SongName);
            txtSong.setSelected(true);
            txtSong.setText(songs.get(position).getName()+"-"+songs.get(position).getSubtitle());
            return view;
        }
    }

}