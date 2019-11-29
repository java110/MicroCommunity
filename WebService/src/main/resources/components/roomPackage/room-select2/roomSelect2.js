(function(vc){
    vc.extends({
        propTypes: {
            parentModal:vc.propTypes.string,
            callBackListener:vc.propTypes.string, //父组件名称
            callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            roomSelect2Info:{
                units:[],
                floorId:'-1',
                unitId:'-1',
                roomId:'',
                unitName:'',
            }
        },
        watch:{
            roomSelect2Info:{
                deep: true,
                handler:function(){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.roomSelect2Info);
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
           vc.on('roomSelect2', "transferUnit",function (_param) {
                vc.copyObject(_param, vc.component.roomSelect2Info);
           });
        },
        methods: {
            _initUnitSelect2: function () {
                console.log("调用_initRoomSelect2 方法");
                $.fn.modal.Constructor.prototype.enforceFocus = function () {};
                $.fn.select2.defaults.set('width', '100%');
                $('.roomSelector').select2({
                    placeholder: '必填，请选择房屋',
                    ajax: {
                        url: "/callComponent/roomSelect2/loadRooms",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            console.log("param", params);
                            var _term = "";
                            if(params.hasOwnProperty("term")){
                                _term = params.term;
                            }
                            return {
                                roomNum: _term,
                                page: 1,
                                row:10,
                                unitId:vc.component.roomSelect2Info.unitId,
                                communityId:vc.getCurrentCommunity().communityId
                            };
                        },
                        processResults: function (data) {
                            console.log(data, vc.component._filterRoomData(data));
                            return {
                                results: vc.component._filterRoomData(data)
                            };
                        },
                        cache: true
                    }
                });
                $('.floorSelector').on("select2:select", function (evt) {
                    //这里是选中触发的事件
                    //evt.params.data 是选中项的信息
                    console.log('select',evt);
                    vc.component.roomSelect2Info.roomId = evt.params.data.id;
                    vc.component.roomSelect2Info.roomNum = evt.params.data.text;
                });

                $('.floorSelector').on("select2:unselect", function (evt) {
                    //这里是取消选中触发的事件
                    //如配置allowClear: true后，触发
                    console.log('unselect',evt)

                });
            },
            _filterRoomData:function (_rooms) {
                var _tmpRooms = [];
                for (var i = 0; i < _rooms.length; i++) {
                    var _tmpRoom = {
                        id:_rooms[i].roomId,
                        text:_rooms[i].roomNum
                    };
                    _tmpRooms.push(_tmpRoom);
                }
                return _tmpRooms;
            }
        }
    });
})(window.vc);
