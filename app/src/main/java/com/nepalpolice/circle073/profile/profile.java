package com.nepalpolice.circle073.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.github.clans.fab.FloatingActionButton;
import com.nepalpolice.circle073.R;
import com.nepalpolice.circle073.feedback.MainActivity;


public class profile extends AppCompatActivity {

    FloatingActionButton Call;
    FloatingActionButton Email;
    FloatingActionButton Faceb;
    FloatingActionButton Bag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Call = (FloatingActionButton) findViewById(R.id.call);
        Email = (FloatingActionButton) findViewById(R.id.email);
        Faceb = (FloatingActionButton) findViewById(R.id.facebook);
        Bag = (FloatingActionButton) findViewById(R.id.bags);


        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+9779868336847"));
                startActivity(intent);

            }
        });
        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, MainActivity.class);//Physics is personal Mnemonics
                startActivity(intent);

            }
        });
        Bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://play.google.com/store/apps/developer?id=Sagar%20Rawal&hl=en";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);


            }
        });

        Faceb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://www.facebook.com/groups/nayasoch/?ref=bookmarks";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);


            }
        });
    }
}