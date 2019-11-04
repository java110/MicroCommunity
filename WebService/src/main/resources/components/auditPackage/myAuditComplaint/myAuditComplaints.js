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
            _openAuditComplaintModel: function (_complaintInfo) {
                //vc.jumpToPage("/flow/addAuditOrderstepFlow")
                vc.component.myAuditComplaintsInfo.currentTaskId = _complaintInfo.taskId;
                vc.component.myAuditComplaintsInfo.currentComplaintId = _complaintInfo.complaintId;
                vc.emit('audit', 'openAuditModal', {});
            },
            _queryAuditOrdersMethod: function () {
                vc.component._listAuditOrders(DEFAULT_PAGE, DEFAULT_ROWS);
            },
            _auditComplaintInfo: function (_auditInfo) {
                _auditInfo.communityId = vc.getCurrentCommunity().communityId;
                _auditInfo.taskId = vc.component.myAuditComplaintsInfo.currentTaskId;
                _auditInfo.complaintId = vc.component.myAuditComplaintsInfo.currentComplaintId;
                //发送get请求
                vc.http.post('myAuditComplaints',
                    'audit',
                    JSON.stringify(_auditInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        vc.message("处理成功");
                        vc.component._listAuditOrders(DEFAULT_PAGE, DEFAULT_ROWS);
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                        vc.message("处理失败：" + errInfo);
                    }
                );
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
