package com.sabri.naat.amjadsabri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Tanin on 5/19/2016.
 */
public class See_Video_Adapt extends ArrayAdapter<String> {

    String[] gojol_n;
    int[] logo;

    boolean[] itemChecked;
    Context c;
    LayoutInflater inflater;
    public See_Video_Adapt(Context context, String[] gojol_n_st) {
        super(context, R.layout.see_video_adapt, gojol_n_st);
        this.c = context;
        //this.no= no;
        this.gojol_n = gojol_n_st;
        itemChecked = new boolean[gojol_n_st.length];
        this.logo = logo;
    }

    public class ViewHolder{
        TextView tvNo, tvName, tvArabic;
        ImageView leftLogo, rightLogo;
        CheckBox ck1;

        Button deleteIT;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView== null)
        {
            inflater= (LayoutInflater) c.getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.see_video_adapt,null);
        }

        final ViewHolder holder= new ViewHolder();
       // holder.tvNo= (TextView) convertView.findViewById(R.id.tvNo);
        holder.tvName= (TextView) convertView.findViewById(R.id.tvName);
        holder.deleteIT= (Button) convertView.findViewById(R.id.deleteIT);
//        holder.leftLogo= (ImageView) convertView.findViewById(R.id.leftLogo);
//        holder.rightLogo= (ImageView) convertView.findViewById(R.id.rightLogo);

        holder.deleteIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.tvName.getText().toString().equals("Nothing Added!")){

                }else {
                    if (c instanceof VideoStartPage){
                        ((VideoStartPage)c).adaptTasker(position);
                    }

                }
//                holder.deleteIT.setEnabled(false);
            }
        });

       // holder.tvNo.setText(no[position]);
        holder.tvName.setText(gojol_n[position]);
//        holder.leftLogo.setImageResource(logo[position]);
//        holder.rightLogo.setImageResource(logo[position]);

        return convertView;
    }


}
