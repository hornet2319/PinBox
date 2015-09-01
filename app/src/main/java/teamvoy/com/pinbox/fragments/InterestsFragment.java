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
import com.pinterest.android.pdk.PDKResponse;

import teamvoy.com.pinbox.R;
import teamvoy.com.pinbox.adapters.PinsRecyclerAdapter;

/**
 * Created by lubomyrshershun on 9/1/15.
 */
public class InterestsFragment extends Fragment {
    PinsRecyclerAdapter adapter;
    private static PDKCallback myPinsCallback;
    private static PDKResponse myPinsResponse;
    private static boolean _loading = false;
    private SwipeRefreshLayout swipe;
    private static final String FIELDS = "id,link,creator,image,counts,note,created_at,board,metadata";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        swipe=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe);
        swipe.setColorSchemeColors(getResources().getColor(R.color.red_dark));
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.dummyfrag_scrollableview);


        StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(2,1);
        Display display = ((WindowManager) getActivity().getSystemService(Activity.WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            staggeredLayoutManager = new StaggeredGridLayoutManager(3,1);
        }
        recyclerView.setLayoutManager(staggeredLayoutManager);
        recyclerView.setHasFixedSize(false);

        adapter = new PinsRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        myPinsCallback = new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                _loading = false;
                myPinsResponse = response;
                adapter.setPinList(response.getPinList());
                adapter.notifyDataSetChanged();
                Log.d("Pin List", "size=" + response.getPinList().size());
                if(swipe.isRefreshing()) swipe.setRefreshing(false);

            }

            @Override
            public void onFailure(PDKException exception) {
                _loading = false;
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        };
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchPins();

            }
        });
        _loading = true;

        return rootView;
    }
    private void fetchPins() {
        adapter.setPinList(null);
        adapter.notifyDataSetChanged();
        PDKClient.getInstance().getMyLikes(FIELDS, myPinsCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchPins();
    }
}