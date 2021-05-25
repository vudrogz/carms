$(function() {
	//初始化表格
	easyExt.initTreeGrid=function(id,url){
		$(id).treegrid({
			 url: easyExt.url+url,
			 method: 'get',
			 fitColumns : true,
			 idField : 'id',
			 treeField:'name',
			 fit : true,
			 rownumbers: true,
			 pageSize: 10,
			 autoRowHeight: false,
			 showRefresh: true,
			 pagination: true,
			 animate: true,
			 collapsible: true,
			 pagePosition: 'bottom',
			 columns: [[{
				 title: '序号', 
		         field: 'id', 
		         align: 'center', 
		         //复选框 
		         checkbox: true 
			 },
			 {
				 title: '名字', 
		         field: 'name', 
		         align: 'center'
		         //复选框 
		        
			 }
			 ]],
			
			 onLoadError: function(){
				 $.messager.alert("提示", "数据加载失败！", "info");
			 },onLoadSuccess:function(row, data){
				 
			 }
		 }).treegrid('clientPaging');
	};
	easyExt.initDataGrid('#dg','/agents_customer/findAllByPage');
	easyExt.initTreeGrid('#tg','/servicecategory/getServiceCategory'); 
	
	
	//easyExt.initTreeGrid('#tg','/serviceitem/findAll'); 

	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.del(selRows,'/agents_customer/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	
	
	
	//添加实现
	$("#add").click(function(){
		console.log('too young too simple');
		easyExt.add('/agents_customer/addOne',function(){
			$('#dg').datagrid('reload'); 
		});
	});
	
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.edit(selRows,'/agents_customer/updateOne',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	//搜索实现
	$("#search").click(function(){
		$('#dg').datagrid('load',$('#tForm').serializeJson());
	});
	$('#dept').combotree({
		method: 'get',
	    url: easyExt.url+'/sys_department/tree'
	});
	
	
	
	
	//用户角色实现
	$("#user_role").click(function(){
		var selRows=$('#dg').datagrid('getSelections');//返回选中行
		if(selRows.length==0){
			$.messager.alert("提示", "请选择要修改的用户角色！", "info");  
			return;
		}else if(selRows.length==1){
			getUserRole(selRows[0]);
		}else{
			$.messager.alert("提示", "只能单项修改,请选择一行！", "info");
		}
	});
	function getUserRole(user){
		var json={};
		var roleId="";
		$('#userRole').dialog({
			title:'用户角色管理',  
			buttons:[{
					text:'确认',
					handler:function(){
						var role=$('#ur_dg').datagrid('getSelections');
						for(var i=0,j=role.length;i<j;i++){
							roleId+=role[i].id+",";
						}
					    easyExt.ajax({url:easyExt.url+"/sys_role/saveUserRole?userId="+user.id+"&roleId="+roleId},function(data,status, xhr){
					    	$.messager.alert("提示", "请求成功！", "info");
					    });
						$('#userRole').dialog('close');
					}
				},{
					text:'取消',
					handler:function(){
						$('#userRole').dialog('close');
				}
		    }]
		});
		
	}
});

var agents=agents ||{};

//查看服务管理
function lookP(id){
	agents.id=id;
	$('#xray').dialog('open').dialog('setTitle','查看服务');
	$('#dg').datagrid('clearSelections');
	easyExt.ajax({url:easyExt.url+"/agents_customer/getServices?id="+id,type:'GET'},function(data,status, xhr){
		$('#tg').treegrid("unselectAll");
		for(var i=0;j=data.length,i<j;i++){
			$('#tg').treegrid("select",data[i].columns.id);
		}
	});
}


function formatter(value, row, index){
	return '<a href="javascript:lookP('+row.id+')"><div class="icon-hamburg-lock" style="width:16px;height:16px" title="查看服务"></div></a>';
}


function formatter1(value, row, index){
	return '<a href="javascript:lookP1('+row.id+')"><div class="icon-hamburg-lock" style="width:16px;height:16px" title="添加子帐号"></div></a>';
}


function lookP1(id){
	agents.id=id;
	$("#parent_id").val(id);
	$("#add2Dialog").dialog('open').dialog('setTitle','添加子帐号');
}



function addSonManager(){
	easyExt.ajax({url:easyExt.url+"/agents_customer/addManage?"+$("#add2Form").serialize()},function(data, status, xhr){
		if(data){
			$.messager.alert("提示", "添加子帐号成功!", "info");
		}
	});
	
	
}

//开通服务
function saveSerice(){
	var p=$('#tg').treegrid("getSelections");
	var pIds="";
	for(var i=0;j=p.length,i<j;i++){
		pIds+=p[i].id+",";
	}
	easyExt.ajax({url:easyExt.url+"/agents_customer/saveAgentsService?agenstId="+agents.id+"&privilegeIds="+pIds},function(data, status, xhr){
		if(data){
			$.messager.alert("提示", "开通服务成功！", "info");
		}
	});
}