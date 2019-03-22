(function(vc){
    vc.extends({
            data:{
               validateCode:'',
               codeImage:'/callComponent/validate-code/generateValidateCode',
            },
            _initMethod:function(){
                 vc.component.generateCode();

            },
            _initEvent:function(){

            },
            watch: {
                validateCode:function(){
                    var validateParam = {
                        validateCode:vc.component.validateCode
                    };
                    vc.component.$emit('login_param_change_event',validateParam);
                }
            },
            methods:{
                generateCode(){
                    var param = {
                        _uId:'123'
                    }
                    vc.http.call('validate-code','generateValidateCode',param,
                                {
                                    emulateJSON:true
                                 },
                                 function(json,res){
                                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                    if(res.status == 200){
                                        vc.component.codeImage = json;
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