(function(vc) {

    vc.extends({
        data: {
            roomCreateFeeAddInfo: {
                feeTypeCds:[],
                feeConfigs:[],
                locationTypeCd: '',
                locationObjId: '',
                floorId: '',
                floorNum: '',
                floorName: '',
                unitId: '',
                unitName: '',
                roomId: '',
                feeTypeCd:'',
                configId:'',
                billType:'',
                roomState:'',
                isMore:false,
                locationTypeCdName:'',
            }
        },
        _initMethod: function() {
            vc.getDict('pay_fee_config',"fee_type_cd",function(_data){
                vc.component.roomCreateFeeAddInfo.feeTypeCds = _data;
            });

        },
        _initEvent: function() {
            vc.on('roomCreateFeeAdd', 'openRoomCreateFeeAddModal',
            function(_room) {
                vc.component.roomCreateFeeAddInfo.isMore =_room.isMore;
                if(!_room.isMore){
                    vc.component.roomCreateFeeAddInfo.locationTypeCd = '5008';
                    vc.component.roomCreateFeeAddInfo.locationObjId = _room.room.roomId;
                    var room =  _room.room;
                    vc.component.roomCreateFeeAddInfo.locationTypeCdName = room.floorNum +'号楼'+room.unitNum+'单元'+room.roomNum+'室';
                }
                $('#roomCreateFeeAddModel').modal('show');

            });

            vc.on("roomCreateFeeAdd", "notify", function (_param) {
                if (_param.hasOwnProperty("floorId")) {
                    vc.component.roomCreateFeeAddInfo.floorId = _param.floorId;
                }

                if (_param.hasOwnProperty("unitId")) {
                    vc.component.roomCreateFeeAddInfo.unitId = _param.unitId;
                }

                if (_param.hasOwnProperty("roomId")) {
                    vc.component.roomCreateFeeAddInfo.roomId = _param.roomId;
                }
            });
        },
        methods: {

            roomCreateFeeAddValidate() {
                return vc.validate.validate({
                    roomCreateFeeAddInfo: vc.component.roomCreateFeeAddInfo
                },
                {
                    'roomCreateFeeAddInfo.locationTypeCd': [{
                        limit: "required",
                        param: "",
                        errInfo: "收费范围不能为空"
                    },
                    {
                        limit: "num",
                        param: "",
                        errInfo: "收费范围格式错误"
                    },
                    ],
                    'roomCreateFeeAddInfo.locationObjId': [{
                        limit: "required",
                        param: "",
                        errInfo: "收费对象不能为空"
                    }
                    ],
                    'roomCreateFeeAddInfo.feeTypeCd': [{
                        limit: "required",
                        param: "",
                        errInfo: "费用类型不能为空"
                    }
                    ],
                    'roomCreateFeeAddInfo.configId': [{
                        limit: "required",
                        param: "",
                        errInfo: "费用项目不能为空"
                    }
                    ],
                    'roomCreateFeeAddInfo.billType': [{
                        limit: "required",
                        param: "",
                        errInfo: "出账类型不能为空"
                    }
                    ],
                     'roomCreateFeeAddInfo.roomState': [{
                         limit: "required",
                         param: "",
                         errInfo: "出账类型不能为空"
                     }
                     ]
                });
            },
            saveRoomCreateFeeInfo: function() {

                vc.component.roomCreateFeeAddInfo.communityId = vc.getCurrentCommunity().communityId;
                if (vc.component.roomCreateFeeAddInfo.locationTypeCd == '1000') { //大门时直接写 小区ID
                    vc.component.roomCreateFeeAddInfo.locationObjId = vc.component.roomCreateFeeAddInfo.communityId;
                } else if (vc.component.roomCreateFeeAddInfo.locationTypeCd == '2000') {
                    vc.component.roomCreateFeeAddInfo.locationObjId = vc.component.roomCreateFeeAddInfo.unitId;
                } else if (vc.component.roomCreateFeeAddInfo.locationTypeCd == '3000') {
                    vc.component.roomCreateFeeAddInfo.locationObjId = vc.component.roomCreateFeeAddInfo.roomId;
                } else if (vc.component.roomCreateFeeAddInfo.locationTypeCd == '4000') {
                    vc.component.roomCreateFeeAddInfo.locationObjId = vc.component.roomCreateFeeAddInfo.floorId;
                } else if (vc.component.roomCreateFeeAddInfo.locationTypeCd == '5008') {
                }else {
                    vc.toast("收费范围错误");
                    return;
                }

                if (!vc.component.roomCreateFeeAddValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.component.roomCreateFeeAddInfo.communityId = vc.getCurrentCommunity().communityId;
                var _roomCreateFeeAddInfo = JSON.parse(JSON.stringify(vc.component.roomCreateFeeAddInfo));
                if(_roomCreateFeeAddInfo.locationTypeCd == '5008'){
                    _roomCreateFeeAddInfo.locationTypeCd = '3000';
                }

                vc.http.post('roomCreateFeeAdd', 'save', JSON.stringify(_roomCreateFeeAddInfo), {
                    emulateJSON: true
                },
                function(json, res) {
                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                    if (res.status == 200) {
                        //关闭model
                        var _json = JSON.parse(json);
                        $('#roomCreateFeeAddModel').modal('hide');
                        vc.component.clearAddFeeConfigInfo();
                        vc.toast("创建收费成功，总共["+_json.totalRoom+"]房屋，成功["+_json.successRoom+"],失败["+_json.errorRoom+"]",8000);
                        return;
                    }
                    vc.message(json);

                },
                function(errInfo, error) {
                    console.log('请求失败处理');

                    vc.message(errInfo);

                });
            },
            clearAddFeeConfigInfo: function() {
                var _feeTypeCds = vc.component.roomCreateFeeAddInfo.feeTypeCds;
                vc.component.roomCreateFeeAddInfo = {
                    feeConfigs:[],
                    locationTypeCd: '',
                    locationObjId: '',
                    floorId: '',
                    floorNum: '',
                    floorName: '',
                    unitId: '',
                    unitName: '',
                    roomId: '',
                    feeTypeCd:'',
                    configId:'',
                    billType:'',
                    roomState:'',
                    isMore:false,
                    locationTypeCdName:'',
                };

                vc.component.roomCreateFeeAddInfo.feeTypeCds = _feeTypeCds;
            },
            _changeFeeTypeCd:function(_feeTypeCd){

                var param = {
                    params: {
                        page:1,
                        row:20,
                        communityId:vc.getCurrentCommunity().communityId,
                        feeTypeCd:_feeTypeCd,
                        isDefault:'F'
                    }
                };

                //发送get请求
                vc.http.get('roomCreateFeeAdd', 'list', param,
                function(json, res) {
                    var _feeConfigManageInfo = JSON.parse(json);
                    vc.component.roomCreateFeeAddInfo.feeConfigs = _feeConfigManageInfo.feeConfigs;
                },
                function(errInfo, error) {
                    console.log('请求失败处理');
                });
            }
        }
    });

})(window.vc);