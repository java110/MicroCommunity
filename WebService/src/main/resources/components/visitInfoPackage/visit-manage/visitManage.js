/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            appManageInfo: {
                apps: [],
                total: 0,
                records: 1
            }
        },
        _initMethod: function () {
            vc.component._listApps(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {
            vc.on('appManage', 'listApp', function (_param) {
                vc.component._listApps(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listApps(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listApps: function (_page, _rows) {
                var param = {
                        params: {
                            page: _page,
                            row: _rows,
                            communityId:vc.getCurrentCommunity().communityId
                        }

                    }

                //发送get请求
                vc.http.get('visitManage',
                    'list',
                    param,
                    function (json, res) {
                        var _visitManageInfo = JSON.parse(json);
                        console.log(_visitManageInfo);
                        vc.component.appManageInfo.total = _visitManageInfo.total;
                        vc.component.appManageInfo.records = _visitManageInfo.records;
                        vc.component.appManageInfo.visits = _visitManageInfo.visits;
                        vc.emit('pagination', 'init', {
                            total: _visitManageInfo.total,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _openAddVisitModal: function () {
                vc.jumpToPage("/flow/addNewOneVisit")
                // vc.emit('addApp','openAddAppModal',{});
            },
            _openEditVisitModel: function (_app) {
                vc.emit('editVisit', 'openEditVisitModel', _app);
                // vc.emit('deleteApp','openDeleteAppModal',_app);

            },
            _openDeleteAppModel: function (_app) {
                vc.emit('deleteApp', 'openDeleteAppModal', _app);
            }
        }
    });
})(window.vc);