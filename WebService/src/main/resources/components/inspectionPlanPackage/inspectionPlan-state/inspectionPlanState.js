(function (vc, vm) {

    vc.extends({
        data: {
            inspectionPlanStateInfo: {
                inspectionPlanId: '',
                stateName: '',
                state: ''

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('inspectionPlanState', 'openInspectionPlanStateModal', function (_params) {
                vc.copyObject(_params, vc.component.inspectionPlanStateInfo);
                console.log("收到参数："+_params.state);
                console.log("收到参数："+_params.inspectionPlanId);
                console.log("收到参数："+_params.stateName);
                $('#inspectionPlanStateModel').modal('show');

            });
        },
        methods: {
            _changeInspectionPlanState: function () {
                vc.component.inspectionPlanStateInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'updateInspectionPlanState',
                    'update',
                    JSON.stringify(vc.component.inspectionPlanStateInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#inspectionPlanStateModel').modal('hide');
                            vc.emit('inspectionPlanManage', 'listInspectionPlan', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.message(json);

                    });
            },
            _closeInspectionPlanStateModel: function () {
                $('#inspectionPlanStateModel').modal('hide');
            }
        }
    });

})(window.vc, window.vc.component);
