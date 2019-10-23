/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            itemOutInfo:{
                $step:{},
                index:0,
                infos:[]
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("itemOut", "notify", function(_info){
                vc.component.itemOutInfo.infos[vc.component.itemOutInfo.index] = _info;
            });

        },
        methods:{
            _initStep:function(){
                vc.component.itemOutInfo.$step = $("#step");
                vc.component.itemOutInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择物品","选择员工","选择员工"]
                });
                vc.component.itemOutInfo.index = vc.component.itemOutInfo.$step.getIndex();
            },
            _prevStep:function(){
                vc.component.itemOutInfo.$step.prevStep();
                vc.component.itemOutInfo.index = vc.component.itemOutInfo.$step.getIndex();

                vc.emit('viewItemOutInfo', 'onIndex', vc.component.itemOutInfo.index);
vc.emit('viewStaffInfo', 'onIndex', vc.component.itemOutInfo.index);
vc.emit('viewOrgInfo', 'onIndex', vc.component.itemOutInfo.index);

            },
            _nextStep:function(){
                var _currentData = vc.component.itemOutInfo.infos[vc.component.itemOutInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.itemOutInfo.$step.nextStep();
                vc.component.itemOutInfo.index = vc.component.itemOutInfo.$step.getIndex();

                 vc.emit('viewItemOutInfo', 'onIndex', vc.component.itemOutInfo.index);
vc.emit('viewStaffInfo', 'onIndex', vc.component.itemOutInfo.index);
vc.emit('viewOrgInfo', 'onIndex', vc.component.itemOutInfo.index);

            },
            _finishStep:function(){


                var _currentData = vc.component.itemOutInfo.infos[vc.component.itemOutInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }

                var param = {
                    data:vc.component.itemOutInfo.infos
                }

               vc.http.post(
                   'itemOutBinding',
                   'binding',
                   JSON.stringify(param),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       if(res.status == 200){

                           vc.message('处理成功',true);
                           //关闭model
                           vc.jumpToPage("/flow/itemOut?" + vc.objToGetParam(JSON.parse(json)));
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
