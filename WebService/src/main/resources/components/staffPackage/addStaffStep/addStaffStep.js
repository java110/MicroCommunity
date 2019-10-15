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
                staffInfo:{
                    orgId:'',
                    username:'',
                    sex:'',
                    email:'',
                    tel:'',
                    address:'',
                }
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("addStaffStep", "notify", function(_info){
                vc.component.addStaffStepInfo.infos[vc.component.addStaffStepInfo.index] = _info;

                if(vc.component.addStaffStepInfo.index == 1){
                   vc.component.addStaffStepInfo.staffInfo.orgId = _info.orgId
                }

                if(vc.component.addStaffStepInfo.index == 2){
                    vc.copyObject(_info, vc.component.addStaffStepInfo.staffInfo);
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
            },
            _prevStep:function(){
                vc.component.addStaffStepInfo.$step.prevStep();
                vc.component.addStaffStepInfo.index = vc.component.addStaffStepInfo.$step.getIndex();

                vc.emit('viewOrgInfo', 'onIndex', vc.component.addStaffStepInfo.index);
                vc.emit('addStaff', 'onIndex', vc.component.addStaffStepInfo.index);

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
                vc.emit('addStaff', 'onIndex', vc.component.addStaffStepInfo.index);

            },
            _finishStep:function(){
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
                           vc.jumpToPage("/flow/staff?" + vc.objToGetParam(JSON.parse(json)));
                           return ;
                       }
                       vc.message(json);
                    },
                    function(errInfo,error){
                       console.log('请求失败处理');

                       vc.message(errInfo);
                    });
            }
        }
    });
})(window.vc);
