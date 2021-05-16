$(function() {
	//初始化表格
	easyExt.initDataGrid('#dg','/servicecategory/findAllByPage');
	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.del(selRows,'/servicecategory/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	
	
	
	//添加实现
	$("#add").click(function(){
		easyExt.add('/servicecategory/addOne',function(){
			$('#dg').datagrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.edit(selRows,'/servicecategory/updateOne',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	//搜索实现
	$("#search").click(function(){
		$('#dg').datagrid('load',$('#tForm').serializeJson());
	});
	


});