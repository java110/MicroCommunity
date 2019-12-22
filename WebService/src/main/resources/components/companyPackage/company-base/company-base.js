/**
 初始化 公司信息

 **/

(function (vc) {
    vc.extends({
        data: {
            storeTypes: [],
            companyBaseInfo: {
                name: "",
                areaAddress: '',
                address: "",
                tel: "",
                storeTypeCd: "",
                nearbyLandmarks: ""
            },
            areas: [],
            provs: [],
            citys: [],
            selectProv: '',
            selectCity: '',
            allCity: []
        },
        _initMethod: function () {
            vc.component.initStoreType();

            vc.component._initArea('101', '0');
        },
        _initEvent: function () {
//              vc.component.$on('errorInfoEvent',function(_errorInfo){
//                     vc.component.registerInfo.errorInfo = _errorInfo;
//                     console.log('errorInfoEvent 事件被监听',_errorInfo)
//                 });

        },
        watch: {
            companyBaseInfo: {
                deep: true,
                handler: function () {
                    vc.component.$emit('companyBaseEvent', vc.component.companyBaseInfo);
                }
            }
        },
        methods: {
            getProv: function (_prov) {
                vc.component._initArea('202', _prov);
            },
            getCity: function (_city) {
                //vc.component._initArea('303',_city);
                vc.component.companyBaseInfo.areaAddress = '';
                if (vc.component.provs == null || vc.component.provs == undefined) {
                    return;
                }
                vc.component.provs.forEach(function (_param) {
                    if (_param.areaCode == vc.component.selectProv) {
                        vc.component.companyBaseInfo.areaAddress = _param.areaName;
                    }
                });

                vc.component.citys.forEach(function (_param) {
                    if (_param.areaCode == vc.component.selectCity) {
                        vc.component.companyBaseInfo.areaAddress += _param.areaName;
                    }
                });

            },
            initStoreType: function () {
                var param = {
                    params: {
                        msg: "123"
                    }

                }
                vc.http.get('company', 'getStoreType',
                    JSON.stringify(param),
                    function (json, res) {
                        if (res.status == 200) {
                            vc.component.storeTypes = JSON.parse(json);
                            return;
                        }
                        //vc.component.$emit('errorInfoEvent',json);
                    }, function (errInfo, error) {
                        console.log('请求失败处理', errInfo, error);
                        vc.component.$emit('errorInfoEvent', errInfo);
                    });

            },
            _initArea: function (_areaLevel, _parentAreaCode) { //加载区域
                var _param = {
                    params: {
                        areaLevel: _areaLevel,
                        parentAreaCode: _parentAreaCode
                    }
                };
                vc.http.get('company', 'getAreas',
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
            validateBase: function () {
                return vc.validate.validate({
                    companyBaseInfo: vc.component.companyBaseInfo
                }, {
                    'companyBaseInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "公司名不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "100",
                            errInfo: "用户名长度必须在100位之内"
                        },
                    ],
                    'companyBaseInfo.areaAddress': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "请选择地址"
                        }
                    ],
                    'companyBaseInfo.address': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "地址不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "地址长度必须在200位之内"
                        },
                    ],
                    'companyBaseInfo.tel': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "手机号不能为空"
                        },
                        {
                            limit: "phone",
                            param: "",
                            errInfo: "不是有效的手机号"
                        }
                    ],
                    'companyBaseInfo.storeTypeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "商户类型不能为空"
                        }
                    ],
                    'companyBaseInfo.nearbyLandmarks': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "附近建筑不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "地址长度必须在200位之内"
                        }
                    ],

                });
            }
        }

    });

})(window.vc);

