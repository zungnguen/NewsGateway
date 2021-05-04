package com.example.newsgatewayfinal;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    static final String SERVICE_MSG = "SERVICE_MSG";
    static final String NEWS_MSG = "NEWS_MSG";
    private static final String TAG = "";
    private ArrayAdapter mDrawerListadapter;
    private ViewPager pager;
    private NewsArticle newsarticle;
    private Reciever catReciever;
    private MyPageAdapter nAdapter;
    private List<Fragment> fragments;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    HashMap hashmap = new HashMap();
    private int counter = 1;
    private static int counter2 = 1;
    private ArrayList<SpannableString> spannableStrings = new ArrayList<>();
    private ArrayList<NewsSource> newsresourcelist = new ArrayList<>();
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayList<String> newsresource = new ArrayList<>();
    private ArrayList<String> newsresource1 = new ArrayList<>();

    String[] categoryStringarray = new String[newsresource.size()];
    Fragment mContent;
    private static FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        String allcategory = "all";
        SourceRunnable sourceRunnable = new SourceRunnable(MainActivity.this);
        new Thread(sourceRunnable).start();

        catReciever = new Reciever();

        Intent serviceintent = new Intent(MainActivity.this, NewsService.class);
        startService(serviceintent);
        IntentFilter filter1 = new IntentFilter(NEWS_MSG);
        registerReceiver(catReciever, filter1);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        mDrawerListadapter = new ArrayAdapter<>(this,
                R.layout.drawer_list_object, items);
        mDrawerList.setAdapter(mDrawerListadapter);
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        selectItem(position);

                        pager.setBackground(null);
                        for(int i = 0; i< newsresourcelist.size(); i++)
                        {
                            if(items.get(position).equals(newsresourcelist.get(i).getName()))
                            {
                                Intent newintent = new Intent();
                                newintent.putExtra("myinfo", newsresourcelist.get(i));

                                newintent.setAction(SERVICE_MSG);
                                sendBroadcast(newintent);
                                mDrawerLayout.closeDrawer(mDrawerList);

                            }
                        }
                    }
                }
        );

        if (savedInstanceState != null) {

            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        fragments = getFragments();

        nAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager =  findViewById(R.id.viewpager);
        pager.setBackgroundResource(R.drawable.news);
        pager.setAdapter(nAdapter);

    }



    private void selectItem(int position)
    {
        Toast.makeText(this, items.get(position), Toast.LENGTH_SHORT).show();
        setTitle(items.get(position));
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.action_menu, menu);

        return true;
    }


    public void setSources(ArrayList<NewsSource> newsresourcelist, ArrayList<String> newsresourcecategory1)
    {
        hashmap.clear();
        items.removeAll(items);
        this.newsresourcelist.removeAll(this.newsresourcelist);
        this.newsresourcelist.addAll(newsresourcelist);

        newsresourcecategory1.add(0, "all");


        if(counter == 1)
        {
            newsresource.removeAll(newsresource);
            newsresource.addAll(newsresourcecategory1);
            categoryStringarray = newsresource.toArray(new String[newsresource.size()]);
            counter++;

        }



        for(int i = 0; i< this.newsresourcelist.size(); i++)
        {
            items.add(this.newsresourcelist.get(i).getName());
            hashmap.put(this.newsresourcelist.get(i).getName(), this.newsresourcelist.get(i));
        }
        invalidateOptionsMenu();
        mDrawerListadapter.notifyDataSetChanged();


    }

    private void reDoFragments(ArrayList<NewsArticle> article)
    {
        for (int j = 0; j < nAdapter.getCount(); j++)
        {
            nAdapter.notifyChangeInPosition(j);
        }
        fragments.clear();

        for (int f = 0; f < article.size(); f++)
        {

            fragments.add(NewsFragment.newInstance(article.get(f).getTitle(), article.get(f).getImageurl(), article.get(f).getAuthor(), article.get(f).getDescription(), article.get(f).getDatepub(), article.get(f).getUrl(), " Page " + (f+1) + " of " + article.size()));

        }

        nAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        return fList;
    }

    @Override
    protected void onResume() {
        IntentFilter filter1 = new IntentFilter(NEWS_MSG);
        registerReceiver(catReciever, filter1);
        super.onResume();

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.clear();

        if(categoryStringarray.length != 0)
        {
            for (int i = 0; i < categoryStringarray.length; i++)
            {
                menu.add(R.menu.action_menu, Menu.NONE, 0, categoryStringarray[i]);
            }
            Integer[] colorarray = {Color.BLACK,Color.YELLOW,Color.BLUE,Color.GREEN,Color.RED,Color.CYAN,Color.MAGENTA,Color.DKGRAY};

            for(int i =0; i<menu.size();i++){
                MenuItem item = menu.getItem(i);
                SpannableString s = new SpannableString(menu.getItem(i).getTitle().toString());
                s.setSpan(new ForegroundColorSpan(colorarray[i]), 0, s.length(), 0);
                item.setTitle(s);
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }


        SourceRunnable sourceRunnable = new SourceRunnable(MainActivity.this, valueOf(item));
        new Thread(sourceRunnable).start();
        return true;
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(catReciever);
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        items.addAll(savedInstanceState.getStringArrayList("HISTORY"));
        newsresource.addAll(savedInstanceState.getStringArrayList("HISTORY1"));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("HISTORY",items);
        outState.putStringArrayList("HISTORY1",newsresource);

    }

    public void error404() {
        Log.d(TAG, "error404");;
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {

            return baseId + position;
        }


        public void notifyChangeInPosition(int n) {

            baseId += getCount() + n;
        }

    }

    private class Reciever extends BroadcastReceiver
    {
        @Override

        public void onReceive(Context context, Intent intent)
        {
            switch (intent.getAction())
            {
                case NEWS_MSG:
                    if (intent.hasExtra("test"))
                    {
                        reDoFragments((ArrayList<NewsArticle>) intent.getSerializableExtra("test"));
                    }
            }

        }
    }


}