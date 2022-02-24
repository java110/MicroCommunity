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
package com.java110.api.controller.app;

import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.api.smo.complaint.ISaveComplaintSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 投诉意见
 */
@RestController
@RequestMapping(path = "/app")
public class ComplaintController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(ComplaintController.class);

    @Autowired
    private ISaveComplaintSMO saveComplaintSMOImpl;


    /**
     * 微信登录接口
     *
     * @param postInfo
     * @param request
     */
    @RequestMapping(path = "/complaint", method = RequestMethod.POST)
    public ResponseEntity<String> complaint(@RequestBody String postInfo, HttpServletRequest request) {

        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/

        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        String appId = request.getHeader("APP_ID");
        if(StringUtil.isEmpty(appId)){
            appId = request.getHeader("APP-ID");
        }
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(),pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(),
                appId,
                pd.getHeaders());

        return saveComplaintSMOImpl.save(newPd);
    }

}
