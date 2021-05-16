$(function() {
	//权限列表
	
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
			 /**
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
		         align: 'center',
		        
		         //复选框 
		        
			 }
			 ]],
			 **/
			 onLoadError: function(){
				 $.messager.alert("提示", "数据加载失败！", "info");
			 }
		 }).treegrid('clientPaging');
	};
	
	
	
	easyExt.initTreeGrid('#tg','/sys_privilege/findAll'); 
	easyExt.initDataGrid('#dg','/sys_privilege/findAllByPage');
	
	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').treegrid('getSelections');
		easyExt.del(selRows,'/sys_privilege/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	//添加实现
	$("#add").click(function(){
		easyExt.add('/sys_privilege/addOne',function(){
			$('#dg').datagrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#dg').treegrid('getSelections');
		easyExt.edit(selRows,'/sys_privilege/updateOne');
	});
	//添加基本权限
	$("#addPermission").click(function(){
		var selRows=$('#tg').treegrid('getSelections');//返回选中行
		easyExt.ajax({url:easyExt.url+"/sys_privilege/addPermission?pId="+selRows[0].id,type:'GET'},function(data, status, xhr){
			if(data){
				$.messager.alert("提示", "添加基本权限成功！", "info");
			}
		});
	});
	//上级菜单
	$('#parent').combotree({
		width:180,
		method:'GET',
	    url: easyExt.url+'/sys_privilege/getMenu',
		iconCls: 'iconCls',
	    animate:true
	}); 
});
