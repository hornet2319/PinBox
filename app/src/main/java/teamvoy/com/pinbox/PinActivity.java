package teamvoy.com.pinbox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import teamvoy.com.pinbox.utils.ImageLoaderUtil;


public class PinActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";
    private final String USER_FIELDS = "id,image,counts,created_at,first_name,last_name,bio,username";
    private static final String PIN_FIELDS = "id,link,creator,image,counts,note,created_at,board,metadata";
    private static String _ID;
    private static String user_id;
    private static String board_id;
    private  PDKPin pin;
    private boolean liked=true;
    ImageView pin_img,pin_author_img,pin_board_img,like_number_img;
    TextView pin_note,pin_time,pin_author_txt,pin_board_txt,pin_number_txt,like_number_txt;
    private FloatingActionButton fab;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        toolbar=(Toolbar)findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!liked) like(true);
            }
        });
        getSupportActionBar().setTitle("Back");
        _ID=getIntent().getStringExtra("id");

        //Initializing views
        context=this;
        like_number_img=(ImageView)findViewById(R.id.like_number_img);
        pin_img=(ImageView)findViewById(R.id.pin_img);
        pin_author_img=(ImageView)findViewById(R.id.pin_author_img);
        pin_board_img=(ImageView)findViewById(R.id.pin_board_img);

        pin_note=(TextView)findViewById(R.id.pin_note);
        pin_author_txt=(TextView)findViewById(R.id.pin_author_txt);
        pin_board_txt=(TextView)findViewById(R.id.pin_board_txt);
        pin_time=(TextView)findViewById(R.id.pin_time);
        pin_number_txt=(TextView)findViewById(R.id.pin_number_txt);
        like_number_txt=(TextView)findViewById(R.id.like_number_txt);

        LinearLayout author=(LinearLayout)findViewById(R.id.pin_author);
        LinearLayout board=(LinearLayout)findViewById(R.id.pin_board);
        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo place UserActivity invocation here;
            }
        });
        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin != null) {
                    Intent intent=new Intent(context,BoardActivity.class);
                    intent.putExtra("id", pin.getBoard().getUid());
                    startActivity(intent);
                }
            }
        });
        like_number_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab.getVisibility()==View.GONE) dislike(true);
            }
        });
        PDKClient.getInstance().getPin(_ID, PIN_FIELDS, new PDKCallback() {

                    @Override
                    public void onSuccess(final PDKResponse response) {
                        pin = response.getPin();
                        pin_note.setText(response.getPin().getNote());
                        pin_number_txt.setText("" + response.getPin().getRepinCount());
                        like_number_txt.setText("" + response.getPin().getLikeCount());
                        isLiked();
                        user_id = response.getPin().getUser().getUid();
                        PDKClient.getInstance().getUser(user_id, USER_FIELDS, new PDKCallback() {
                            @Override
                            public void onSuccess(PDKResponse response) {
                                Picasso.with(context).load(response.getUser().getImageUrl()).into(pin_author_img);
                                pin_author_txt.setText(response.getUser().getUsername());
                            }

                            @Override
                            public void onFailure(PDKException exception) {

                                Log.e(getClass().getName(), exception.getDetailMessage());
                            }
                        });

                        board_id = response.getPin().getBoard().getUid();
                        PDKClient.getInstance().getBoard(board_id, BOARD_FIELDS, new PDKCallback() {
                            @Override
                            public void onSuccess(PDKResponse response) {
                                Picasso.with(context).load(response.getBoard().getImageUrl()).into(pin_board_img);
                                pin_board_txt.setText(response.getBoard().getName());
                            }

                            @Override
                            public void onFailure(PDKException exception) {

                                Log.e(getClass().getName(), exception.getDetailMessage());
                            }
                        });
                        board_id = response.getPin().getBoard().getUid();
                        Picasso.with(context).load(pin.getImageUrl()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                //  initViews(response.getPin());
                                int result;
                                int img_width = bitmap.getWidth();
                                int img_height = bitmap.getHeight();
                                Display display = getWindowManager().getDefaultDisplay();
                                Point size = new Point();
                                display.getSize(size);
                                int screen_width = size.x;
                                result = screen_width * img_height / img_width;
                                ViewGroup.LayoutParams params = pin_img.getLayoutParams();
                                params.height = result;
                                pin_img.setLayoutParams(params);
                                pin_img.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                        // Log.d("user image url", pin.getUser().getImageUrl());


                    }

                    @Override
                    public void onFailure(PDKException exception) {

                        Log.e(getClass().getName(), exception.getDetailMessage());
                    }
                }

        );

    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    private void initViews(PDKPin pin){
        //ImageLoaderUtil.getImageLoader(context).displayImage(pin.getUser().getImageUrl(), pin_author_img);
        //ImageLoaderUtil.getImageLoader(context).displayImage(pin.getBoard().getImageUrl(),pin_board_img);

    }
    private void dislike(boolean decr){
        liked=false;
        like_number_img.setImageResource(R.drawable.ic_like);
        if(decr) like_number_txt.setText(Integer.parseInt(like_number_txt.getText().toString())-1+"");
        fab.setVisibility(View.VISIBLE);
    }
    private void like(boolean incr){
        liked=true;
        like_number_img.setImageResource(R.drawable.ic_like_red);
        if (incr) like_number_txt.setText(Integer.parseInt(like_number_txt.getText().toString()) + 1 + "");
        fab.setVisibility(View.GONE);
    }
    private void isLiked(){
        PDKClient.getInstance().getMyLikes(PIN_FIELDS,new PDKCallback(){
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);
                List<PDKPin> list=response.getPinList();
                for (int i=0;i<list.size();i++){
                    if (list.get(i).getUid().equals(pin.getUid())){
                        like(false);
                        break;
                    }
                    else dislike(false);
                }
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_pin) {
            Intent intent=new Intent(context,CreatePinActivity.class);
            intent.putExtra("id",_ID);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
