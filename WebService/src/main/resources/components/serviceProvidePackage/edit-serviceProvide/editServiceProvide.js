(function (vc, vm) {

    vc.extends({
        data: {
            editServiceProvideInfo: {
                id: '',
                name: '',
                serviceCode: '',
                params: '',
                queryModel: '',
                sql: '',
                template: '',
                proc: '',
                javaScript: '',
                remark: '',

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('editServiceProvide', 'openEditServiceProvideModal', function (_params) {
                vc.component.refreshEditServiceProvideInfo();
                $('#editServiceProvideModel').modal('show');
                vc.copyObject(_params, vc.component.editServiceProvideInfo);
                //vc.component.editServiceProvideInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            editServiceProvideValidate: function () {
                return vc.validate.validate({
                    editServiceProvideInfo: vc.component.editServiceProvideInfo
                }, {
                    'editServiceProvideInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "服务名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,50",
                            errInfo: "服务名称必须在2至50字符之间"
                        },
                    ],
                    'editServiceProvideInfo.serviceCode': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "服务编码不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,50",
                            errInfo: "服务编码必须在2至50字符之间"
                        },
                    ],
                    'editServiceProvideInfo.params': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "参数不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "500",
                            errInfo: "参数内容不能超过200"
                        },
                    ],
                    'editServiceProvideInfo.queryModel': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "实现方式不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,12",
                            errInfo: "实现方式错误"
                        },
                    ],
                    'editServiceProvideInfo.remark': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "备注内容不能超过200"
                        },
                    ],
                    'editServiceProvideInfo.id': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "提供ID不能为空"
                        }]

                });
            },
            editServiceProvide: function () {
                if (!vc.component.editServiceProvideValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editServiceProvide',
                    'update',
                    JSON.stringify(vc.component.editServiceProvideInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editServiceProvideModel').modal('hide');
                            vc.emit('serviceProvideManage', 'listServiceProvide', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditServiceProvideInfo: function () {
                vc.component.editServiceProvideInfo = {
                    id: '',
                    name: '',
                    serviceCode: '',
                    params: '',
                    queryModel: '',
                    sql: '',
                    template: '',
                    proc: '',
                    javaScript: '',
                    remark: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);
