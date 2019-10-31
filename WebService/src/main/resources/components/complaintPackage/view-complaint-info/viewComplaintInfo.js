/**
    投诉建议 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewComplaintInfo:{
                index:0,
                flowComponent:'viewComplaintInfo',
                storeId:'',
typeCd:'',
roomId:'',
complaintName:'',
tel:'',
state:'',
context:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadComplaintInfoData();
        },
        _initEvent:function(){
            vc.on('viewComplaintInfo','chooseComplaint',function(_app){
                vc.copyObject(_app, vc.component.viewComplaintInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewComplaintInfo);
            });

            vc.on('viewComplaintInfo', 'onIndex', function(_index){
                vc.component.viewComplaintInfo.index = _index;
            });

        },
        methods:{

            _openSelectComplaintInfoModel(){
                vc.emit('chooseComplaint','openChooseComplaintModel',{});
            },
            _openAddComplaintInfoModel(){
                vc.emit('addComplaint','openAddComplaintModal',{});
            },
            _loadComplaintInfoData:function(){

            }
        }
    });

})(window.vc);
