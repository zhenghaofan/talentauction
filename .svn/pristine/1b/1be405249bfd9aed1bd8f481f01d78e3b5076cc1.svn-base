require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('indexCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		$rootScope.tabIndex = 1;
		$('.viewport').show();
		
		setTimeout(function() {
			$('#header').css({'position': 'relative'});
			$('.index-wrap').css({'margin-top': '0'});
			$('.red-top').slideDown();
		}, 300);
		
		/* 红包弹出框*/
		$(document).on('click', '#header .red-top', function() {
			$('.guide-modal').fadeIn(150);
		});
		
		$(document).on('click', '#main .red-close', function() {
			$('.guide-modal').fadeOut(150);
		});
		
		//鼠标hover按钮出现
		$(document).on("mouseover",'.cooper-item',function(_event){
			$(_event.currentTarget).children('.img-text').show();
		}).on("mouseout", '.cooper-item', function(_event){
			$(_event.currentTarget).children('.img-text').hide();
		});
 	}]);
	
	app.bootstrap();
});
