(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            serviceProvideRemarkViewInfo:{
                flowComponent:'serviceProvideRemarkView',
                remark:'',

            }
        },
        watch:{
            serviceProvideRemarkViewInfo:{
                deep: true,
                handler:function(){
                    vc.component.saveServiceProvideRemarkInfo();
                }
             }
        },
         _initMethod:function(){

         },
         _initEvent:function(){

            vc.on('serviceProvideRemarkViewInfo', 'onIndex', function(_index){
                vc.component.serviceProvideRemarkViewInfo.index = _index;
            });
        },
        methods:{
            serviceProvideRemarkValidate(){
                return vc.validate.validate({
                    serviceProvideRemarkViewInfo:vc.component.serviceProvideRemarkViewInfo
                },{
                    'serviceProvideRemarkViewInfo.remark':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注内容不能超过200"
                        },
                    ],

                });
            },
            saveServiceProvideRemarkInfo:function(){
                if(vc.component.serviceProvideRemarkValidate()){
                    //侦听回传
                    vc.emit($props.callBackListener,$props.callBackFunction, vc.component.serviceProvideRemarkViewInfo);
                    return ;
                }
            }
        }
    });

})(window.vc);
