(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            devServiceProvideViewInfo:{
                flowComponent:'devServiceProvideView',
                queryModel:'',
params:'',
sql:'',
template:'',
proc:'',
javaScript:'',

            }
        },
        watch:{
            devServiceProvideViewInfo:{
                deep: true,
                handler:function(){
                    vc.component.saveDevServiceProvideInfo();
                }
             }
        },
         _initMethod:function(){

         },
         _initEvent:function(){

            vc.on('devServiceProvideViewInfo', 'onIndex', function(_index){
                vc.component.devServiceProvideViewInfo.index = _index;
            });
        },
        methods:{
            devServiceProvideValidate(){
                return vc.validate.validate({
                    devServiceProvideViewInfo:vc.component.devServiceProvideViewInfo
                },{
                    'devServiceProvideViewInfo.queryModel':[
{
                            limit:"required",
                            param:"",
                            errInfo:"实现方式不能为空"
                        },
 {
                            limit:"maxin",
                            param:"1,12",
                            errInfo:"实现方式错误"
                        },
                    ],
'devServiceProvideViewInfo.params':[
{
                            limit:"required",
                            param:"",
                            errInfo:"参数不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"500",
                            errInfo:"参数内容不能超过200"
                        },
                    ],
'devServiceProvideViewInfo.sql':[
 {
                            limit:"maxLength",
                            param:"2000",
                            errInfo:"sql不能超过2000"
                        },
                    ],
'devServiceProvideViewInfo.template':[
 {
                            limit:"maxLength",
                            param:"2000",
                            errInfo:"输出模板不能超过2000"
                        },
                    ],
'devServiceProvideViewInfo.proc':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"存储过程不能超过200"
                        },
                    ],
'devServiceProvideViewInfo.javaScript':[
 {
                            limit:"maxLength",
                            param:"2000",
                            errInfo:"java不能超过2000"
                        },
                    ],

                });
            },
            saveDevServiceProvideInfo:function(){
                if(vc.component.devServiceProvideValidate()){
                    //侦听回传
                    vc.emit($props.callBackListener,$props.callBackFunction, vc.component.devServiceProvideViewInfo);
                    return ;
                }
            }
        }
    });

})(window.vc);
