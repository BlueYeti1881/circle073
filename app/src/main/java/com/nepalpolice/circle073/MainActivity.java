package com.nepalpolice.circle073;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nepalpolice.circle073.offline.feedbacks;
import com.nepalpolice.circle073.offline.links;
import com.nepalpolice.circle073.offline.morangD;
import com.nepalpolice.circle073.offline.nonveg;
import com.nepalpolice.circle073.offline.rhymes;
import com.nepalpolice.circle073.offline.snacks;
import com.nepalpolice.circle073.offline.special;
import com.nepalpolice.circle073.offline.technology;
import com.nepalpolice.circle073.offline.vegI;
import com.nepalpolice.circle073.offline.visa;
import com.nepalpolice.circle073.update.UpdateService;
import com.nepalpolice.circle073.update.UpdateTask;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Menu mymenu;
    private ViewPager mViewPager;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    private static final String TAG = MainActivity.class.getSimpleName();

    static final int NOTIFICATION_ID = 89906862;
    public static boolean isAppRunning;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);



        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().show();

//Update Dialog



        if (!isNetworkStatusAvialable(getApplicationContext())) {


            Snackbar.make(findViewById(android.R.id.content), "No Internet", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();

        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFragment(new vegI(), "CDP");
        adapter.addFragment(new nonveg(), "Patan");
        adapter.addFragment(new snacks(), "Ascol");
        adapter.addFragment(new technology(), "Golden Gate");
        adapter.addFragment(new special(), "St.Xavier");
        adapter.addFragment(new visa(), "PNC");
        adapter.addFragment(new rhymes(), "Birendra");
        adapter.addFragment(new morangD(), "Morang");
        adapter.addFragment(new feedbacks(), "Feedback");
        adapter.addFragment(new links(), "Links");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mymenu = menu;
        return true;
    }
    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_refresh:

                if (isNetworkStatusAvialable(getApplicationContext())) {
                    Intent intent = new Intent(MainActivity.this, UpdateService.class);
                    startService(intent);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    ImageView iv = (ImageView) inflater.inflate(R.layout.iv_refresh, null);
                    Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
                    rotation.setRepeatCount(Animation.INFINITE);
                    iv.startAnimation(rotation);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        item.setActionView(iv);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                        new UpdateTask(this).execute();


                    }

                    Intent update_intent = new Intent(this, UpdateService.class);
                    startService(update_intent);
                    return true;
                }else{

                    Snackbar.make(findViewById(android.R.id.content), "No Internet", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }


            case R.id.menu_rate:

 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nepalpolice.circle073")));

            return true;

            case R.id.menu_share:


            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Your app url");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

            return true;



        }
        return super.onOptionsItemSelected(item);
    }



    public void resetUpdating() {
        // Get our refresh item from the menu
        MenuItem m = mymenu.findItem(R.id.action_refresh);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (m.getActionView() != null) {
                // Remove the animation.
                m.getActionView().clearAnimation();
                m.setActionView(null);
            }
        }

    }


        public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }
    @Override
    protected void onStart() {
        super.onStart();

        if (!isNetworkStatusAvialable(getApplicationContext())) {
            Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getBoolean("isFirstRunhajur", true);

            if (isFirstRun) {
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                        .putBoolean("isFirstRunhajur", false).commit();


                alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("No Internet") //AlertDialog title
                        .setMessage("App requires active Internet connection at First launch") //AlertDialog description
                        .setCancelable(true)
                        .setIcon(R.mipmap.ic_launcher)


                        //call button
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);

                            }
                        }).show();


                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);

            }
        }
    }



}

