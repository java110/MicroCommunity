(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addInspectionPlanInfo: {
                inspectionPlanId: '',
                inspectionPlanName: '',
                inspectionRouteId: '',
                inspectionPlanPeriod:'',
                inspectionPlanPeriods:[],
                staffId: '',
                staffName:'',
                startTime: '',
                endTime: '',
                signType:'',
                signTypes:[],
                state:'',
                states:[],
                remark: '',

            }
        },
        _initMethod: function () {
            vc.component._initAddInspectionPlanDateInfo();
            vc.getDict('inspection_plan',"inspection_plan_period",function(_data){
                console.log("收到字典参数:"+_data);
                vc.component.addInspectionPlanInfo.inspectionPlanPeriods = _data;
            });
            vc.getDict('inspection_plan',"state",function(_data){
                vc.component.addInspectionPlanInfo.states = _data;
            });
            vc.getDict('inspection_plan',"sign_type",function(_data){
                vc.component.addInspectionPlanInfo.signTypes = _data;
            });

        },
        _initEvent: function () {
            vc.on('addInspectionPlan', 'openAddInspectionPlanModal', function () {
                $('#addInspectionPlanModel').modal('show');
            });

            vc.on("addInspectionPlanInfo", "notify", function (_param) {
                if (_param.hasOwnProperty("staffId")) {
                    vc.component.addInspectionPlanInfo.staffId = _param.staffId;
                    vc.component.addInspectionPlanInfo.staffName = _param.staffName;
                }
                if (_param.hasOwnProperty("inspectionRouteId")) {
                    vc.component.addInspectionPlanInfo.inspectionRouteId = _param.inspectionRouteId;
                }
            });

        },
        methods: {
            addInspectionPlanValidate() {
                return vc.validate.validate({
                    addInspectionPlanInfo: vc.component.addInspectionPlanInfo
                }, {
                    'addInspectionPlanInfo.inspectionPlanName': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "计划名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,100",
                            errInfo: "巡检计划名称不能超过100位"
                        },
                    ],
                    'addInspectionPlanInfo.inspectionPlanPeriod': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "执行周期不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,12",
                            errInfo: "执行周期格式错误"
                        },
                    ],
                    'addInspectionPlanInfo.staffId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "执行人员不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,30",
                            errInfo: "执行人员不能超过30位"
                        },
                    ],
                    'addInspectionPlanInfo.startTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "开始时间不能为空"
                        },
                        {
                            limit: "dateTime",
                            param: "",
                            errInfo: "计计划开始时间不是有效的时间格式"
                        },
                    ],
                    'addInspectionPlanInfo.endTime': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "结束时间不能为空"
                        },
                        {
                            limit: "dateTime",
                            param: "",
                            errInfo: "计划结束时间不是有效的时间格式"
                        },
                    ],
                    'addInspectionPlanInfo.signType': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "签到方式不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "签到方式格式错误"
                        },
                    ],
                    'addInspectionPlanInfo.state': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "状态不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "签到方式格式错误"
                        },
                    ],
                    'addInspectionPlanInfo.remark': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "备注信息不能超过200位"
                        },
                    ],


                });
            },
            _initAddInspectionPlanDateInfo: function () {
                vc.component.addInspectionPlanInfo.startTime = vc.dateFormat(new Date().getTime());
                $('.addInspectionPlanStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.addInspectionPlanStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".addFeeConfigStartTime").val();
                        vc.component.addInspectionPlanInfo.startTime = value;
                    });
                $('.addInspectionPlanEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.addInspectionPlanEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".addInspectionPlanEndTime").val();
                        vc.component.addInspectionPlanInfo.endTime = value;
                    });
            },
            saveInspectionPlanInfo: function () {
                if (!vc.component.addInspectionPlanValidate()) {
                    vc.toast(vc.validate.errInfo);

                    return;
                }

                vc.component.addInspectionPlanInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addInspectionPlanInfo);
                    $('#addInspectionPlanModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addInspectionPlan',
                    'save',
                    JSON.stringify(vc.component.addInspectionPlanInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addInspectionPlanModel').modal('hide');
                            vc.component.clearAddInspectionPlanInfo();
                            vc.emit('inspectionPlanManage', 'listInspectionPlan', {});

                            return;
                        }
                        vc.message(json);

                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);

                    });
            },
            clearAddInspectionPlanInfo: function () {
                var states =  vc.component.addInspectionPlanInfo.states;
                var inspectionPlanPeriods = vc.component.addInspectionPlanInfo.inspectionPlanPeriods;
                var signTypes = vc.component.addInspectionPlanInfo.signTypes;
                vc.component.addInspectionPlanInfo = {
                    inspectionPlanName: '',
                    inspectionRouteId: '',
                    inspectionPlanPeriod:'',
                    staffId: '',
                    startTime: '',
                    endTime: '',
                    signType:'',
                    state:'',
                    remark: '',
                    states:states,
                    signTypes:signTypes,
                    inspectionPlanPeriods:inspectionPlanPeriods
                };
            },
            cleanInspectionPlanAddModel:function(){
                vc.component.clearAddInspectionPlanInfo();
                //员工select2
                vc.emit('addInspectionPlan', 'inspectionRouteSelect2', 'clearInspectionRoute', {});
            }
        }
    });

})(window.vc);
