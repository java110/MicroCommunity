(function (vc, vm) {

    vc.extends({
        data: {
            editBasePrivilegeInfo: {
                pId: '',
                name: '',
                resource: '',
                domain: '',
                description: '',

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('editBasePrivilege', 'openEditBasePrivilegeModal', function (_params) {
                vc.component.refreshEditBasePrivilegeInfo();
                $('#editBasePrivilegeModel').modal('show');
                vc.copyObject(_params, vc.component.editBasePrivilegeInfo);
                vc.component.editBasePrivilegeInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            editBasePrivilegeValidate: function () {
                return vc.validate.validate({
                    editBasePrivilegeInfo: vc.component.editBasePrivilegeInfo
                }, {
                    'editBasePrivilegeInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "权限名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,10",
                            errInfo: "权限名称必须在2至10字符之间"
                        },
                    ],
                    'editBasePrivilegeInfo.resource': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "资源路径不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,10",
                            errInfo: "资源路径必须在1至200字符之间"
                        },
                    ],
                    'editBasePrivilegeInfo.domain': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "商户类型不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,12",
                            errInfo: "商户类型错误"
                        },
                    ],
                    'editBasePrivilegeInfo.description': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "备注内容不能超过200"
                        },
                    ],
                    'editBasePrivilegeInfo.pId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "权限ID不能为空"
                        }]

                });
            },
            editBasePrivilege: function () {
                if (!vc.component.editBasePrivilegeValidate()) {
                    vc.message(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editBasePrivilege',
                    'update',
                    JSON.stringify(vc.component.editBasePrivilegeInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editBasePrivilegeModel').modal('hide');
                            vc.emit('basePrivilegeManage', 'listBasePrivilege', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditBasePrivilegeInfo: function () {
                vc.component.editBasePrivilegeInfo = {
                    pId: '',
                    name: '',
                    domain: '',
                    description: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);
