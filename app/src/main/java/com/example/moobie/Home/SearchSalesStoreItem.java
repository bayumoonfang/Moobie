package com.example.moobie.Home;

public class SearchSalesStoreItem {
    Integer int1;
    String text1 ;
    String text2 ;

    public SearchSalesStoreItem(
            Integer int1,
            String text1,
            String text2
    ) {
        this.int1 = int1;
        this.text1 = text1;
        this.text2 = text2;


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


}
