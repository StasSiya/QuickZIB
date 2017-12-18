package com.example.anastasiyaivanova.newjsonparser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anastasiyaivanova on 10.12.17.
 */

// Create an adapter that extends RecyclerView.Adapter<ZibAdapter> and implements methods: onCreateViewHolder,onBindViewHolder,getItemcount
//This adapter creates views that hold the title of the orfSendung and is not bound to any data source
public class ZibAdapter extends RecyclerView.Adapter<ZibAdapter.ZibViewHolder> {
    private final String TAG = "ZibAdapter";

    private List<OrfSendung> sendungen;
    private Context context;
    final private ListItemClickListener mOnClickListener;


    //ItemClickHandling: add an interface ListiItemClicklistener and

    public interface ListItemClickListener {
        void onClick(OrfSendung aktuelleSendung);
    }

    //give Adapter access to the listener, by passing ListItemClickListener to the adapter as a param
    public ZibAdapter(Context context, List<OrfSendung> mItems, ListItemClickListener listener) {
        this.sendungen = mItems;
        this.context = context;
        mOnClickListener = listener;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    class ZibViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemView;

        //implement OnItemClicklistner so that the ViewHolder can invoce it

        public ZibViewHolder(View itemView) {
            super(itemView);

            listItemView = (TextView) itemView.findViewById(R.id.zib_item);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            OrfSendung aktuelleSendung = sendungen.get(clickedPosition);


            mOnClickListener.onClick(aktuelleSendung);


        }
    }


    //create a constructor for ZibAdapter that accepts
    @Override
    //create the views: inflate the itemView from XML; returns a ViewHolderObject
    public ZibViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the item using Layoutinflater:takes the id of the layout file.
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediatly = false;

        View view = layoutInflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediatly);

        ZibViewHolder zibViewHolder = new ZibViewHolder(view);

        return zibViewHolder;
    }

    @Override
    public void onBindViewHolder(ZibViewHolder holder, int position) {
        holder.listItemView.setText(sendungen.get(position).getName());
        /*String currentShow = mShowData.toString().indexOf();//get the data for the current show
        holder.listItemView.setText(currentShow);//set the data of the currentshow onto the ListItemView*/
    }

    @Override

    public int getItemCount() {
        return sendungen.size();
    }

    //create a ViewHolder class as an inner class of ZibAdapter class; here the view objects are cashed, that will be populated with data
    //here you call findViewById for each view that will be showing data from the adapter


}
