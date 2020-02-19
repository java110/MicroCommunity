(function (vc, vm) {

    vc.extends({
        data: {
            editInspectionPlanInfo: {
                inspectionPlanId: '',
                inspectionPlanName: '',
                inspectionRouteId: '',
                inspectionPlanPeriod: '',
                staffId: '',
                staffName: '',
                startTime: '',
                endTime: '',
                signType: '',
                signTypes:[],
                inspectionPlanPeriods: [],
                states: [],
                isDefault:'',
                state: '',
                remark: '',
                companyId:'',
                companyName:'',
                departmentId:'',
                departmentName:'',
                createUserId:'',
                createUserName:'',
                inspectionRouteName:''
            }
        },
        _initMethod: function () {
            vc.getDict('inspection_plan',"sign_type",function(_data){
                vc.component.editInspectionPlanInfo.signTypes = _data;
            });
            vc.getDict('inspection_plan',"inspection_plan_period",function(_data){
                vc.component.editInspectionPlanInfo.inspectionPlanPeriods = _data;
            });
            vc.getDict('inspection_plan',"state",function(_data){
                vc.component.editInspectionPlanInfo.states = _data;
            });

        },
        _initEvent: function () {
            vc.component._initEditInspectionPlanDateInfo();
            vc.on('editInspectionPlan', 'openEditInspectionPlanModal', function (_params) {
                vc.component.refreshEditInspectionPlanInfo();
                vc.copyObject(_params, vc.component.editInspectionPlanInfo);
                vc.component.editInspectionPlanInfo.communityId = vc.getCurrentCommunity().communityId;
                //公司select2
                vc.emit('editInspectionPlan', 'orgSelect2', 'setOrg', {
                    orgId: vc.component.editInspectionPlanInfo.companyId,
                    orgName: vc.component.editInspectionPlanInfo.companyName,
                });

                //部门select2
                vc.emit('editInspectionPlan', 'departmentSelect2', 'setDepartment', {
                    departmentId: vc.component.editInspectionPlanInfo.departmentId,
                    departmentName: vc.component.editInspectionPlanInfo.departmentName,
                });

                //员工select2
                vc.emit('editInspectionPlan', 'staffSelect2', 'setStaff', {
                    staffId: vc.component.editInspectionPlanInfo.staffId,
                    staffName: vc.component.editInspectionPlanInfo.staffName,
                });

                //传输数据到machineSelect2组件
                vc.emit('editInspectionPlan', 'inspectionRouteSelect2', 'setInspectionRoute', {
                    inspectionRouteId: vc.component.editInspectionPlanInfo.inspectionRouteId,
                    routeName: vc.component.editInspectionPlanInfo.inspectionRouteName,
                });




                $('#editInspectionPlanModel').modal('show');



            });
        },
        methods: {
            editInspectionPlanValidate: function () {
                return vc.validate.validate({
                    editInspectionPlanInfo: vc.component.editInspectionPlanInfo
                }, {
                    'editInspectionPlanInfo.inspectionPlanName': [
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
                    'editInspectionPlanInfo.inspectionRouteId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "巡检路线不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "1,30",
                            errInfo: "巡检路线不能超过30位"
                        },
                    ],
                    'editInspectionPlanInfo.inspectionPlanPeriod': [
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
                    'editInspectionPlanInfo.staffId': [
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
                    'editInspectionPlanInfo.startTime': [
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
                    'editInspectionPlanInfo.endTime': [
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
                    'editInspectionPlanInfo.signType': [
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
                    'editInspectionPlanInfo.state': [
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
                    'editInspectionPlanInfo.remark': [
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "备注信息不能超过200位"
                        },
                    ],
                    'editInspectionPlanInfo.inspectionPlanId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "巡检计划名称不能为空"
                        }]

                });
            },
            editInspectionPlan: function () {
                if (!vc.component.editInspectionPlanValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editInspectionPlan',
                    'update',
                    JSON.stringify(vc.component.editInspectionPlanInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editInspectionPlanModel').modal('hide');
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
            refreshEditInspectionPlanInfo: function () {
                var signTypes = vc.component.editInspectionPlanInfo.signTypes;
                var states = vc.component.editInspectionPlanInfo.states;
                var inspectionPlanPeriods = vc.component.editInspectionPlanInfo.inspectionPlanPeriods;
                vc.component.editInspectionPlanInfo = {
                    inspectionPlanId: '',
                    inspectionPlanName: '',
                    inspectionRouteId: '',
                    inspectionPlanPeriod: '',
                    staffId: '',
                    staffName: '',
                    startTime: '',
                    endTime: '',
                    signType: '',
                    state: '',
                    remark: '',
                    companyId:'',
                    companyName:'',
                    departmentId:'',
                    departmentName:'',
                    createUserId:'',
                    createUserName:'',
                    inspectionRouteName:''
                };
                vc.component.editInspectionPlanInfo.signTypes = signTypes;
                vc.component.editInspectionPlanInfo.states = states;
                vc.component.editInspectionPlanInfo.inspectionPlanPeriods = inspectionPlanPeriods;
            },
            _initEditInspectionPlanDateInfo:function(){
                $('.editInspectionPlanStartTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true

                });
                $('.editInspectionPlanStartTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".editInspectionPlanStartTime").val();
                        vc.component.editInspectionPlanInfo.startTime = value;
                    });
                $('.editInspectionPlanEndTime').datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    initTime: true,
                    initialDate: new Date(),
                    autoClose: 1,
                    todayBtn: true
                });
                $('.editInspectionPlanEndTime').datetimepicker()
                    .on('changeDate', function (ev) {
                        var value = $(".editInspectionPlanEndTime").val();
                        vc.component.editInspectionPlanInfo.endTime = value;
                    });
            }
        }
    });

})(window.vc, window.vc.component);
