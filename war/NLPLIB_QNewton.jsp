<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page import="method.QNewton" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>非線形計画法</title>
<style type="text/css">
	body { background-color:#f0f0ff }
	.tbl { border-style:solid; border-width:1px;border-color:darkgreen;border-collapse:collapse; width:1200px }
	.dt_o { border-style:solid; border-width:1px; border-color:darkgreen }
	.dt_e { border-style:solid; border-width:1px; border-color:darkgreen;background-color:#c0c0ff }
	.head { border-style:solid; border-width:1px; border-color:darkgreen;background-color:darkgreen;color:white }
</style>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      google.setOnLoadCallback(drawChart1);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
			['繰り返し数', '目的関数値', '理論値'],
                                                          
	<%
	QNewton qn1 = (QNewton)request.getAttribute( "res" );
	double x1[][] = qn1.getX();
	double obj1[] = qn1.getObj();
	for(int i = 0; i < obj1.length; i++){
		 if(i == obj1.length-1){
			 %> 
			 ['<%= i %>',<%=obj1[i]%>, 0.000]
			 <%
		 }else{
		 %> 
		 ['<%= i %>',<%=obj1[i]%>, 0.000],
		 <%
		 }
		
	}
	
	%>
        ]);

        var options = {
          title: '目的関数：収束の様子'
        };

        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));

        chart.draw(data, options);
      }
      
      
      function drawChart1() {
          var data1 = google.visualization.arrayToDataTable([
  			['繰り返し数', '微分ノルム', '収束設定値'],
                                                            
  	<%
  	double norm1[] = qn1.getNorm();
  	double conv = qn1.getConv();
  	
  	for(int i = 0; i < norm1.length; i++){
  		 if(i == norm1.length-1){
  			 %> 
  			 ['<%= i %>',<%=norm1[i]%>, <%=conv%>]
  			 <%
  		 }else{
  		 %> 
  		 ['<%= i %>',<%=norm1[i]%>, <%=conv%>],
  		 <%
  		 }
  		
  	}
  	
  	%>
          ]);

          var options1 = {
            title: '微分ノルム：収束の様子'
          };

          var chart1 = new google.visualization.LineChart(document.getElementById('chart_div1'));

          chart1.draw(data1, options1);
        }
      
    </script>


</head>
<body>
<h2 style='text-align:center'>準ニュートン法(BFGS)</h2>
<hr/>
<a href="/NLPLIB_Servlet">メインへ</a>

<br>
[設定値]
<table class='tbl'>
<tr>
<th class='head'>最適化手法</th><th class='head'>対象関数</th><th class='head'>変数の数</th><th class='head'>初期点x<sub>1</sub></th><th class='head'>初期点x<sub>2</sub></th><th class='head'>直線探索基準</th><th class='head'>反復回数上限</th><th class='head'>収束条件</th><th class='head'>B00</th><th class='head'>B01</th><th class='head'>B10</th><th class='head'>B11</th>
</tr>

<% 
	int cnt = 0;
	QNewton qn = (QNewton)request.getAttribute( "res" );
	double x[][] = qn.getX();
	double obj[] = qn.getObj();
	int code = qn.getCode();
	double norm[] = qn.getNorm();
	double d[][] = qn.getD();
	double alpha[] = qn.getAlpha();
	long time_conv = qn.getTime_conv();
	long time_lim = qn.getTime_lim();
	double initx[] = qn.getInitx();
	double BB[][][] = qn.getBB();
%>

<% String cls = "dt_e"; %>
	<tr>
	<td class='<%=cls%>' width='120' style='text-align:center'>準ニュートン法(BFGS)</td>
	<td class='<%=cls%>' width='120' style='text-align:center'>f(x) = 1/2(x<sub>1</sub> - x<sub>2</sub><sup>2</sup>)<sup>2</sup> + 1/4(x<sub>2</sub> - 2)<sup>4</sup></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=initx.length %></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=initx[0] %></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=initx[1] %></td>
	<td class='<%=cls%>' width='120' style='text-align:center'>Armijo</td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=obj.length%></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=conv %></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=BB[0][0][0] %></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=BB[0][0][1] %></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=BB[0][1][0] %></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=BB[0][1][1] %></td>
	</tr>
</table>

<br>
<br>
<br>
[結果]
<table class='tbl'>
<tr>
<th class='head'>目的関数(理論値)</th><th class='head'>目的関数(計算値)</th><th class='head'>相対誤差%(目的関数)</th><th class='head'>x1(理論値)</th><th class='head'>x1(計算値)</th><th class='head'>x2(理論値)</th><th class='head'>x2(計算値)</th><th class='head'>収束ループ数</th><th class='head'>計算時間(mm)</th>
</tr>


	<tr>
	<td class='<%=cls%>' width='120' style='text-align:center'>0.00</td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=obj[code]%></td>
	<td class='<%=cls%>' width='120' style='text-align:center'>???</td>
	<td class='<%=cls%>' width='120' style='text-align:center'>4.000</td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=x[code][0]%></td>
	<td class='<%=cls%>' width='120' style='text-align:center'>2.000</td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=x[code][1]%></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=code%></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=time_conv %></td>
	</tr>
	
	<% cls = "dt_o"; %>
	<tr>
	<td class='<%=cls%>' width='120' style='text-align:center'>0.00</td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=obj[obj.length-1]%></td>
	<td class='<%=cls%>' width='120' style='text-align:center'>???</td>
	<td class='<%=cls%>' width='120' style='text-align:center'>4.000</td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=x[obj.length-1][0]%></td>
	<td class='<%=cls%>' width='120' style='text-align:center'>2.000</td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=x[obj.length-1][1]%></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=obj.length%></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=time_lim %></td>
	</tr>
	
</table>

<br>
<div id="chart_div" style="width: 1000px; height: 500px;"></div>
<br>
<div id="chart_div1" style="width: 1000px; height: 500px;"></div>
<br>
[計算ログ]
<table class='tbl'>
<tr>
<th class='head' ">ループ数</th><th class='head'>x1</th><th class='head'>x2</th><th class='head'>目的関数</th><th class='head'>微分ノルム</th><th class='head'>d1</th><th class='head'>d2</th><th class='head'>Alpha</th><th class='head'>B00</th><th class='head'>B01</th><th class='head'>B10</th><th class='head'>B11</th>
</tr>

<%

	
	//if(code < 100){
		for(int i = 0; i < obj.length; i++){
//			String cls;
			if ((cnt % 2) == 0) {
				cls = "dt_e";
			} else {
				cls = "dt_o";
			}%>

	<tr>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=i%></td>
	<td class='<%=cls%>' width='200' style='text-align:center'><%=x[i][0]%></td>
	<td class='<%=cls%>' width='200' style='text-align:center'><%=x[i][1]%></td>
	<td class='<%=cls%>' width='400' style='text-align:center'><%=obj[i]%></td>
	<td class='<%=cls%>' width='400' style='text-align:center'><%=norm[i]%></td>
	<td class='<%=cls%>' width='200' style='text-align:center'><%=d[i][0]%></td>
	<td class='<%=cls%>' width='200' style='text-align:center'><%=d[i][1]%></td>
	<td class='<%=cls%>' width='200' style='text-align:center'><%=alpha[i]%></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=BB[i][0][0] %></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=BB[i][0][1] %></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=BB[i][1][0] %></td>
	<td class='<%=cls%>' width='120' style='text-align:center'><%=BB[i][1][1] %></td>
	</tr>
	
	<%
		cnt++;

   //}
   }%>


</body>
</html>