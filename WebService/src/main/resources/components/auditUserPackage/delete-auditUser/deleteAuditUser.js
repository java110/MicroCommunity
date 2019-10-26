(function (vc, vm) {

    vc.extends({
        data: {
            deleteAuditUserInfo: {}
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('deleteAuditUser', 'openDeleteAuditUserModal', function (_params) {

                vc.component.deleteAuditUserInfo = _params;
                $('#deleteAuditUserModel').modal('show');

            });
        },
        methods: {
            deleteAuditUser: function () {
                vc.component.deleteAuditUserInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteAuditUser',
                    'delete',
                    JSON.stringify(vc.component.deleteAuditUserInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#deleteAuditUserModel').modal('hide');
                            vc.emit('auditUserManage', 'listAuditUser', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.message(json);

                    });
            },
            closeDeleteAuditUserModel: function () {
                $('#deleteAuditUserModel').modal('hide');
            }
        }
    });

})(window.vc, window.vc.component);
