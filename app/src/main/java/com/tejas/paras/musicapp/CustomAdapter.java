package com.tejas.paras.musicapp;

import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.ArrayList;

/**
 * Created by paras on 2/4/2017.
 */
public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;
    String time,time2;

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView data;
        TextView album;
        TextView artist;
        TextView duration;
        ImageView artist1;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.album = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
            viewHolder.artist1 = (ImageView) convertView.findViewById(R.id.imageView);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        lastPosition = position;
        viewHolder.name.setText(dataModel.getName());
        viewHolder.album.setText(dataModel.getVersion_number());
        time=getTime(dataModel.getDuration());
        viewHolder.duration.setText(time);

        return convertView;
    }

    public String getTime(String time)
    {
        long dur = Long.parseLong(time);
        String seconds = String.valueOf((dur % 60000) / 1000);

        String minutes = String.valueOf(dur / 60000);
        if (seconds.length() == 1) {
            time2="0" + minutes + ":0" + seconds;
        }else {
            time2="0" + minutes + ":" + seconds;
        }
        return  time2;
    }
}
