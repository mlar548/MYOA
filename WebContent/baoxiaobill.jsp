<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>流程管理</title>

    <!-- Bootstrap -->
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="css/content.css" rel="stylesheet">
 	<link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
    <script src="js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
	<script src="static/layui/layui.js" charset="utf-8"></script>
	<script>

    layui.use('table', function(){
        var table = layui.table;
		var	layer = layui.layer;
        table.render({
            elem: '#mybaoxiaobill'
            ,url:'/myBaoxiaoBillList'
            ,cellMinWidth: 50 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,width : '100%'
            ,height : 'full-200'
            ,cols: [[
                {field:'id', width:65, title: 'ID', sort: true}
                ,{field:'money', width:95, title: '报销金额'}
                ,{field:'title', width:80, title: '标题'}
                ,{field:'remark', width:150, title: '备注'}
                ,{field:'creatdate', width:170, title: '时间'}
                ,{field:'state', width:90, title: '状态'}
                ,{field: 'state', width:358,title: '操作', align:'center', toolbar: '#tool'}
            ]]
            ,page:true
            ,done:function(res, curr, count) {
            $("[data-field = 'state']").children().each(function(){
                if($(this).text() == '1'){
                    $(this).text("审核中")
                }else if ($(this).text() == '2'){
                    $(this).text("审核完成")
                }
            })
        }
        });




     table.on('tool(test2)', function(obj){
    	 //table.on('event(lay-filter)',function(obj){ 
    	//这是格式，event有toolbar头部栏事件，tool行标签事件，edit编辑事件，等等
    	var data = obj.data;
    	
    	switch (obj.event) {
    	//删除
    	case 'del':
    		if(obj.data.state===1){
   			 layer.msg("报销审核还未完成，不能删除");
   		 	}	
			else{
				layer.confirm('是否删除？', function(index) {
    			 $.ajax({
                     url: "/leaveBillAction_delete",
                     type: "POST",
                     data: {id:data.id},
                     success: function (msg) {
                         if (msg.result === 1) {
                             //删除这一行
                             obj.del();//记得删除后要有obj.del()哦，否则数据是不会更新的！
                             //关闭弹框
                             layer.close(index);
                             layer.msg("删除成功", {icon: 6});
                         } else {
                             layer.msg("删除失败", {icon: 5});
                         }
                     }
                 });
    		 })
    		}
   			 
			
			break;
    	 case 'look':
            location.href = "/viewHisComment?id=" + obj.data.id;
            //viewHisComment?id=${bill.id}
            break;//获取id跳转到详情页
    	 case 'lookimg':
    		  if(obj.data.state===2){
    			 layer.msg("报销审核已完成，流程图结束");
    		 }else{
    			 location.href = "/viewCurrentImageByBill?billId=" + obj.data.id;
    		 } 
    		 
             //viewCurrentImageByBill?billId=${bill.id}
             break;//获取id跳转到详情页
		default:
			break;
		}
    	
   
    }) 
    
    });
</script>

</head>
<body>

<!--路径导航-->
<ol class="breadcrumb breadcrumb_nav">
    <li>首页</li>
    <li>报销管理</li>
    <li class="active">我的报销单</li>
</ol>
<div class="page-content">
<!--     <form class="form-inline"> -->
        <div class="panel panel-default">
            <div class="panel-heading">报销单列表</div>
 </div>
 
            <div class="layui-row">
                <table id="mybaoxiaobill" lay-filter="test2"></table>
            </div>
            <div class="layui-hide layui-btn-group" id="tool">
            <a class="layui-btn layui-btn-danger" lay-event="del">删除</a>
            <a class="layui-btn layui-btn-normal" lay-event="look">查看审核记录</a>
			<a class="layui-btn layui-btn-warm" lay-event="lookimg">查看流程图</a> 
			</div>

        

<!--路径导航-->

	
</body>
</html>