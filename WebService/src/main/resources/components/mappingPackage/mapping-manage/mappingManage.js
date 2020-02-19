/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            mappingManageInfo:{
                mappings:[],
                name:'',
                total:0,
                records:1,
                conditions:{
                    domain:'',
                    name:'',
                    key:''
                }
            }
        },
        _initMethod:function(){
            vc.component._listMappings(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('mappingManage','listMapping',function(_param){
                  vc.component._listMappings(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listMappings(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listMappings:function(_page, _rows){
                vc.component.mappingManageInfo.conditions.page = _page;
                vc.component.mappingManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.mappingManageInfo.conditions
                 };

               //发送get请求
               vc.http.get('mappingManage',
                            'list',
                             param,
                             function(json,res){
                                var _mappingManageInfo=JSON.parse(json);
                                vc.component.mappingManageInfo.total = _mappingManageInfo.total;
                                vc.component.mappingManageInfo.records = _mappingManageInfo.records;
                                vc.component.mappingManageInfo.mappings = _mappingManageInfo.mappings;
                                vc.emit('pagination','init',{
                                     total:vc.component.mappingManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddMappingModal:function(){
                vc.emit('addMapping','openAddMappingModal',{});
            },
            _openEditMappingModel:function(_mapping){
                vc.emit('editMapping','openEditMappingModal',_mapping);
            },
            _openDeleteMappingModel:function(_mapping){
                vc.emit('deleteMapping','openDeleteMappingModal',_mapping);
            },
            _queryMappingMethod:function(){
                vc.component._listMappings(DEFAULT_PAGE, DEFAULT_ROWS);

            }
        }
    });
})(window.vc);
