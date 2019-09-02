/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            devServiceProvideInfo:{
                $step:{},
                index:0,
                infos:[]
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("devServiceProvide", "notify", function(_info){
                vc.component.devServiceProvideInfo.infos[vc.component.devServiceProvideInfo.index] = _info;
            });

        },
        methods:{
            _initStep:function(){
                vc.component.devServiceProvideInfo.$step = $("#step");
                vc.component.devServiceProvideInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择服务","开发服务实现","备注信息"]
                });
                vc.component.devServiceProvideInfo.index = vc.component.devServiceProvideInfo.$step.getIndex();
            },
            _prevStep:function(){
                vc.component.devServiceProvideInfo.$step.prevStep();
                vc.component.devServiceProvideInfo.index = vc.component.devServiceProvideInfo.$step.getIndex();

                vc.emit('viewServiceInfo', 'onIndex', vc.component.devServiceProvideInfo.index);
vc.emit('devServiceProvideView', 'onIndex', vc.component.devServiceProvideInfo.index);
vc.emit('serviceProvideRemarkView', 'onIndex', vc.component.devServiceProvideInfo.index);

            },
            _nextStep:function(){
                var _currentData = vc.component.devServiceProvideInfo.infos[vc.component.devServiceProvideInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.devServiceProvideInfo.$step.nextStep();
                vc.component.devServiceProvideInfo.index = vc.component.devServiceProvideInfo.$step.getIndex();

                 vc.emit('viewServiceInfo', 'onIndex', vc.component.devServiceProvideInfo.index);
vc.emit('devServiceProvideView', 'onIndex', vc.component.devServiceProvideInfo.index);
vc.emit('serviceProvideRemarkView', 'onIndex', vc.component.devServiceProvideInfo.index);

            },
            _finishStep:function(){


                var _currentData = vc.component.devServiceProvideInfo.infos[vc.component.devServiceProvideInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }

                var param = {
                    data:vc.component.devServiceProvideInfo.infos
                }

               vc.http.post(
                   'devServiceProvideBinding',
                   'binding',
                   JSON.stringify(param),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       if(res.status == 200){

                           vc.message('处理成功',true);
                           //关闭model
                           vc.jumpToPage("/flow/devServiceProvideFlow?" + vc.objToGetParam(JSON.parse(json)));
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
