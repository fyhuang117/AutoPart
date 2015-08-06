<script src="js/min.js"></script>
<script src="js/basic.min.js"></script>
<script>
$(function(){
		$(".custom-sidebar-parent a").click(function(){
			var icon = $(this).find("span");
			var o = $(this).parent();
			if(icon.html() == "+"){
				o.nextAll(".custom-sidebar-child").show();
				icon.html("-");	
			}else{
				o.nextAll(".custom-sidebar-child").hide();
				icon.html("+");		
			}
			
		});
		
		
		$(".bottbords").hover(function(){
			$(this).toggleClass("bottbords-hover");
		});
	});
</script>