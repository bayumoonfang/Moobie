package com.example.moobie.Report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.moobie.R;

import java.util.List;


public class DetailTransaksiResumeAdapter extends ArrayAdapter<DetailTransaksiResumeItem> {

    private List<DetailTransaksiResumeItem> ActivitytList;
    private Context mCtx;
    public DetailTransaksiResumeAdapter(List<DetailTransaksiResumeItem> ActivitytList, Context mCtx) {
        super(mCtx, R.layout.grid_detailtransdaily, ActivitytList);
        this.ActivitytList = ActivitytList;
        this.mCtx = mCtx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View listViewItem = inflater.inflate(R.layout.grid_detailtransdaily, null, true);

        //getting text views
        TextView text1 = listViewItem.findViewById(R.id.txtNamaProduk);
        TextView text2 = listViewItem.findViewById(R.id.txtQty);
        TextView text3 = listViewItem.findViewById(R.id.txtTotalJual);

        //Getting the hero for the specified position
        DetailTransaksiResumeItem detailItem = ActivitytList.get(position);

        //setting hero values to textviews
        text1.setText(detailItem.getText1());
        text2.setText(detailItem.getText2());
        text3.setText(detailItem.getText3());


        return listViewItem;
    }
}
