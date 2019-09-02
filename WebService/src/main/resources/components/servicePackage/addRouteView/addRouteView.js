(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addRouteViewInfo:{
                flowComponent:'addRouteView',
                orderTypeCd:'',
invokeLimitTimes:'1000',
invokeModel:'',

            }
        },
        watch:{
            addRouteViewInfo:{
                deep: true,
                handler:function(){
                    vc.component.saveAddRouteInfo();
                }
             }
        },
         _initMethod:function(){

         },
         _initEvent:function(){

            vc.on('addRouteViewInfo', 'onIndex', function(_index){
                vc.component.addRouteViewInfo.index = _index;
            });
        },
        methods:{
            addRouteValidate(){
                return vc.validate.validate({
                    addRouteViewInfo:vc.component.addRouteViewInfo
                },{
                    'addRouteViewInfo.orderTypeCd':[
{
                            limit:"required",
                            param:"",
                            errInfo:"订单类型不能为空"
                        },
 {
                            limit:"maxin",
                            param:"1,4",
                            errInfo:"订单类型错误"
                        },
                    ],
'addRouteViewInfo.invokeLimitTimes':[
{
                            limit:"required",
                            param:"",
                            errInfo:"调用次数不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"调用次数错误"
                        },
                    ],
'addRouteViewInfo.invokeModel':[
{
                            limit:"required",
                            param:"",
                            errInfo:"调用方式不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"50",
                            errInfo:"消息队列不能超过50"
                        },
                    ],

                });
            },
            saveAddRouteInfo:function(){
                if(vc.component.addRouteValidate()){
                    //侦听回传
                    vc.emit($props.callBackListener,$props.callBackFunction, vc.component.addRouteViewInfo);
                    return ;
                }
            }
        }
    });

})(window.vc);
