package teamvoy.com.pinbox;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import teamvoy.com.pinbox.fragments.BoardActivityFragment;


public class BoardActivity extends AppCompatActivity {
    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";
    private final String USER_FIELDS = "id,image,counts,created_at,first_name,last_name,bio,username";
    private static final String PIN_FIELDS = "id,link,creator,image,counts,note,created_at,board,metadata";
    private Toolbar toolbar;
    private static String _ID;
    private static boolean myBoard;
    private static Context context;
    private TextView pin_number_txt;
    private Button cancel_btn,edit_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        toolbar=(Toolbar)findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //initializing views
        pin_number_txt=(TextView)findViewById(R.id.pin_number_txt);
        cancel_btn=(Button)findViewById(R.id.board_cancel_subscr_bth);
        edit_btn=(Button)findViewById(R.id.board_edit_bth);
        //getting data
        _ID=getIntent().getStringExtra("id");
        BoardActivityFragment.setBoardID(_ID);
        myBoard=getIntent().getBooleanExtra("my", true);
        Log.d("myBoard",""+myBoard);
        buttonSwitcher(myBoard);
        PDKClient.getInstance().getBoard(_ID,BOARD_FIELDS,new PDKCallback(){
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);
                getSupportActionBar().setTitle(response.getBoard().getName());
                pin_number_txt.setText(""+response.getBoard().getPinsCount());
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
       // getMenuInflater().inflate(R.menu.menu_board, menu);
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
    private void buttonSwitcher(boolean myBoard ){
        if(myBoard){
            edit_btn.setVisibility(View.VISIBLE);
            cancel_btn.setVisibility(View.GONE);
        }
        else {
            edit_btn.setVisibility(View.GONE);
            cancel_btn.setVisibility(View.VISIBLE);
        }
    }
}
