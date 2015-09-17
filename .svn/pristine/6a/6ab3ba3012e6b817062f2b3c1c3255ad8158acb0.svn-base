$(function($) {

	 var time = 3;
	 var handle = setInterval(function() {
		 if( time > 0 ) {
			 time --;
			 $("#time_text").text(time);
		 } else {
			 clearInterval(handle);
		 	WeixinJSBridge.call('closeWindow');
		 }
	 }, 1000);
	 
	$("#close_btn").on('click', function() {
		 WeixinJSBridge.call('closeWindow');
	});
});