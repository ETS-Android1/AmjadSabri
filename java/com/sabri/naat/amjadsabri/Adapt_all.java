package com.sabri.naat.amjadsabri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Tanin on 5/19/2016.
 */
public class Adapt_all extends ArrayAdapter<String> {

    String[] gojol_n ;
    int[] logo;

    Context c;
    LayoutInflater inflater;
    public Adapt_all(Context context, String[] gojol_n_st) {
        super(context, R.layout.adapt_all, gojol_n_st);
        this.c = context;
        //this.no= no;
        this.gojol_n = gojol_n_st;
        this.logo = logo;
    }

    public class ViewHolder{
        TextView tvNo, tvName, tvArabic;
        ImageView leftLogo, rightLogo;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView== null)
        {
            inflater= (LayoutInflater) c.getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.adapt_all,null);
        }

        final ViewHolder holder= new ViewHolder();
       // holder.tvNo= (TextView) convertView.findViewById(R.id.tvNo);
        holder.tvName= (TextView) convertView.findViewById(R.id.tvName);
//        holder.leftLogo= (ImageView) convertView.findViewById(R.id.leftLogo);
//        holder.rightLogo= (ImageView) convertView.findViewById(R.id.rightLogo);


       // holder.tvNo.setText(no[position]);
        holder.tvName.setText(gojol_n[position]);
//        holder.leftLogo.setImageResource(logo[position]);
//        holder.rightLogo.setImageResource(logo[position]);

        return convertView;
    }


}
