/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    var ALL_ROWS = 100;
    vc.extends({
        data: {
            orgCommunityManageInfo: {
                orgCommunitys: [],
                total: 0,
                records: 1,
                moreCondition: false,
                orgId:'',
                orgName:''

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('orgCommunityManageInfo', 'openOrgCommunity', function (_param) {
                vc.copyObject(_param, vc.component.orgCommunityManageInfo);
                vc.component._listOrgCommunitys(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('orgCommunityManageInfo', 'listOrgCommunity', function (_param) {
                //vc.copyObject(_param, vc.component.orgCommunityManageInfo.conditions);
                vc.component._listOrgCommunitys(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listOrgCommunitys(_currentPage, DEFAULT_ROWS);
            });

        },
        methods: {
            _listOrgCommunitys: function (_page, _rows) {

                var param = {
                    params: {
                        page:_page,
                        row:_rows,
                        orgId:vc.component.orgCommunityManageInfo.orgId
                    }
                };

                //发送get请求
                vc.http.get('orgCommunityManage',
                    'list',
                    param,
                    function (json, res) {
                        var _orgCommunityManageInfo = JSON.parse(json);
                        vc.component.orgCommunityManageInfo.total = _orgCommunityManageInfo.total;
                        vc.component.orgCommunityManageInfo.records = _orgCommunityManageInfo.records;
                        vc.component.orgCommunityManageInfo.orgCommunitys = _orgCommunityManageInfo.orgCommunitys;
                        vc.emit('pagination', 'init', {
                            total: vc.component.orgCommunityManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _openAddOrgCommunityModal: function () {
                vc.emit('addOrgCommunity', 'openAddOrgCommunityModal', {
                    orgId: vc.component.orgCommunityManageInfo.orgId,
                    orgName: vc.component.orgCommunityManageInfo.orgName
                });
            },
            _openEditOrgCommunityModel: function (_orgCommunity) {
                vc.emit('editOrgCommunity', 'openEditOrgCommunityModal', _orgCommunity);
            },
            _openDeleteOrgCommunityModel: function (_orgCommunity) {
                vc.emit('deleteOrgCommunity', 'openDeleteOrgCommunityModal', _orgCommunity);
            },
            _openBeyondCommunity:function(_orgCommunity){
            },
            _queryOrgCommunityMethod: function () {
                vc.component._listOrgCommunitys(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function () {
                if (vc.component.orgCommunityManageInfo.moreCondition) {
                    vc.component.orgCommunityManageInfo.moreCondition = false;
                } else {
                    vc.component.orgCommunityManageInfo.moreCondition = true;
                }
            },
            _goBack:function(){
                vc.emit('orgManage','onBack',{});
            }




        }
    });
})(window.vc);
