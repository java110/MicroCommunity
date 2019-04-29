/**
    分页组件
**/
(function(vc){
    vc.extends({
        data:{
            paginationInfo:{
                total:0,
                currentPage:1
            }
        },
        _initEvent:function(){
             vc.component.$on('pagination_info_event',function(_paginationInfo){
                    vc.component.paginationInfo.total = _paginationInfo.total;
                    vc.component.paginationInfo.currentPage = _paginationInfo.currentPage;
                });

             vc.on('pagination','init',function(_paginationInfo){
                 vc.component.paginationInfo.total = _paginationInfo.total;
                 vc.component.paginationInfo.currentPage = _paginationInfo.currentPage;
             });
        },
        methods:{
            previous:function(){
                // 当前页为 1时 不触发消息
                if(vc.component.paginationInfo.currentPage <=1){
                    return;
                }
                vc.component.paginationInfo.currentPage = vc.component.paginationInfo.currentPage -1;
                vc.component.$emit('pagination_page_event',vc.component.paginationInfo.currentPage);
            },
            next:function(){
                if(vc.component.paginationInfo.currentPage >=vc.component.paginationInfo.total){
                    return ;
                }
                vc.component.paginationInfo.currentPage = vc.component.paginationInfo.currentPage +1;
                vc.component.$emit('pagination_page_event',vc.component.paginationInfo.currentPage);

            },
            current:function(_page){
                if(_page > vc.component.paginationInfo.total){
                    return ;
                }
                vc.component.paginationInfo.currentPage = _page;

                vc.component.$emit('pagination_page_event',vc.component.paginationInfo.currentPage);

            }
        }
    });
})(window.vc);