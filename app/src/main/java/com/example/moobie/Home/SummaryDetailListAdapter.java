package com.example.moobie.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.moobie.R;

import java.util.List;


public class SummaryDetailListAdapter extends ArrayAdapter<SummaryDetailItem> {

    private List<SummaryDetailItem> ActivitytList;
    private Context mCtx;
    public SummaryDetailListAdapter(List<SummaryDetailItem> ActivitytList, Context mCtx) {
        super(mCtx, R.layout.listitem_summaryproductsales, ActivitytList);
        this.ActivitytList = ActivitytList;
        this.mCtx = mCtx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View listViewItem = inflater.inflate(R.layout.listitem_summaryproductsales, null, true);

        //getting text views
        TextView text1 = listViewItem.findViewById(R.id.txtSummaryKodeProduk);
        TextView text2 = listViewItem.findViewById(R.id.txtSummaryNamaProduk);
        TextView text3 = listViewItem.findViewById(R.id.txtSummaryQtyProduk);

        //Getting the hero for the specified position
        SummaryDetailItem detailItem = ActivitytList.get(position);

        //setting hero values to textviews
        text1.setText(detailItem.getText1());
        text2.setText(detailItem.getText2());
        text3.setText(detailItem.getText3());

        return listViewItem;
    }
}
