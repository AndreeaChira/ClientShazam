package ro.fmit.ac;

import java.io.Serializable;
import java.util.List;

public class LoginResponse implements Serializable {
    private String email;
    private String name;
    private String pass;
    List<Song> songList;
    List<Integer> likedSongs;

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public List<Integer> getLikedSongs() {
        return likedSongs;
    }

    public void setLikedSongs(List<Integer> likedSongs) {
        this.likedSongs = likedSongs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    //private songs
    //private favsongs
}
