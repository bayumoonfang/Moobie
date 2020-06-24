package com.example.moobie.Jualan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.moobie.Function.RoundRectCornerImageView;
import com.example.moobie.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class JualanAdapter extends ArrayAdapter<JualanItem> {

    private List<JualanItem> projectList;
    private Context mCtx;

    public JualanAdapter(List<JualanItem> projectList, Context mCtx) {
        super(mCtx, R.layout.grid_jualanproduk, projectList);
        this.projectList = projectList;
        this.mCtx = mCtx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View listViewItem = inflater.inflate(R.layout.grid_jualanproduk, null, true);
        TextView valtext1 = (TextView) listViewItem.findViewById(R.id.txtVal1);
        TextView valtext2 = (TextView) listViewItem.findViewById(R.id.txtVal2);
        TextView valint1 = (TextView) listViewItem.findViewById(R.id.txtVal3);
        RoundRectCornerImageView circleImageView = (RoundRectCornerImageView) listViewItem.findViewById(R.id.ImgVal);
        TextView valtext3 = (TextView) listViewItem.findViewById(R.id.txtVal4);

        JualanItem itemAdapter = projectList.get(position);
        // Locale localeID = new Locale("in", "ID");
        //NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        valtext1.setText(itemAdapter.getText1());
        valtext2.setText(itemAdapter.getText2());

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        valint1.setText(decimalFormat.format(itemAdapter.getText4()));

        if (itemAdapter.getImage1().equals("null") || itemAdapter.getImage1().equals("") ) {
            Picasso.get().load(R.drawable.noimage)
                    //.placeholder(R.drawable.ic_wait)
                    //.resize(95, 90)
                    //.error(R.drawable.ic_wait)
                    .fit().centerCrop()
                    .into(circleImageView);
        } else  {
            Picasso.get().load("http://duakata-dev.com/moobi/UAT2/media/images/photo/"+itemAdapter.getImage1())
                    //.placeholder(R.drawable.ic_wait)
                    //.resize(95, 90)
                    //.error(R.drawable.ic_wait)
                    .fit().centerCrop()
                    .into(circleImageView);
        }

        valtext3.setText(itemAdapter.getText3());

        return listViewItem;
    }
}