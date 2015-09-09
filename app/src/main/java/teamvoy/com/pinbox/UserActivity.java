package teamvoy.com.pinbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;
import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity {
    private final String USER_FIELDS = "id,image,counts,created_at,first_name,last_name,bio,username";
    private final String TAG="UserActivity";
    private ImageView user_img;
    private TextView user_name,user_username,user_bio,pin_count,boards_count,likes_count,followers_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar mToolbar=(Toolbar)findViewById(R.id.user_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initializing UI elements
        user_img=(ImageView)findViewById(R.id.user_img);

        user_name=(TextView)findViewById(R.id.user_name);
        user_username=(TextView)findViewById(R.id.user_username);
        user_bio=(TextView)findViewById(R.id.user_bio);
        pin_count=(TextView)findViewById(R.id.pin_number_txt);
        boards_count=(TextView)findViewById(R.id.board_number_txt);
        likes_count=(TextView)findViewById(R.id.like_number_txt);
        followers_count=(TextView)findViewById(R.id.followers_number_txt);

        //getting user data
        PDKClient.getInstance().getMe(USER_FIELDS, new PDKCallback(){
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);
                PDKUser user=response.getUser();
                Picasso.with(getApplicationContext()).load(user.getImageUrl()).into(user_img);
                user_name.setText(user.getFirstName()+" "+user.getLastName());
                user_username.setText("Username: "+user.getUsername());
                user_bio.setText(user.getBio());
                pin_count.setText(""+user.getPinCount());
                boards_count.setText(""+user.getBoardsCount());
                likes_count.setText(""+user.getLikesCount());
                followers_count.setText(""+user.getFollowersCount());
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
                Log.e(TAG,exception.getDetailMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
