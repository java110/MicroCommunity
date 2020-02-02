(function(vc) {

    vc.extends({
        data: {
            parkingSpaceCreateFeeAddInfo: {
                feeTypeCds:[],
                feeConfigs:[],
                parkingAreas:[],
                locationTypeCd: '',
                locationObjId: '',
                psId: '',
                feeTypeCd:'',
                configId:'',
                billType:'',
                parkingSpaceState:'',
                isMore:false,
                locationTypeCdName:'',
            }
        },
        _initMethod: function() {
            vc.getDict('pay_fee_config',"fee_type_cd",function(_data){
                vc.component.parkingSpaceCreateFeeAddInfo.feeTypeCds = _data;
            });

            vc.component._loadParkingAreas();

        },
        _initEvent: function() {
            vc.on('parkingSpaceCreateFeeAdd', 'openParkingSpaceCreateFeeAddModal',
            function(_parkingSpace) {
                vc.component.parkingSpaceCreateFeeAddInfo.isMore =_parkingSpace.isMore;
                if(!_parkingSpace.isMore){
                    vc.component.parkingSpaceCreateFeeAddInfo.locationTypeCd = '3000';
                    vc.component.parkingSpaceCreateFeeAddInfo.locationObjId = _parkingSpace.parkingSpace.psId;
                    vc.component.parkingSpaceCreateFeeAddInfo.psId = _parkingSpace.parkingSpace.psId;
                    var parkingSpace =  _parkingSpace.parkingSpace;
                    vc.component.parkingSpaceCreateFeeAddInfo.locationTypeCdName = parkingSpace.areaNum +'号停车场'+parkingSpace.num+'号车位';
                }
                $('#parkingSpaceCreateFeeAddModel').modal('show');

            });
        },
        methods: {

            parkingSpaceCreateFeeAddValidate() {
                return vc.validate.validate({
                    parkingSpaceCreateFeeAddInfo: vc.component.parkingSpaceCreateFeeAddInfo
                },
                {
                    'parkingSpaceCreateFeeAddInfo.locationTypeCd': [{
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
                    'parkingSpaceCreateFeeAddInfo.locationObjId': [{
                        limit: "required",
                        param: "",
                        errInfo: "收费对象不能为空"
                    }
                    ],
                    'parkingSpaceCreateFeeAddInfo.feeTypeCd': [{
                        limit: "required",
                        param: "",
                        errInfo: "费用类型不能为空"
                    }
                    ],
                    'parkingSpaceCreateFeeAddInfo.configId': [{
                        limit: "required",
                        param: "",
                        errInfo: "费用项目不能为空"
                    }
                    ],
                    'parkingSpaceCreateFeeAddInfo.billType': [{
                        limit: "required",
                        param: "",
                        errInfo: "出账类型不能为空"
                    }
                    ],
                     'parkingSpaceCreateFeeAddInfo.parkingSpaceState': [{
                         limit: "required",
                         param: "",
                         errInfo: "出账类型不能为空"
                     }
                     ]
                });
            },
            saveParkingSpaceCreateFeeInfo: function() {

                vc.component.parkingSpaceCreateFeeAddInfo.communityId = vc.getCurrentCommunity().communityId;
                if (vc.component.parkingSpaceCreateFeeAddInfo.locationTypeCd == '1000') { // 小区ID
                    vc.component.parkingSpaceCreateFeeAddInfo.locationObjId = vc.component.parkingSpaceCreateFeeAddInfo.communityId;
                } else if (vc.component.parkingSpaceCreateFeeAddInfo.locationTypeCd == '2000') {
                } else if (vc.component.parkingSpaceCreateFeeAddInfo.locationTypeCd == '3000') {
                    vc.component.parkingSpaceCreateFeeAddInfo.locationObjId = vc.component.parkingSpaceCreateFeeAddInfo.psId;
                } else {
                    vc.toast("收费范围错误");
                    return;
                }

                if (!vc.component.parkingSpaceCreateFeeAddValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.component.parkingSpaceCreateFeeAddInfo.communityId = vc.getCurrentCommunity().communityId;

                vc.http.post('parkingSpaceCreateFeeAdd', 'save', JSON.stringify(vc.component.parkingSpaceCreateFeeAddInfo), {
                    emulateJSON: true
                },
                function(json, res) {
                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                    if (res.status == 200) {
                        //关闭model
                        var _json = JSON.parse(json);
                        $('#parkingSpaceCreateFeeAddModel').modal('hide');
                        vc.component.clearAddFeeConfigInfo();
                        vc.toast("创建收费成功，总共["+_json.totalParkingSpace+"]车位，成功["+_json.successParkingSpace+"],失败["+_json.errorParkingSpace+"]",8000);
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
                var _feeTypeCds = vc.component.parkingSpaceCreateFeeAddInfo.feeTypeCds;
                var _parkingAreas = vc.component.parkingSpaceCreateFeeAddInfo.parkingAreas;
                vc.component.parkingSpaceCreateFeeAddInfo = {
                     feeTypeCds:[],
                    feeConfigs:[],
                    parkingAreas:[],
                    locationTypeCd: '',
                    locationObjId: '',
                    psId: '',
                    feeTypeCd:'',
                    configId:'',
                    billType:'',
                    parkingSpaceState:'',
                    isMore:false,
                    locationTypeCdName:'',
                };

                vc.component.parkingSpaceCreateFeeAddInfo.feeTypeCds = _feeTypeCds;
                vc.component.parkingSpaceCreateFeeAddInfo.parkingAreas = _parkingAreas;
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
                vc.http.get('parkingSpaceCreateFeeAdd', 'list', param,
                function(json, res) {
                    var _feeConfigManageInfo = JSON.parse(json);
                    vc.component.parkingSpaceCreateFeeAddInfo.feeConfigs = _feeConfigManageInfo.feeConfigs;
                },
                function(errInfo, error) {
                    console.log('请求失败处理');
                });
            },
            _loadParkingAreas:function(_feeTypeCd){

                var param = {
                    params: {
                        page:1,
                        row:20,
                        communityId:vc.getCurrentCommunity().communityId,
                    }
                };

                //发送get请求
                vc.http.get('parkingSpaceCreateFeeAdd', 'listParkingArea', param,
                function(json, res) {
                    if(res.status == 200){
                        var _parkingAreaInfo = JSON.parse(json);
                        vc.component.parkingSpaceCreateFeeAddInfo.parkingAreas = _parkingAreaInfo.parkingAreas;
                    }
                },
                function(errInfo, error) {
                    console.log('请求失败处理');
                });
            }
        }
    });

})(window.vc);