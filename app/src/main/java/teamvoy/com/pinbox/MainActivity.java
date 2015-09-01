package teamvoy.com.pinbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;

import java.util.ArrayList;
import java.util.List;

import teamvoy.com.pinbox.adapters.PinsRecyclerAdapter;
import teamvoy.com.pinbox.fragments.BoardsFragment;
import teamvoy.com.pinbox.fragments.FollowersFragment;
import teamvoy.com.pinbox.fragments.InterestsFragment;
import teamvoy.com.pinbox.fragments.PinsFragment;
import teamvoy.com.pinbox.fragments.SubscriptionsFragment;
import teamvoy.com.pinbox.utils.ImageLoaderUtil;

public class MainActivity extends AppCompatActivity {
    ImageLoaderUtil img_util;
    private ImageView user_img;
    private TextView user_txt;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_util = new ImageLoaderUtil(context);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        context=this;
        //initialize pdk
        PDKClient.configureInstance(this, getString(R.string.app_id));
        PDKClient.getInstance().onConnect(this);
        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);

        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_dark));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        setupViewPager(viewPager);


        user_txt=(TextView)findViewById(R.id.user_textview);

      //  TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
      //  tabLayout.setupWithViewPager(viewPager);

        onLogin();
     /*   tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        showToast("One");
                        break;
                    case 1:
                        showToast("Two");

                        break;
                    case 2:
                        showToast("Three");

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }); */
    }

    public void onLogin(){
        PDKClient pdkClient=PDKClient.getInstance();
        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);

        pdkClient.login(this, scopes, new PDKCallback() {

            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(getClass().getName(), response.getData().toString());
                //user logged in, use response.getUser() to get PDKUser object
                PDKUser user = response.getUser();
                //  Picasso.with(context).load(user.getImageUrl()).into(user_img);
                user_txt.setText(user.getFirstName() + " " + user.getLastName());




            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });
    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PinsFragment(), "Pins");
        adapter.addFrag(new BoardsFragment(), "Boards");
        adapter.addFrag(new InterestsFragment(), "Likes");
        adapter.addFrag(new SubscriptionsFragment(), "Subscriptions");
        adapter.addFrag(new FollowersFragment(), "Followers");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout: {
                PDKClient.getInstance().logout();
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PDKClient.getInstance().onOauthResponse(requestCode, resultCode, data);
    }

    public static class DummyFragment extends Fragment {
        int color;
        PinsRecyclerAdapter adapter;

        public DummyFragment() {
        }

        @SuppressLint("ValidFragment")
        public DummyFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_main, container, false);

            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);


            StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(2,1);
            Display display = ((WindowManager) getActivity().getSystemService(WINDOW_SERVICE))
                    .getDefaultDisplay();

            int orientation = display.getRotation();

            if (orientation == Surface.ROTATION_90
                    || orientation == Surface.ROTATION_270) {

                staggeredLayoutManager = new StaggeredGridLayoutManager(3,1);
            }
            recyclerView.setLayoutManager(staggeredLayoutManager);
            recyclerView.setHasFixedSize(true);

            List<PDKPin> list = new ArrayList<>();


            adapter = new PinsRecyclerAdapter(list);
            recyclerView.setAdapter(adapter);

            return view;
        }

    }

}