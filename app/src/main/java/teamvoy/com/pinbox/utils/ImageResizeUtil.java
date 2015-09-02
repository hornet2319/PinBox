package teamvoy.com.pinbox.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lubomyrshershun on 9/2/15.
 */
public class ImageResizeUtil {
    private ImageView view;
    private String path;
    private Context context;
    private ImageLoaderUtil util;
    private File cachedImage;

    public ImageResizeUtil(Context context) {
        this.context = context;
        util=new ImageLoaderUtil(context);

    }

    public void setImage(String path) {
        this.path = util.getImageLoader().getDiskCache().get(path).getPath();
    }

    public void setView(ImageView view) {
        this.view = view;
    }
    public void resize(){
        ViewGroup.LayoutParams params= view.getLayoutParams();
        if (getHeight()!=0)
        params.height=getHeight();
        view.setLayoutParams(params);

    }

    private void download(){

    }
    private int getHeight(){
        int result=0;
        File file=new File(path);
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            int img_width = bitmap.getWidth();
            int img_height = bitmap.getHeight();
            int screen_width = view.getWidth();
            result = screen_width * img_height / img_width;
        }
        return result;
    }
}
