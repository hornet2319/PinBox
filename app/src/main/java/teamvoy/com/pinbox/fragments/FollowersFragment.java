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
import teamvoy.com.pinbox.adapters.UsersRecyclerAdapter;

/**
 * Created by lubomyrshershun on 9/1/15.
 */
public class FollowersFragment extends Fragment {
    UsersRecyclerAdapter adapter;
    private static PDKCallback myPinsCallback;
    private static PDKResponse myPinsResponse;
    private static boolean _loading = false;
    private SwipeRefreshLayout swipe;
    private final String USER_FIELDS = "id,image,counts,created_at,first_name,last_name,bio,username";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        swipe=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe);
        // swipe.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.red_dark));
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

        adapter = new UsersRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        myPinsCallback = new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                _loading = false;
                myPinsResponse = response;
                adapter.setUsersList(response.getUserList());
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

                fetchFollowers();

            }
        });
        _loading = true;
        return rootView;
    }
    public static void loadNext() {
        if (!_loading && myPinsResponse.hasNext()) {
            _loading = true;
            myPinsResponse.loadNext(myPinsCallback);
        }
    }

    private void fetchFollowers() {
        adapter.setUsersList(null);
        adapter.notifyDataSetChanged();
        PDKClient.getInstance().getMyFollowers(USER_FIELDS,  myPinsCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchFollowers();
    }
}
