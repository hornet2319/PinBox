package teamvoy.com.pinbox.adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.pinterest.android.pdk.PDKPin;
import com.squareup.picasso.Picasso;

import java.util.List;

import teamvoy.com.pinbox.R;

/**
 * Created by Lubomyr Shershun on 30.08.2015.
 * l.sherhsun@gmail.com
 */
public class PinsRecyclerAdapter extends RecyclerView.Adapter<PinsRecyclerAdapter.VersionViewHolder> {
    List<PDKPin> pinList;



    Context context;
    OnItemClickListener clickListener;


    public PinsRecyclerAdapter(Context context) {
        this.context=context;
    }

    public PinsRecyclerAdapter(List<PDKPin> versionModels) {
        this.pinList = versionModels;

    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i) {

            versionViewHolder.title.setText(pinList.get(i).getNote());
            Picasso.with(context).load(pinList.get(i).getImageUrl()).into(versionViewHolder.imageView);



    }


    @Override
    public int getItemCount() {
            return pinList == null ? 0 : pinList.size();
    }

    public void setPinList(List<PDKPin> pinList) {
        this.pinList = pinList;
    }


    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardItemLayout;
        ImageView imageView;
        TextView title;
        TextView subTitle;

        public VersionViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.cardlist_image);
            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            title = (TextView) itemView.findViewById(R.id.listitem_name);
            subTitle = (TextView) itemView.findViewById(R.id.listitem_subname);

                itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}