### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2019-06-17|wuxw

### 本页内容

主要讲述前段开发步骤。

## 组件概念

vc组件框架是在WebService服务下的src\resources\components目录下html，js以及组件名+Component.java 文件组成的，主要是通过<vc:create name="listDemo"></vc:create>
自定义标签完成的，<vc:create> 指令原理为将HTML文件和js文件组合成一个文件 复制到<vc:create></vc:create> 的位置，具体可以参考VueComponentElement.java类
系统在启动时，将src\resources\components目录下的HTML和js加载到内存中，可以查看VueComponentTemplate.java类，在解析<vc:create name="abc"> 标签时根据name 查找对应的
html和js 将HTML和js 组合在一起，将<vc:create name="abc">替换为html和js组合的内容。


## 流程页面

流程页面，如：地址https://demo.java110.com/flow/ownerFlow 中ownerFlow 就叫为一个流程页面，它存放在src\resources\view目录下
流程页面中包含，页面基本框架 菜单，头部导航栏，尾部版权栏等，中间为组件信息，公用js和css 引入。必须以Flow结尾，如demoFlow.html


## vue对象vc.component

vc.component 其实就是一个vue对象，在页面加载最后去创建，页面组件中只存在一个vue对象，也就是多个组件公用一个vue对象，是用vc.extends()方法去继承
vue对象，extends参数介绍如下：

```
vc.extends({
        propTypes: {

        },
        data:{

        },
        watch:function(){
        },
        _initMethod:function(){

        },
        _initEvent:function(){

        },
        methods:{

        }
    });
```

### propTypes节点

组件参数，一般情况下这个节点可以不用写，只有 引入组件时，需要传参时，才会用到。

### data节点

data节点就是vue的data节点用作数据绑定，data下的最好是一个对象，对象名取名为当前组件名+info组成，对象下再去写需要绑定的字段信息，这样做的目的是为
多个组件之间取相同的字段名称 导致影响显示效果。

### watch节点

watch节点为 vue的watch节点

### _initMethod节点

组件在加载时执行的代码可以放到这里，访问data下变量或method下的方法时必须 加入前缀 vc.component. 例如访问loadData方法时，vc.component.loadData()。

### _initEvent 节点

组件在加载时 做事件监听用，访问data下变量或method下的方法时必须 加入前缀 vc.component. 。


### methods 节点

等同于 vue 的method，各个组件的方法名建议不要一直，防止冲突，访问data下变量或method下的方法时必须 加入前缀 vc.component. 。

## 组件之间通信

组件通信主要用 监听方法 vc.on(componentName,businessAction,function(_param){}); 和 触发方法vc.emit(componentName,businessAction,_param);

### vc.on 方法

componentName 当前组件名 如listDemo  businessAction 业务动作 loadData, function(_param){} 再监听到事件后触发函数

### vc.emit方法

componentName 要触发的方法的组件名，要出发businessAction 业务动作，_param 传递参数，以对象的方式传递

## 组件传参

当一个组件被多个页面引用时，当组件处理完业务时，将结果传递给父组件，这样就引入一次组件就得在触发的地方多加一个触发情况，比较麻烦，测试我们引入组件参数

```
    propTypes: {
       emitChooseOwner:vc.propTypes.string
    },
```

将触发的父组件名称通过参数 emitChooseOwner 传入进来，如

```
    <vc:create name="listDemo"
               emitChooseOwner="parentComponent"></vc:create>

```

在组件中使用时，用$props.emitChooseOwner 方式使用，也就是在变量之前加入 $props.  如：

```
    vc.emit($props.emitChooseOwner,'chooseOwner',_owner);

```

## vc 组件提供函数

### vc.http.post方法

vc.http.post(componentCode,componentMethod,param,options,successCallback,errorCallback); http post 异步请求方法

componentCode 一般为当前组件名 是java组件的beanId

componentMethod 为组件中方法名

param 为post的参数

options 为 默认传 { emulateJSON:true } 对象

successCallback 成功时的回调函数

errorCallback 失败时的回调函数

### vc.http.get方法

vc.http.get(componentCode,componentMethod,param,successCallback,errorCallback); http get 异步请求方法

componentCode 一般为当前组件名 是java组件的beanId

componentMethod 为组件中方法名

param 为get的参数

successCallback 成功时的回调函数

errorCallback 失败时的回调函数

### vc.jumpToPage方法

vc.jumpToPage(url) 为页面跳转方法

url为跳转页面的地址 如跳转到业主页面则 写 /flow/ownerFlow

### vc.getCurrentCommunity 方法

vc.getCurrentCommunity(); 获取当前菜单方法，返回值为如 {"communityId":"123213","name":"测试小区"}

### vc.copyObject方法

vc.copyObject(org,dst);将org 对象中属性拷贝到 dst对象中属性相同的属性值

### vc.getParam 方法

vc.getParam(_key); 获取当前地址栏上的参数值

### vc.objToGetParam 方法

vc.objToGetParam(obj); 对象转为get参数方法，不包含？号



