(function(vc){
    vc.extends({
            data:{
               validateCode:'',
               codeImage:'',
            },
            _initMethod:function(){
                console.log("validate-code _initMethod 方法调用");
                 vc.component.generateCode();

            },
            _initEvent:function(){

            },
            watch: {
                validateCode:function(){
                    var validateParam = {
                        validateCode:vc.component.validateCode
                    };
                    vc.component.$emit('validate_code_component_param_change_event',validateParam);
                }
            },
            methods:{
                generateCode:function(){
                    var param = {
                        params:{
                            _uId:'123'
                        }
                    };
                    console.log("validate-code generateCode",param);
                    vc.http.get('validate-code','generateValidateCode',
                                 param,
                                 function(json,res){
                                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                    if(res.status == 200){
                                        vc.component.codeImage = json;
                                        return ;
                                    }
                                    vc.component.$emit('errorInfoEvent',json);
                                 },function(errInfo,error){
                                    console.log('请求失败处理',errInfo,error);
                                    vc.component.$emit('errorInfoEvent',errInfo);
                                 });

                },
                enterToLogin:function(){
                    vc.emit('login','doLogin',{});
                }
            },
             _destroyedMethod:function(){
                 console.log("登录验证码页面销毁调用");
             }

    });

})(window.vc);