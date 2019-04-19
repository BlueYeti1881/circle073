/*
 * Copyright (c) 2015-2016 Filippo Engidashet. All Rights Reserved.
 * <p>
 *  Save to the extent permitted by law, you may not use, copy, modify,
 *  distribute or create derivative works of this material or any part
 *  of it without the prior written consent of Filippo Engidashet.
 *  <p>
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 */

package com.nepalpolice.circle073.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.nepalpolice.circle073.model.callback.morangT.morangTFetchListener;
import com.nepalpolice.circle073.model.helper.Utils;
import com.nepalpolice.circle073.model.helper.morangTConstant;
import com.nepalpolice.circle073.model.pojo.morangT;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Filippo Engidashet
 * @version 1.0
 * @date today
 */
public class morangTDatabase extends SQLiteOpenHelper {

    private static final String TAG = morangTDatabase.class.getSimpleName();

    public morangTDatabase(Context context) {
        super(context, morangTConstant.DATABASE.DB_NAME, null, morangTConstant.DATABASE.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(morangTConstant.DATABASE.CREATE_TABLE_QUERY);
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(morangTConstant.DATABASE.DROP_QUERY);
        this.onCreate(db);
    }

    public void addFlower(morangT flower) {

        Log.d(TAG, "Values Got " + flower.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(morangTConstant.DATABASE.PRODUCT_ID, flower.getProductId());
        values.put(morangTConstant.DATABASE.CATEGORY, flower.getCategory());
        values.put(morangTConstant.DATABASE.PRICE, flower.getPrice());
        values.put(morangTConstant.DATABASE.DURATION_TIME, flower.getDuration());
        values.put(morangTConstant.DATABASE.INSTRUCTIONS, flower.getInstructions());
        values.put(morangTConstant.DATABASE.NAME, flower.getName());
        values.put(morangTConstant.DATABASE.PHOTO_URL, flower.getPhoto());
        values.put(morangTConstant.DATABASE.PHOTO, Utils.getPictureByteOfArray(flower.getPicture()));

        try {
            db.insert(morangTConstant.DATABASE.TABLE_NAME, null, values);
        } catch (Exception e) {

        }
        db.close();
    }

    public void fetchFlowers(morangTFetchListener listener) {
        FlowerFetcher fetcher = new FlowerFetcher(listener, this.getWritableDatabase());
        fetcher.start();
    }

    public class FlowerFetcher extends Thread {

        private final morangTFetchListener mListener;
        private final SQLiteDatabase mDb;

        public FlowerFetcher(morangTFetchListener listener, SQLiteDatabase db) {
            mListener = listener;
            mDb = db;
        }

        @Override
        public void run() {
            Cursor cursor = mDb.rawQuery(morangTConstant.DATABASE.GET_FLOWERS_QUERY, null);

            final List<morangT> flowerList = new ArrayList<>();

            if (cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    do {
                        morangT flower = new morangT();
                                    flower.setFromDatabase(true);
                        flower.setName(cursor.getString(cursor.getColumnIndex(morangTConstant.DATABASE.NAME)));
                        flower.setPrice(cursor.getString(cursor.getColumnIndex(morangTConstant.DATABASE.PRICE)));
                        flower.setProductId(cursor.getString(cursor.getColumnIndex(morangTConstant.DATABASE.PRODUCT_ID)));
                        flower.setDuration(cursor.getString(cursor.getColumnIndex(morangTConstant.DATABASE.DURATION_TIME)));
                        flower.setInstructions(cursor.getString(cursor.getColumnIndex(morangTConstant.DATABASE.INSTRUCTIONS)));
                        flower.setCategory(cursor.getString(cursor.getColumnIndex(morangTConstant.DATABASE.CATEGORY)));
                        flower.setPicture(Utils.getBitmapFromByte(cursor.getBlob(cursor.getColumnIndex(morangTConstant.DATABASE.PHOTO))));

                        flower.setPhoto(cursor.getString(cursor.getColumnIndex(morangTConstant.DATABASE.PHOTO_URL)));

                        flowerList.add(flower);
                        publishFlower(flower);

                    } while (cursor.moveToNext());
                }
            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDeliverAllFlowers(flowerList);
                    mListener.onHideDialog();
                }
            });
        }

        public void publishFlower(final morangT flower) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDeliverFlower(flower);
                }
            });
        }
    }
}
