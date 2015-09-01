package teamvoy.com.pinbox.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import teamvoy.com.pinbox.R;
import teamvoy.com.pinbox.adapters.BoardsRecyclerAdapter;


/**
 * Created by lubomyrshershun on 9/1/15.
 */
public class BoardsFragment extends Fragment {
    private BoardsRecyclerAdapter adapter;
    private static PDKCallback myBoardsCallback;
    private static PDKResponse myBoardsResponse;
    private SwipeRefreshLayout swipe;
    private static boolean _loading = false;
    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_board, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.dummyfrag_scrollableview);
        TextView text = (TextView) rootView.findViewById(R.id.fragment_txt);
        swipe=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe);
        swipe.setColorSchemeColors(getResources().getColor(R.color.red_dark));
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO create new board code goes here
            }
        });

        StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(2, 1);
        Display display = ((WindowManager) getActivity().getSystemService(Activity.WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            staggeredLayoutManager = new StaggeredGridLayoutManager(3, 1);
        }
        recyclerView.setLayoutManager(staggeredLayoutManager);
        recyclerView.setHasFixedSize(false);

        adapter = new BoardsRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        myBoardsCallback = new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                _loading = false;
                myBoardsResponse = response;
                adapter.setBoardList(response.getBoardList());
                adapter.notifyDataSetChanged();
                Log.d("Boards List", "size=" + response.getPinList().size());
                if(swipe.isRefreshing()) swipe.setRefreshing(false);

            }

            @Override
            public void onFailure(PDKException exception) {
                _loading = false;
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        };
        _loading = true;
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchBoards();
            }
        });
        return rootView;
    }
    private void fetchBoards() {
        adapter.setBoardList(null);
        adapter.notifyDataSetChanged();
        PDKClient.getInstance().getMyBoards(BOARD_FIELDS, myBoardsCallback);
    }
    public static void loadNext() {
        if (!_loading && myBoardsResponse.hasNext()) {
            _loading = true;
            myBoardsResponse.loadNext(myBoardsCallback);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        fetchBoards();
    }
}