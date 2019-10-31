/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 30;
    vc.extends({
        data: {
            staffFeeManageInfo: {
                staffFees: [],
                staffFeeTypes:[],
                total: 0,
                records: 1,
                moreCondition: false,
                name: '',
                conditions: {
                    communityId: vc.getCurrentCommunity().communityId,
                    startTime: '',
                    endTime: '',
                    userCode:''
                }
            }
        },
        _initMethod: function () {
            vc.component._initDate();
            vc.component._liststaffFees(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._liststaffFees(_currentPage, DEFAULT_ROWS);
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
                        vc.component.staffFeeManageInfo.conditions.startTime = value ;
                    });
                $('.end_time').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".end_time").val();
                        vc.component.staffFeeManageInfo.conditions.endTime = value ;
                    });
            },
            _liststaffFees: function (_page, _rows) {

                vc.component.staffFeeManageInfo.conditions.page = _page;
                vc.component.staffFeeManageInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.staffFeeManageInfo.conditions
                };

                //发送get请求
                vc.http.get('staffFeeManage',
                    'list',
                    param,
                    function (json, res) {
                        var _staffFeeManageInfo = JSON.parse(json);
                        vc.component.staffFeeManageInfo.total = _staffFeeManageInfo.total;
                        vc.component.staffFeeManageInfo.records = parseInt(_staffFeeManageInfo.total/_rows +1);
                        vc.component.staffFeeManageInfo.staffFees = _staffFeeManageInfo.staffFees;
                        vc.emit('pagination', 'init', {
                            total: vc.component.staffFeeManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _querystaffFeeMethod: function () {
                vc.component._liststaffFees(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function () {
                if (vc.component.staffFeeManageInfo.moreCondition) {
                    vc.component.staffFeeManageInfo.moreCondition = false;
                } else {
                    vc.component.staffFeeManageInfo.moreCondition = true;
                }
            },
            _exportExcel:function () {

            }
        }
    });
})(window.vc);
