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
                            row: _rows
                        }

                    }

                //发送get请求
                vc.http.get('visitManage',
                    'list',
                    param,
                    function (json, res) {
                        var _visitManageInfo = JSON.parse(json);
                        for (var k in _visitManageInfo.visits){
                            _visitManageInfo.visits[k].visitTime=new Date(_visitManageInfo.visits[k].visitTime).format('yyyy-MM-dd');
                            _visitManageInfo.visits[k].departureTime=new Date(_visitManageInfo.visits[k].departureTime).format('yyyy-MM-dd');
                        }
                        vc.component.appManageInfo.total = _visitManageInfo.total;
                        vc.component.appManageInfo.records = _visitManageInfo.records;
                        vc.component.appManageInfo.visits = _visitManageInfo.visits;
                        vc.emit('pagination', 'init', {
                            total: vc.component.appManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _openAddVisitModal: function () {
                vc.jumpToPage("/flow/viewVisitInfo")
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

Date.prototype.format =function(format){
    var o = {
        "M+" : this.getMonth()+1, //month
        "d+" : this.getDate(), //day
        "h+" : this.getHours(), //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3), //quarter
        "S" : this.getMilliseconds() //millisecond
    }
    if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
        (this.getFullYear()+"").substr(4- RegExp.$1.length));
    for(var k in o)if(new RegExp("("+ k +")").test(format))
        format = format.replace(RegExp.$1,
            RegExp.$1.length==1? o[k] :
                ("00"+ o[k]).substr((""+ o[k]).length));
    return format;
}