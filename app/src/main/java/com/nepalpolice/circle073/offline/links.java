package com.nepalpolice.circle073.offline;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nepalpolice.circle073.R;
import com.nepalpolice.circle073.profile.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sagar on 2017/09/23.
 */


public class links extends Fragment {
    // Array of strings storing country names
    String[] countries = new String[]{
            "Developer",
            "Facebook",
            "Blog",
            "Youtube",




    };

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] flags = new int[]{
            R.drawable.drawable_dev,
            R.drawable.drawable_fb,
            R.drawable.drawable_blog,
            R.drawable.drawable_ytd,



    };

    // Array of strings to store currencies
    String[] currency = new String[]{

            "Check Profile of  Developer behind the app for Support or More Info ",
            "Like us on Facebook and get updates on our future updates and More  ",
            "Visit Our Website to find More Details about our new recipes and various articles ",
            "Find Us on youtube for latest videos and don't forget to Subscribe us Too",




    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_links, container, false);
// Each row in the list stores country name, currency and flag
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 4; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("mero", "" + countries[i]);
            hm.put("cur"," " + currency[i]);
            hm.put("flag", Integer.toString(flags[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"flag", "mero","cur"};

        // Ids of views in listview_layout
        int[] to = {R.id.flag, R.id.mero,R.id.cur};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.listview_layout_links, from, to);
        // Getting a reference to listview of main.xml layout file
        ListView listView = (ListView) view.findViewById(R.id.listview);
        // Setting the adapter to the listView
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
        return view;
    }

// Setting the adapter to the listView


    // Item Click Listener for the listview
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
            // Getting the Container Layout of the ListView
            if(position == 0/*or any other position*/){

                Intent intent = new Intent(getActivity(), profile.class);
                startActivity(intent);
            }
            else if(position == 1){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/nayasoch/")));



            } // etc...

            else if(position == 2){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.blogger.com/blogger.g?blogID=8009971266772445616#allposts/src=sidebar")));




            } // etc...


            else if(position == 3){
   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCMSVH2cijOaLiNQ9LZyGhBw?view_as=subscriber")));

            } // etc...


        }
    };
}









