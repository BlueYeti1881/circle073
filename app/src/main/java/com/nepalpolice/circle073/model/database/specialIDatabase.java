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

import com.nepalpolice.circle073.model.callback.specialI.specialIFetchListener;
import com.nepalpolice.circle073.model.helper.Utils;
import com.nepalpolice.circle073.model.helper.specialIConstant;
import com.nepalpolice.circle073.model.pojo.specialI;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Filippo Engidashet
 * @version 1.0
 * @date today
 */
public class specialIDatabase extends SQLiteOpenHelper {

    private static final String TAG = specialIDatabase.class.getSimpleName();

    public specialIDatabase(Context context) {
        super(context, specialIConstant.DATABASE.DB_NAME, null, specialIConstant.DATABASE.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(specialIConstant.DATABASE.CREATE_TABLE_QUERY);
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(specialIConstant.DATABASE.DROP_QUERY);
        this.onCreate(db);
    }

    public void addFlower(specialI flower) {

        Log.d(TAG, "Values Got " + flower.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(specialIConstant.DATABASE.PRODUCT_ID, flower.getProductId());
        values.put(specialIConstant.DATABASE.CATEGORY, flower.getCategory());
        values.put(specialIConstant.DATABASE.PRICE, flower.getPrice());
        values.put(specialIConstant.DATABASE.DURATION_TIME, flower.getDuration());
        values.put(specialIConstant.DATABASE.INSTRUCTIONS, flower.getInstructions());
        values.put(specialIConstant.DATABASE.NAME, flower.getName());
        values.put(specialIConstant.DATABASE.PHOTO_URL, flower.getPhoto());
        values.put(specialIConstant.DATABASE.PHOTO, Utils.getPictureByteOfArray(flower.getPicture()));

        try {
            db.insert(specialIConstant.DATABASE.TABLE_NAME, null, values);
        } catch (Exception e) {

        }
        db.close();
    }

    public void fetchFlowers(specialIFetchListener listener) {
        FlowerFetcher fetcher = new FlowerFetcher(listener, this.getWritableDatabase());
        fetcher.start();
    }

    public class FlowerFetcher extends Thread {

        private final specialIFetchListener mListener;
        private final SQLiteDatabase mDb;

        public FlowerFetcher(specialIFetchListener listener, SQLiteDatabase db) {
            mListener = listener;
            mDb = db;
        }

        @Override
        public void run() {
            Cursor cursor = mDb.rawQuery(specialIConstant.DATABASE.GET_FLOWERS_QUERY, null);

            final List<specialI> flowerList = new ArrayList<>();

            if (cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    do {
                        specialI flower = new specialI();
                                    flower.setFromDatabase(true);
                        flower.setName(cursor.getString(cursor.getColumnIndex(specialIConstant.DATABASE.NAME)));
                        flower.setPrice(cursor.getString(cursor.getColumnIndex(specialIConstant.DATABASE.PRICE)));
                        flower.setProductId(cursor.getString(cursor.getColumnIndex(specialIConstant.DATABASE.PRODUCT_ID)));
                        flower.setDuration(cursor.getString(cursor.getColumnIndex(specialIConstant.DATABASE.DURATION_TIME)));
                        flower.setInstructions(cursor.getString(cursor.getColumnIndex(specialIConstant.DATABASE.INSTRUCTIONS)));
                        flower.setCategory(cursor.getString(cursor.getColumnIndex(specialIConstant.DATABASE.CATEGORY)));
                        flower.setPicture(Utils.getBitmapFromByte(cursor.getBlob(cursor.getColumnIndex(specialIConstant.DATABASE.PHOTO))));

                        flower.setPhoto(cursor.getString(cursor.getColumnIndex(specialIConstant.DATABASE.PHOTO_URL)));

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

        public void publishFlower(final specialI flower) {
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
