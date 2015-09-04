package teamvoy.com.pinbox.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKModel;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;

import teamvoy.com.pinbox.fragments.BoardsFragment;

/**
 * Created by lubomyrshershun on 9/3/15.
 */
public class ConfirmationDialog {
    private Context context;
    private String title;
    private String message;
    private boolean confirmed=false;
    private PDKPin pin=null;
    private PDKBoard board=null;


    public ConfirmationDialog(Context context) {
        this.context = context;
    }
    public void show(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (!message.isEmpty()) dialog.setTitle(title);
        if (!title.isEmpty()) dialog.setMessage(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // confirmed = true;
                if (pin != null) {
                    PDKClient.getInstance().deletePin(pin.getUid(),new PDKCallback());

                }
                else if (board!=null){
                    PDKClient.getInstance().deleteBoard(board.getUid(), new PDKCallback() {
                        @Override
                        public void onSuccess(PDKResponse response) {
                            super.onSuccess(response);
                            Log.i("Info", "Board " + board.getName() + " deleted successfully");
                            BoardsFragment.update();
                        }

                        @Override
                        public void onFailure(PDKException exception) {
                            super.onFailure(exception);
                        }
                    });

                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // confirmed = false;
               dialog.dismiss();
            }
        });
        dialog.show();
    }

  //  public boolean isConfirmed() {return confirmed;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPin(PDKPin pin) {
        this.pin = pin;
    }

    public void setBoard(PDKBoard board) {
        this.board = board;
    }
}

