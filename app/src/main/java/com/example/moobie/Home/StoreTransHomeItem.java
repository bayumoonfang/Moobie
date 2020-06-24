package com.example.moobie.Home;

public class StoreTransHomeItem {
    Integer int1;
    String text1 ;
    String text2 ;
    String text3 ;
    public StoreTransHomeItem(
            Integer int1,
            String text1,
            String text2,
            String text3
    ) {
        this.int1 = int1;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;

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

}
