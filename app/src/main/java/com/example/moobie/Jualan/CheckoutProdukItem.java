package com.example.moobie.Home;

public class CheckoutProdukItem {
    Integer int1;
    String text1 ;
    String text2 ;
    String text3 ;
    String text4 ;
    public CheckoutProdukItem(
            Integer int1,
            String text1,
            String text2,
            String text3,
            String text4
    ) {
        this.int1 = int1;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;

    }
    public Integer getInt1() {
        return int1;
    }
    public String getText1() {
        return text1;
    }
    public String getText2() {
        return text2;
    }
    public String getText3() {
        return text3;
    }
    public String getText4() {
        return text4;
    }

}
