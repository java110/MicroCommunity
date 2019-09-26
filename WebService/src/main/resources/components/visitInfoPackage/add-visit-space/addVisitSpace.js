/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            newVisitInfo:{
                $step:{},
                index:0,
                infos:[],
            }
        },
        _initMethod:function(){
            vc.component._initStep();
        },

        _initEvent:function(){

           vc.on("addVisitSpace", "notify", function(_info){
               console.log("侦听到index为 "+_info);
                vc.component.newVisitInfo.infos[vc.component.newVisitInfo.index] = _info;
            });
            vc.on("addVisitSpace", "ownerId", function(_ownerId){
                vc.component.newVisitInfo.infos[0]["ownerId"] = _ownerId;
            });
            vc.on("addVisitSpace", "visitCase", function(_visitCase){
                vc.component.newVisitInfo.infos[0]["visitCase"] = _visitCase;
            });

        },
        methods:{
            _initStep:function(){
                vc.component.newVisitInfo.$step = $("#step");
                vc.component.newVisitInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["新增访客","选择目标业主","填写拜访事由"]
                });
                vc.component.newVisitInfo.index = vc.component.newVisitInfo.$step.getIndex();
            },
            _prevStep:function(){
                vc.component.newVisitInfo.$step.prevStep();
                vc.component.newVisitInfo.index = vc.component.newVisitInfo.$step.getIndex();

                vc.emit('addVisit', 'onIndex', vc.component.newVisitInfo.index);
                vc.emit('visitForOwner', 'onIndex', vc.component.newVisitInfo.index);
                vc.emit('addVisitCase', 'onIndex', vc.component.newVisitInfo.index);

                // if(vc.component.newVisitInfo.index == 1){
                //     vc.emit('viewOwnerInfo','callBackOwnerInfo',{});
                // }

            },
            _nextStep:function(){
                var _currentData = vc.component.newVisitInfo.infos[vc.component.viewVisitInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.newVisitInfo.$step.nextStep();
                vc.component.newVisitInfo.index = vc.component.newVisitInfo.$step.getIndex();
                vc.emit('addVisit', 'onIndex', vc.component.newVisitInfo.index);
                vc.emit('visitForOwner', 'onIndex', vc.component.newVisitInfo.index);
                vc.emit('addVisitCase', 'onIndex', vc.component.newVisitInfo.index);

                // if(vc.component.newVisitInfo.index == 1){
                //     vc.emit('viewOwnerInfo','callBackOwnerInfo',{});
                // }

            },
            _addVisitFinish:function(){


                var _currentData = vc.component.newVisitInfo.infos[vc.component.newVisitInfo.index];
                if( _currentData == null || _currentData == undefined){
                    vc.message("请选择或填写必选信息");
                    return ;
                }
                vc.component.newVisitInfo.infos[0]['communityId']=vc.getCurrentCommunity().communityId;
                var param = {
                    name:vc.component.newVisitInfo.infos[0]['name'],
                    visitGender:vc.component.newVisitInfo.infos[0]['visitGender'],
                    phoneNumber:vc.component.newVisitInfo.infos[0]['phoneNumber'],
                    communityId:vc.component.newVisitInfo.infos[0]['communityId'],
                    ownerId:vc.component.newVisitInfo.infos[0]['ownerId'],
                    visitCase:vc.component.newVisitInfo.infos[0]['visitCase'],
                    visitTime:new Date()
                }
                console.log(param);
               vc.http.post(
                   'addVisit',
                   'save',
                   JSON.stringify(param),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                       if(res.status == 200){
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