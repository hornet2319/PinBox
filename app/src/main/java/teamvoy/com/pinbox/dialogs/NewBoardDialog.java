package teamvoy.com.pinbox.dialogs;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import teamvoy.com.pinbox.CreatePinActivity;
import teamvoy.com.pinbox.R;
import teamvoy.com.pinbox.fragments.BoardsFragment;

/**
 * Created by lubomyrshershun on 9/3/15.
 */
public class NewBoardDialog {
    private Context context;
    private boolean bool=false;

    public NewBoardDialog(Context context) {
        this.context = context;
    }

    public NewBoardDialog(Context context, boolean b) {
        this.context = context;
        bool=true;
    }

    public void show() {
        final AlertDialog.Builder boardDialog = new AlertDialog.Builder(context);
        boardDialog.setTitle("Create board");

        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_create_board, null);
        final EditText name=(EditText)view.findViewById(R.id.name_et);
        final EditText description=(EditText)view.findViewById(R.id.description_et);

        boardDialog.setView(view);
        boardDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                //TODO descrpiption is not aivailable for now
                if (!name.getText().toString().isEmpty()) {
                    PDKClient.getInstance().createBoard(name.getText().toString(), null, new PDKCallback() {
                        @Override
                        public void onSuccess(PDKResponse response) {
                           // super.onSuccess(response);
                            if (!bool)
                            BoardsFragment.update();
                            else CreatePinActivity.setBoard(response.getBoard());

                        }

                        @Override
                        public void onFailure(PDKException exception) {
                          //  super.onFailure(exception);
                            if(!bool)
                            BoardsFragment.update();
                            else Log.e("NewBoardDialog",exception.getDetailMessage());
                        }
                    });
                }
                else Toast.makeText(context,"board name cannot be empty",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
      /*  boardDialog.setNeutralButton("More options", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                description.setVisibility(View.VISIBLE);
            }
        }); */
        boardDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        boardDialog.show();
    }
}
