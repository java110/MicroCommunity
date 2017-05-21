ruleService模块(规则服务模块)

1、建立规则相关表 

1> group_rule 表 规则组， 可以根据业务去指定走那个规则组

2> group_rule_relation 表 规则组域规则关系

3> rule_entrance 表 规则入口（根据订单动作），如只有客户订单走规则

4> rule_cond 表 规则实现方式，反射java 方法 配置sql 执行 过程

5> rule_cond_cfg 表 配置sql 表 和过程入参






