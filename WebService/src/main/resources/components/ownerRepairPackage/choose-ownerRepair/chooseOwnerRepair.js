(function(vc){
    vc.extends({
        propTypes: {
           emitChooseOwnerRepair:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseOwnerRepairInfo:{
                ownerRepairs:[],
                _currentOwnerRepairName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseOwnerRepair','openChooseOwnerRepairModel',function(_param){
                $('#chooseOwnerRepairModel').modal('show');
                vc.component._refreshChooseOwnerRepairInfo();
                vc.component._loadAllOwnerRepairInfo(1,10,'');
            });
        },
        methods:{
            _loadAllOwnerRepairInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseOwnerRepair',
                            'list',
                             param,
                             function(json){
                                var _ownerRepairInfo = JSON.parse(json);
                                vc.component.chooseOwnerRepairInfo.ownerRepairs = _ownerRepairInfo.ownerRepairs;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseOwnerRepair:function(_ownerRepair){
                if(_ownerRepair.hasOwnProperty('name')){
                     _ownerRepair.ownerRepairName = _ownerRepair.name;
                }
                vc.emit($props.emitChooseOwnerRepair,'chooseOwnerRepair',_ownerRepair);
                vc.emit($props.emitLoadData,'listOwnerRepairData',{
                    ownerRepairId:_ownerRepair.ownerRepairId
                });
                $('#chooseOwnerRepairModel').modal('hide');
            },
            queryOwnerRepairs:function(){
                vc.component._loadAllOwnerRepairInfo(1,10,vc.component.chooseOwnerRepairInfo._currentOwnerRepairName);
            },
            _refreshChooseOwnerRepairInfo:function(){
                vc.component.chooseOwnerRepairInfo._currentOwnerRepairName = "";
            }
        }

    });
})(window.vc);
