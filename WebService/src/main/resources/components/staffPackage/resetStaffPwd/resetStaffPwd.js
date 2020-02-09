(function(vc){
    vc.extends({
        data:{
            resetStaffPwdInfo:{}
        },
        _initEvent:function(){
             vc.on('resetStaffPwd','openResetStaffPwd',function(_staffInfo){
                    vc.component.resetStaffPwdInfo = _staffInfo;
                    $('#resetStaffPwdModel').modal('show');
                });
        },
        methods:{
            closeDeleteStaffModel:function(){
                $('#resetStaffPwdModel').modal('hide');
            },

            resetStaffPwd:function(){
                var _dataObj = {
                    communityId:vc.getCurrentCommunity().communityId,
                    userId:vc.component.resetStaffPwdInfo.userId
                };
                vc.http.post(
                    'resetStaffPwd',
                    'reset',
                    JSON.stringify(_dataObj),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            var _pwd = JSON.parse(json);
                            $('#resetStaffPwdModel').modal('hide');
                            vc.toast("修改密码成功，密码为"+_pwd.pwd+"请及时修改密码",10*1000);
                            return ;
                        }
                        vc.component.resetStaffPwdInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.resetStaffPwdInfo.errorInfo = errInfo;
                     });
            }
        }
    });
})(window.vc);