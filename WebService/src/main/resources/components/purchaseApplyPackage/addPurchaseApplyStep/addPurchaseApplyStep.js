/**
 入驻小区
 **/
(function (vc) {
    vc.extends({
        data: {
            addPurchaseApplyStepInfo: {
                $step: {},
                index: 0,
                infos: [],
                purchaseApply:{
                    resourceStores:[],
                    description:'',
                    file:'',

                }
            }
        },
        _initMethod: function () {
            vc.component._initStep();
        },
        _initEvent: function () {
            vc.on("addPurchaseApplyStep", "notify", function (viewResourceStoreInfo2) {
                console.log("收到最终参数："+viewResourceStoreInfo2);
                vc.component.addPurchaseApplyStepInfo.purchaseApply.resourceStores = viewResourceStoreInfo2.resourceStores;
            });

            vc.on("addPurchaseApplyStep", "notify2", function (info) {
                vc.component.addPurchaseApplyStepInfo.purchaseApply.description = info.description;
            });

        },
        methods: {
            _initStep: function () {
                vc.component.addPurchaseApplyStepInfo.$step = $("#step");
                vc.component.addPurchaseApplyStepInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择物品", "申请说明"]
                });
                vc.component.addPurchaseApplyStepInfo.index = vc.component.addPurchaseApplyStepInfo.$step.getIndex();
            },
            _prevStep: function () {
                vc.component.addPurchaseApplyStepInfo.$step.prevStep();
                vc.component.addPurchaseApplyStepInfo.index = vc.component.addPurchaseApplyStepInfo.$step.getIndex();

                vc.emit('viewResourceStoreInfo2', 'onIndex', vc.component.addPurchaseApplyStepInfo.index);
                vc.emit('addPurchaseApplyView', 'onIndex', vc.component.addPurchaseApplyStepInfo.index);

            },
            _nextStep: function () {
                vc.emit('viewResourceStoreInfo2', 'getSelectResourceStores', null);
                var _resourceStores = vc.component.addPurchaseApplyStepInfo.purchaseApply.resourceStores;
                if (_resourceStores.length == 0) {
                    vc.message("请完善需要采购的物品信息");
                    return;
                }
               for( var i = 0; i < _resourceStores.length; i++){
                   if(_resourceStores[i].quantity <= 0){
                       vc.message("请完善需要采购的物品信息");
                        return;
                   }
               }
                vc.component.addPurchaseApplyStepInfo.$step.nextStep();
                vc.component.addPurchaseApplyStepInfo.index = vc.component.addPurchaseApplyStepInfo.$step.getIndex();

                vc.emit('viewResourceStoreInfo2', 'onIndex', vc.component.addPurchaseApplyStepInfo.index);
                vc.emit('addPurchaseApplyView', 'onIndex', vc.component.addPurchaseApplyStepInfo.index);

            },
            _finishStep: function () {
                vc.emit('addPurchaseApplyViewInfo', 'setPurchaseApplyInfo', null);
                var _description = vc.component.addPurchaseApplyStepInfo.purchaseApply.description;
                if (_description == null || _description == '') {
                    vc.message("请填写申请说明");
                    return;
                }
                vc.http.post(
                    'addPurchaseApply',
                    'save',
                    JSON.stringify(vc.component.addPurchaseApplyStepInfo.purchaseApply),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        if (res.status == 200) {

                            vc.message('处理成功', true);
                            //关闭model
                            vc.jumpToPage("/flow/purchaseApplyFlow?" + vc.objToGetParam(JSON.parse(json)));
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            }
        }
    });
})(window.vc);
