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

package com.nepalpolice.circle073.controller;

import com.nepalpolice.circle073.model.callback.morangT.morangTService;
import com.nepalpolice.circle073.model.helper.morangTConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @date 1/22/2016
 */
public class morangTManager {

    private morangTService mFlowerService;

    public morangTService getFlowerService() {
        if (mFlowerService == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(morangTConstant.HTTP.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mFlowerService = retrofit.create(morangTService.class);
        }
        return mFlowerService;
    }
}
