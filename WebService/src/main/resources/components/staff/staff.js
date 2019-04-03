(function(vc){
   vc.extends({
            data:{
                staffData:[],

            },
            _initMethod:function(){
                vc.component.loadData();
            },
            _initEvent:function(){
                 vc.component.$on('pagination_page_event',function(_currentPage){
                        vc.component.currentPage(_currentPage);
                    });
                 vc.component.$on('addStaff_reload_event',function(){
                     vc.component.loadData();
                 });
            },
            methods:{
                loadData:function(){
                    var param = {
                        msg:'123',
                    };

                    //发送get请求
                   vc.http.get('staff',
                                'loadData',
                                 param,
                                 function(json){
                                    var _staffInfo = JSON.parse(json);
                                    vc.component.staffData = _staffInfo.datas;
                                    vc.component.$emit('pagination_info_event',{
                                        total:_staffInfo.total,
                                        currentPage:_staffInfo.page
                                    });

                                 },function(){
                                    console.log('请求失败处理');
                                 }
                               );

                },
                currentPage:function(_currentPage){

                },
                addStaff:function(){
                    var param = {
                        msg:123
                    };
                      //发送get请求
                   vc.http.post('nav',
                                'logout',
                                JSON.stringify(param),
                               {
                                   emulateJSON:true
                                },
                                 function(json,res){
                                   if(res.status == 200){
                                       vc.jumpToPage("/flow/login");
                                       return ;
                                   }
                                 },function(){
                                    console.log('请求失败处理');
                                 }
                               );
                },
                enableStaff:function(){
                    //获取用户名
                    var param = {
                                        msg:'123',
                    };

                    //发送get请求
                   vc.http.get('nav',
                                'getUserInfo',
                                 param,
                                 function(json,res){
                                    if(res.status == 200){
                                        var tmpUserInfo = JSON.parse(json);
                                       vc.component.userName = tmpUserInfo.name;
                                   }
                                 },function(){
                                    console.log('请求失败处理');
                                 }
                               );
                },
                disableStaff:function(){

                }
            }


        });

})(window.vc);