/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            configMenuInfo:{
                $step:{},
                index:0,
                infos:[]
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("configMenu", "notify", function(_info){
                vc.component.configMenuInfo.infos[vc.component.configMenuInfo.index] = _info;
            });

        },
        methods:{
            _initStep:function(){
                vc.component.configMenuInfo.$step = $("#step");
                vc.component.configMenuInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择菜单组","菜单信息","权限信息"]
                });
                vc.component.configMenuInfo.index = vc.component.configMenuInfo.$step.getIndex();
            },
            _prevStep:function(){
                vc.component.configMenuInfo.$step.prevStep();
                vc.component.configMenuInfo.index = vc.component.configMenuInfo.$step.getIndex();

                vc.emit('viewMenuGroupInfo', 'onIndex', vc.component.configMenuInfo.index);
                vc.emit('addMenuView', 'onIndex', vc.component.configMenuInfo.index);
                vc.emit('addPrivilegeView', 'onIndex', vc.component.configMenuInfo.index);

            },
            _nextStep:function(){
                var _currentData = vc.component.configMenuInfo.infos[vc.component.configMenuInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.configMenuInfo.$step.nextStep();
                vc.component.configMenuInfo.index = vc.component.configMenuInfo.$step.getIndex();

                 vc.emit('viewMenuGroupInfo', 'onIndex', vc.component.configMenuInfo.index);
                vc.emit('addMenuView', 'onIndex', vc.component.configMenuInfo.index);
                vc.emit('addPrivilegeView', 'onIndex', vc.component.configMenuInfo.index);

            },
            _finishStep:function(){


                var _currentData = vc.component.configMenuInfo.infos[vc.component.configMenuInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }

                var param = {
                    data:vc.component.configMenuInfo.infos
                }

               vc.http.post(
                   'configMenuBinding',
                   'binding',
                   JSON.stringify(param),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       if(res.status == 200){

                           vc.message('处理成功',true);
                           //关闭model
                           vc.jumpToPage("/flow/menuFlow?" + vc.objToGetParam(JSON.parse(json)));
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
