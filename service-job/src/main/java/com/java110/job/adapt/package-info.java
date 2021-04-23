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
package com.java110.job.adapt;

/**
 * 该包 下抒写databus 适配器信息，抒写时为了不影响主业务的受理，建议 同步其他平台 一律采用异步方式传输，
 * 例如再hcIot 包下新建 asyn ，在MachineTransactionIotAdapt 中同步准备要求的报文，但是同步外围系统时 采用
 *  TransactionMachineAsynImpl 异步方式
 *
 *  add by wuxw 2020-12-08
 *
 **/