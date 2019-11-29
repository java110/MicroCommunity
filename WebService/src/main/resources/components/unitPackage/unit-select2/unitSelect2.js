(function(vc){
    vc.extends({
        propTypes: {
            parentModal:vc.propTypes.string,
            callBackListener:vc.propTypes.string, //父组件名称
            callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            unitSelect2Info:{
                units:[],
                floorId:'',
                unitId:'',
                unitNum:'',
                unitName:'',
            }
        },
        watch:{
            unitSelect2Info:{
                deep: true,
                handler:function(){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.unitSelect2Info);
                }
            }
        },
        _initMethod:function(){
                vc.component._initUnitSelect2();
        },
        _initEvent:function(){
            //监听 modal 打开
           /* $('#'+$props.parentModal).on('show.bs.modal', function () {
                 vc.component._initUnitSelect2();
            })*/
           vc.on('unitSelect2', "transferFloor",function (_param) {
                vc.copyObject(_param, vc.component.unitSelect2Info);
           });
        },
        methods: {
            _initUnitSelect2: function () {
                console.log("调用_initUnitSelect2 方法");
                $.fn.modal.Constructor.prototype.enforceFocus = function () {};
                $.fn.select2.defaults.set('width', '100%');
                if(!vc.notNull(vc.component.unitSelect2Info.floorId)){
                    vc.toast("未选择楼栋信息，请先选择楼栋");
                    return ;
                }
                $('.unitSelector').select2({
                    placeholder: '必填，请选择单元',
                    ajax: {
                        url: "/callComponent/unitSelect2/loadUnits",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            console.log("param", params);
                            var _term = "";
                            if(params.hasOwnProperty("term")){
                                _term = params.term;
                            }
                            return {
                                unitName: _term,
                                page: 1,
                                row:10,
                                floorId:vc.component.unitSelect2Info.floorId,
                                communityId:vc.getCurrentCommunity().communityId
                            };
                        },
                        processResults: function (data) {
                            console.log(data, vc.component._filterUnitData(data.units));
                            return {
                                results: vc.component._filterUnitData(data.units)
                            };
                        },
                        cache: true
                    }
                });
                $('.floorSelector').on("select2:select", function (evt) {
                    //这里是选中触发的事件
                    //evt.params.data 是选中项的信息
                    console.log('select',evt);
                    vc.component.unitSelect2Info.unitId = evt.params.data.id;
                    vc.component.unitSelect2Info.unitName = evt.params.data.text;
                });

                $('.floorSelector').on("select2:unselect", function (evt) {
                    //这里是取消选中触发的事件
                    //如配置allowClear: true后，触发
                    console.log('unselect',evt)

                });
            },
            _filterUnitData:function (_units) {
                var _tmpUnits = [];
                for (var i = 0; i < _units.length; i++) {
                    var _tmpUnit = {
                        id:_units[i].unitId,
                        text:_units[i].unitName
                    };
                    _tmpUnits.push(_tmpUnit);
                }
                return _tmpUnit;
            }
        }
    });
})(window.vc);
