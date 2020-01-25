(function (vc) {
    vc.extends({
        propTypes: {
            parentModal: vc.propTypes.string,
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            parkingAreaSelect2Info: {
                parkingAreas: [],
                paId: '-1',
                num: '',
                parkingAreaSelector: {},
            }
        },
        watch: {
            parkingAreaSelect2Info: {
                deep: true,
                handler: function () {
                    vc.emit($props.callBackListener, $props.callBackFunction, this.parkingAreaSelect2Info);
                }
            }
        },
        _initMethod: function () {
            this._initParkingAreaSelect2();
        },
        _initEvent: function () {
            //监听 modal 打开
            vc.on('parkingAreaSelect2', 'setParkingArea', function (_param) {
                vc.copyObject(_param, this.parkingAreaSelect2Info);

                var option = new Option(_param.num + '号停车场', _param.paId, true, true);
                this.parkingAreaSelect2Info.parkingAreaSelector.append(option);
            });

            vc.on('parkingAreaSelect2', 'clearParkingArea', function (_param) {
                this.parkingAreaSelect2Info = {
                    parkingAreas: [],
                    paId: '-1',
                    num: '',
                    parkingAreaSelector: {},
                };
            });
        },
        methods: {
            _initParkingAreaSelect2: function () {
                console.log("调用_initParkingAreaSelect2 方法");
                $.fn.modal.Constructor.prototype.enforceFocus = function () {
                };
                $.fn.select2.defaults.set('width', '100%');
                this.parkingAreaSelect2Info.parkingAreaSelector = $('#parkingAreaSelector').select2({
                    placeholder: '必填，请选择停车场',
                    allowClear: true,//允许清空
                    escapeMarkup: function (markup) {
                        return markup;
                    }, // 自定义格式化防止xss注入
                    ajax: {
                        url: "/callComponent/parkingAreaSelect2/list",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            console.log("param", params);
                            var _term = "";
                            if (params.hasOwnProperty("term")) {
                                _term = params.term;
                            }
                            return {
                                num: _term,
                                page: 1,
                                row: 10,
                                communityId: vc.getCurrentCommunity().communityId
                            };
                        },
                        processResults: function (data) {
                            console.log(data, this._filterParkingAreaData(data.parkingAreas));
                            return {
                                results: this._filterParkingAreaData(data.parkingAreas)
                            };
                        },
                        cache: true
                    }
                });
                $('#parkingAreaSelector').on("select2:select", function (evt) {
                    //这里是选中触发的事件
                    //evt.params.data 是选中项的信息
                    console.log('select', evt);
                    this.parkingAreaSelect2Info.paId = evt.params.data.id;
                    this.parkingAreaSelect2Info.num = evt.params.data.text;
                });

                $('#parkingAreaSelector').on("select2:unselect", function (evt) {
                    //这里是取消选中触发的事件
                    //如配置allowClear: true后，触发
                    console.log('unselect', evt);
                    this.parkingAreaSelect2Info.paId = '-1';
                    this.parkingAreaSelect2Info.num = '';

                });
            },
            _filterParkingAreaData: function (_parkingAreas) {
                var _tmpParkingAreas = [];
                for (var i = 0; i < _parkingAreas.length; i++) {
                    var _tmpParkingArea = {
                        id: _parkingAreas[i].paId,
                        text: _parkingAreas[i].num
                    };
                    _tmpParkingAreas.push(_tmpParkingArea);
                }
                return _tmpParkingAreas;
            }
        }
    });
})(window.vc);
