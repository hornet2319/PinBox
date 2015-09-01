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
import com.pinterest.android.pdk.PDKUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import teamvoy.com.pinbox.R;
import teamvoy.com.pinbox.fragments.PinsFragment;

/**
 * Created by lubomyrshershun on 9/1/15.
 */
public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.VersionViewHolder> {
    List<PDKUser> userList;


    Context context;
    OnItemClickListener clickListener;


    public UsersRecyclerAdapter(Context context) {
        this.context = context;
    }

    public UsersRecyclerAdapter(List<PDKUser> versionModels) {
        this.userList = versionModels;

    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item_pins, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        if (userList.size() - i < 5) {
            PinsFragment.loadNext();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i) {

        versionViewHolder.title.setText(userList.get(i).getUsername());
     //   versionViewHolder.subTitle.setText(pinList.get(i).getBoard().getName());
//        Log.d("Board","name="+pinList.get(i).getBoard().toString());
        Picasso.with(context).load(userList.get(i).getImageUrl()).into(versionViewHolder.imageView);


    }


    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public void setUsersList(List<PDKUser> pinList) {
        this.userList = pinList;
    }


    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardItemLayout;
        ImageView imageView;
        TextView title;
        TextView subTitle;

        public VersionViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.cardlist_image);
            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            title = (TextView) itemView.findViewById(R.id.listitem_name);
          //  subTitle = (TextView) itemView.findViewById(R.id.listitem_subname);

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