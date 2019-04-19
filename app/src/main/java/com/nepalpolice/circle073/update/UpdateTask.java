package com.nepalpolice.circle073.update;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.nepalpolice.circle073.MainActivity;

/**
 * Created by Sagar on 2018/03/17.
 */
public class UpdateTask extends AsyncTask<Void, Void, Void> {

    private Context mCon;

    public UpdateTask(Context con)
    {
        mCon = con;
    }

    @Override
    protected Void doInBackground(Void... nope) {
        try {
            // Set a time to simulate a long update process.
            Thread.sleep(4000);

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void nope) {

        ((MainActivity) mCon).resetUpdating();

    }
}

