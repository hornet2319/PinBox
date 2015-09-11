package teamvoy.com.pinbox.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import java.util.ArrayList;
import java.util.List;

import teamvoy.com.pinbox.R;
import teamvoy.com.pinbox.adapters.BoardsRecyclerAdapter;
import teamvoy.com.pinbox.dialogs.NewBoardDialog;


/**
 * Created by lubomyrshershun on 9/1/15.
 */
public class BoardsFragment extends Fragment {
    private static BoardsRecyclerAdapter adapter;
    private static PDKCallback myBoardsCallback;
    private static PDKResponse myBoardsResponse;
    private SwipeRefreshLayout swipe;
    private FloatingActionButton fab;
    private static List<PDKBoard> data;
    private static boolean _loading = true;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_board, container, false);
        data=new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.dummyfrag_scrollableview);

        swipe=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe);
        swipe.setColorSchemeColors(getResources().getColor(R.color.red_dark));
        swipe.setRefreshing(true);

        fab=(FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setClickable(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewBoardDialog createBoard=new NewBoardDialog(getActivity());
                createBoard.show();
            }
        });


        staggeredLayoutManager = new StaggeredGridLayoutManager(2, 1);
        Display display = ((WindowManager) getActivity().getSystemService(Activity.WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            staggeredLayoutManager = new StaggeredGridLayoutManager(3, 1);
        }
        recyclerView.setLayoutManager(staggeredLayoutManager);
        recyclerView.setHasFixedSize(false);

        adapter = new BoardsRecyclerAdapter(getActivity(),true,true);
        recyclerView.setAdapter(adapter);
        //checking if last element reached
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalItemCount;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = staggeredLayoutManager.getItemCount();
                int[] lastVisibleItemsPosition = new int[staggeredLayoutManager.getSpanCount()];
                staggeredLayoutManager.findLastVisibleItemPositions(lastVisibleItemsPosition);
                if (_loading) {
                    for (int i = 0; i < lastVisibleItemsPosition.length; i++) {
                        if (lastVisibleItemsPosition[i] == totalItemCount - 1) {
                            _loading = false;
                            loadNext();
                        }
                    }

                }
            }
        });


        myBoardsCallback = new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                myBoardsResponse = response;
                data.addAll(response.getBoardList());
                adapter.setBoardList(data);
                adapter.notifyDataSetChanged();
                _loading=true;
                if(swipe.isRefreshing()) swipe.setRefreshing(false);

            }
            @Override
            public void onFailure(PDKException exception) {

                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        };
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchBoards();
            }
        });
        return rootView;
    }
    private static void fetchBoards() {
        data.clear();
        adapter.setBoardList(null);
        adapter.notifyDataSetChanged();
        PDKClient.getInstance().getMyBoards(BOARD_FIELDS, myBoardsCallback);
    }
    public static void loadNext() {
        if (myBoardsResponse.hasNext()) {

            myBoardsResponse.loadNext(myBoardsCallback);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        fetchBoards();
    }
    public static void update(){fetchBoards();}
}