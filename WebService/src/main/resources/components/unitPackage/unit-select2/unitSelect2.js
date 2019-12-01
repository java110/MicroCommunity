(function (vc) {
    vc.extends({
        propTypes: {
            parentModal: vc.propTypes.string,
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            unitSelect2Info: {
                units: [],
                floorId: '-1',
                unitId: '-1',
                unitNum: '',
                unitName: '',
                unitSelector:{}
            }
        },
        watch: {
            unitSelect2Info: {
                deep: true,
                handler: function () {
                    vc.emit($props.callBackListener, $props.callBackFunction, this.unitSelect2Info);
                    console.log('是否执行 watch',$props.callBackListener, $props.callBackFunction, this.unitSelect2Info);
                    vc.emit($namespace, 'roomSelect2', "transferRoom", this.unitSelect2Info);
                }
            }
        },
        _initMethod: function () {
            this._initUnitSelect2();
        },
        _initEvent: function () {
            //监听 modal 打开
            /* $('#'+$props.parentModal).on('show.bs.modal', function () {
                  this._initUnitSelect2();
             })*/
            vc.on('unitSelect2', "transferFloor", function (_param) {
                vc.copyObject(_param, this.unitSelect2Info);
            });
            vc.on('unitSelect2', 'setUnit', function (_param) {
                vc.copyObject(_param, this.unitSelect2Info);
               /* $(".unitSelector").val(_param.unitId).select2();*/
                var option = new Option(_param.unitNum , _param.unitId, true, true);
                this.unitSelect2Info.unitSelector.append(option);
            });
        },
        methods: {
            _initUnitSelect2: function () {
                console.log("调用_initUnitSelect2 方法");
                $.fn.modal.Constructor.prototype.enforceFocus = function () {
                };
                $.fn.select2.defaults.set('width', '100%');
                this.unitSelect2Info.unitSelector = $('#unitSelector').select2({
                    placeholder: '必填，请选择单元',
                    allowClear: true,//允许清空
                    escapeMarkup: function (markup) {
                        return markup;
                    }, // 自定义格式化防止xss注入
                    ajax: {
                        url: "/callComponent/unitSelect2/loadUnits",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            console.log("param", params);
                            var _term = "";
                            if (params.hasOwnProperty("term")) {
                                _term = params.term;
                            }
                            return {
                                unitNum: _term,
                                page: 1,
                                row: 10,
                                floorId: this.unitSelect2Info.floorId,
                                communityId: vc.getCurrentCommunity().communityId
                            };
                        },
                        processResults: function (data) {
                            console.log(data, this._filterUnitData(data));
                            return {
                                results: this._filterUnitData(data)
                            };
                        },
                        cache: true
                    }
                });
                $('#unitSelector').on("select2:select", function (evt) {
                    //这里是选中触发的事件
                    //evt.params.data 是选中项的信息
                    console.log('select', evt);
                    this.unitSelect2Info.unitId = evt.params.data.id;
                    this.unitSelect2Info.unitName = evt.params.data.text;
                });

                $('#unitSelector').on("select2:unselect", function (evt) {
                    //这里是取消选中触发的事件
                    //如配置allowClear: true后，触发
                    console.log('unselect', evt)

                });
            },
            _filterUnitData: function (_units) {
                var _tmpUnits = [];
                for (var i = 0; i < _units.length; i++) {
                    var _tmpUnit = {
                        id: _units[i].unitId,
                        text: _units[i].unitNum
                    };
                    _tmpUnits.push(_tmpUnit);
                }
                return _tmpUnits;
            }
        }
    });
})(window.vc);
