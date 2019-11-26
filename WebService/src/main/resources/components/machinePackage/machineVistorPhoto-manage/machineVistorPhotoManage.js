/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            machineVistorPhotoManageInfo: {
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

            vc.on('machineVistorPhotoManage', 'listMachineRecord', function (_param) {
                vc.component._listMachineRecords(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listMachineRecords(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listMachineRecords: function (_page, _rows) {

                vc.component.machineVistorPhotoManageInfo.conditions.page = _page;
                vc.component.machineVistorPhotoManageInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.machineVistorPhotoManageInfo.conditions
                };

                //发送get请求
                vc.http.get('machineVistorPhotoManage',
                    'list',
                    param,
                    function (json, res) {
                        var _machineVistorPhotoManageInfo = JSON.parse(json);
                        vc.component.machineVistorPhotoManageInfo.total = _machineVistorPhotoManageInfo.total;
                        vc.component.machineVistorPhotoManageInfo.records = _machineVistorPhotoManageInfo.records;
                        vc.component.machineVistorPhotoManageInfo.machineRecords = _machineVistorPhotoManageInfo.machineRecords;
                        vc.emit('pagination', 'init', {
                            total: vc.component.machineVistorPhotoManageInfo.records,
                            dataCount: vc.component.machineVistorPhotoManageInfo.total,
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
            _openMachineRecordDetailModel:function(_machineRecord){
                vc.emit('machineRecordDetail', 'openMachineRecordDetailModal',_machineRecord);
            },
            _moreCondition: function () {
                if (vc.component.machineVistorPhotoManageInfo.moreCondition) {
                    vc.component.machineVistorPhotoManageInfo.moreCondition = false;
                } else {
                    vc.component.machineVistorPhotoManageInfo.moreCondition = true;
                }
            }


        }
    });
})(window.vc);
