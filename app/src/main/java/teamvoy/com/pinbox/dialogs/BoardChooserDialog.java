package teamvoy.com.pinbox.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import java.util.ArrayList;
import java.util.List;

import teamvoy.com.pinbox.CreatePinActivity;
import teamvoy.com.pinbox.R;

/**
 * Created by lubomyrshershun on 9/7/15.
 */
public class BoardChooserDialog {
    ArrayAdapter<String> mAdapter;
    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";
    private Context context;
    private int mPosition;
    private List<PDKBoard> boardList=new ArrayList<>();
    private List<String> content=new ArrayList<>();
    private TextView text;


    public BoardChooserDialog(Context context) {
        this.context = context;

    }

    public void show() {
        new ATask().execute();
        mAdapter=
                new ArrayAdapter<String>(
                        context,
                        R.layout.list_item,
                        R.id.list_item_textview);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Choose board");

        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_boardchooser, null);
        ListView listView=(ListView)view.findViewById(R.id.boards_list);

        text=(TextView)view.findViewById(R.id.chooser_text);
        listView.setAdapter(mAdapter);
        dialog.setView(view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition=position;
                text.setText("Confirm your choise: "+boardList.get(position).getName());
            }
        });
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreatePinActivity.setBoard(boardList.get(mPosition));
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private List<String> getStringList() {
        List<String> list = new ArrayList<>();
        for (PDKBoard board:boardList) {
            list.add(board.getName());
        }

        return list;
    }

    private void getBoardList() {



    }

    PDKCallback callback = new PDKCallback() {


        @Override
        public void onSuccess(PDKResponse response) {
            super.onSuccess(response);
            boardList.clear();
            boardList.addAll(response.getBoardList());
            content=getStringList();
            mAdapter.clear();
            for (String dayForecastStr : content) {
                mAdapter.add(dayForecastStr);
            }

        }

        @Override
        public void onFailure(PDKException exception) {
            Log.e("BoardCooserDialog",exception.getDetailMessage());
            super.onFailure(exception);
        }
    };
    private class ATask extends AsyncTask<Void,Void,Void> {

        ProgressDialog refreshDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshDialog = ProgressDialog.show(context, null, null);

            // Spin the wheel whilst the dialog exists
            refreshDialog.setContentView(R.layout.progress_dialog_loader);
            // Don't exit the dialog when the screen is touched
            refreshDialog.setCanceledOnTouchOutside(false);
            // Don't exit the dialog when back is pressed
            refreshDialog.setCancelable(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            PDKClient.getInstance().getMyBoards(BOARD_FIELDS, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {
                    Log.d("BoardCooserDialog","responce size="+response.getBoardList().size());
                    super.onSuccess(response);
                    boardList.clear();
                    boardList.addAll(response.getBoardList());
                    content=getStringList();
                    mAdapter.clear();
                    for (String dayForecastStr : content) {
                        mAdapter.add(dayForecastStr);
                    }
                    refreshDialog.dismiss();

                }

                @Override
                public void onFailure(PDKException exception) {
                    Log.e("BoardCooserDialog","something goes wrong!");
                    super.onFailure(exception);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("BoardChooserDialog", "Atask finished, content size=" + content.size() + ",boardList size=" + boardList.size());
            super.onPostExecute(aVoid);

            if (content != null) {
                mAdapter.clear();
                for (String dayForecastStr : content) {
                    mAdapter.add(dayForecastStr);
                }
                refreshDialog.dismiss();
            }
        }
    }
}