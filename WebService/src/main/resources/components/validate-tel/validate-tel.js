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
                tel:function(){
                    vc.component.$emit('validate_tel_component_param_change_event',vc.component.validateParam);
                },
                messageCode:function(){
                    vc.component.$emit('validate_tel_component_param_change_event',vc.component.validateParam);
                }
            },
            methods:{
                sendTelMessageCode(){
                    var param = {
                        params:{
                            _uId:'123'
                        }
                    };
                    console.log("validate-tel sendTelMessageCode",param);
                    vc.http.get('validate-tel','sendTelMessageCode',
                                 param,
                                 function(json,res){
                                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                    if(res.status == 200){
                                        vc.component.errorInfo = json;
                                        return ;
                                    }
                                    vc.component.$emit('errorInfoEvent',json);
                                 },function(errInfo,error){
                                    console.log('请求失败处理');
                                    vc.component.errorInfo = errInfo;
                                 });

                }
            }

    });

})(window.vc);