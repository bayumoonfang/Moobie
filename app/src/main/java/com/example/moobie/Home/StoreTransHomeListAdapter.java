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


public class StoreTransHomeListAdapter extends ArrayAdapter<StoreTransHomeItem> {

    private List<StoreTransHomeItem> ActivitytList;
    private Context mCtx;
    public StoreTransHomeListAdapter(List<StoreTransHomeItem> ActivitytList, Context mCtx) {
        super(mCtx, R.layout.listitem_home, ActivitytList);
        this.ActivitytList = ActivitytList;
        this.mCtx = mCtx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View listViewItem = inflater.inflate(R.layout.listitem_home, null, true);

        //getting text views
        TextView int1 = listViewItem.findViewById(R.id.storsalesHome);
        TextView text1 = listViewItem.findViewById(R.id.storenameHome);
        TextView text2 = listViewItem.findViewById(R.id.storeinfoHome);
        TextView text3 = listViewItem.findViewById(R.id.idValList1);
        //TextView text4 = listViewItem.findViewById(R.id.idValList1);


        //Getting the hero for the specified position
        StoreTransHomeItem detailItem = ActivitytList.get(position);
       /* Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        //int1.setText(formatRupiah.format((double)detailItem.getInt1()));*/



        //setting hero values to textviews
        text1.setText(detailItem.getText1());
        text2.setText(detailItem.getText2());
        text3.setText(detailItem.getText3());

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        int1.setText(decimalFormat.format(detailItem.getInt1()));

        /*final String idVal1 = detailItem.getText3();
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, DetailSaleStoreActivity.class);
                i.putExtra("id",idVal1);
                mCtx.startActivity(i);
            }
        });*/

        return listViewItem;
    }
}
