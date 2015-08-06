function changeCss(obj){
var alist = document.getElementById("ulli").getElementsByTagName("li");
for(var i =0;i < alist.length;i++){ 
alist[i].className=""; //给所有a标签赋原色
}
obj.className="Selected";//令当前标签高亮
}


function changezy(obj){
var alist = document.getElementById("ul_zy").getElementsByTagName("tr");
for(var i =0;i < alist.length;i++){ 
alist[i].className="bottbords"; //给所有a标签赋原色
}
obj.className="bottbords Selecteds";//令当前标签高亮
}

function changefy(obj){
var alist = document.getElementById("ul_fy").getElementsByTagName("span");
for(var i =0;i < alist.length;i++){ 
alist[i].className=""; //给所有a标签赋原色
}
obj.className="fy";//令当前标签高亮
}


//弹出产品详细层
function showDiv(id){
document.getElementById('popDiv').style.display='block';
document.getElementById('bg').style.display='block';
}

function closeDiv(){
document.getElementById('popDiv').style.display='none';
document.getElementById('bg').style.display='none';
}

function showEditDiv(id){
	document.getElementById('popEditDiv').style.display='block';
	document.getElementById('bg').style.display='block';
}

function closeEditDiv(){
	document.getElementById('popEditDiv').style.display='none';
	document.getElementById('bg').style.display='none';
}

