/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            addOwnerInfo:{
                $step:{},
                index:0,
                infos:[]
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },
        _initEvent:function(){
            vc.on("addOwner", "notify", function(_info){
                vc.component.addOwnerInfo.infos[vc.component.addOwnerInfo.index] = _info;
            });

        },
        methods:{
            _initStep:function(){
                vc.component.addOwnerInfo.$step = $("#step");
                vc.component.addOwnerInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择楼栋","选择房屋","填写业主信息"]
                });
                vc.component.addOwnerInfo.index = vc.component.addOwnerInfo.$step.getIndex();
            },
            _prevStep:function(){
                vc.component.addOwnerInfo.$step.prevStep();
                vc.component.addOwnerInfo.index = vc.component.addOwnerInfo.$step.getIndex();

                vc.emit('viewFloorInfo', 'onIndex', vc.component.addOwnerInfo.index);
vc.emit('sellRoomSelectRoom', 'onIndex', vc.component.addOwnerInfo.index);
vc.emit('addOwner', 'onIndex', vc.component.addOwnerInfo.index);

            },
            _nextStep:function(){
                var _currentData = vc.component.addOwnerInfo.infos[vc.component.addOwnerInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.addOwnerInfo.$step.nextStep();
                vc.component.addOwnerInfo.index = vc.component.addOwnerInfo.$step.getIndex();

                 vc.emit('viewFloorInfo', 'onIndex', vc.component.addOwnerInfo.index);
vc.emit('sellRoomSelectRoom', 'onIndex', vc.component.addOwnerInfo.index);
vc.emit('addOwner', 'onIndex', vc.component.addOwnerInfo.index);

            },
            _finishStep:function(){


                var _currentData = vc.component.addOwnerInfo.infos[vc.component.addOwnerInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }

                var param = {
                    data:vc.component.addOwnerInfo.infos
                }

               vc.http.post(
                   'addOwnerBinding',
                   'binding',
                   JSON.stringify(param),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       if(res.status == 200){

                           vc.message('处理成功',true);
                           //关闭model
                           vc.jumpToPage("/flow/ownerFlow?" + vc.objToGetParam(JSON.parse(json)));
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
