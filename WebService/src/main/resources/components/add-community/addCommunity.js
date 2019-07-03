(function(vc){

    vc.extends({
        data:{
            addCommunityInfo:{
                name:'',
address:'',
nearbyLandmarks:'',
cityCode:'0971',
mapX:'101.33',
mapY:'101.33',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addCommunity','openAddCommunityModal',function(){
                $('#addCommunityModel').modal('show');
            });
        },
        methods:{
            addCommunityValidate(){
                return vc.validate.validate({
                    addCommunityInfo:vc.component.addCommunityInfo
                },{
                    'addCommunityInfo.name':[
{
                            limit:"required",
                            param:"",
                            errInfo:"小区名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"10,20",
                            errInfo:"小区名称必须在10至20字符之间"
                        },
                    ],
'addCommunityInfo.address':[
{
                            limit:"required",
                            param:"",
                            errInfo:"小区地址不能为空"
                        },
 {
                            limit:"maxin",
                            param:"200",
                            errInfo:"小区地址不能大于200个字符"
                        },
                    ],
'addCommunityInfo.nearbyLandmarks':[
{
                            limit:"required",
                            param:"",
                            errInfo:"附近地标不能为空"
                        },
 {
                            limit:"maxin",
                            param:"50",
                            errInfo:"小区附近地标不能大于50个字符"
                        },
                    ],
'addCommunityInfo.cityCode':[
 {
                            limit:"maxin",
                            param:"12",
                            errInfo:"小区城市编码不能大于4个字符"
                        },
                    ],
'addCommunityInfo.mapX':[
 {
                            limit:"maxin",
                            param:"20",
                            errInfo:"小区城市编码不能大于4个字符"
                        },
                    ],
'addCommunityInfo.mapY':[
 {
                            limit:"maxin",
                            param:"20",
                            errInfo:"小区城市编码不能大于4个字符"
                        },
                    ],




                });
            },
            saveCommunityInfo:function(){
                if(!vc.component.addCommunityValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.addCommunityInfo.communityId = vc.getCurrentCommunity().communityId;

                vc.http.post(
                    'addCommunity',
                    'save',
                    JSON.stringify(vc.component.addCommunityInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addCommunityModel').modal('hide');
                            vc.component.clearAddCommunityInfo();
                            vc.emit('communityManage','listCommunity',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearAddCommunityInfo:function(){
                vc.component.addCommunityInfo = {
                                            name:'',
address:'',
nearbyLandmarks:'',
cityCode:'0971',
mapX:'101.33',
mapY:'101.33',

                                        };
            }
        }
    });

})(window.vc);
