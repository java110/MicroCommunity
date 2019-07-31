(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addRoomViewInfo:{
                flowComponent:'addRoom',
                roomNum:'',
layer:'',
section:'',
apartment:'',
builtUpArea:'',
unitPrice:'',
state:'2002',
remark:'',

            }
        },
        watch:{
            addRoomViewInfo:{
                deep: true,
                handler:function(){
                    vc.component.saveAddRoomInfo();
                }
             }
        },
         _initMethod:function(){

         },
         _initEvent:function(){

            vc.on('addRoomViewInfo', 'onIndex', function(_index){
                vc.component.addRoomViewInfo.index = _index;
            });
        },
        methods:{
            addRoomValidate(){
                return vc.validate.validate({
                    addRoomViewInfo:vc.component.addRoomViewInfo
                },{
                    'addRoomViewInfo.roomNum':[
{
                            limit:"required",
                            param:"",
                            errInfo:"房屋编号不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"12",
                            errInfo:"房屋编号长度不能超过12位"
                        },
                    ],
'addRoomViewInfo.layer':[
{
                            limit:"required",
                            param:"",
                            errInfo:"房屋楼层不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"房屋楼层高度必须为数字"
                        },
                    ],
'addRoomViewInfo.section':[
{
                            limit:"required",
                            param:"",
                            errInfo:"房间数不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"房间数必须为数字"
                        },
                    ],
'addRoomViewInfo.apartment':[
{
                            limit:"required",
                            param:"",
                            errInfo:"房屋户型不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"50",
                            errInfo:"房屋户型不能大于50"
                        },
                    ],
'addRoomViewInfo.builtUpArea':[
{
                            limit:"required",
                            param:"",
                            errInfo:"建筑面积不能为空"
                        },
 {
                            limit:"money",
                            param:"",
                            errInfo:"建筑面积错误，如 300.00"
                        },
                    ],
'addRoomViewInfo.unitPrice':[
{
                            limit:"required",
                            param:"",
                            errInfo:"房屋单价不能为空"
                        },
 {
                            limit:"money",
                            param:"",
                            errInfo:"房屋单价错误 如 300.00"
                        },
                    ],
'addRoomViewInfo.state':[
{
                            limit:"required",
                            param:"",
                            errInfo:"房屋状态不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"12",
                            errInfo:"房屋状态 不能超过12位"
                        },
                    ],
'addRoomViewInfo.remark':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注内容不能超过200"
                        },
                    ],

                });
            },
            saveAddRoomInfo:function(){
                if(vc.component.addRoomValidate()){
                    //侦听回传
                    vc.emit($props.callBackListener,$props.callBackFunction, vc.component.addRoomViewInfo);
                    return ;
                }
            }
        }
    });

})(window.vc);
