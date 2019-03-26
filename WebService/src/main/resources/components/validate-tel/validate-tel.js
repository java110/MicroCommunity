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
                    if(!vc.validate.phone(vc.component.validateParam.tel)){
                          vc.component.$emit('errorInfoEvent',"手机号码为空或不正确，不能发送验证码");
                        return;
                    }
                    // 开启定时
                    vc.component.messageCodeTimer();

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

                },
                //验证码定时
                messageCodeTimer(){
                      var num = 0;

                    var _timer = vc.createTimer(function(){
                        num ++;
                        vc.component.buttonInfo = num +" 秒后重试";
                        if(num === 60){
                            vc.clearTimer(_timer);
                            vc.component.buttonInfo = "验证码";
                        }
                    },1000);
                }
            },
             _destroyedMethod:function(){
                 console.log("登录页面销毁调用");
             }

    });

})(window.vc);