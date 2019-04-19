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

import com.nepalpolice.circle073.model.callback.longT.longTFetchListener;
import com.nepalpolice.circle073.model.helper.Utils;
import com.nepalpolice.circle073.model.helper.longTConstant;
import com.nepalpolice.circle073.model.pojo.longT;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Filippo Engidashet
 * @version 1.0
 * @date today
 */
public class longTDatabase extends SQLiteOpenHelper {

    private static final String TAG = longTDatabase.class.getSimpleName();

    public longTDatabase(Context context) {
        super(context, longTConstant.DATABASE.DB_NAME, null, longTConstant.DATABASE.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(longTConstant.DATABASE.CREATE_TABLE_QUERY);
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(longTConstant.DATABASE.DROP_QUERY);
        this.onCreate(db);
    }

    public void addFlower(longT flower) {

        Log.d(TAG, "Values Got " + flower.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(longTConstant.DATABASE.PRODUCT_ID, flower.getProductId());
        values.put(longTConstant.DATABASE.CATEGORY, flower.getCategory());
        values.put(longTConstant.DATABASE.PRICE, flower.getPrice());
        values.put(longTConstant.DATABASE.DURATION_TIME, flower.getDuration());
        values.put(longTConstant.DATABASE.INSTRUCTIONS, flower.getInstructions());
        values.put(longTConstant.DATABASE.NAME, flower.getName());
        values.put(longTConstant.DATABASE.PHOTO_URL, flower.getPhoto());
        values.put(longTConstant.DATABASE.PHOTO, Utils.getPictureByteOfArray(flower.getPicture()));

        try {
            db.insert(longTConstant.DATABASE.TABLE_NAME, null, values);
        } catch (Exception e) {

        }
        db.close();
    }

    public void fetchFlowers(longTFetchListener listener) {
        FlowerFetcher fetcher = new FlowerFetcher(listener, this.getWritableDatabase());
        fetcher.start();
    }

    public class FlowerFetcher extends Thread {

        private final longTFetchListener mListener;
        private final SQLiteDatabase mDb;

        public FlowerFetcher(longTFetchListener listener, SQLiteDatabase db) {
            mListener = listener;
            mDb = db;
        }

        @Override
        public void run() {
            Cursor cursor = mDb.rawQuery(longTConstant.DATABASE.GET_FLOWERS_QUERY, null);

            final List<longT> flowerList = new ArrayList<>();

            if (cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    do {
                        longT flower = new longT();
                                    flower.setFromDatabase(true);
                        flower.setName(cursor.getString(cursor.getColumnIndex(longTConstant.DATABASE.NAME)));
                        flower.setPrice(cursor.getString(cursor.getColumnIndex(longTConstant.DATABASE.PRICE)));
                        flower.setProductId(cursor.getString(cursor.getColumnIndex(longTConstant.DATABASE.PRODUCT_ID)));
                        flower.setDuration(cursor.getString(cursor.getColumnIndex(longTConstant.DATABASE.DURATION_TIME)));
                        flower.setInstructions(cursor.getString(cursor.getColumnIndex(longTConstant.DATABASE.INSTRUCTIONS)));
                        flower.setCategory(cursor.getString(cursor.getColumnIndex(longTConstant.DATABASE.CATEGORY)));
                        flower.setPicture(Utils.getBitmapFromByte(cursor.getBlob(cursor.getColumnIndex(longTConstant.DATABASE.PHOTO))));

                        flower.setPhoto(cursor.getString(cursor.getColumnIndex(longTConstant.DATABASE.PHOTO_URL)));

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

        public void publishFlower(final longT flower) {
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
