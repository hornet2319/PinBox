package teamvoy.com.pinbox;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
    private PDKUser user;
    private ProgressDialog refreshDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);


        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);

        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_dark));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);

        setupViewPager(viewPager);


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
            case R.id.action_user:{
                Intent intent=new Intent(context,UserActivity.class);
                startActivity(intent);
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




}