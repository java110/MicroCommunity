(function (vc) {
    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            areaSelectInfo:{
                areas: [],
                provs: [],
                citys: [],
                allCity: [],
                selectProv: '',
                selectProvName: '',
                selectCity: '',
                selectCityName: '',
                selectArea: '',
                selectAreaName: ''
            }
        },
        _initMethod: function () {
            this._initArea('101', '0');
        },
        _initEvent: function () {

        },
        methods: {
            getProv: function (_prov) {
                this._initArea('202', _prov);
            },
            getCity: function (_city) {
                this._initArea('303',_city);
            },
            getArea:function(_area){
              this.areaSelectInfo.cityCode = _area;

                this.areaSelectInfo.areaAddress = '';
                if (this.areaSelectInfo.provs == null || this.areaSelectInfo.provs == undefined) {
                    return;
                }
                this.areaSelectInfo.provs.forEach(function (_param) {
                    if (_param.areaCode == this.areaSelectInfo.selectProv) {
                        this.areaSelectInfo.selectProvName = _param.areaName;
                    }
                });

                this.areaSelectInfo.citys.forEach(function (_param) {
                    if (_param.areaCode == this.areaSelectInfo.selectCity) {
                        this.areaSelectInfo.selectCityName = _param.areaName;
                    }
                });

                this.areaSelectInfo.areas.forEach(function (_param) {
                    if (_param.areaCode == this.areaSelectInfo.selectArea) {
                        this.areaSelectInfo.selectAreaName = _param.areaName;
                    }
                });

                vc.emit($namespace,$props.callBackListener,$props.callBackFunction,{
                    selectProv: this.areaSelectInfo.selectProv,
                    selectProvName: this.areaSelectInfo.selectProvName,
                    selectCity: this.areaSelectInfo.selectCity,
                    selectCityName: this.areaSelectInfo.selectCityName,
                    selectArea: this.areaSelectInfo.selectArea,
                    selectAreaName: this.areaSelectInfo.selectAreaName
                })
            },
            _initArea: function (_areaLevel, _parentAreaCode) { //加载区域
                var _param = {
                    params: {
                        areaLevel: _areaLevel,
                        parentAreaCode: _parentAreaCode
                    }
                };
                vc.http.get('areaSelect', 'getAreas',
                    _param,
                    function (json, res) {
                        if (res.status == 200) {
                            var _tmpAreas = JSON.parse(json);
                            if (_areaLevel == '101') {
                                this.areaSelectInfo.provs = _tmpAreas;
                            } else if (_areaLevel == '202') {
                                this.areaSelectInfo.citys = _tmpAreas;
                            } else {
                                this.areaSelectInfo.areas = _tmpAreas;
                            }
                            return;
                        }
                        //this.$emit('errorInfoEvent',json);
                    }, function (errInfo, error) {
                        console.log('请求失败处理', errInfo, error);
                        vc.toast("查询地区失败");
                    });
            },
        }
    });

})(window.vc);
