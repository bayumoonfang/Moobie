package com.example.moobie.Outlet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.moobie.R;

import java.util.List;


public class OutletAdapter extends ArrayAdapter<OutletItem> {

    private List<OutletItem> projectList;
    private Context mCtx;

    public OutletAdapter(List<OutletItem> projectList, Context mCtx) {
        super(mCtx, R.layout.grid_outlet, projectList);
        this.projectList = projectList;
        this.mCtx = mCtx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View listViewItem = inflater.inflate(R.layout.grid_outlet, null, true);
        TextView valtext1 = (TextView) listViewItem.findViewById(R.id.txtNamaOutlet);
        TextView valtext2 = (TextView) listViewItem.findViewById(R.id.txtAlamatOutlet);
        TextView valtext3 = (TextView) listViewItem.findViewById(R.id.txtIDOutlet);

        OutletItem itemAdapter = projectList.get(position);
        valtext1.setText(itemAdapter.getText1());
        valtext2.setText(itemAdapter.getText2());
        valtext3.setText(itemAdapter.getText3());
        return listViewItem;
    }
}