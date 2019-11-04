/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            myAuditComplaintsInfo: {
                complaints: [],
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
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listAuditOrders(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listAuditOrders: function (_page, _rows) {

                vc.component.myAuditComplaintsInfo.conditions.page = _page;
                vc.component.myAuditComplaintsInfo.conditions.row = _rows;
                vc.component.myAuditComplaintsInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params: vc.component.myAuditComplaintsInfo.conditions
                };

                //发送get请求
                vc.http.get('myAuditComplaints',
                    'list',
                    param,
                    function (json, res) {
                        var _myAuditComplaintsInfo = JSON.parse(json);
                        vc.component.myAuditComplaintsInfo.total = _myAuditComplaintsInfo.total;
                        vc.component.myAuditComplaintsInfo.records = _myAuditComplaintsInfo.records;
                        vc.component.myAuditComplaintsInfo.complaints = _myAuditComplaintsInfo.complaints;
                        vc.emit('pagination', 'init', {
                            total: vc.component.myAuditComplaintsInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _openAuditComplaintModel: function () {
                //vc.jumpToPage("/flow/addAuditOrderstepFlow")
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
