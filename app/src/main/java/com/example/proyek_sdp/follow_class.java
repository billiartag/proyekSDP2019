package com.example.proyek_sdp;

public class follow_class {
    private String id_follow,id_user,following;
    public follow_class(){}
    public follow_class(String id_follow, String id_user, String following) {
        this.id_follow = id_follow;
        this.id_user = id_user;
        this.following = following;
    }

    public String getId_follow() {
        return id_follow;
    }

    public void setId_follow(String id_follow) {
        this.id_follow = id_follow;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
}
