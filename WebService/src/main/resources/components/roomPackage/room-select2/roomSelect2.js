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
                    vc.emit($props.callBackListener,$props.callBackFunction,this.roomSelect2Info);
                }
            }
        },
        _initMethod:function(){
                this._initRoomSelect2();
        },
        _initEvent:function(){
            //监听 modal 打开
           /* $('#'+$props.parentModal).on('show.bs.modal', function () {
                 this._initUnitSelect2();
            })*/
           vc.on('roomSelect2', "transferRoom",function (_param) {
                vc.copyObject(_param, this.roomSelect2Info);
           });
            vc.on('roomSelect2','setRoom',function (_param) {
                vc.copyObject(_param, this.roomSelect2Info);
                $("#roomSelector").val(_param.roomId).select2();
            });
        },
        methods: {
            _initRoomSelect2: function () {
                console.log("调用_initRoomSelect2 方法");
                $.fn.modal.Constructor.prototype.enforceFocus = function () {};
                $.fn.select2.defaults.set('width', '100%');
                $('#roomSelector').select2({
                    placeholder: '必填，请选择房屋',
                    ajax: {
                        url: "/callComponent/roomSelect2/listRoom",
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
                                unitId:this.roomSelect2Info.unitId,
                                communityId:vc.getCurrentCommunity().communityId
                            };
                        },
                        processResults: function (data) {
                            console.log(data, this._filterRoomData(data.rooms));
                            return {
                                results: this._filterRoomData(data.rooms)
                            };
                        },
                        cache: true
                    }
                });
                $('#roomSelector').on("select2:select", function (evt) {
                    //这里是选中触发的事件
                    //evt.params.data 是选中项的信息
                    console.log('select',evt);
                    this.roomSelect2Info.roomId = evt.params.data.id;
                    this.roomSelect2Info.roomNum = evt.params.data.text;
                });

                $('#roomSelector').on("select2:unselect", function (evt) {
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
