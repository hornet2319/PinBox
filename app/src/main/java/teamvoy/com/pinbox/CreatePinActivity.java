package teamvoy.com.pinbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import teamvoy.com.pinbox.dialogs.BoardChooserDialog;
import teamvoy.com.pinbox.dialogs.NewBoardDialog;

public class CreatePinActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isBoardChosen=false;
    private ImageView pin_img;
    private static TextView pin_board;
    private EditText pin_note;
    private PDKPin pin;
    private Context context;
    private Button confirm_btn,cancel_btn;
    private String _ID;
    private static PDKBoard board=null;
    private static final String PIN_FIELDS = "id,link,creator,image,counts,note,created_at,board,metadata";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);
        context=this;
        //initializing views
        pin_img = (ImageView)findViewById(R.id.pin_img);
        pin_board=(TextView)findViewById(R.id.create_pin_board_id);
        pin_note=(EditText)findViewById(R.id.pin_create_note);
        pin_note.setImeOptions(EditorInfo.IME_ACTION_DONE);
        pin_note.setSingleLine();
        //initializing buttons
        confirm_btn=(Button)findViewById(R.id.save_button);
        cancel_btn=(Button)findViewById(R.id.cancel_button);
        //set listeners
        confirm_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        //set listener for textView
        BoardChooserDialog dialog=new BoardChooserDialog(context);
        dialog.show();
        pin_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardChooserDialog dialog=new BoardChooserDialog(context);
                dialog.show();
            }
        });

        _ID=getIntent().getStringExtra("id");
        PDKClient.getInstance().getPin(_ID,PIN_FIELDS,new PDKCallback(){
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);
                pin=response.getPin();
                pin_note.setText(pin.getNote());
                Picasso.with(context).load(pin.getImageUrl()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
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
      //  getMenuInflater().inflate(R.menu.menu_create_pin, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (isBoardChosen==true){
            onClick(confirm_btn);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_button:{
                if (board!=null){
                   savePin();
                }
                else {

                    isBoardChosen=true;
                   // BoardChooserDialog dialog=new BoardChooserDialog(context);
                   // dialog.show();
                   NewBoardDialog dialog=new NewBoardDialog(context,true);
                    dialog.show();
                }

            }
            case R.id.cancel_button:{
                onBackPressed();
            }
        }
    }

    private void savePin() {
        PDKClient.getInstance().createPin(pin_note.getText().toString(),
                board.getUid(),pin.getImageUrl(),pin.getLink(),new PDKCallback(){
                    @Override
                    public void onSuccess(PDKResponse response) {
                        super.onSuccess(response);

                    }

                    @Override
                    public void onFailure(PDKException exception) {
                        super.onFailure(exception);
                    }
                });
    }

    public static void setBoard(PDKBoard board) {
        CreatePinActivity.board = board;
        pin_board.setText(board.getName());
    }
}
