package teamvoy.com.pinbox.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by lubomyrshershun on 9/3/15.
 */
public class ConfirmationDialog {
    private Context context;
    private String title;
    private String message;
    private boolean confirmed=false;

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
                confirmed = true;
                Log.d("Confirmation", ""+true);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmed = false;
                Log.d("Confirmation", ""+false);
            }
        });
        dialog.show();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
