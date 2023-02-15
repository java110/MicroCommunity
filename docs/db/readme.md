## 数据库脚本说明

1、HC小区管理系统数据库分为两个一个是TT库一个是hc_community库，分别对应的文件为TT.sql 和hc_community.sql库</br>

2、数据库版本推荐mysql5.6,也适用于mysql5.7 和mysql8.0 其他数据官方未做测试，请谨慎适用</br>
采用mysql5.7 或者 8.0时，需要关闭mysql大小写，group by，日期非空 校验</br>

3、导入后，需要确定 TT库表是否大于570张表，hc_community表是否大于120张表</br>

4、如果开发环境 sql 导入始终无法成功，您可以找台服务器 用梓豪安装HC（梓豪安装后会自动导入sql TT.sql 和hc_community.sql 不需要再导入）</br>
然后本地代码连接梓豪安装的mysql，梓豪默认安装的 用户名密码为 TT/hc12345678  hc_community/hc12345678

温馨提示：如果你把两个库合并为一个库 可能会存在部分功能不正常，比如数据重复等，请谨慎操作，除非贵团队熟悉sharejdbc