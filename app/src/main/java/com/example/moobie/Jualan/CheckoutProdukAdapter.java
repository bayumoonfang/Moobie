package com.example.moobie.Jualan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.moobie.Home.CheckoutProdukItem;
import com.example.moobie.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class CheckoutProdukAdapter extends ArrayAdapter<CheckoutProdukItem> {

    private List<CheckoutProdukItem> ActivitytList;
    private Context mCtx;
    public CheckoutProdukAdapter(List<CheckoutProdukItem> ActivitytList, Context mCtx) {
        super(mCtx, R.layout.grid_checkoutproduk, ActivitytList);
        this.ActivitytList = ActivitytList;
        this.mCtx = mCtx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View listViewItem = inflater.inflate(R.layout.grid_checkoutproduk, null, true);

        //getting text views
        TextView int1 = listViewItem.findViewById(R.id.txtDetailResumeAmount);
        TextView text1 = listViewItem.findViewById(R.id.txtDetailResumeNamamenu);
        TextView text2 = listViewItem.findViewById(R.id.txtDetailResumeQty);
        TextView text3 = listViewItem.findViewById(R.id.txtDetailResumeDetail);
        final TextView text4 = listViewItem.findViewById(R.id.txtOrderNumber);
        RelativeLayout hapus = listViewItem.findViewById(R.id.cekme2);
        //final RelativeLayout check1 = listViewItem.findViewById(R.id.cekme2);
        //final RelativeLayout check2 = listViewItem.findViewById(R.id.layOut2);
        //RoundRectCornerImageView circleImageView = (RoundRectCornerImageView) listViewItem.findViewById(R.id.imagelistHome);

        //Getting the hero for the specified position
        CheckoutProdukItem detailItem = ActivitytList.get(position);

        //setting hero values to textviews
        text1.setText(detailItem.getText1());
        text2.setText(detailItem.getText2());

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        int1.setText(decimalFormat.format(detailItem.getInt1()));
        text3.setText(detailItem.getText3());
        text4.setText(detailItem.getText4());



       /* if (detailItem.getImage1().equals("null") || detailItem.getImage1().equals("") ) {
            Picasso.get().load(R.drawable.noimage)
                    //.placeholder(R.drawable.ic_wait)
                    //.resize(95, 90)
                    //.error(R.drawable.ic_wait)
                    .fit().centerCrop()
                    .into(circleImageView);
        } else  {
            Picasso.get().load("http://duakata-dev.com/moobi/UAT2/media/images/photo/"+detailItem.getImage1())
                    //.placeholder(R.drawable.ic_wait)
                    //.resize(95, 90)
                    //.error(R.drawable.ic_wait)
                    .fit().centerCrop()
                    .into(circleImageView);
        }*/

        return listViewItem;
    }


}
