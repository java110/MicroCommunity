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
                 vc.component.$on('editStaff_reload_event',function(){
                      vc.component.loadData();
                  });


            },
            methods:{
                loadData:function(){
                    var param = {
                        params:{
                            page:1,
                            rows:10
                        }
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
                openEditStaff:function(_staffInfo){
                     vc.component.$emit('edit_staff_event',_staffInfo);
                }

            }


        });

})(window.vc);