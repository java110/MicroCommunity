/**
    组织管理 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewOrgInfo:{
                componentName:'组织管理信息',
                buttonName:'选择组织',
                index:0,
                flowComponent:'viewOrgInfo',
                orgName:'',
                orgLevel:'',
                parentOrgId:'',
                description:'',
                branchOrgId:'',
            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadOrgInfoData();


        },
        _initEvent:function(){
            vc.on('viewOrgInfo','chooseOrg',function(_app){
                vc.copyObject(_app, vc.component.viewOrgInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewOrgInfo);
            });

            vc.on('viewOrgInfo', 'onIndex', function(_index){
                vc.component.viewOrgInfo.index = _index;
            });

            vc.on('viewOrgInfo', '_initInfo', function(_info){
                //vc.component.viewOrgInfo.index = _index;
                vc.copyObject(_info,vc.component.viewOrgInfo);
                console.log(_info);
            });

        },
        methods:{

            _openSelectOrgInfoModel(){
                vc.emit('chooseOrg','openChooseOrgModel',{
                    orgLevel:vc.component.viewOrgInfo.orgLevel,
                    parentOrgId:vc.component.viewOrgInfo.parentOrgId,
                });
            },
            _openAddOrgInfoModel(){
                vc.emit('addOrg','openAddOrgModal',{});
            },
            _loadOrgInfoData:function(){

            }
        }
    });

})(window.vc);
