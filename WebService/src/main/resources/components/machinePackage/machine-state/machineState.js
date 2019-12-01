(function (vc, vm) {

    vc.extends({
        data: {
            machineStateInfo: {
                machineId: '',
                stateName: '',
                state: ''

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('machineState', 'openMachineStateModal', function (_params) {
                vc.copyObject(_params, vc.component.machineStateInfo);
                $('#machineStateModel').modal('show');

            });
        },
        methods: {
            machineState: function () {
                vc.component.machineStateInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'machineState',
                    'delete',
                    JSON.stringify(vc.component.machineStateInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#machineStateModel').modal('hide');
                            vc.emit('machineManage', 'listMachine', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.message(json);

                    });
            },
            closeDeleteMachineModel: function () {
                $('#machineStateModel').modal('hide');
            }
        }
    });

})(window.vc, window.vc.component);
