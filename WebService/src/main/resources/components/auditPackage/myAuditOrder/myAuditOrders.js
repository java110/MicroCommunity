/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            myAuditOrdersInfo: {
                auditOrders: [],
                total: 0,
                records: 1,
                moreCondition: false,
                userName: '',
                conditions: {
                    AuditOrdersId: '',
                    userName: '',
                    auditLink: '',

                }
            }
        },
        _initMethod: function () {
            vc.component._listAuditOrders(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {

            vc.on('myAuditOrders', 'listAuditOrders', function (_param) {
                vc.component._listAuditOrders(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listAuditOrders(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listAuditOrders: function (_page, _rows) {

                vc.component.myAuditOrdersInfo.conditions.page = _page;
                vc.component.myAuditOrdersInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.myAuditOrdersInfo.conditions
                };

                //发送get请求
                vc.http.get('myAuditOrders',
                    'list',
                    param,
                    function (json, res) {
                        var _myAuditOrdersInfo = JSON.parse(json);
                        vc.component.myAuditOrdersInfo.total = _myAuditOrdersInfo.total;
                        vc.component.myAuditOrdersInfo.records = _myAuditOrdersInfo.records;
                        vc.component.myAuditOrdersInfo.AuditOrders = _myAuditOrdersInfo.AuditOrders;
                        vc.emit('pagination', 'init', {
                            total: vc.component.myAuditOrdersInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _openAuditOrderModel: function () {
                vc.jumpToPage("/flow/addAuditOrderstepFlow")
            },
            _queryAuditOrdersMethod: function () {
                vc.component._listAuditOrders(DEFAULT_PAGE, DEFAULT_ROWS);
            },
            _moreCondition: function () {
                if (vc.component.AuditOrdersManageInfo.moreCondition) {
                    vc.component.AuditOrdersManageInfo.moreCondition = false;
                } else {
                    vc.component.AuditOrdersManageInfo.moreCondition = true;
                }
            }


        }
    });
})(window.vc);
