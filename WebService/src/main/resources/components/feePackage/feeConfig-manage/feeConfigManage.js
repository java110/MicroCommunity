/**
    入驻小区
**/
(function(vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            feeConfigManageInfo: {
                feeConfigs: [],
                total: 0,
                records: 1,
                moreCondition: false,
                feeName: '',
                conditions: {
                    feeFlag: '',
                    feeName: '',
                    feeTypeCd: '',

                }
            }
        },
        _initMethod: function() {
            vc.component._listFeeConfigs(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function() {

            vc.on('feeConfigManage', 'listFeeConfig',
            function(_param) {
                vc.component._listFeeConfigs(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event',
            function(_currentPage) {
                vc.component._listFeeConfigs(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listFeeConfigs: function(_page, _rows) {

                vc.component.feeConfigManageInfo.conditions.page = _page;
                vc.component.feeConfigManageInfo.conditions.row = _rows;
                vc.component.feeConfigManageInfo.conditions.communityId = vc.getCurrentCommunity().communityId;

                var param = {
                    params: vc.component.feeConfigManageInfo.conditions
                };

                //发送get请求
                vc.http.get('feeConfigManage', 'list', param,
                function(json, res) {
                    var _feeConfigManageInfo = JSON.parse(json);
                    vc.component.feeConfigManageInfo.total = _feeConfigManageInfo.total;
                    vc.component.feeConfigManageInfo.records = _feeConfigManageInfo.records;
                    vc.component.feeConfigManageInfo.feeConfigs = _feeConfigManageInfo.feeConfigs;
                    vc.emit('pagination', 'init', {
                        total: vc.component.feeConfigManageInfo.records,
                        currentPage: _page
                    });
                },
                function(errInfo, error) {
                    console.log('请求失败处理');
                });
            },
            _openAddFeeConfigModal: function() {
                vc.emit('addFeeConfig', 'openAddFeeConfigModal', {});
            },
            _openEditFeeConfigModel: function(_feeConfig) {
                vc.emit('editFeeConfig', 'openEditFeeConfigModal', _feeConfig);
            },
            _openDeleteFeeConfigModel: function(_feeConfig) {
                vc.emit('deleteFeeConfig', 'openDeleteFeeConfigModal', _feeConfig);
            },
            _queryFeeConfigMethod: function() {
                vc.component._listFeeConfigs(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function() {
                if (vc.component.feeConfigManageInfo.moreCondition) {
                    vc.component.feeConfigManageInfo.moreCondition = false;
                } else {
                    vc.component.feeConfigManageInfo.moreCondition = true;
                }
            }

        }
    });
})(window.vc);