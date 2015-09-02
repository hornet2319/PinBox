package teamvoy.com.pinbox.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.pinterest.android.pdk.PDKPin;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import teamvoy.com.pinbox.PinActivity;
import teamvoy.com.pinbox.R;
import teamvoy.com.pinbox.fragments.PinsFragment;
import teamvoy.com.pinbox.utils.ImageLoaderUtil;
import teamvoy.com.pinbox.utils.ImageResizeUtil;

/**
 * Created by Lubomyr Shershun on 30.08.2015.
 * l.sherhsun@gmail.com
 */
public class PinsRecyclerAdapter extends RecyclerView.Adapter<PinsRecyclerAdapter.VersionViewHolder> {
    List<PDKPin> pinList;

    ImageLoaderUtil loaderUtil;
    ImageResizeUtil resizeUtil;

    Context context;
    OnItemClickListener clickListener;


    public PinsRecyclerAdapter(Context context) {
        this.context = context;
        loaderUtil = new ImageLoaderUtil(context);

    }

    public PinsRecyclerAdapter(List<PDKPin> versionModels) {
        this.pinList = versionModels;

    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item_pins, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        resizeUtil = new ImageResizeUtil(context);

        if (pinList.size() - i < 5) {
            PinsFragment.loadNext();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        versionViewHolder.title.setText(pinList.get(i).getNote());
        versionViewHolder.subTitle.setText(pinList.get(i).getBoard().getName());
//        Log.d("Board","name="+pinList.get(i).getBoard().toString());

        resizeUtil.setView(versionViewHolder.imageView);
        resizeUtil.setImage(pinList.get(i).getImageUrl());
     //   new ATask(pinList.get(i).getImageUrl(),versionViewHolder.imageView,context).execute();
       // loaderUtil.getImageLoader().displayImage(pinList.get(i).getImageUrl(),versionViewHolder.imageView);

       // loadImage(i,versionViewHolder);
 /*       Picasso.with(context).load(pinList.get(i).getImageUrl()).into(new Target() {
            ImageView image=versionViewHolder.imageView;
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                ViewGroup.LayoutParams params= image.getLayoutParams();
                if (getHeight(bitmap,image)!=0)
                    params.height=getHeight(bitmap,image);
                image.setLayoutParams(params);
                image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
*/
    Picasso.with(context).load(pinList.get(i).getImageUrl()).into(versionViewHolder.imageView);
    }
    void loadImage(int i, final VersionViewHolder versionViewHolder){
        final int _i=i;
        final VersionViewHolder _versionViewHolder=versionViewHolder;
        Picasso.with(context).load(pinList.get(i).getImageUrl()).into(new Target() {
            ImageView image=versionViewHolder.imageView;
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap bitmapDrawable=bitmap;

                ViewGroup.LayoutParams params= image.getLayoutParams();
                if (getHeight(bitmap,image)!=0)
                    params.height=getHeight(bitmap,image);
                image.setLayoutParams(params);
                image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                loadImage(_i,_versionViewHolder);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
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


            imageView = (ImageView) itemView.findViewById(R.id.cardlist_image);
            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            title = (TextView) itemView.findViewById(R.id.listitem_name);
            subTitle = (TextView) itemView.findViewById(R.id.listitem_subname);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
           // clickListener.onItemClick(v, getPosition());
            Intent intent=new Intent(context, PinActivity.class);
            intent.putExtra("id",pinList.get(getPosition()).getUid());
            context.startActivity(intent);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    private class ATask extends AsyncTask<Void, Void, Void> {
        private File cachedImage;
        private String url;
        private ImageView image;
        private Bitmap bitmapDrawable;
        private Context context;

        public ATask(String url,ImageView image,Context context) {
            this.url = url;
            this.image=image;
            this.context=context;

        }

        @Override
        protected Void doInBackground(Void... params) {
            Picasso.with(context).load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmapDrawable=bitmap;

                    ViewGroup.LayoutParams params= image.getLayoutParams();
                    if (getHeight(bitmap,image)!=0)
                        params.height=getHeight(bitmap,image);
                    image.setLayoutParams(params);
                    image.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            image.setImageBitmap(bitmapDrawable);
        }

        }
    int getHeight(Bitmap bitmapDrawable, ImageView image){
        int result=0;
        Bitmap bitmap = bitmapDrawable;
        int img_width = bitmap.getWidth();
        int img_height = bitmap.getHeight();
        int screen_width = image.getWidth();
        result = screen_width * img_height / img_width;
        return result;
    }
}