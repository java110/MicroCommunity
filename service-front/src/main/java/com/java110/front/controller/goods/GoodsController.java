/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.front.controller.goods;

import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.front.smo.payment.IGoodsToPaySMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @desc 商品相关入口类 ，主要 对支付做拦截
 * <p>
 * add by 吴学文 8:54
 */

@RestController
@RequestMapping(path = "/goods")
public class GoodsController extends BaseController {


    //@Autowired
    private IGoodsToPaySMO goodsToPaySMOImpl;

    /**
     * <p>统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/unifieldOrder", method = RequestMethod.POST)
    public ResponseEntity<String> unifieldOrder(@RequestBody String postInfo, HttpServletRequest request) {
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(),
                appId);
        return goodsToPaySMOImpl.toPay(newPd);
    }
}
