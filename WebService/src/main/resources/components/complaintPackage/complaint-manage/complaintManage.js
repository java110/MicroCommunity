/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            complaintManageInfo: {
                complaints: [],
                total: 0,
                records: 1,
                moreCondition: false,
                complaintName: '',
                conditions: {
                    complaintId: '',
                    typeCd: '',
                    complaintName: '',
                    tel: '',
                    roomId: '',
                    state: '',

                }
            }
        },
        _initMethod: function () {
            vc.component._listComplaints(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {

            vc.on('complaintManage', 'listComplaint', function (_param) {
                vc.component._listComplaints(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listComplaints(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listComplaints: function (_page, _rows) {

                vc.component.complaintManageInfo.conditions.page = _page;
                vc.component.complaintManageInfo.conditions.row = _rows;
                vc.component.complaintManageInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params: vc.component.complaintManageInfo.conditions
                };

                //发送get请求
                vc.http.get('complaintManage',
                    'list',
                    param,
                    function (json, res) {
                        var _complaintManageInfo = JSON.parse(json);
                        vc.component.complaintManageInfo.total = _complaintManageInfo.total;
                        vc.component.complaintManageInfo.records = _complaintManageInfo.records;
                        vc.component.complaintManageInfo.complaints = _complaintManageInfo.complaints;
                        vc.emit('pagination', 'init', {
                            total: vc.component.complaintManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _openAddComplaintModal: function () {
                //vc.emit('addComplaint', 'openAddComplaintModal', {});
                vc.jumpToPage("/flow/addComplaintStepFlow")
            },
            _openEditComplaintModel: function (_complaint) {
                vc.emit('editComplaint', 'openEditComplaintModal', _complaint);
            },
            _openDeleteComplaintModel: function (_complaint) {
                vc.emit('deleteComplaint', 'openDeleteComplaintModal', _complaint);
            },
            _queryComplaintMethod: function () {
                vc.component._listComplaints(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function () {
                if (vc.component.complaintManageInfo.moreCondition) {
                    vc.component.complaintManageInfo.moreCondition = false;
                } else {
                    vc.component.complaintManageInfo.moreCondition = true;
                }
            }


        }
    });
})(window.vc);
