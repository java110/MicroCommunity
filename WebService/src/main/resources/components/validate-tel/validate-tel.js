(function(vc){
    vc.extends({
            data:{
                validateParam:{
                    tel:'',
                    messageCode:'',
                },
               buttonInfo:'验证码'
            },
            _initMethod:function(){
                console.log("validate-tel _initMethod 方法调用");
                 //vc.component.generateCode();

            },
            _initEvent:function(){

            },
            watch: {
                validateParam:{
                    deep: true,
                    handler:function(){
                        console.log('通知号码信息',vc.component.validateParam.tel);
                        vc.component.$emit('validate_tel_change_event',vc.component.validateParam);
                    }
                 }
//                messageCode:function(){
//                    vc.component.$emit('validate_tel_change_event',vc.component.validateParam);
//                }
            },
            methods:{
                sendTelMessageCode(){
                    var param = {
                            tel:vc.component.validateParam.tel,
                    };
                    console.log("validate-tel sendTelMessageCode",JSON.stringify(param));
                    vc.http.post('validate-tel','sendTelMessageCode',
                                 JSON.stringify(param),
                                 {
                                     emulateJSON:true
                                  },
                                 function(json,res){
                                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                    if(res.status == 200){
                                        vc.component.errorInfo = json;
                                        return ;
                                    }
                                    vc.component.$emit('errorInfoEvent',json);
                                 },function(errInfo,error){
                                    console.log('请求失败处理',errInfo,error);
                                    vc.component.$emit('errorInfoEvent',errInfo);
                                 });

                }
            }

    });

})(window.vc);