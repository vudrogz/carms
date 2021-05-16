$(function() {
	//初始化表格
	easyExt.initDataGrid('#dg','/serviceitem/findAllByPage');
	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.del(selRows,'/serviceitem/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	


	

	
	//添加实现
	$("#add").click(function(){
		
		easyExt.add('/serviceitem/addOne',function(){
			$('#dg').datagrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.edit(selRows,'/serviceitem/updateOne',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	//搜索实现
	$("#search").click(function(){
		$('#dg').datagrid('load',$('#tForm').serializeJson());
	});
	
	//上级菜单
	$('#parent').combotree({
		width:180,
		method:'GET',
	    url: easyExt.url+'/serviceitem/getCategory',
		iconCls: 'iconCls',
	    animate:true
	}); 

});




function formatter(value, row, index){
	if(value=='1, 2')
	return '支持按点数,支持按次数';
	if(value=='1')
		return '支持按点数';
	if(value=='2')
		return '支持按次数';
}


function formatter2(value, row, index){
	
	var name;
	  $.ajax({
    	url: easyExt.url+"/servicecategory/findById?id="+value,
    	type: 'GET',
    	async:false,
    	dataType: 'json',
    	contentType:'application/json;charset=utf-8',
    	timeout: 5000,
    	success:function(data){
    		if(data){
    			name =  data.rows[0].name;
    		}
    	}
    	});
	return name;
}

function formatter1(value, row, index){
	if(value=='1')
	return '开启';
	if(value=='0')
		return '关闭';
	return "未知";
}