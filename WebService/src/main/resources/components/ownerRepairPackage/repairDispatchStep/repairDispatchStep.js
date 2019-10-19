/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            repairDispatchStepInfo:{
                $step:{},
                index:0,
                repairId:'',
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
                repairDispatchInfo:{
                    orgId:'',
                    userId:'',
                    name:'',
                    email:'',
                    tel:'',
                    sex:''
                }
            }
        },
        _initMethod:function(){
            vc.component.repairDispatchStepInfo.repairId = vc.getParam('repairId');

            if(!vc.notNull(vc.component.repairDispatchStepInfo.repairId)){
                vc.message("非法数据，未找到派单信息");
                vc.jumpToPage("/flow/ownerFlow");
                return ;
            }
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("repairDispatchStep", "notify", function(_info){
                if(vc.component.repairDispatchStepInfo.index == 0){
                    vc.copyObject(_info,vc.component.repairDispatchStepInfo.branchOrgInfo);
                    vc.component.repairDispatchStepInfo.infos[0] = vc.component.repairDispatchStepInfo.branchOrgInfo;
                }else if(vc.component.repairDispatchStepInfo.index == 1){
                    vc.copyObject(_info,vc.component.repairDispatchStepInfo.departmemtOrgInfo);
                    vc.component.repairDispatchStepInfo.staffInfo.orgId = _info.orgId
                    vc.component.repairDispatchStepInfo.infos[1] = vc.component.repairDispatchStepInfo.departmemtOrgInfo;

                }else{
                    vc.copyObject(_info, vc.component.repairDispatchStepInfo.repairDispatchInfo);
                    vc.component.repairDispatchStepInfo.infos[2] = vc.component.repairDispatchStepInfo.repairDispatchInfo;
                }

            });

        },
        methods:{
            _initStep:function(){
                vc.component.repairDispatchStepInfo.$step = $("#step");
                vc.component.repairDispatchStepInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择分公司","选择部门","选择员工"]
                });
                vc.component.repairDispatchStepInfo.index = vc.component.repairDispatchStepInfo.$step.getIndex();
                 vc.component._notifyViewOrgInfoComponentData();
            },
            _prevStep:function(){
                vc.component.repairDispatchStepInfo.$step.prevStep();
                vc.component.repairDispatchStepInfo.index = vc.component.repairDispatchStepInfo.$step.getIndex();

                vc.emit('viewOrgInfo', 'onIndex', vc.component.repairDispatchStepInfo.index);
                vc.emit('viewStaffInfo', 'onIndex', vc.component.repairDispatchStepInfo.index);
                vc.component._notifyViewOrgInfoComponentData();
            },
            _nextStep:function(){
                var _currentData = vc.component.repairDispatchStepInfo.infos[vc.component.repairDispatchStepInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.repairDispatchStepInfo.$step.nextStep();
                vc.component.repairDispatchStepInfo.index = vc.component.repairDispatchStepInfo.$step.getIndex();

                 vc.emit('viewOrgInfo', 'onIndex', vc.component.repairDispatchStepInfo.index);
                vc.emit('viewStaffInfo', 'onIndex', vc.component.repairDispatchStepInfo.index);
                vc.component._notifyViewOrgInfoComponentData();

            },
            _finishStep:function(){


                var _currentData = vc.component.repairDispatchStepInfo.infos[vc.component.repairDispatchStepInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }

                vc.component.repairDispatchStepInfo.repairDispatchInfo.repairId = vc.component.repairDispatchStepInfo.repairId;

               vc.http.post(
                   'repairDispatchStepBinding',
                   'binding',
                   JSON.stringify(vc.component.repairDispatchStepInfo.repairDispatchInfo),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       if(res.status == 200){

                           vc.message('处理成功',true);
                           //关闭model
                           vc.jumpToPage("/flow/repairDispatch?" + vc.objToGetParam(JSON.parse(json)));
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

                if(vc.component.repairDispatchStepInfo.index == 0){
                    vc.emit('viewOrgInfo', '_initInfo',vc.component.repairDispatchStepInfo.branchOrgInfo);
                }else if(vc.component.repairDispatchStepInfo.index == 1){
                    vc.component.repairDispatchStepInfo.departmemtOrgInfo.parentOrgId = vc.component.repairDispatchStepInfo.branchOrgInfo.orgId;
                    vc.emit('viewOrgInfo', '_initInfo',vc.component.repairDispatchStepInfo.departmemtOrgInfo);
                }
            }
        }
    });
})(window.vc);
