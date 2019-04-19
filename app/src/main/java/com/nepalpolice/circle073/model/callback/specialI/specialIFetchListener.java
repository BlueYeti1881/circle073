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

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @date 1/24/2016
 */
public interface specialIFetchListener {

    void onDeliverAllFlowers(List<specialI> flowers);

    void onDeliverFlower(specialI flower);

    void onHideDialog();
}
