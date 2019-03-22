(function(vc){
    var vm = new Vue({
        el:'#validatecode',
            data:function(){
                return {
                   validateCode:'123',
                   codeImage:'/callComponent/validate-code/generateValidateCode',
                   errorInfo:''
                }
            },
            mounted:function(){
                       this.generateCode();
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
                                        vm.codeImage = json;
                                        return ;
                                    }
                                    vc.component.$emit('errorInfoEvent',json);
                                 },function(errInfo,error){
                                    console.log('请求失败处理');

                                    vm.errorInfo = errInfo;
                                 });

                }
            }

    });

})(window.vc);