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

/**
 * 费用信息 包
 * <p>
 * 包括：
 * 费用折扣 公摊公式 费用打印 收据 缴费审核 费用导入 水电抄表 租赁费用等信息
 * <p>
 * api 请写 对外提供接口信息
 * bmo 业务相关内容
 * dao 数据库操作相关内容
 * discount 优惠折扣实现类
 * kafka 为订单调度异步处理
 * listener 为方式一开发中的 业务处理
 * smo 内部服务调用接口能力开放
 * <p>
 * add by 吴学文 2020-12-23
 * <p>
 * 文档参考 ： http://www.homecommunity.cn/
 */
package com.java110.fee;