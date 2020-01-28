/**
    入驻小区
**/
(function(vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            carInManageInfo: {
                carIns: [],
                total: 0,
                records: 1,
                moreCondition: false,
                carNum: '',
                conditions: {
                    state: '100300,100400,100600',
                    carNum: '',
                    inoutId: '',
                    startTime: '',
                    endTime: '',

                }
            }
        },
        _initMethod: function() {
            vc.component._listCarIns(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function() {

            vc.on('carInoutManage', 'listCarInout',
            function(_param) {
                vc.component._listCarIns(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event',
            function(_currentPage) {
                vc.component._listCarIns(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listCarIns: function(_page, _rows) {

                vc.component.carInManageInfo.conditions.page = _page;
                vc.component.carInManageInfo.conditions.row = _rows;
                vc.component.carInManageInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params: vc.component.carInManageInfo.conditions
                };

                //发送get请求
                vc.http.get('carInManage', 'list', param,
                function(json, res) {
                    var _carInManageInfo = JSON.parse(json);
                    vc.component.carInManageInfo.total = _carInManageInfo.total;
                    vc.component.carInManageInfo.records = _carInManageInfo.records;
                    var _tmpCarIns = [];
                    for(var carInIndex = 0 ; carInIndex < _carInManageInfo.carInouts.length;carInIndex ++){
                        var _tmpCarIn = _carInManageInfo.carInouts[carInIndex];
                        var _tmpInTime = new Date(_tmpCarIn.inTime);
                        var _tmpNow = new Date();
                        var diff = _tmpNow - _tmpInTime;
                        //计算出小时数
                        var leave1=diff%(24*3600*1000)    //计算天数后剩余的毫秒数
                        var hours=Math.floor(leave1/(3600*1000))
                        //计算相差分钟数
                        var leave2=leave1%(3600*1000)        //计算小时数后剩余的毫秒数
                        var minutes=Math.floor(leave2/(60*1000))
                        if (isNaN(hours)) {
                         _tmpCarIn.continueHours = hours;
                        }else{
                          var newHours = hours+":"+minutes;
                          _tmpCarIn.continueHours = newHours;
                        }
                        _tmpCarIns.push(_tmpCarIn);
                    }
                    vc.component.carInManageInfo.carIns = _tmpCarIns;
                    vc.emit('pagination', 'init', {
                        total: vc.component.carInManageInfo.records,
                        currentPage: _page
                    });
                },
                function(errInfo, error) {
                    console.log('请求失败处理');
                });
            },
            _queryCarInoutMethod: function() {
                vc.component._listCarIns(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function() {
                if (vc.component.carInManageInfo.moreCondition) {
                    vc.component.carInManageInfo.moreCondition = false;
                } else {
                    vc.component.carInManageInfo.moreCondition = true;
                }
            }

        }
    });
})(window.vc);