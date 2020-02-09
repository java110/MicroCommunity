(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
   vc.extends({
            data:{
                staffInfo:{
                    moreCondition:false,
                    branchOrgs:[],
                    departmentOrgs:[],
                    conditions:{
                        branchOrgId:'',
                        departmentOrgId:'',
                        orgId:'',
                        orgName:'',
                        orgLevel:'',
                        parentOrgId:'',
                        name:'',
                        tel:'',
                       staffId:''
                    }
                },
                staffData:[],

            },
            watch:{
                "staffInfo.conditions.branchOrgId":{//深度监听，可监听到对象、数组的变化
                    handler(val, oldVal){
                       vc.component._getOrgsByOrgLevelStaff(DEFAULT_PAGE, DEFAULT_ROWS,3,val);

                       vc.component.staffInfo.conditions.branchOrgId = val;
                       vc.component.staffInfo.conditions.parentOrgId = val;


                       vc.component.staffInfo.conditions.departmentOrgId = '';

                       vc.component.loadData(DEFAULT_PAGE, DEFAULT_ROWS);

                    },
                    deep:true
                },
                "staffInfo.conditions.departmentOrgId":{//深度监听，可监听到对象、数组的变化
                    handler(val, oldVal){
                       vc.component.staffInfo.conditions.orgId = val;
                       vc.component.loadData(DEFAULT_PAGE, DEFAULT_ROWS);
                    },
                    deep:true
                }
             },
            _initMethod:function(){
                vc.component.loadData(1,10);
                vc.component._getOrgsByOrgLevelStaff(DEFAULT_PAGE, DEFAULT_ROWS,2,'');

            },
            _initEvent:function(){
                 vc.component.$on('pagination_page_event',function(_currentPage){
                        vc.component.currentPage(_currentPage);
                    });
                 vc.component.$on('addStaff_reload_event',function(){
                     vc.component.loadData(1,10);
                 });
                 vc.component.$on('editStaff_reload_event',function(){
                      vc.component.loadData(1,10);
                  });
                  vc.component.$on('deleteStaff_reload_event',function(){
                     vc.component.loadData(1,10);
                  });


            },
            methods:{
                loadData:function(_page,_rows){
                       vc.component.staffInfo.conditions.page = _page;
                       vc.component.staffInfo.conditions.rows = _rows;
                       vc.component.staffInfo.conditions.row = _rows;
                       var param = {
                           params:vc.component.staffInfo.conditions
                      };

                    //发送get请求
                   vc.http.get('staff',
                                'loadData',
                                 param,
                                 function(json){
                                    var _staffInfo = JSON.parse(json);
                                    vc.component.staffData = _staffInfo.staffs;
                                    vc.component.$emit('pagination_info_event',{
                                        total:_staffInfo.records,
                                        currentPage:_staffInfo.page
                                    });

                                 },function(){
                                    console.log('请求失败处理');
                                 }
                               );

                },
                currentPage:function(_currentPage){
                    vc.component.loadData(_currentPage,10);
                },
                openEditStaff:function(_staffInfo){
                     vc.component.$emit('edit_staff_event',_staffInfo);
                },
                openDeleteStaff:function(_staffInfo){
                     vc.component.$emit('delete_staff_event',_staffInfo);
                },
                _moreCondition:function(){
                    if(vc.component.staffInfo.moreCondition){
                        vc.component.staffInfo.moreCondition = false;
                    }else{
                        vc.component.staffInfo.moreCondition = true;
                    }
                },
                 _getOrgsByOrgLevelStaff:function(_page, _rows,_orgLevel,_parentOrgId){

                    var param = {
                        params:{
                            page: _page,
                            row: _rows,
                            orgLevel:_orgLevel,
                            parentOrgId: _parentOrgId
                        }
                     };

                   //发送get请求
                   vc.http.get('staff',
                                'list',
                                 param,
                                 function(json,res){
                                    var _orgInfo=JSON.parse(json);
                                    if(_orgLevel == 2){
                                         vc.component.staffInfo.branchOrgs = _orgInfo.orgs;
                                    }else{
                                         vc.component.staffInfo.departmentOrgs = _orgInfo.orgs;
                                    }
                                 },function(errInfo,error){
                                    console.log('请求失败处理');
                                 }
                               );
                },
                _openAddStaffStepPage:function(){
                    vc.jumpToPage("/flow/addStaffStepFlow")
                },
                _queryStaffMethod:function(){
                    vc.component.loadData(DEFAULT_PAGE,DEFAULT_ROWS)
                },
                _resetStaffPwd:function(_staff){
                    vc.emit('resetStaffPwd','openResetStaffPwd',_staff);
                }

            },



        });

})(window.vc);