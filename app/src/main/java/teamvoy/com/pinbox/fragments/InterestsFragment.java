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

import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;

import java.util.ArrayList;
import java.util.List;

import teamvoy.com.pinbox.R;
import teamvoy.com.pinbox.adapters.PinsRecyclerAdapter;

/**
 * Created by lubomyrshershun on 9/1/15.
 */
public class InterestsFragment extends Fragment {
    PinsRecyclerAdapter adapter;
    private static PDKCallback myPinsCallback;
    private static PDKResponse myPinsResponse;
    private static boolean _loading = true;
    private SwipeRefreshLayout swipe;
    private static List<PDKPin> data;
    StaggeredGridLayoutManager staggeredLayoutManager;
    private static final String FIELDS = "id,link,creator,image,counts,note,created_at,board,metadata";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        swipe=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe);
        swipe.setColorSchemeColors(getResources().getColor(R.color.red_dark));
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.dummyfrag_scrollableview);
        data=new ArrayList<>();

        staggeredLayoutManager = new StaggeredGridLayoutManager(2,1);
        Display display = ((WindowManager) getActivity().getSystemService(Activity.WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            staggeredLayoutManager = new StaggeredGridLayoutManager(3,1);
        }
        recyclerView.setLayoutManager(staggeredLayoutManager);
        recyclerView.setHasFixedSize(false);

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
        adapter = new PinsRecyclerAdapter(getActivity(),false);
        recyclerView.setAdapter(adapter);
        myPinsCallback = new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                data.addAll(response.getPinList());
                myPinsResponse = response;
                adapter.setPinList(data);
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

                fetchPins();

            }
        });


        return rootView;
    }
    private void fetchPins() {
        data.clear();
        adapter.setPinList(null);
        adapter.notifyDataSetChanged();
        PDKClient.getInstance().getMyLikes(FIELDS, myPinsCallback);
    }
    public static void loadNext() {
        if (myPinsResponse.hasNext()) {

            myPinsResponse.loadNext(myPinsCallback);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        fetchPins();
    }
}
