package teamvoy.com.pinbox.utils;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import teamvoy.com.pinbox.R;


/**
 * Created by lubomyrshershun on 8/25/15.
 *
 * This class initialize Image loader with configuration and returns it.
 */
public class ImageLoaderUtil{
    private ImageLoader imageLoader;
    private Context context;
    DisplayImageOptions options;

    public ImageLoaderUtil(Context context) {
        this.context=context;
        imageLoader =ImageLoader.getInstance();
    }
    public ImageLoader getImageLoader(){
        init();
        return imageLoader;
    }
    public DisplayImageOptions getOptions(){
        return options;
    }

    private void init(){
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "img");
        // Create configuration for ImageLoader (all options are optional)
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .writeDebugLogs()
                        // You can pass your own memory cache implementation
                .discCache(new com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache(cacheDir)) // You can pass your own disc cache implementation
                        //     .diskCacheSize(50 * 1024 * 1024)
                        //     .diskCacheFileCount(100)
                        //     .discCacheFileNameGenerator(new HashCodeFileNameGenerator())

                .build();
        // Initialize ImageLoader with created configuration. Do it once.
        imageLoader.init(config);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.nopc)//display stub image
                .showImageOnFail(R.drawable.nopc)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
    }

}