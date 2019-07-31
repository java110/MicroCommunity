/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            @@templateCode@@Info:{
                $step:{},
                index:0,
                infos:[]
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("@@templateCode@@", "notify", function(_info){
                vc.component.@@templateCode@@Info.infos[vc.component.@@templateCode@@Info.index] = _info;
            });

        },
        methods:{
            _initStep:function(){
                vc.component.@@templateCode@@Info.$step = $("#step");
                vc.component.@@templateCode@@Info.$step.step({
                    index: 0,
                    time: 500,
                    title: @@stepTitle@@
                });
                vc.component.@@templateCode@@Info.index = vc.component.@@templateCode@@Info.$step.getIndex();
            },
            _prevStep:function(){
                vc.component.@@templateCode@@Info.$step.prevStep();
                vc.component.@@templateCode@@Info.index = vc.component.@@templateCode@@Info.$step.getIndex();

                @@notifyOnIndex@@
            },
            _nextStep:function(){
                var _currentData = vc.component.@@templateCode@@Info.infos[vc.component.@@templateCode@@Info.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.@@templateCode@@Info.$step.nextStep();
                vc.component.@@templateCode@@Info.index = vc.component.@@templateCode@@Info.$step.getIndex();

                 @@notifyOnIndex@@
            },
            _finishStep:function(){


                var _currentData = vc.component.@@templateCode@@Info.infos[vc.component.@@templateCode@@Info.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }

                var param = {
                    data:vc.component.@@templateCode@@Info.infos
                }

               vc.http.post(
                   '@@templateCode@@Binding',
                   'binding',
                   JSON.stringify(param),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       if(res.status == 200){

                           vc.message('处理成功',true);
                           //关闭model
                           vc.jumpToPage("@@jumpUrl@@?" + vc.objToGetParam(JSON.parse(json)));
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
