$(function(){
	//角色列表
	
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
	
	easyExt.initDataGrid('#dg','/sys_role/findAllByPage');
	easyExt.initTreeGrid('#tg','/sys_privilege/findAll'); 
	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.del(selRows,'/sys_role/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	//添加实现
	$("#add").click(function(){
		easyExt.add('/sys_role/addOne',function(){
			$('#dg').datagrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.edit(selRows,'/sys_role/updateOne',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
});
var role=role||{};
//查看权限
function lookP(id){
	role.id=id;
	$('#xray').dialog('open');
	$('#dg').datagrid('clearSelections');
	easyExt.ajax({url:easyExt.url+"/sys_role/getPrivilege?id="+id,type:'GET'},function(data,status, xhr){
		$('#tg').treegrid("unselectAll");
		for(var i=0;j=data.length,i<j;i++){
			$('#tg').treegrid("select",data[i].columns.id);
		}
	});
}
function formatter(value, row, index){
	return '<a href="javascript:lookP('+row.id+')"><div class="icon-hamburg-lock" style="width:16px;height:16px" title="查看权限"></div></a>';
}
//保存权限
function savePermission(){
	var p=$('#tg').treegrid("getSelections");
	var pIds="";
	for(var i=0;j=p.length,i<j;i++){
		pIds+=p[i].id+",";
	}
	easyExt.ajax({url:easyExt.url+"/sys_role/saveRolePrivilege?roleId="+role.id+"&privilegeIds="+pIds},function(data, status, xhr){
		if(data){
			$.messager.alert("提示", "保存权限成功！", "info");
		}
	});
}