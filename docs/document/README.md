
## 介绍

HC小区管理系统是前后端完全开源并免费商用的小区物业管理系统，包含小区后端项目，小区前段项目，
物业版uni-app和业主版uni-app,最新SQL文件加群获取827669685

## 项目结构说明

    > java110-bean      #存放实体bean Vo对象（前台返回对象） Dto对象（数据传输对象） Po对象（数据库持久层对象）
    > java110-config    #存放系统配置类，公共配置文件如logback.xml 日志配置文件
    > java110-core      #存放系统核心类，各个微服务接口类，上下文对象，工厂类和事件相关类
    > java110-db        #存放整套系统的SQL文件mapper
    > java110-generator #代码生成器
    > java110-service   #存放服务基础信息，基本存放基类和自研动态SQL解析引擎
    > java110-util      #存放工具类，如时间工具类，日志工具类 常量类 异常处理类
    > service-api       #统一接口封装服务，对外提供统一场景化接口能力
    > service-comment   #评论信息存放服务
    > service-common    #配置公共存放服务，包括图片上传ftp, 工作流，设备信息
    > service-community #小区资产信息 存放服务 楼栋 单元 房屋 巡检 通知 停车位 报修 访客等功能
    > service-eureka    #微服务管理服务
    > service-fee       #费用处理微服务 费用项功能 缴费 退费功能
    > service-front     #统一对外接入服务，主要处理 登录验证 权限验证，对接微信 ，app 等
    > service-job       #系统定时任务服务 人脸同步设备 员工同步考勤机和费用定时出账
    > service-log       #系统日志记录服务
    > service-order     #系统服务调度服务,主要保证事务一致性，和业务轨迹记录
    > service-report    #报表服务
    > service-rule      #业务规则服务
    > service-sequence  #序列生成服务，采用 google 雪花算法
    > service-store     #商户服务，系统中 物业 代理商 开发者 运营团队 都属于商户
    > service-user      #用户服务，用户信息 业主信息 家庭成员