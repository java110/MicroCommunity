/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            orgManageInfo:{
                orgs:[],
                total:0,
                records:1,
                moreCondition:false,
                orgName:'',
                conditions:{
                    orgId:'',
orgName:'',
orgLevel:'',
parentOrgId:'',

                }
            }
        },
        _initMethod:function(){
            vc.component._listOrgs(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){
            
            vc.on('orgManage','listOrg',function(_param){
                  vc.component._listOrgs(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listOrgs(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _listOrgs:function(_page, _rows){

                vc.component.orgManageInfo.conditions.page = _page;
                vc.component.orgManageInfo.conditions.row = _rows;
                var param = {
                    params:vc.component.orgManageInfo.conditions
               };

               //发送get请求
               vc.http.get('orgManage',
                            'list',
                             param,
                             function(json,res){
                                var _orgManageInfo=JSON.parse(json);
                                vc.component.orgManageInfo.total = _orgManageInfo.total;
                                vc.component.orgManageInfo.records = _orgManageInfo.records;
                                vc.component.orgManageInfo.orgs = _orgManageInfo.orgs;
                                vc.emit('pagination','init',{
                                     total:vc.component.orgManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddOrgModal:function(){
                vc.emit('addOrg','openAddOrgModal',{});
            },
            _openEditOrgModel:function(_org){
                vc.emit('editOrg','openEditOrgModal',_org);
            },
            _openDeleteOrgModel:function(_org){
                vc.emit('deleteOrg','openDeleteOrgModal',_org);
            },
            _queryOrgMethod:function(){
                vc.component._listOrgs(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.orgManageInfo.moreCondition){
                    vc.component.orgManageInfo.moreCondition = false;
                }else{
                    vc.component.orgManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);
