package com.example.moobie.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.moobie.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;


public class SearchSalesStoreListAdapter extends ArrayAdapter<SearchSalesStoreItem> {

    private List<SearchSalesStoreItem> ActivitytList;
    private Context mCtx;
    public SearchSalesStoreListAdapter(List<SearchSalesStoreItem> ActivitytList, Context mCtx) {
        super(mCtx, R.layout.listitem_searchtranshome, ActivitytList);
        this.ActivitytList = ActivitytList;
        this.mCtx = mCtx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View listViewItem = inflater.inflate(R.layout.listitem_searchtranshome, null, true);

        //getting text views
        TextView int1 = listViewItem.findViewById(R.id.txtTransAmount2);
        TextView text1 = listViewItem.findViewById(R.id.txtTransOrderNumb2);
        TextView text2 = listViewItem.findViewById(R.id.txtTransCust2);

        //Getting the hero for the specified position
        SearchSalesStoreItem detailItem = ActivitytList.get(position);

        //setting hero values to textviews

        text1.setText(detailItem.getText1());
        text2.setText(detailItem.getText2());


        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        int1.setText(decimalFormat.format(detailItem.getInt1()));

        return listViewItem;
    }
}
