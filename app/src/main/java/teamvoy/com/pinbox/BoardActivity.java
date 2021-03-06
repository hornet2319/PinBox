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

import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import java.util.List;

import teamvoy.com.pinbox.fragments.BoardActivityFragment;


public class BoardActivity extends AppCompatActivity {
    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";
    private final String USER_FIELDS = "id,image,counts,created_at,first_name,last_name,bio,username";
    private static final String PIN_FIELDS = "id,link,creator,image,counts,note,created_at,board,metadata";
    private Toolbar toolbar;
    private static String _ID;
    private PDKBoard board;
    public static boolean myBoard;
    private static boolean subscribed;
    private static Context context;
    private TextView pin_number_txt;
    private Button cancel_btn,edit_btn,subscr_btn;


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
        subscr_btn=(Button)findViewById(R.id.board_subscr_bth);
        //getting data
        _ID=getIntent().getStringExtra("id");
        BoardActivityFragment.setBoardID(_ID);
        //myBoard=getIntent().getBooleanExtra("my", true);
       // subscribed=getIntent().getBooleanExtra("subscr",true);
        PDKClient.getInstance().getBoard(_ID,BOARD_FIELDS,new PDKCallback(){
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);
                board=response.getBoard();
                getSupportActionBar().setTitle(response.getBoard().getName());
                pin_number_txt.setText("" + response.getBoard().getPinsCount());
                isSubscribed(board);
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
            }
        });
        //listener for toolbar buttons
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //unfollowing board
              /*  if(board!=null)
                PDKClient.getInstance().unfollowBoard(board.getUid(),board.getName(),new PDKCallback(){
                    @Override
                    public void onSuccess(PDKResponse response) {
                        super.onSuccess(response);

                    }

                    @Override
                    public void onFailure(PDKException exception) {
                        super.onFailure(exception);
                        Log.e("BoardActivity", exception.getDetailMessage());
                    }
                });*/
            }
        });
        subscr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(board!=null)
                    PDKClient.getInstance().followBoard(board.getName(),new PDKCallback(){
                        @Override
                        public void onSuccess(PDKResponse response) {
                            super.onSuccess(response);
                           // buttonSwitcher(isMyBoard(board),isSubscribed(board));
                        }

                        @Override
                        public void onFailure(PDKException exception) {
                            super.onFailure(exception);
                            Log.e("BoardActivity",exception.getDetailMessage());
                        }
                    });*/
            }
        });

    }
//TODO create buttons listeners;

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
    private void buttonSwitcher(boolean myBoard, boolean subscr){

        if(myBoard){
            edit_btn.setVisibility(View.VISIBLE);
            cancel_btn.setVisibility(View.GONE);
            subscr_btn.setVisibility(View.GONE);
        }
        else {
            if(subscr) {
                edit_btn.setVisibility(View.GONE);
                cancel_btn.setVisibility(View.GONE);
                subscr_btn.setVisibility(View.VISIBLE);
            }
            else {
                subscr_btn.setVisibility(View.GONE);
                edit_btn.setVisibility(View.GONE);
                cancel_btn.setVisibility(View.VISIBLE);
            }
        }
    }
    private void isMyBoard(final PDKBoard board){
        final boolean[] result = new boolean[1];
        PDKClient.getInstance().getMyBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);
                result[0] = false;

                for (int i = 0; i < response.getBoardList().size(); i++) {
                    PDKBoard _board = response.getBoardList().get(i);
                    if (_board.getUid().equals(board.getUid())) result[0] = true;
                }
                myBoard = result[0];

                buttonSwitcher(myBoard, subscribed);

            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
                Log.e("BoardActivity",exception.getDetailMessage());
            }
        });

      //  return result[0];

    }
    private void isSubscribed(final PDKBoard board){
        final boolean[] result = new boolean[1];
        PDKClient.getInstance().getMyFollowedBoards(BOARD_FIELDS,new PDKCallback(){
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);

                if(response.getBoardList().contains(board)) result[0] = true;
                else result[0] = false;
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
            }
        });
        subscribed=result[0];
        isMyBoard(board);
    }
}
