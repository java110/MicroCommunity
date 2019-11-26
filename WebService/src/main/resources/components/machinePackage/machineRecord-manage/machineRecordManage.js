/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            machineRecordManageInfo: {
                machineRecords: [],
                total: 0,
                records: 1,
                moreCondition: false,
                name: '',
                conditions: {
                    name: '',
                    openTypeCd: '',
                    tel: '',
                    ownerTypeCd: '',
                    machineName: '',
                    machineCode: '',

                }
            }
        },
        _initMethod: function () {
            vc.component._listMachineRecords(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {

            vc.on('machineRecordManage', 'listMachineRecord', function (_param) {
                vc.component._listMachineRecords(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listMachineRecords(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listMachineRecords: function (_page, _rows) {

                vc.component.machineRecordManageInfo.conditions.page = _page;
                vc.component.machineRecordManageInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.machineRecordManageInfo.conditions
                };

                //发送get请求
                vc.http.get('machineRecordManage',
                    'list',
                    param,
                    function (json, res) {
                        var _machineRecordManageInfo = JSON.parse(json);
                        vc.component.machineRecordManageInfo.total = _machineRecordManageInfo.total;
                        vc.component.machineRecordManageInfo.records = _machineRecordManageInfo.records;
                        vc.component.machineRecordManageInfo.machineRecords = _machineRecordManageInfo.machineRecords;
                        vc.emit('pagination', 'init', {
                            total: vc.component.machineRecordManageInfo.records,
                            dataCount: vc.component.machineRecordManageInfo.total,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _queryMachineRecordMethod: function () {
                vc.component._listMachineRecords(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function () {
                if (vc.component.machineRecordManageInfo.moreCondition) {
                    vc.component.machineRecordManageInfo.moreCondition = false;
                } else {
                    vc.component.machineRecordManageInfo.moreCondition = true;
                }
            }


        }
    });
})(window.vc);
