package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Allen on 2018/7/3.
 */

public class User {
    @SerializedName("uid")
    private String uid;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("real_name")
    private String realName;
    @SerializedName("email")
    private String email;
    @SerializedName("student_number")
    private String studentNumber;
    @SerializedName("photo")
    private String photo;
    @SerializedName("phone")
    private String phone;
//    @SerializedName("school")
//    private School school;
    @SerializedName("gender")
    private Integer gender = 0; // may be null sometimes

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getRealName() {
        return realName;
    }

    public String getEmail() {
        return email;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhone() {
        return phone;
    }

//    public School getSchool() {
//        return school;
//    }

    public Integer getGender() {
        return gender;
    }

    public String getGenderString() {
        if (gender == 2) // FIXME: use enum
            return "女";
        if (gender == 1)
            return "男";
        return "保密";
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public void setSchool(School school) {
//        this.school = school;
//    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}
