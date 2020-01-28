/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 30;
    vc.extends({
        data: {
            tempCarInoutFeeManageInfo: {
                payFees: [],
                total: 0,
                records: 1,
                moreCondition: false,
                name: '',
                conditions: {
                    communityId: vc.getCurrentCommunity().communityId,
                    feeTypeCd: '888800010007',
                    startTime: '',
                    endTime: '',
                    carNum:''
                }
            }
        },
        _initMethod: function () {
            vc.component._initDate();
            vc.component._listpayFees(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listpayFees(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _initDate:function(){
                $(".start_time").datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    minView: "month",
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $(".end_time").datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    minView: "month",
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.start_time').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".start_time").val();
                        vc.component.tempCarInoutFeeManageInfo.conditions.startTime = value ;
                    });
                $('.end_time').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".end_time").val();
                        vc.component.tempCarInoutFeeManageInfo.conditions.endTime = value ;
                    });
            },
            _listpayFees: function (_page, _rows) {

                vc.component.tempCarInoutFeeManageInfo.conditions.page = _page;
                vc.component.tempCarInoutFeeManageInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.tempCarInoutFeeManageInfo.conditions
                };

                //发送get请求
                vc.http.get('tempCarInoutFeeManage',
                    'list',
                    param,
                    function (json, res) {
                        var _tempCarInoutFeeManageInfo = JSON.parse(json);
                        vc.component.tempCarInoutFeeManageInfo.total = _tempCarInoutFeeManageInfo.total;
                        vc.component.tempCarInoutFeeManageInfo.records = parseInt(_tempCarInoutFeeManageInfo.total/_rows +1);
                        vc.component.tempCarInoutFeeManageInfo.payFees = _tempCarInoutFeeManageInfo.tempCarInoutFees;
                        vc.emit('pagination', 'init', {
                            total: vc.component.tempCarInoutFeeManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _queryPayFeeMethod: function () {
                vc.component._listpayFees(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function () {
                if (vc.component.tempCarInoutFeeManageInfo.moreCondition) {
                    vc.component.tempCarInoutFeeManageInfo.moreCondition = false;
                } else {
                    vc.component.tempCarInoutFeeManageInfo.moreCondition = true;
                }
            },
            _exportExcel:function () {

            }
        }
    });
})(window.vc);
