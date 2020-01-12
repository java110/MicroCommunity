(function (vc, vm) {

    vc.extends({
        data: {
            editMenuInfo: {
                mId: '',
                name: '',
                url: '',
                seq: '',
                isShow: '',
                description: '',

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('editMenu', 'openEditMenuModal', function (_params) {
                vc.component.refreshEditMenuInfo();
                $('#editMenuModel').modal('show');
                vc.copyObject(_params, vc.component.editMenuInfo);

            });
        },
        methods: {
            editMenuValidate: function () {
                return vc.validate.validate({
                    editMenuInfo: vc.component.editMenuInfo
                }, {
                    'editMenuInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "菜单名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,10",
                            errInfo: "菜单名称必须在2至10字符之间"
                        },
                    ],
                    'editMenuInfo.url': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "菜单地址不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "菜单地址不能超过200"
                        },
                    ],
                    'editMenuInfo.seq': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "序列不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "序列必须为整数"
                        },
                    ],
                    'editMenuInfo.isShow': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "菜单显示不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,12",
                            errInfo: "菜单显示错误"
                        },
                    ],
                    'editMenuInfo.description': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "备注内容不能超过200"
                        },
                    ],
                    'editMenuInfo.mId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "菜单ID不能为空"
                        }]

                });
            },
            editMenu: function () {
                if (!vc.component.editMenuValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editMenu',
                    'update',
                    JSON.stringify(vc.component.editMenuInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editMenuModel').modal('hide');
                            vc.emit('menuManage', 'listMenu', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditMenuInfo: function () {
                vc.component.editMenuInfo = {
                    mId: '',
                    name: '',
                    url: '',
                    seq: '',
                    isShow: '',
                    description: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);
