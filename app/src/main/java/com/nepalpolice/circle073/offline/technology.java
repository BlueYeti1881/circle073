package com.nepalpolice.circle073.offline;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.nepalpolice.circle073.R;
import com.nepalpolice.circle073.model.helper.Utils;
import com.nepalpolice.circle073.model.pojo.techT;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.nepalpolice.circle073.controller.techTManager;
import com.nepalpolice.circle073.model.adapter.techTAdapter;
import com.nepalpolice.circle073.model.callback.techT.techTFetchListener;
import com.nepalpolice.circle073.model.database.techTDatabase;
import com.nepalpolice.circle073.model.helper.Utils;
import com.nepalpolice.circle073.model.helper.techTConstant;



public class technology extends Fragment implements techTAdapter.FlowerClickListener, techTFetchListener {
    Boolean _areLecturesLoaded =false;
    private techTManager mManager;
    private techTAdapter mFlowerAdapter;
    private techTDatabase mDatabase;
    private Button mReload;
    private ProgressDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);


        configViews();
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mFlowerAdapter);

        mManager = new techTManager();
        mDatabase = new techTDatabase(getActivity());

        loadFlowerFeed();


        return view;


    }


    private void configViews() {


        mFlowerAdapter = new techTAdapter(this);

    }



    private void loadFlowerFeed() {

        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Loading information..");
        mDialog.setCancelable(true);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);

        mFlowerAdapter.reset();

        mDialog.show();

        if (getNetworkAvailability()) {
            getFeed();
        } else {
            getFeedFromDatabase();
        }
    }

    private void getFeedFromDatabase() {
        mDatabase.fetchFlowers(this);
    }



    @Override
    public void onClick(int position) {

        techT flower = mFlowerAdapter.getSelectedFlower(position);
        final String email = flower.getCategory();
        final String phone = flower.getInstructions();

        CharSequence options[] = new CharSequence[]{"Email", "Call"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

        builder.setTitle("Select Options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Click Event for each item.
                if(i == 0){

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                    startActivity(Intent.createChooser(intent, ""));

                }

                if(i == 1){
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
                    // callIntent.setData(Uri.parse("tel:"+uri));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);

                }

            }
        });

        builder.show();



    }

    public void getFeed() {

        Call<List<techT>> listCall = mManager.getFlowerService().getAllFlowers();
        listCall.enqueue(new Callback<List<techT>>() {
            @Override
            public void onResponse(Call<List<techT>> call, Response<List<techT>> response) {
                if (response.isSuccessful()) {
                    List<techT> flowerList = response.body();

                    for (int i = 0; i < flowerList.size(); i++) {
                        techT flower = flowerList.get(i);
                         SaveIntoDatabase task = new SaveIntoDatabase();
                         task.execute(flower);

                        mFlowerAdapter.addFlower(flower);
                    }
                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<techT>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public boolean getNetworkAvailability() {
        return Utils.isNetworkAvailable(getActivity());
    }

    @Override
    public void onDeliverAllFlowers(List<techT> flowers) {

    }

    @Override
    public void onDeliverFlower(techT flower) {
        mFlowerAdapter.addFlower(flower);
    }

    @Override
    public void onHideDialog() {
        mDialog.dismiss();
    }

    public class SaveIntoDatabase extends AsyncTask<techT, Void, Void> {


        private final String TAG = SaveIntoDatabase.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(techT... params) {

            techT flower = params[0];

            try {
                InputStream stream = new URL(techTConstant.HTTP.BASE_URL + "/photos/" + flower.getPhoto()).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                flower.setPicture(bitmap);
                mDatabase.addFlower(flower);

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return null;
        }

    }

}


