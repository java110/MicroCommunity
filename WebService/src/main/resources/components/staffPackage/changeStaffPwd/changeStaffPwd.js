/**
 权限组
 **/
(function (vc) {

    vc.extends({
        data: {
            changeStaffPwdInfo: {
                communityId: vc.getCurrentCommunity().communityId,
                oldPwd: '',
                newPwd: '',
                reNewPwd:''
            }
        },

        _initMethod: function () {

        },
        _initEvent: function () {

        },
        methods: {
            assetImportValidate: function () {
                return vc.validate.validate({
                    changeStaffPwdInfo: vc.component.changeStaffPwdInfo
                }, {

                    'changeStaffPwdInfo.oldPwd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "原始密码不能为空"
                        }
                    ],
                    'changeStaffPwdInfo.newPwd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "新密码不能为空"
                        }
                    ],
                    'changeStaffPwdInfo.reNewPwd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "确认密码不能为空"
                        }
                    ],
                    'changeStaffPwdInfo.communityId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "还未入驻小区，请先入驻小区"
                        }
                    ]
                });
            },
            _changePwd: function () {

                if (!vc.component.assetImportValidate()) {
                    return;
                }

                var _userInfo = vc.getData("/nav/getUserInfo");

                if(_userInfo.name == 'wuxw' || _userInfo.name == 'dev' || _userInfo.name == 'dails' || _userInfo.name == 'admin'){
                    vc.toast("演示环境密码，不允许修改");
                    return ;
                }


                if(vc.component.changeStaffPwdInfo.newPwd != vc.component.changeStaffPwdInfo.reNewPwd){
                    vc.toast('两次密码不一致');
                    return ;
                }

                vc.http.post(
                    'changeStaffPwd',
                    'change',
                    JSON.stringify(vc.component.changeStaffPwdInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            vc.toast("修改成功");
                            vc.component.changeStaffPwdInfo.oldPwd = '';
                            vc.component.changeStaffPwdInfo.newPwd = '';
                            vc.component.changeStaffPwdInfo.reNewPwd = '';
                            return ;
                        }
                        vc.toast(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.toast(errInfo);
                     });

            }
        }
    });

})(window.vc);