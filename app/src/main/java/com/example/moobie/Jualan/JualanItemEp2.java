package com.example.moobie.Jualan;

public class JualanItemEp2 {

    String text1, text2,  image1, text3;
    Integer text4;

    public JualanItemEp2(String text1,
                      String text2,
                      Integer text4,
                      String image1,
                      String text3) {
        this.text1 = text1;
        this.text2 = text2;

        this.text4 = text4;
        this.image1 = image1;
        this.text3 = text3;
    }

    public String getText1() {
        return text1;
    }
    public String getText2() {
        return text2;
    }
    public Integer getText4() {
        return text4;
    }
    public String getImage1() {
        return image1;
    }
    public String getText3() {
        return text3;
    }
}


