(function (vc, vm) {

    vc.extends({
        data: {
            editResourceStoreInfo: {
                resId: '',
                resName: '',
                resCode: '',
                price: '',
                description: '',
                stock: ''

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('editItemNumberStore', 'openEditItemNumberStoreModal', function (_params) {
                vc.component.refreshEditResourceStoreInfo();
                $('#editItemNumberStoreModel').modal('show');
                vc.copyObject(_params, vc.component.editResourceStoreInfo);
                vc.component.editResourceStoreInfo.stock = _params.stock;
                vc.component.editResourceStoreInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            editResourceStoreValidate: function () {
                return vc.validate.validate({
                    editResourceStoreInfo: vc.component.editResourceStoreInfo
                }, {
                    'editResourceStoreInfo.resName': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "物品名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,100",
                            errInfo: "物品名称长度为2至100"
                        },
                    ],
                    'editResourceStoreInfo.resCode': [
                        {
                            limit: "maxLength",
                            param: "50",
                            errInfo: "物品编码不能超过50位"
                        },
                    ],
                    'editResourceStoreInfo.price': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "物品价格不能为空"
                        },
                        {
                            limit: "money",
                            param: "",
                            errInfo: "物品价格格式错误"
                        },
                    ],

                    'editResourceStoreInfo.description': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "描述不能为空"
                        },
                    ],
                    'editResourceStoreInfo.resId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "物品ID不能为空"
                        }]

                });
            },
            editResourceStoreForStock: function () {
                if (!vc.component.editResourceStoreValidate()) {
                    vc.message(vc.validate.errInfo);
                    return;
                }
                console.log("开始向后台发送请求!");
                vc.http.post(
                    'editResourceStore',
                    'update',
                    JSON.stringify(vc.component.editResourceStoreInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        console.log("请求结果为：");
                        console.log(res);
                        console.log(json);
                        if (res.status == 200) {
                            //关闭model
                            $('#editItemNumberStoreModel').modal('hide');
                            vc.emit('resourceStoreManage', 'listResourceStore', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditResourceStoreInfo: function () {
                vc.component.editResourceStoreInfo = {
                    resId: '',
                    resName: '',
                    resCode: '',
                    price: '',
                    description: '',
                    stock: ''
                }
            }
        }
    });

})(window.vc, window.vc.component);
