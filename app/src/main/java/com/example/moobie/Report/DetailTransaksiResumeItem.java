package com.example.moobie.Report;

public class DetailTransaksiResumeItem {

    String text1 ;
    String text2 ;
    String text3 ;


    public DetailTransaksiResumeItem(
            String text1,
            String text2,
            String text3
    ) {

        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;

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

}
