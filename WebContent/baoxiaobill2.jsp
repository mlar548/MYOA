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
    <link href="bootstrap-table-master/dist/bootstrap-table.min.css"
	rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    	<script src="bootstrap-table-master/dist/bootstrap-table.min.js"></script>
	<script src="bootstrap-table-master/dist/locale/bootstrap-table-zh-CN.min.js"></script>
</head>
<body>

<!--数据筛选按钮-->
<div class="input-group input-group-sm">
    <label for="jjname">基金名称</label>
    <input id="jjname" type="text">
    <label for="qsname">券商</label>
    <input id="qsname" type="text">
    <label for="jjdm">基金代码</label>
    <input id="jjdm" type="text">
    <label for="shichang">市场</label>
    <select id="shichang" name="proType">
        <option value="">全部</option>
        <option value="0">上海</option>
        <option value="1">深圳</option>
    </select>
    <label for="xwlx">席位类型</label>
    <select id="xwlx" name="proType">
        <option value="">全部</option>
        <option value="0">新租</option>
        <option value="1">共用</option>
    </select>
    <button id="btn_fund_search" onclick="xwscreen();" type="button"
        class="btn btn-primary btn-sm">
        <span class="glyphicon glyphicon-search" aria-hidden="true"></span>查询
    </button>
</div>
<!--分页表单-->
<table id="table_data" class="table table-hover table-bordered" >
</table>

<script type="text/javascript">
function getpages(jjname,qsname,jjdm,shichang,xwlx) {
    $("#table_data").bootstrapTable({
        url: '/getdata',//请求后台的url
        method: 'get',
        dataType: "json",
        striped: true,//设置为 true 会有隔行变色效果
        undefinedText: "空",//当数据为 undefined 时显示的字符
        pagination: true, //分页
        // paginationLoop:true,//设置为 true 启用分页条无限循环的功能。
        showToggle: "true",//是否显示 切换试图（table/card）按钮
        showColumns: "true",//是否显示 内容列下拉框
        pageNumber: 1,//如果设置了分页，首页页码
        onlyInfoPagination:false,
        // showPaginationSwitch:true,//是否显示 数据条数选择框
        pageSize: 10,//如果设置了分页，页面数据条数
        pageList: [10, 20, 40, 80, 160],    //如果设置了分页，设置可供选择的页面数据条数。设置为All 则显示所有记录。
        paginationPreText: '‹',//指定分页条中上一页按钮的图标或文字,这里是<
        paginationNextText: '›',//指定分页条中下一页按钮的图标或文字,这里是>
        // singleSelect: false,//设置True 将禁止多选
        showRefresh : true,//刷新按钮
        search: false, //显示搜索框
        sidePagination: "server", //服务端处理分页
        queryParams: function (params) {//自定义参数，这里的参数是传给后台的
            return {//这里的params是table提供的
                pageNum: (params.offset / params.limit) + 1,//从数据库第几条记录开始
                pageSize: params.limit,//找多少条
                sortName: params.sort,
                sortOrder: params.order,
                FUND_NAME:jjname,
                SECURITIES_MERCHANT_NAME:qsname,
                FUNDCODE:jjdm,
                MARKET:shichang,
                OPTIONTYPE:xwlx
            };
        },
        columns: [
            {
                field: 'fUND_NAME',
                title: '基金名称',
                align: 'center',
                sortable: true,
                sortOrder: "asc"
            },
            {
                field: 'fUNDCODE',
                title: '基金代码',
                align: 'center',
                sortable: true,
                sortOrder: "asc"
            },
            {
                title: '券商',
                field: 'sECURITIES_MERCHANT_NAME',
                align: 'center',
                sortable: true,
                sortOrder: "asc"
            },{
                title: '市场',
                formatter: function (value, rows, index) {
                    var str = '';
                    if (rows.mARKET == 0) {
                        str = '上海';
                    } else if (rows.mARKET == 1) {
                        str = '深圳';
                    }else{
                        str = '未知';
                    }
                    return str;
                },
                align: 'center',
                sortable: true,
                sortOrder: "asc"
            },{
                title: '席位类型',
                formatter: function (value, rows, index) {
                    var str = '';
                    if (rows.tRADE_TYPE == 0) {
                        str = '主席位';
                    } else if (rows.tRADE_TYPE == 1) {
                        str = '普通席位';
                    }else{
                        str = '未知';
                    }
                    return str;
                },
                align: 'center',
                sortable: true,
                sortOrder: "asc"
            },{
                title: '产品状态',
                field: 'pRODUCT_STATE',
                align: 'center',
                sortable: true,
                sortOrder: "asc"
            },{
                title: '席位使用类型',
                formatter: function (value, rows, index) {
                    var str = '';
                    if (rows.oPTIONTYPE == 0) {
                        str = '新租';
                    } else if (rows.oPTIONTYPE == 1) {
                        str = '共用';
                    }else{
                        str = '未知';
                    }
                    return str;
                },
                align: 'center',
                sortable: true,
                sortOrder: "asc"
            },{
                title: '席位状态',
                //field: 'fUND_SEAT_STATUS',
                formatter: function (value, rows, index) {
                    var typ = '';
                    if (rows.fUND_SEAT_STATUS == 1) {
                        typ = '租用中';
                    } else if (rows.fUND_SEAT_STATUS == 2) {
                        typ = '停用';
                    }else if(rows.fUND_SEAT_STATUS == 3){
                        typ = '退租';
                    }else{
                        typ = '未知';
                    }
                    return typ;
                },
                align: 'center',
                sortable: true,
                sortOrder: "asc"
            }
        ]
    });
}
</script>
	
</body>
</html>