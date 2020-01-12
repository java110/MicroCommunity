(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            closeOrderInfo:{
                state:'',
                remark:''
            }
        },
        watch:{
            "closeOrderInfo.state":{//深度监听，可监听到对象、数组的变化
                handler(val, oldVal){
                    if(vc.notNull(val) && vc.component.closeOrderInfo.state == '1100'){
                        vc.component.closeOrderInfo.remark = "已处理";
                    }else{
                        vc.component.closeOrderInfo.remark = "";
                    }

                },
                deep:true
            }
         },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('closeOrder','openCloseOrderModal',function(){
                $('#closeOrderModel').modal('show');
            });
        },
        methods:{
            closeOrderValidate(){
                return vc.validate.validate({
                    closeOrderInfo:vc.component.closeOrderInfo
                },{
                    'closeOrderInfo.state':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"订单状态不能为空"
                        },
                        {
                            limit:"num",
                            param:"",
                            errInfo:"订单状态错误"
                        },
                    ],
                    'closeOrderInfo.remark':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"原因内容不能为空"
                        },
                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"原因内容不能超过200"
                        },
                    ]
                });
            },
            _closeOrderSubmit:function(){
                if(!vc.component.closeOrderValidate()){
                    vc.toast(vc.validate.errInfo);
                    return ;
                }
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.closeOrderInfo);
                    $('#closeOrderModel').modal('hide');

                    vc.component.clearAddBasePrivilegeInfo();
                    return ;
                }


            },
            clearAddBasePrivilegeInfo:function(){
                vc.component.closeOrderInfo={
                             state:'',
                             remark:''
                }
            }
        }
    });

})(window.vc);
