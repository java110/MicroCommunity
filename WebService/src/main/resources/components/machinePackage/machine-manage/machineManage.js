/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            machineManageInfo: {
                machines: [],
                total: 0,
                records: 1,
                moreCondition: false,
                machineName: '',
                conditions: {
                    machineCode: '',
                    machineTypeCd: '',
                    machineName: '',
                    machineIp: '',
                    machineMac: '',
                    communityId: vc.getCurrentCommunity().communityId

                }
            }
        },
        _initMethod: function () {
            vc.component._listMachines(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {

            vc.on('machineManage', 'listMachine', function (_param) {
                vc.component._listMachines(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listMachines(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listMachines: function (_page, _rows) {

                vc.component.machineManageInfo.conditions.page = _page;
                vc.component.machineManageInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.machineManageInfo.conditions
                };

                //发送get请求
                vc.http.get('machineManage',
                    'list',
                    param,
                    function (json, res) {
                        var _machineManageInfo = JSON.parse(json);
                        vc.component.machineManageInfo.total = _machineManageInfo.total;
                        vc.component.machineManageInfo.records = _machineManageInfo.records;
                        vc.component.machineManageInfo.machines = _machineManageInfo.machines;
                        vc.emit('pagination', 'init', {
                            total: vc.component.machineManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _openAddMachineModal: function () {
                vc.emit('addMachine', 'openAddMachineModal', {});
            },
            _openEditMachineModel: function (_machine) {
                vc.emit('editMachine', 'openEditMachineModal', _machine);
            },
            _openDeleteMachineModel: function (_machine) {
                vc.emit('deleteMachine', 'openDeleteMachineModal', _machine);
            },
            _queryMachineMethod: function () {
                vc.component._listMachines(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function () {
                if (vc.component.machineManageInfo.moreCondition) {
                    vc.component.machineManageInfo.moreCondition = false;
                } else {
                    vc.component.machineManageInfo.moreCondition = true;
                }
            },
            _openMachineDetailModel: function (_machine) {

            },
            _openRestartMachineModel: function (_machine) { //设备重启处理
                vc.emit('machineState', 'openMachineStateModal', {
                    machineId: _machine.machineId,
                    stateName: '重启',
                    state: '1400'
                });
            },
            _openUpgradeMachineModel: function (_machine) { //设备升级处理
                vc.emit('machineState', 'openMachineStateModal', {
                    machineId: _machine.machineId,
                    stateName: '升级',
                    state: '1500'
                });
            },
            _openForbiddenMachineModel: function (_machine) { //设备禁用处理
                vc.emit('machineState', 'openMachineStateModal', {
                    machineId: _machine.machineId,
                    stateName: '禁用',
                    state: '1600'
                });
            }


        }
    });
})(window.vc);
