package com.example.proyek_sdp;

public class WishList_class {
    private String id_wishlist,id_user,id_barang;
    public WishList_class() { }
    public WishList_class(String id_wishlist, String id_user, String id_barang) {
        this.id_wishlist = id_wishlist;
        this.id_user = id_user;
        this.id_barang = id_barang;
    }

    public String getId_wishlist() {
        return id_wishlist;
    }

    public void setId_wishlist(String id_wishlist) {
        this.id_wishlist = id_wishlist;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }
}
