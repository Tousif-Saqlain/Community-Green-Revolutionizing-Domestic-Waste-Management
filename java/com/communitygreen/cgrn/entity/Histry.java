package com.communitygreen.cgrn.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HISTRY")
public class Histry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int iID;
    private String image;
    private String address;

    @ManyToOne
    private User user;

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean equals(Object obj){
        return this.iID==((Histry)obj).getiID();
    }

//    @Override
//    public String toString() {
//        return "Histry{" +
//                "iID=" + iID +
//                ", image='" + image + '\'' +
//                ", address='" + address + '\'' +
//                ", user=" + user +
//                '}';
//    }
}
