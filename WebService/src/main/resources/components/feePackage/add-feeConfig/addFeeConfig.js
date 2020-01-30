(function(vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string,
            //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addFeeConfigInfo: {
                configId: '',
                feeTypeCd: '',
                feeName: '',
                feeFlag: '',
                startTime: '',
                endTime: '',
                computingFormula: '',
                squarePrice: '',
                additionalAmount: '',
                feeTypeCds:[],
                computingFormulas:[]
            }
        },
        _initMethod: function() {
            vc.component._initAddFeeConfigDateInfo();
            vc.getDict('pay_fee_config',"fee_type_cd",function(_data){
                vc.component.addFeeConfigInfo.feeTypeCds = _data;
            });
            vc.getDict('pay_fee_config',"computing_formula",function(_data){
                vc.component.addFeeConfigInfo.computingFormulas = _data;
            });
        },
        _initEvent: function() {
            vc.on('addFeeConfig', 'openAddFeeConfigModal',
            function() {
                $('#addFeeConfigModel').modal('show');
            });
        },
        methods: {
            _initAddFeeConfigDateInfo: function () {
                vc.component.addFeeConfigInfo.startTime = vc.dateFormat(new Date().getTime());
                $('.addFeeConfigStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.addFeeConfigStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".addFeeConfigStartTime").val();
                        vc.component.addFeeConfigInfo.startTime = value;
                    });
                $('.addFeeConfigEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.addFeeConfigEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".addFeeConfigEndTime").val();
                        vc.component.addFeeConfigInfo.endTime = value;
                    });
            },
            addFeeConfigValidate() {
                return vc.validate.validate({
                    addFeeConfigInfo: vc.component.addFeeConfigInfo
                },
                {
                    'addFeeConfigInfo.feeTypeCd': [{
                        limit: "required",
                        param: "",
                        errInfo: "费用类型不能为空"
                    },
                    {
                        limit: "num",
                        param: "",
                        errInfo: "费用类型格式错误"
                    },
                    ],
                    'addFeeConfigInfo.feeName': [{
                        limit: "required",
                        param: "",
                        errInfo: "收费项目不能为空"
                    },
                    {
                        limit: "maxin",
                        param: "1,100",
                        errInfo: "收费项目不能超过100位"
                    },
                    ],
                    'addFeeConfigInfo.feeFlag': [{
                        limit: "required",
                        param: "",
                        errInfo: "费用标识不能为空"
                    },
                    {
                        limit: "num",
                        param: "",
                        errInfo: "费用类型格式错误"
                    },
                    ],
                    'addFeeConfigInfo.startTime': [{
                        limit: "required",
                        param: "",
                        errInfo: "计费起始时间不能为空"
                    },
                    {
                        limit: "dateTime",
                        param: "",
                        errInfo: "计费起始时间不是有效的时间格式"
                    },
                    ],
                    'addFeeConfigInfo.endTime': [{
                        limit: "required",
                        param: "",
                        errInfo: "计费终止时间不能为空"
                    },
                    {
                        limit: "dateTime",
                        param: "",
                        errInfo: "计费终止时间不是有效的时间格式"
                    },
                    ],
                    'addFeeConfigInfo.computingFormula': [{
                        limit: "required",
                        param: "",
                        errInfo: "计算公式不能为空"
                    },
                    {
                        limit: "num",
                        param: "",
                        errInfo: "计算公式格式错误"
                    },
                    ],
                    'addFeeConfigInfo.squarePrice': [{
                        limit: "required",
                        param: "",
                        errInfo: "计费单价不能为空"
                    },
                    {
                        limit: "money",
                        param: "",
                        errInfo: "计费单价格式错误"
                    },
                    ],
                    'addFeeConfigInfo.additionalAmount': [{
                        limit: "required",
                        param: "",
                        errInfo: "附加费用不能为空"
                    },
                    {
                        limit: "money",
                        param: "",
                        errInfo: "附加费用格式错误"
                    },
                    ],

                });
            },
            saveFeeConfigInfo: function() {
                //固定费用
                if(vc.component.addFeeConfigInfo.computingFormula == '2002'){
                   vc.component.addFeeConfigInfo.squarePrice = "0.00";
                }
                if (!vc.component.addFeeConfigValidate()) {
                    vc.toast(vc.validate.errInfo);

                    return;
                }

                vc.component.addFeeConfigInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addFeeConfigInfo);
                    $('#addFeeConfigModel').modal('hide');
                    return;
                }

                vc.http.post('addFeeConfig', 'save', JSON.stringify(vc.component.addFeeConfigInfo), {
                    emulateJSON: true
                },
                function(json, res) {
                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                    if (res.status == 200) {
                        //关闭model
                        $('#addFeeConfigModel').modal('hide');
                        vc.component.clearAddFeeConfigInfo();
                        vc.emit('feeConfigManage', 'listFeeConfig', {});

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
                vc.component.addFeeConfigInfo = {
                    feeTypeCd: '',
                    feeName: '',
                    feeFlag: '',
                    startTime: '',
                    endTime: '',
                    computingFormula: '',
                    squarePrice: '',
                    additionalAmount: '',

                };
            }
        }
    });

})(window.vc);