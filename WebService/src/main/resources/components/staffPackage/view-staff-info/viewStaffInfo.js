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
            viewStaffInfo:{
                index:0,
                flowComponent:'viewStaffInfo',
                userId:'',
                name:'',
                email:'',
                tel:'',
                sex:'',
                orgId:''
            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadStaffInfoData();
        },
        _initEvent:function(){
            vc.on('viewStaffInfo','chooseStaff',function(_app){
                vc.copyObject(_app, vc.component.viewStaffInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewStaffInfo);
            });

            vc.on('viewStaffInfo', 'onIndex', function(_index){
                vc.component.viewStaffInfo.index = _index;
            });

            vc.on('viewStaffInfo', '_initInfo', function(_info){
                //vc.component.viewStaffInfo.index = _index;
                vc.copyObject(_info,vc.component.viewStaffInfo);
                console.log(_info);
            });

            vc.on('viewStaffInfo', '_clear', function(_info){
                //vc.component.viewStaffInfo.index = _index;
                vc.component.viewStaffInfo = {
                    index:0,
                    flowComponent:'viewStaffInfo',
                    userId:'',
                    name:'',
                    email:'',
                    tel:'',
                    sex:'',
                    orgId:''
                };
            });

        },
        methods:{

            _openSelectStaffInfoModel(){
                vc.emit('searchStaff','openSearchStaffModel',{
                    orgId:vc.component.viewStaffInfo.orgId
                });
            },
            _loadStaffInfoData:function(){

            }
        }
    });

})(window.vc);
