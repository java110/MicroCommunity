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
 * <b>report主要实现报表统计:</b><br>
 *     微服务 数据 分布在不通的数据库的不通片上，导致报表复杂SQL无法实现，
 *     特意涉及 报表服务是为了 报表服务连接融合库 方便写复杂SQL
 *
 *     add by wuxw 2020-10-15
 * @site http://www.homecommunity.cn
 * @gitee https://gitee.com/wuxw7/MicroCommunity.git
 */
package com.java110.report;