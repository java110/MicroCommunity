/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 30;
    vc.extends({
        data: {
            payFeeManageInfo: {
                payFees: [],
                payFeeTypes:[],
                total: 0,
                records: 1,
                moreCondition: false,
                name: '',
                conditions: {
                    communityId: vc.getCurrentCommunity().communityId,
                    feeTypeCd: '',
                    startTime: '',
                    endTime: '',
                    userCode:''
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
                        vc.component.payFeeManageInfo.conditions.startTime = value ;
                    });
                $('.end_time').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".end_time").val();
                        vc.component.payFeeManageInfo.conditions.endTime = value ;
                    });
            },
            _listpayFees: function (_page, _rows) {

                vc.component.payFeeManageInfo.conditions.page = _page;
                vc.component.payFeeManageInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.payFeeManageInfo.conditions
                };

                //发送get请求
                vc.http.get('payFeeManage',
                    'list',
                    param,
                    function (json, res) {
                        var _payFeeManageInfo = JSON.parse(json);
                        vc.component.payFeeManageInfo.total = _payFeeManageInfo.total;
                        vc.component.payFeeManageInfo.records = parseInt(_payFeeManageInfo.total/_rows +1);
                        vc.component.payFeeManageInfo.payFees = _payFeeManageInfo.payFees;
                        vc.emit('pagination', 'init', {
                            total: vc.component.payFeeManageInfo.records,
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
                if (vc.component.payFeeManageInfo.moreCondition) {
                    vc.component.payFeeManageInfo.moreCondition = false;
                } else {
                    vc.component.payFeeManageInfo.moreCondition = true;
                }
            },
            _exportExcel:function () {

            },
            _listpayFees: function (_page, _rows) {
                var param = {
                    params:{
                        "hc":"cc@cc"
                    }
                };

                //发送get请求
                vc.http.get('payFeeManage',
                    'listFeeType',
                    param,
                    function (json, res) {
                        var _feeTypesInfo = JSON.parse(json);
                        vc.component.payFeeManageInfo.payFeeTypes = _feeTypesInfo;

                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
        }
    });
})(window.vc);
