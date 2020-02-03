/**
 入驻小区
 **/
(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 30;
    vc.extends({
        data: {
            feeSummaryInfo: {
                fees: [],
                feeSummaryType:'1001',
                total: 0,
                records: 1,
                moreCondition: false,
                name: '',
            }
        },
        _initMethod: function () {
            vc.component._listFees(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent: function () {
            vc.on('pagination', 'page_event', function (_currentPage) {
                vc.component._listFeeSummarys(_currentPage, DEFAULT_ROWS);
            });
        },
        methods: {
            _listFeeSummarys: function (_page, _rows) {
                var param = {
                    params: {
                        page: _page,
                        row:_rows,
                        feeSummaryType:vc.component.feeSummaryInfo.feeSummaryType,
                        communityId:vc.getCurrentCommunity().communityId
                    }
                };

                //发送get请求
                vc.http.get('feeSummary',
                    'list',
                    param,
                    function (json, res) {
                        var _feeSummaryInfo = JSON.parse(json);
                        vc.component.feeSummaryInfo.total = _feeSummaryInfo.total;
                        vc.component.feeSummaryInfo.records = parseInt(_feeSummaryInfo.total/_rows +1);
                        vc.component.feeSummaryInfo.fees = _feeSummaryInfo.fees;
                        vc.emit('pagination', 'init', {
                            total: vc.component.feeSummaryInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _exportExcel:function () {

            },
            _switchFeeSummaryType:function(_feeSummaryType){
                vc.component.feeSummaryInfo.feeSummaryType = _feeSummaryType;
                vc.component._listFeeSummarys(DEFAULT_PAGE,DEFAULT_ROWS);
            }
        }
    });
})(window.vc);
