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

public class PreviewStrukAdapter extends ArrayAdapter<PreviewStrukItem> {

    private List<PreviewStrukItem> projectList;
    private Context mCtx;

    public PreviewStrukAdapter(List<PreviewStrukItem> projectList, Context mCtx) {
        super(mCtx, R.layout.grid_strukproduk, projectList);
        this.projectList = projectList;
        this.mCtx = mCtx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View listViewItem = inflater.inflate(R.layout.grid_strukproduk, null, true);
        TextView valtext1 = (TextView) listViewItem.findViewById(R.id.txtNamaMenu);
        TextView valtext2 = (TextView) listViewItem.findViewById(R.id.txtDetailMenu);
        TextView valtext3 = (TextView) listViewItem.findViewById(R.id.txtCommentMenu);
        TextView valtext4 = (TextView) listViewItem.findViewById(R.id.txtHargaMenu);

        PreviewStrukItem itemAdapter = projectList.get(position);
        valtext1.setText(itemAdapter.getText1());
        valtext2.setText(itemAdapter.getText2());
        if (itemAdapter.getText3().equals("null") || itemAdapter.getText3().equals("") ) {
            valtext3.setVisibility(View.GONE);
        } else  {
            valtext3.setVisibility(View.VISIBLE);
            valtext3.setText(itemAdapter.getText3());

        }


        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        valtext4.setText(decimalFormat.format(itemAdapter.getText4()));




        return listViewItem;
    }
}