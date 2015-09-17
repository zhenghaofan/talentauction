if (typeof define !== 'function') {
	var define = require('amdefine')(module);
}

define(['$','angular','app'], function($, angular, app) {
	app.factory('uiservice', ['$http', '$q',
	   function($http, $q) {
		   
		  var exports = {};
		  
		  /* 提示框*/
		  exports.alert = function(text, _callback) {
			  _init_modal();
			  
			  $('#bidModal .modal-body span').text(text);
			  $('#bidModal .cancel').hide();
			  $('#bidModal').fadeIn(150);
			  
			  $('.modal-footer button, .modal-header button').click(function() {
				  $('#bidModal').remove();
				  var text = $(this).text();
				  if(typeof _callback === 'function' && angular.equals(text, '确定')) {
					  _callback();
				  }
			  });			  
		  };
		  
		  /* 确认框*/
		  exports.confirm = function(text, _callback) {
			  _init_modal();
			  
			  $('#bidModal .modal-body span').html(text);
			  $('#bidModal').fadeIn(150);
			  
			  $('.global-btn span').click(function() {
				  $('#bidModal').animate({"opacity":"hide","top":"-10px"});
				  $('#bidModal').remove();
				  if($(this).hasClass('btn1')) {
					  _callback();
				  }
			  });			  
		  };

		   
		  
		  /* 通知*/
		  exports.notify = function(text, type, _callback, _errback) {
			  _init_notify(text, type);
			  _close_notify(type, _callback, _errback);
		  };
		  
		  /* 通知状态*/
		  exports.isNotify = function() {
			  return $("#notify").css('display') == 'none'? false : true;
		  };
		  
		  /* 通知初始化*/
		  function _init_notify(text, type) {
			  var notify = $("#notify");
			  var bidTag = $('#bid_tag');
			  var scroll = $(document).scrollTop();
			  
			  if(type) {
				  notify.find('.notify-item').addClass('err');
			  }else {
				  notify.find('.notify-item').removeClass('err');
			  }
			  
			  if(scroll == 0) {
				  $('#header').css({position: 'relative'});
				  $('#notify_view').css({margin: '54px auto 80px'}); 
				  if(bidTag.length) $('.viewport').css({'padding-top' : '0'}).find('#bid_tag').css({'margin-top' : '54px'});
			  }else{
				  if(bidTag.length) $('.viewport').css({'padding-top' : '0'}).find('#bid_tag').css({'margin-top' : '100px'}); 
			  }
			  
			  notify.find('.notify-text').text(text);
			  notify.slideDown("slow");
		  }
		  
		  /* 通知关闭*/
		  function _close_notify(type, _callback, _errback) {
			  setTimeout(function() {
				  $("#notify").slideUp("slow", function() {
					  var bidTag = $('#bid_tag');
					  $('#header').css({position: 'fixed'});
					  $('#notify_view').css({margin: '100px auto 80px'});
					  $('#notify .notify-text').text('');
					  
					  if(bidTag.length) $('.viewport').css({'padding-top' : '100px'}).find('#bid_tag').css({'margin-top' : '0'});
					  if(typeof _callback === 'function' && !type) _callback();
					  if(typeof _errback === 'function' && type) _errback();
				  });				  
			  }, 1000);	
		  };
		  
		  /* 弹出框初始化*/
		  function _init_modal() {
			  $('body').append(  
				'<div id="bidModal" class="bid-modal modal">'+
					'<div class="modal-backdrop in"></div>'+
					'<div class="modal-dialog">'+
						'<div class="modal-content">'+
							'<div class="modal-body">'+
								'<div class="popt-resume-warp">'+
									'<span >你确定要提交审核吗？</span>'+
								'</div>'+
							'</div>'+
							'<div class="global-btn">'+					
									'<span class="btn2">取&nbsp;&nbsp;&nbsp;&nbsp;消</span>'+
									'<span class="btn1">确&nbsp;&nbsp;&nbsp;&nbsp;定</span>'+
							'</div>'+		
						'</div>'+
					'</div>'+
				'</div>');

		  }
		  
		  return exports;
	}]);
});