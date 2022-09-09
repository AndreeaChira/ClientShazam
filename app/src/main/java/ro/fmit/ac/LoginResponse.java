package ro.fmit.ac;

import java.io.Serializable;
import java.util.List;

public class LoginResponse implements Serializable {
    private String email;
    private String name;
    private String password;
    private List<Song> songs;
    private List<Integer> likedSongs;
    private String id;

    public List<Song> getSongList() {
        return songs;
    }

    public void setSongList(List<Song> songList) {
        this.songs = songList;
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
        return password;
    }

    public void setPass(String pass) {
        this.password = pass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
