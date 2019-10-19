/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            repairDispatchStepInfo:{
                $step:{},
                index:0,
                infos:[]
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("repairDispatchStep", "notify", function(_info){
                vc.component.repairDispatchStepInfo.infos[vc.component.repairDispatchStepInfo.index] = _info;
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
            },
            _prevStep:function(){
                vc.component.repairDispatchStepInfo.$step.prevStep();
                vc.component.repairDispatchStepInfo.index = vc.component.repairDispatchStepInfo.$step.getIndex();

                vc.emit('viewOrgInfo', 'onIndex', vc.component.repairDispatchStepInfo.index);
                vc.emit('viewOrgInfo', 'onIndex', vc.component.repairDispatchStepInfo.index);
                vc.emit('viewStaffInfo', 'onIndex', vc.component.repairDispatchStepInfo.index);

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
                vc.emit('viewOrgInfo', 'onIndex', vc.component.repairDispatchStepInfo.index);
                vc.emit('viewStaffInfo', 'onIndex', vc.component.repairDispatchStepInfo.index);

            },
            _finishStep:function(){


                var _currentData = vc.component.repairDispatchStepInfo.infos[vc.component.repairDispatchStepInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }

                var param = {
                    data:vc.component.repairDispatchStepInfo.infos
                }

               vc.http.post(
                   'repairDispatchStepBinding',
                   'binding',
                   JSON.stringify(param),
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
            }
        }
    });
})(window.vc);
