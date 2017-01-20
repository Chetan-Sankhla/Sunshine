package com.sankhla.sunshine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chetan_Sankhla on 16-Jan-17.
 */

public class ForecastAdapter extends RecyclerView.Adapter
{
    private Context mContext;
    ArrayList<HashMap<String, String>> mainList;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    public ForecastAdapter(Context context)
    {
        mContext = context;
        this.mainList = new ArrayList<>();
    }

    public ForecastAdapter(ArrayList<HashMap<String, String>> list) {
        mainList = list;
    }

    public void addData(ArrayList<HashMap<String, String>> list)
    {
        //add data to array list.
        mainList.addAll(list);
        Log.d("addData: ", mainList.toString());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        int layoutId=-1;//Default layout Id
        //Get view according to day.
        switch (viewType)
        {
            case VIEW_TYPE_TODAY:
                layoutId=R.layout.list_item_forecast_today;
                break;
            case VIEW_TYPE_FUTURE_DAY:
                layoutId=R.layout.list_item_forecast;
                break;
        }
        //Inflate view.
        view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        ViewHolderMain holderMain = new ViewHolderMain(view);
        return holderMain;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        final ViewHolderMain holderMain = (ViewHolderMain) holder.itemView.getTag();
        //Set data from array list to view and also format them.
//        holderMain.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(Integer.parseInt(mainList.get(position).get(FetchWeatherTask.KEY_WEATHER_ID))));
        holderMain.dateView.setText(Utility.getFriendlyDayString(mContext,Long.parseLong(mainList.get(position).get(FetchWeatherTask.KEY_DATE))));
        holderMain.descriptionView.setText(mainList.get(position).get(FetchWeatherTask.KEY_DESC));
        boolean isMetric=Utility.isMetric(mContext);
        holderMain.highView.setText(Utility.formatTemperature(mContext,Double.parseDouble((mainList.get(position).get(FetchWeatherTask.KEY_MAX)))));
        holderMain.lowView.setText(Utility.formatTemperature(mContext,Double.parseDouble((mainList.get(position).get(FetchWeatherTask.KEY_MIN)))));
    }

    @Override
    public int getItemCount()
    {
        return mainList.size();
    }

    public class ViewHolderMain extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView iconView;
        TextView dateView, descriptionView, highView, lowView;

        public ViewHolderMain(View view)
        {
            super(view);
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
            view.setTag(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            String forecast=mainList.get(getAdapterPosition()).toString();
            Intent intent=new Intent(mContext,DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT,forecast);
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }
}
