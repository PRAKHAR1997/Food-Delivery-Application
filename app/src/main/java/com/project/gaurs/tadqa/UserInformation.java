package com.project.gaurs.tadqa;

/**
 * Created by gaurs on 5/29/2017.
 */

public class UserInformation {
    public String num, name, id;

    public UserInformation() {
    }

    public UserInformation(String num, String name) {
        this.num = num;
        this.name = name;
    }

    public UserInformation(String name, String num, String id) {
        this.id = id;
        this.num = num;
        this.name = name;
    }
}
