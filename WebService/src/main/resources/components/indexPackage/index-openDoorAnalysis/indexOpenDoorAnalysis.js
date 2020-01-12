(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            indexOpenDoorAnalysisInfo: {
                analysis: [],
                openTypeCd: '1000',
                total: 0,
                records: 1,
                startTime: '',
                endTime: ''
            }
        },
        _initMethod: function () {
            //vc.component._listAnalysisData(DEFAULT_PAGE,DEFAULT_ROWS);
            vc.component._initMachineRecordData();
            vc.component._initOpenDoorAnalysisDateInfo();
        },
        _initEvent: function () {
            vc.on("indexOpenDoorAnalysis", "_listAnalysisData", function () {
                vc.component._listAnalysisData(DEFAULT_PAGE, DEFAULT_ROWS);
            });
        },
        methods: {
            _initMachineRecordData: function () {
                var dom = document.getElementById("machine-record");
                dom.style.width = (window.innerWidth * 0.9) + 'px';
                var myChart = echarts.init(dom);

                var app = {};
                option = null;
                app.title = '坐标轴刻度与标签对齐';

                option = {
                    color: ['#3398DB'],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: ['12月9日', '12月10日', '12月11日', '12月12日', '12月13日', '12月14日', '12月15日'],
                            axisTick: {
                                alignWithLabel: true
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: '开门次数',
                            type: 'bar',
                            barWidth: '60%',
                            data: [10, 52, 200, 334, 390, 330, 220]
                        }
                    ]
                };
                if (option && typeof option === "object") {
                    myChart.setOption(option, true);
                }
            },
            _initOpenDoorAnalysisDateInfo: function () {
                vc.component.indexOpenDoorAnalysisInfo.startTime = vc.dateFormat(new Date().getTime());
                $('.start_time').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.start_time').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".start_time").val();
                        vc.component.indexOpenDoorAnalysisInfo.startTime = value;
                    });
                $('.end_time').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.end_time').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".end_time").val();
                        vc.component.indexOpenDoorAnalysisInfo.endTime = value;
                    });
            },
            _listOpenDoorData: function (_page, _row) {
                if (vc.getCurrentCommunity() == null || vc.getCurrentCommunity == undefined) {
                    return;
                }
                var param = {
                    params: {
                        page: _page,
                        row: _row,
                        communityId: vc.getCurrentCommunity().communityId,
                        openTypeCd: vc.component.indexOpenDoorAnalysisInfo.openTypeCd
                    }
                }

                //发送get请求
                vc.http.get('listAnalysis',
                    'list',
                    param,
                    function (json, res) {
                        var listAnalysisData = JSON.parse(json);

                        vc.component.indexOpenDoorAnalysisInfo.total = listAnalysisData.total;
                        vc.component.indexOpenDoorAnalysisInfo.records = listAnalysisData.records;
                        vc.component.indexOpenDoorAnalysisInfo.arrears = listAnalysisData.arrears;

                        vc.emit('pagination', 'init', {
                            total: vc.component.indexOpenDoorAnalysisInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );

            },
            _switchOpenType: function (_openTypeCd) {
                console.log('_openTypeCd')
                vc.component.indexOpenDoorAnalysisInfo.openTypeCd = _openTypeCd;
                vc.component._listOpenDoorData(DEFAULT_PAGE, DEFAULT_ROWS);
            }
        }
    })
})(window.vc);