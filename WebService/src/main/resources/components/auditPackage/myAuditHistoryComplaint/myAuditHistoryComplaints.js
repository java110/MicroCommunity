/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            myAuditHistoryComplaintsInfo: {
                complaints: [],
                total: 0,
                records: 1,
                moreCondition: false,
                currentTaskId: '',
                currentComplaintId: '',
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
            vc.on('myAuditComplaints', 'auditMessage', function (_auditInfo) {
                vc.component._auditComplaintInfo(_auditInfo);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listAuditOrders(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listAuditOrders: function (_page, _rows) {

                vc.component.myAuditHistoryComplaintsInfo.conditions.page = _page;
                vc.component.myAuditHistoryComplaintsInfo.conditions.row = _rows;
                vc.component.myAuditHistoryComplaintsInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params: vc.component.myAuditHistoryComplaintsInfo.conditions
                };

                //发送get请求
                vc.http.get('myAuditHistoryComplaints',
                    'list',
                    param,
                    function (json, res) {
                        var _myAuditHistoryComplaintsInfo = JSON.parse(json);
                        vc.component.myAuditHistoryComplaintsInfo.total = _myAuditHistoryComplaintsInfo.total;
                        vc.component.myAuditHistoryComplaintsInfo.records = _myAuditHistoryComplaintsInfo.records;
                        vc.component.myAuditHistoryComplaintsInfo.complaints = _myAuditHistoryComplaintsInfo.complaints;
                        vc.emit('pagination', 'init', {
                            total: vc.component.myAuditHistoryComplaintsInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
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
