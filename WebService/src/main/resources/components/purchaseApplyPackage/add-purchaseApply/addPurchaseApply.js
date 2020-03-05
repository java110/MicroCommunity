(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addPurchaseApplyInfo: {
                applyOrderId: '',
                state: '',
                description: ''

            }
        },
        _initMethod: function () {
            //初始化物品表格
            //vc.component.initResourceTable();
        },
        _initEvent: function () {
            vc.on('addPurchaseApply', 'openAddPurchaseApplyModal', function () {
                $('#addPurchaseApplyModel').modal('show');
            });
        },
        methods: {
            addPurchaseApplyValidate() {
                return vc.validate.validate({
                    addPurchaseApplyInfo: vc.component.addPurchaseApplyInfo
                }, {
                    'addPurchaseApplyInfo.state': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "订单状态不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "计算公式格式错误"
                        },
                    ],


                });
            },
            savePurchaseApplyInfo: function () {
                if (!vc.component.addPurchaseApplyValidate()) {
                    vc.toast(vc.validate.errInfo);

                    return;
                }

                vc.component.addPurchaseApplyInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addPurchaseApplyInfo);
                    $('#addPurchaseApplyModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addPurchaseApply',
                    'save',
                    JSON.stringify(vc.component.addPurchaseApplyInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addPurchaseApplyModel').modal('hide');
                            vc.component.clearAddPurchaseApplyInfo();
                            vc.emit('purchaseApplyManage', 'listPurchaseApply', {});

                            return;
                        }
                        vc.message(json);

                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);

                    });
            },
            clearAddPurchaseApplyInfo: function () {
                vc.component.addPurchaseApplyInfo = {
                    state: '',
                };
            },
            //初始化表格
            initResourceTable: function () {
                $('#resource_table').bootstrapTable({
                    height: 350,
                    toolbar: '#toolbar',
                    columns: [
                        {
                            field: 'resId',
                            title: '资源ID',
                        },
                        {
                            field: 'resName',
                            title: '资源名称',
                        }, {
                            field: 'resCode',
                            title: '物品编码',
                        }, {
                            field: 'price',
                            title: '单价',
                        }, {
                            field: 'stock',
                            title: '库存'
                        }, {
                            field: 'quantity',
                            title: '采购数量'
                        },
                        {
                            field: 'remark',
                            title: '备注'
                        }
                    ],
                });
            },
            openResourceStoreTableModel:function () {
                vc.emit('resourceStoreTable','openResourceStoreTableModal',null);
            }
        }
    });

})(window.vc);
