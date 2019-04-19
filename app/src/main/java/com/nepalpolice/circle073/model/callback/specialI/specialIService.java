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

package com.nepalpolice.circle073.model.callback.specialI;


import com.nepalpolice.circle073.model.pojo.specialI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @date 1/22/2016
 */
public interface specialIService {

    @GET("feeds/xavier.json")
    Call<List<specialI>> getAllFlowers();
}
