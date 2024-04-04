package com.example.expensemanager.Model;

public class userid {
    public String uid;
  public String email;

    public userid(String email,String uid) {
        this.uid = uid;
    }

    public  String getId() {

        return uid;
    }

    public void setId(String id) {
        this.uid = id;
    }
    public void setEmail(String email)
    {
        this.email=email;
    }
    public  String getEmail() {
        return email;
    }
    public userid()
    {

    }
}

