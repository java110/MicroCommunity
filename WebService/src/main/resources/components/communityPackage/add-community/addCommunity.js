(function (vc) {

    vc.extends({
        data: {
            addCommunityInfo: {
                name: '',
                address: '',
                areaAddress:'',
                nearbyLandmarks: '',
                cityCode: '0971',
                mapX: '101.33',
                mapY: '101.33',

            },
            areas: [],
            provs: [],
            citys: [],
            selectProv: '',
            selectCity: '',
            selectArea: '',
            allCity: []
        },
        _initMethod: function () {
            vc.component._initArea('101', '0');
        },
        _initEvent: function () {
            vc.on('addCommunity', 'openAddCommunityModal', function () {
                $('#addCommunityModel').modal('show');
            });
        },
        methods: {
            addCommunityValidate() {
                return vc.validate.validate({
                    addCommunityInfo: vc.component.addCommunityInfo
                }, {
                    'addCommunityInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "小区名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "4,20",
                            errInfo: "小区名称必须在10至20字符之间"
                        },
                    ],
                    'addCommunityInfo.address': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "小区地址不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "小区地址不能大于200个字符"
                        },
                    ],
                    'addCommunityInfo.nearbyLandmarks': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "附近地标不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "50",
                            errInfo: "小区附近地标不能大于50个字符"
                        },
                    ],
                    'addCommunityInfo.cityCode': [
                        {
                            limit: "maxLength",
                            param: "12",
                            errInfo: "小区城市编码不能大于4个字符"
                        },
                    ],
                    'addCommunityInfo.mapX': [
                        {
                            limit: "maxLength",
                            param: "20",
                            errInfo: "小区城市编码不能大于4个字符"
                        },
                    ],
                    'addCommunityInfo.mapY': [
                        {
                            limit: "maxLength",
                            param: "20",
                            errInfo: "小区城市编码不能大于4个字符"
                        },
                    ],


                });
            },
            saveCommunityInfo: function () {
                if (!vc.component.addCommunityValidate()) {
                    vc.message(vc.validate.errInfo);

                    return;
                }

                //vc.component.addCommunityInfo.communityId = vc.getCurrentCommunity().communityId;

                vc.component.addCommunityInfo.address = vc.component.addCommunityInfo.areaAddress+ vc.component.addCommunityInfo.address;


                vc.http.post(
                    'addCommunity',
                    'save',
                    JSON.stringify(vc.component.addCommunityInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addCommunityModel').modal('hide');
                            vc.component.clearAddCommunityInfo();
                            vc.emit('communityManage', 'listCommunity', {});

                            return;
                        }
                        vc.message(json);

                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);

                    });
            },
            clearAddCommunityInfo: function () {
                vc.component.addCommunityInfo = {
                    name: '',
                    address: '',
                    nearbyLandmarks: '',
                    cityCode: '0971',
                    mapX: '101.33',
                    mapY: '101.33',

                };
            },
            getProv: function (_prov) {
                vc.component._initArea('202', _prov);
            },
            getCity: function (_city) {
                vc.component._initArea('303',_city);
            },
            getArea:function(_area){
              vc.component.addCommunityInfo.cityCode = _area;

                vc.component.addCommunityInfo.areaAddress = '';
                if (vc.component.provs == null || vc.component.provs == undefined) {
                    return;
                }
                vc.component.provs.forEach(function (_param) {
                    if (_param.areaCode == vc.component.selectProv) {
                        vc.component.addCommunityInfo.areaAddress = _param.areaName;
                    }
                });

                vc.component.citys.forEach(function (_param) {
                    if (_param.areaCode == vc.component.selectCity) {
                        vc.component.addCommunityInfo.areaAddress += _param.areaName;
                    }
                });

                vc.component.areas.forEach(function (_param) {
                    if (_param.areaCode == vc.component.selectArea) {
                        vc.component.addCommunityInfo.areaAddress += _param.areaName;
                    }
                });
            },
            _initArea: function (_areaLevel, _parentAreaCode) { //加载区域
                var _param = {
                    params: {
                        areaLevel: _areaLevel,
                        parentAreaCode: _parentAreaCode
                    }
                };
                vc.http.get('addCommunity', 'getAreas',
                    _param,
                    function (json, res) {
                        if (res.status == 200) {
                            var _tmpAreas = JSON.parse(json);
                            if (_areaLevel == '101') {
                                vc.component.provs = _tmpAreas;
                            } else if (_areaLevel == '202') {
                                vc.component.citys = _tmpAreas;
                            } else {
                                vc.component.areas = _tmpAreas;
                            }
                            return;
                        }
                        //vc.component.$emit('errorInfoEvent',json);
                    }, function (errInfo, error) {
                        console.log('请求失败处理', errInfo, error);
                        vc.toast("查询地区失败");
                    });
            },
        }
    });

})(window.vc);
