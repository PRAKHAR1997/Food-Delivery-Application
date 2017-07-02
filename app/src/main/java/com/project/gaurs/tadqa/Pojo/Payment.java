package com.project.gaurs.tadqa.Pojo;

/**
 * Created by gaurs on 6/9/2017.
 */

public class Payment {
    String paymentMethod;
    int photoId;

    public Payment(String paymentMethod, int photoId) {
        this.paymentMethod = paymentMethod;
        this.photoId = photoId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
