/**
    活动 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewActivitiesInfo:{
                index:0,
                flowComponent:'viewActivitiesInfo',
                title:'',
typeCd:'',
headerImg:'',
context:'',
startTime:'',
endTime:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadActivitiesInfoData();
        },
        _initEvent:function(){
            vc.on('viewActivitiesInfo','chooseActivities',function(_app){
                vc.copyObject(_app, vc.component.viewActivitiesInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewActivitiesInfo);
            });

            vc.on('viewActivitiesInfo', 'onIndex', function(_index){
                vc.component.viewActivitiesInfo.index = _index;
            });

        },
        methods:{

            _openSelectActivitiesInfoModel(){
                vc.emit('chooseActivities','openChooseActivitiesModel',{});
            },
            _openAddActivitiesInfoModel(){
                vc.emit('addActivities','openAddActivitiesModal',{});
            },
            _loadActivitiesInfoData:function(){

            }
        }
    });

})(window.vc);
