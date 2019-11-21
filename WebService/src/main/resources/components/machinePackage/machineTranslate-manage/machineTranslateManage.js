/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            machineTranslateManageInfo: {
                machineTranslates: [],
                total: 0,
                records: 1,
                moreCondition: false,
                machineName: '',
                conditions: {
                    machineCode: '',
                    typeCd: '',
                    objName: '',
                    objId: '',
                    communityId:vc.getCurrentCommunity().communityId
                }
            }
        },
        _initMethod: function () {
            vc.component._listMachineTranslates(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {

            vc.on('machineTranslateManage', 'listMachineTranslate', function (_param) {
                vc.component._listMachineTranslates(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listMachineTranslates(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listMachineTranslates: function (_page, _rows) {

                vc.component.machineTranslateManageInfo.conditions.page = _page;
                vc.component.machineTranslateManageInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.machineTranslateManageInfo.conditions
                };

                //发送get请求
                vc.http.get('machineTranslateManage',
                    'list',
                    param,
                    function (json, res) {
                        var _machineTranslateManageInfo = JSON.parse(json);
                        vc.component.machineTranslateManageInfo.total = _machineTranslateManageInfo.total;
                        vc.component.machineTranslateManageInfo.records = _machineTranslateManageInfo.records;
                        vc.component.machineTranslateManageInfo.machineTranslates = _machineTranslateManageInfo.machineTranslates;
                        vc.emit('pagination', 'init', {
                            total: vc.component.machineTranslateManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _openAddMachineTranslateModal: function () {
                vc.emit('addMachineTranslate', 'openAddMachineTranslateModal', {});
            },
            _openEditMachineTranslateModel: function (_machineTranslate) {
                vc.emit('editMachineTranslate', 'openEditMachineTranslateModal', _machineTranslate);
            },
            _openDeleteMachineTranslateModel: function (_machineTranslate) {
                vc.emit('deleteMachineTranslate', 'openDeleteMachineTranslateModal', _machineTranslate);
            },
            _queryMachineTranslateMethod: function () {
                vc.component._listMachineTranslates(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function () {
                if (vc.component.machineTranslateManageInfo.moreCondition) {
                    vc.component.machineTranslateManageInfo.moreCondition = false;
                } else {
                    vc.component.machineTranslateManageInfo.moreCondition = true;
                }
            }


        }
    });
})(window.vc);
