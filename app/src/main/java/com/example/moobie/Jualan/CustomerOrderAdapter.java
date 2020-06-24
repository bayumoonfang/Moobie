package com.example.moobie.Jualan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.moobie.Customer.CustomerItem;
import com.example.moobie.R;

import java.util.List;

public class CustomerOrderAdapter  extends ArrayAdapter<CustomerOrderItem> {

    private List<CustomerOrderItem> projectList;
    private Context mCtx;

    public CustomerOrderAdapter(List<CustomerOrderItem> projectList, Context mCtx) {
        super(mCtx, R.layout.grid_customerorder, projectList);
        this.projectList = projectList;
        this.mCtx = mCtx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View listViewItem = inflater.inflate(R.layout.grid_customerorder, null, true);
        TextView valtext1 = (TextView) listViewItem.findViewById(R.id.txtNoCustomer);
        TextView valtext2 = (TextView) listViewItem.findViewById(R.id.txtNamaCustomer);
        TextView valtext3 = (TextView) listViewItem.findViewById(R.id.txtTelpCustomer);
        TextView valtext4 = (TextView) listViewItem.findViewById(R.id.txtAlamatCustomer);


        CustomerOrderItem itemAdapter = projectList.get(position);
        valtext1.setText(itemAdapter.getText1());
        valtext2.setText(itemAdapter.getText2());
        valtext3.setText(itemAdapter.getText3());
        valtext4.setText(itemAdapter.getText4());


        return listViewItem;
    }
}