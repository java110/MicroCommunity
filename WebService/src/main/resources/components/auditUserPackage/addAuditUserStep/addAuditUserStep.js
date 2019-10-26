/**
 入驻小区
 **/
(function (vc) {
    vc.extends({
        data: {
            addAuditUserStepInfo: {
                $step: {},
                index: 0,
                infos: [],
                branchOrgInfo:{
                    orgId:'',
                    componentName:'分公司信息',
                    buttonName:'选择分公司',
                    orgName:'',
                    orgLevel:'2',
                    parentOrgId:'',
                    description:'',
                },
                departmemtOrgInfo:{
                    orgId:'',
                    componentName:'部门信息',
                    buttonName:'选择部门',
                    orgName:'',
                    orgLevel:'3',
                    parentOrgId:'',
                    description:'',
                },
                viewStaffInfo:{
                    index:0,
                    flowComponent:'viewStaffInfo',
                    userId:'',
                    name:'',
                    email:'',
                    tel:'',
                    sex:'',
                    orgId:''
                },
                auditUserInfo:{
                    userId: '',
                    userName: '',
                    auditLink: '',
                    objCode: '',
                }
            }
        },
        watch: {
            'addAuditUserStepInfo.departmemtOrgInfo': {
                deep: true,
                handler: function () {
                    vc.emit('viewStaffInfo', '_clear',{});
                }
            },
            'addAuditUserStepInfo.viewStaffInfo': {
                deep: true,
                handler: function () {
                    vc.emit('addAuditUserOtherViewInfo', '_clear',vc.component.addAuditUserStepInfo.viewStaffInfo);
                }
            },
        },
        _initMethod: function () {
            vc.component._initStep();
        },
        _initEvent: function () {
            vc.on("addAuditUserStep", "notify", function (_info) {
                //vc.component.addAuditUserStepInfo.infos[vc.component.addAuditUserStepInfo.index] = _info;

                if(vc.component.addAuditUserStepInfo.index == 0){
                    vc.copyObject(_info,vc.component.addAuditUserStepInfo.branchOrgInfo);
                    vc.component.addAuditUserStepInfo.infos[0] = vc.component.addAuditUserStepInfo.branchOrgInfo;
                }else if(vc.component.addAuditUserStepInfo.index == 1){
                    vc.copyObject(_info,vc.component.addAuditUserStepInfo.departmemtOrgInfo);
                    vc.component.addAuditUserStepInfo.viewStaffInfo.orgId = _info.orgId
                    vc.component.addAuditUserStepInfo.infos[1] = vc.component.addAuditUserStepInfo.departmemtOrgInfo;
                }else if(vc.component.addAuditUserStepInfo.index == 2){
                    vc.copyObject(_info,vc.component.addAuditUserStepInfo.viewStaffInfo);
                    vc.component.addAuditUserStepInfo.infos[vc.component.addAuditUserStepInfo.index] = _info;
                }else{
                    //vc.copyObject(_info, vc.component.addAuditUserStepInfo.repairDispatchInfo);
                    vc.copyObject(_info,vc.component.addAuditUserStepInfo.auditUserInfo);
                    vc.component.addAuditUserStepInfo.infos[vc.component.addAuditUserStepInfo.index] = _info;
                }
            });

        },
        methods: {
            _initStep: function () {
                vc.component.addAuditUserStepInfo.$step = $("#step");
                vc.component.addAuditUserStepInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择分公司", "选择部门", "选择员工", "扩展信息"]
                });
                vc.component.addAuditUserStepInfo.index = vc.component.addAuditUserStepInfo.$step.getIndex();
                vc.component._notifyViewOrgInfoComponentData();
            },
            _prevStep: function () {
                vc.component.addAuditUserStepInfo.$step.prevStep();
                vc.component.addAuditUserStepInfo.index = vc.component.addAuditUserStepInfo.$step.getIndex();

                vc.emit('viewOrgInfo', 'onIndex', vc.component.addAuditUserStepInfo.index);
                vc.emit('viewStaffInfo', 'onIndex', vc.component.addAuditUserStepInfo.index);
                vc.emit('addAuditUserOther', 'onIndex', vc.component.addAuditUserStepInfo.index);
                vc.component._notifyViewOrgInfoComponentData();

            },
            _nextStep: function () {
                var _currentData = vc.component.addAuditUserStepInfo.infos[vc.component.addAuditUserStepInfo.index];
                if (_currentData == null || _currentData == undefined) {
                    vc.message("请选择或填写必选信息");
                    return;
                }
                vc.component.addAuditUserStepInfo.$step.nextStep();
                vc.component.addAuditUserStepInfo.index = vc.component.addAuditUserStepInfo.$step.getIndex();

                vc.emit('viewOrgInfo', 'onIndex', vc.component.addAuditUserStepInfo.index);
                vc.emit('viewStaffInfo', 'onIndex', vc.component.addAuditUserStepInfo.index);
                vc.emit('addAuditUserOther', 'onIndex', vc.component.addAuditUserStepInfo.index);
                vc.component._notifyViewOrgInfoComponentData();

            },
            _finishStep: function () {


                var _currentData = vc.component.addAuditUserStepInfo.infos[vc.component.addAuditUserStepInfo.index];
                if (_currentData == null || _currentData == undefined) {
                    vc.message("请选择或填写必选信息");
                    return;
                }

                var param = {
                    data: vc.component.addAuditUserStepInfo.infos
                }

                vc.http.post(
                    'addAuditUserStepBinding',
                    'binding',
                    JSON.stringify(vc.component.addAuditUserStepInfo.auditUserInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        if (res.status == 200) {

                            vc.message('处理成功', true);
                            //关闭model
                            vc.jumpToPage("/flow/auditUserFlow?" + vc.objToGetParam(JSON.parse(json)));
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            _notifyViewOrgInfoComponentData:function(){

                if(vc.component.addAuditUserStepInfo.index == 0){
                    vc.emit('viewOrgInfo', '_initInfo',vc.component.addAuditUserStepInfo.branchOrgInfo);
                }else if(vc.component.addAuditUserStepInfo.index == 1){
                    vc.component.addAuditUserStepInfo.departmemtOrgInfo.parentOrgId = vc.component.addAuditUserStepInfo.branchOrgInfo.orgId;
                    vc.emit('viewOrgInfo', '_initInfo',vc.component.addAuditUserStepInfo.departmemtOrgInfo);
                }else if(vc.component.addAuditUserStepInfo.index == 2){
                    vc.emit('viewStaffInfo', '_initInfo',vc.component.addAuditUserStepInfo.viewStaffInfo);
                }
            }
        }
    });
})(window.vc);
