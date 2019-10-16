/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            addStaffStepInfo:{
                $step:{},
                index:0,
                infos:[],
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
                staffInfo:{
                    orgId:'',
                    username:'',
                    sex:'',
                    email:'',
                    tel:'',
                    address:'',
                    relCd:'',
                }
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("addStaffStep", "notify", function(_info){
                if(vc.component.addStaffStepInfo.index == 0){
                    vc.copyObject(_info,vc.component.addStaffStepInfo.branchOrgInfo);
                    vc.component.addStaffStepInfo.infos[0] = vc.component.addStaffStepInfo.branchOrgInfo;
                }else if(vc.component.addStaffStepInfo.index == 1){
                    vc.copyObject(_info,vc.component.addStaffStepInfo.departmemtOrgInfo);
                    vc.component.addStaffStepInfo.staffInfo.orgId = _info.orgId
                    vc.component.addStaffStepInfo.infos[1] = vc.component.addStaffStepInfo.departmemtOrgInfo;

                }else{
                    vc.copyObject(_info, vc.component.addStaffStepInfo.staffInfo);
                    vc.component.addStaffStepInfo.infos[2] = vc.component.addStaffStepInfo.staffInfo;
                }

            });

        },
        methods:{
            _initStep:function(){
                vc.component.addStaffStepInfo.$step = $("#step");
                vc.component.addStaffStepInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择分公司","选择部门","员工信息"]
                });
                vc.component.addStaffStepInfo.index = vc.component.addStaffStepInfo.$step.getIndex();
                vc.component._notifyViewOrgInfoComponentData();
            },
            _prevStep:function(){
                vc.component.addStaffStepInfo.$step.prevStep();
                vc.component.addStaffStepInfo.index = vc.component.addStaffStepInfo.$step.getIndex();

                vc.emit('viewOrgInfo', 'onIndex', vc.component.addStaffStepInfo.index);
                vc.emit('addStaffView', 'onIndex', vc.component.addStaffStepInfo.index);

                vc.component._notifyViewOrgInfoComponentData();

            },
            _nextStep:function(){
                var _currentData = vc.component.addStaffStepInfo.infos[vc.component.addStaffStepInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.addStaffStepInfo.$step.nextStep();
                vc.component.addStaffStepInfo.index = vc.component.addStaffStepInfo.$step.getIndex();

                 vc.emit('viewOrgInfo', 'onIndex', vc.component.addStaffStepInfo.index);
                vc.emit('addStaffView', 'onIndex', vc.component.addStaffStepInfo.index);
                vc.component._notifyViewOrgInfoComponentData();


            },
            _finishStep:function(){

                //vc.component.addStaffStepInfo.staffInfo.departmentOrgId = vc.component.addStaffStepInfo.infos[1].orgId;
               vc.http.post(
                   'addStaffStepBinding',
                   'binding',
                   JSON.stringify(vc.component.addStaffStepInfo.staffInfo),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       if(res.status == 200){

                           vc.message('处理成功',true);
                           //关闭model
                           vc.jumpToPage("/flow/staff");
                           return ;
                       }
                       vc.message(json);
                    },
                    function(errInfo,error){
                       console.log('请求失败处理');

                       vc.message(errInfo);
                    });
            },
            _notifyViewOrgInfoComponentData:function(){

                if(vc.component.addStaffStepInfo.index == 0){
                    vc.emit('viewOrgInfo', '_initInfo',vc.component.addStaffStepInfo.branchOrgInfo);
                }else if(vc.component.addStaffStepInfo.index == 1){
                    vc.component.addStaffStepInfo.departmemtOrgInfo.parentOrgId = vc.component.addStaffStepInfo.branchOrgInfo.orgId;
                    vc.emit('viewOrgInfo', '_initInfo',vc.component.addStaffStepInfo.departmemtOrgInfo);
                }
            }
        }
    });
})(window.vc);
