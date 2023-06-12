/**
 * 入口控制类
 *
 * // 1.0 系统基本入口类为 ./app/AppController 通过 servicePost  或者 serviceGet 拦截 请求
 * 请求地址为 /app/xxx.xxx  其中xxx.xxx 统称为 serviceCode  服务编码或者接口编码
 *
 * 数据流转流程： AppController.java下的servicePost 接受请求--->ApiSMOImpl.java下的doApi---> ApiServiceSMOImpl下的service方法 --> ApiServiceSMOImpl下的dealCmd方法
 * --->java110-service 模块下的 CmdApi 类 --->CmdServiceSMOImpl.java 的 cmd方法 ---> ServiceCmdListener.java 下的cmd 类 ---> 到具体的serviceCode 对应的cmd文件
 *
 * //2.0 支付 三方支付系统通知 统一有 ./app/payment 下的类来处理
 *
 * // 3.0 智能电表通知 通过 ./app/smartWeter 下的类来处理
 *
 * // 4.0 智能充电桩 通过./app/charge 下的类来处理
 */
package com.java110.boot.controller;