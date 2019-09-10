/**
    访客登记 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewVisitInfo:{
                index:0,
                flowComponent:'viewVisitInfo',
                name:'',
visitGender:'',
visitGender:'',
phoneNumber:'',
visitTime:'',
departureTime:'',
visitCase:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadVisitInfoData();
        },
        _initEvent:function(){
            vc.on('viewVisitInfo','chooseVisit',function(_app){
                vc.copyObject(_app, vc.component.viewVisitInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewVisitInfo);
            });

            vc.on('viewVisitInfo', 'onIndex', function(_index){
                vc.component.viewVisitInfo.index = _index;
            });

        },
        methods:{

            _openSelectVisitInfoModel(){
                vc.emit('chooseVisit','openChooseVisitModel',{});
            },
            _openAddVisitInfoModel(){
                vc.emit('addVisit','openAddVisitModal',{});
            },
            _loadVisitInfoData:function(){

            }
        }
    });

})(window.vc);
