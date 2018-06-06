package com.java110.code.rest;

import com.java110.core.base.controller.BaseController;

/**
 *
 * 服务配置信息查询接口
 *
 * 实现原理：
 *
 * 首先从redis中查找是否有记录，如果没有记录再查询数据库，然后缓存redis中
 *
 * 将查询到的数据返回
 *
 * Created by wuxw on 2017/10/9.
 */
public class ServiceConfigServiceRest extends BaseController {


}
