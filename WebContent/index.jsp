<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" errorPage="error.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport"    content="width=device-width, initial-scale=1.0">
	
	<title>弹幕网站</title>
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/WEB-INF/assets/css/bootstrap.min.css">

	


	<!-- Custom styles for our template -->
	<link rel="stylesheet" href="<%=request.getContextPath() %>/assets/css/bootstrap-theme.css" media="screen" >
	<link rel="stylesheet" href="<%=request.getContextPath() %>/assets/css/main.css">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/assets/js/highlight/styles/monokai.css">

	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	<script src="assets/js/html5shiv.js"></script>
	<script src="assets/js/respond.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/assets/jquery-2.1.3.min.js"></script>
	<![endif]-->

	<style>
	body {
		  font-family: "Microsoft YaHei" ! important;
		  font-color:#222;
	}
		pre {
			line-height: 2em;
		  font-family: "Microsoft YaHei" ! important;
	}
	h4 {
		line-height: 2em;
	}
	#danmuarea {
		position: relative;
		background: #222;
		width:1920px;
		height: 768px;
		margin-left: 0;
		padding-left:0;
	}

 

	.ctr {
		font-size: 1em;
		  line-height: 2em;
	}
	</style>

</head>
	<!-- Intro -->
	<div>
		<div id="danmuarea">
		 <div id="danmu" >
		 </div>

		 </div>
            <div class="text-center ctr" >
                    <br>
                      <button type="button"  onclick="resumer() ">弹幕开始/继续</button>&nbsp;&nbsp;&nbsp;&nbsp;
                      <button type="button"  onclick="pauser()">弹幕暂停</button>  &nbsp;&nbsp;&nbsp;&nbsp;
                       显示弹幕:<input type='checkbox' checked='checked' id='ishide' value='is' onchange='changehide()'> &nbsp;&nbsp;&nbsp;&nbsp;
                        弹幕透明度:
                              <input type="range" name="op" id="op" onchange="op()" value="100"> <br>
                      	当前弹幕运行时间(分秒)：<span id="time"></span>&nbsp;&nbsp;
                      	设置当前弹幕时间(分秒)： <input type="text" id="set_time" max=20 />    
           <button type="button"  onclick="settime()">设置</button>
          
                	<br>
		
                发弹幕:               
              <select  name="color" id="color" >
              <option value="white">白色</option>
              <option value="red">红色</option>
              <option value="green">绿色</option>
              <option value="blue">蓝色</option>
              <option value="yellow">黄色</option>
              </select>
              <select name="size" id="text_size" >
              <option value="1">大文字</option>
              <option value="0">小文字</option>
              </select>            
              <select name="position" id="position"   >
              <option value="0">滚动</option>
              <option value="1">顶端</option>
              <option value="2">底端</option>
              </select>         
          <input type="textarea" id="text" max=300 />    
           <button type="button"  onclick="send()">发送</button>
           <button type="button" id="testbutton">测试ajax</button>
            </div><br>
</div>	<!-- /container -->
	

		




	<!-- JavaScript libs are placed at the end of the document so the pages load faster -->

	<script src="<%=request.getContextPath() %>/assets/js/jquery-1.11.1.min.js"></script>
	<script src="<%=request.getContextPath() %>/assets/js/bootstrap.js"></script>
	<script src="<%=request.getContextPath() %>/assets/js/headroom.min.js"></script>
	<script src="<%=request.getContextPath() %>/assets/js/jQuery.headroom.min.js"></script>
	<script src="<%=request.getContextPath() %>/assets/js/template.js"></script>
	<script src="<%=request.getContextPath() %>/assets/js/highlight/highlight.pack.js"></script>
	<script src="<%=request.getContextPath() %>/assets/js/jquery.danmu.js"></script>
	<script>hljs.initHighlightingOnLoad();</script>


	<script>
(function(){
$("#danmu").danmu({
	// left:$("#danmuarea").offset().left,
	// top:$("#danmuarea").offset().top,
	// height: 445,
 //    width: 800,
	left:0,
	top:0,
	height:"100%",
	width:"100%",
    speed:30000,
    opacity:1,
    font_size_small:16,
    font_size_big:24,
      top_botton_danmu_time:6000
}
  );
})(jQuery);


query();
timedCount();


var first=true;

$(document).ready(function(){
	$("#testbutton").click(function(){
		getDanmuFromDataBase();
	});
})

/* $("#testbutton").click("addData()"); */

function timedCount()
{
   $("#time").text($('#danmu').data("nowtime"));
 
t=setTimeout("timedCount()",10)

}



function starter(){

  $('#danmu').danmu('danmu_start');

}
function pauser(){
	 $.ajax({
		  url:"/WeixinDemo/pauseDanmu",
		  dataType:'json',
		  timeout:10000,
		  success:function(data){
			if(data.resultState=="1") alert("弹幕暂停成功");  
		  },
		  error:function(){
			  alert("连接失败，请重试");
		  }
	  });
  $('#danmu').danmu('danmu_pause');
}
function resumer(){
  $.ajax({
	  url:"/WeixinDemo/startDanmu",
	  dataType:'json',
	  timeout:10000,
	  success:function(data){
		if(data.resultState=="1") alert("弹幕已经启动");  
	  },
	  error:function(){
		  alert("连接失败，请重试");
	  }
  });
  $('#danmu').danmu('danmu_resume');
}
function stoper(){
  $('#danmu').danmu('danmu_stop');
}

function getime(){
  alert($('#danmu').data("nowtime"));
}

function getpaused(){
  alert($('#danmu').data("paused"));
}

function add(){
var newd=
{ "text":"new2" , "color":"green" ,"size":"1","position":"0","time":60};

  $('#danmu').danmu("add_danmu",newd);
}
function insert(){
  var newd= { "text":"new2" , "color":"green" ,"size":"1","position":"0","time":50};
  str_newd=JSON.stringify(newd);
  $.post("stone.php",{danmu:str_newd},function(data,status){alert(data)});
}
function query() {
  $.get("query.php",function(data,status){
     var danmu_from_sql=eval(data);   
     for (var i=0;i<danmu_from_sql.length;i++){
      var danmu_ls=eval('('+danmu_from_sql[i]+')');
      $('#danmu').danmu("add_danmu",danmu_ls);
     }
  });
}

function send(){
  var text = document.getElementById('text').value;
  var color = document.getElementById('color').value;
  var position = document.getElementById('position').value;
  var time = $('#danmu').data("nowtime")+1;
  var size =document.getElementById('text_size').value;
  var text_obj='{ "text":"'+text+'","color":"'+color+'","size":"'+size+'","position":"'+position+'","time":'+time+'}';
  $.post("stone.php",{danmu:text_obj});
  var text_obj='{ "text":"'+text+'","color":"'+color+'","size":"'+size+'","position":"'+position+'","time":'+time+',"isnew":""}';
  var new_obj=eval('('+text_obj+')');
  $('#danmu').danmu("add_danmu",new_obj);
  document.getElementById('text').value='';
}

function op(){
var op=document.getElementById('op').value;
 $('#danmu').data("opacity",op);
}


function changehide() {
	var op = document.getElementById('op').value;
	op = op / 100;
	if (document.getElementById("ishide").checked) {
		jQuery('#danmu').data("opacity", op);
		jQuery(".flying").css({
			"opacity": op
		});
	} else {
		jQuery('#danmu').data("opacity", 0);
		jQuery(".flying").css({
			"opacity": 0
		});
	}
}


function settime(){
	var t=document.getElementById("set_time").value;
	t=parseInt(t)
	console.log(t)
	$('#danmu').data("nowtime",t);
}

function getDanmuFromDataBase(){
	addData();
	setTimeout("getDanmuFromDataBase()",500);
}

function addData(){
	 $.ajax({
	      url:"/WeixinDemo/getDanmu",
	      dataType:'json',
	      timeout:10000,
	      success:function(data){
	    	    var text=data.text;
	    	    var color=data.color;
	    	    var size=data.size;
	    	    var position=data.position;
		        var time = $('#danmu').data("nowtime");
	    	  var text_obj='{ "text":"'+text+'","color":"'+color+'","size":"'+size+'","position":"'+position+'","time":'+time+'}';
	    	  var new_obj=eval('('+text_obj+')');
	    	  $('#danmu').danmu("add_danmu",new_obj);
	       
	      },
	 	  error:function(data){
	 		alert("failed");  
	 	  }
	    });
}
	</script>
	
</body>
</html>