package teamvoy.com.pinbox.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import teamvoy.com.pinbox.BoardActivity;
import teamvoy.com.pinbox.R;
import teamvoy.com.pinbox.dialogs.ConfirmationDialog;
import teamvoy.com.pinbox.fragments.BoardActivityFragment;
import teamvoy.com.pinbox.fragments.BoardsFragment;

/**
 * Created by lubomyrshershun on 9/1/15.
 */
public class BoardsRecyclerAdapter extends RecyclerView.Adapter<BoardsRecyclerAdapter.VersionViewHolder> {
    List<PDKBoard> boardList;
    private boolean my;
    private boolean subscribed;


    Context context;
    OnItemClickListener clickListener;



    public BoardsRecyclerAdapter(Context context,boolean my, boolean subscribed) {
        this.context=context;
        this.my=my;
    }

    public BoardsRecyclerAdapter(List<PDKBoard> versionModels) {
        this.boardList = versionModels;

    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item_boards, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        if (boardList.size() - i < 5) {
            if(my)
            BoardsFragment.loadNext();
           // else
         //   BoardActivityFragment.loadNext();

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i) {
        String boardSubName="";
        if (boardList.get(i).getPinsCount()>1) boardSubName="pins";
        else boardSubName="pin";
        versionViewHolder.title.setText(boardList.get(i).getName());

        versionViewHolder.subTitle.setText(boardList.get(i).getPinsCount() + " " + boardSubName);
        if(boardList.get(i).getImageUrl()!=null) {
            versionViewHolder.imageView.setVisibility(View.VISIBLE);

            Picasso.with(context).load(boardList.get(i).getImageUrl()).into(versionViewHolder.imageView);
        }
    }



    @Override
    public int getItemCount() {
        return boardList == null ? 0 : boardList.size();
    }

    public void setBoardList(List<PDKBoard> boardList) {
        this.boardList = boardList;
    }

    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
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
            itemView.setOnLongClickListener(this);


        }

        @Override
        public void onClick(View v) {
            //clickListener.onItemClick(v, getPosition());
            Intent intent=new Intent(context, BoardActivity.class);
            intent.putExtra("id",boardList.get(getPosition()).getUid());
            intent.putExtra("my", my);
            intent.putExtra("subscr",subscribed);
           // intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {

            if (my){
                ConfirmationDialog dialog=new ConfirmationDialog(context);
                dialog.setTitle("confirm deleting");
                dialog.setBoard(boardList.get(getPosition()));
                dialog.setMessage("Do you really want to delete " + boardList.get(getPosition()).getName() + "?");
                dialog.show();
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
