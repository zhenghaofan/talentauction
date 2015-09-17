if (typeof define !== 'function') {
	var define = require('amdefine')(module);
}

define(['$', "angular", 'cookie'], function($, angular, cookie) {
	var moduleName = 'bidmeApp';
	var app = angular.module(moduleName, []);

	app.config([ '$httpProvider',
  		function($httpProvider) {
		$httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
		
		$httpProvider.responseInterceptors.push(['$q', function($q) {
             return function(promise) {
                 return promise.then(function(response) {
                     /*response.data.extra = 'Interceptor strikes back';*/
                	 if(response.config.url == '/company/compProgress') {
                		 return response;
                	 }else if(response.data.code == 300) {
                		 window.location.href = '/base/signin';
                	 }
                     return response;
                 }, function(response) {
                     /*if(response.status === 401) {
                         response.data = {
                             status: false,
                             description: 'Authentication required!'
                         };
                         return response;
                     }*/
                     return $q.reject(response);
                 });
             }
         }]);
	}]).controller('headerCtrl', ['$scope', '$rootScope', '$http', '$q', 
	    function($scope, $rootScope, $http, $q) {
		$rootScope.user = {};
		
		/* 获取登录cookie*/
		$scope.getUserInfo = function() {
			var user = cookie.get("userEmail");
			if(!!user) {
				$rootScope.user.email = user.replace("&", "@");
				$rootScope.user.isOnline = true;
				$rootScope.user.status = Number(cookie.get("status"));
				
				if($rootScope.user.status == '1') {
					$scope.getCompMessage()
				}else {
					$scope.getUserMessage();
					$scope.getAutomatic();
				}	
			}
		}
		
		/* 退出登录*/
        $scope.signOut = function() {
            $q.when($http.post('/common/userCancel'))
            .then(function(result) {
            	$rootScope.user = {};
            	$scope.msgCount = undefined;
            	$scope.maticCount = undefined;
            	window.location.href = '/base/signin';
            }, function(err) {
            	console.error('userCancel', err);
            });
        };
        
        /* 关闭提示*/
        $scope.closeTip = function() {
        	$('#count_tip').hide();
        };
        
        /* 获取消息条数*/
        $scope.getUserMessage = function() {
            $q.when($http.post('/resume/getUserMessage'))
            .then(function(result) {
            	var data = result.data;
            	$scope.msgCount = data.count;
            }, function(err) {
            	console.error('getUserMessage', err);
            });
        };
        
        /* 获取消息条数*/
        $scope.getCompMessage = function() {
            $q.when($http.post('/company/getCompMessage'))
            .then(function(result) {
            	var data = result.data;
            	$scope.msgCount = data.count;
            }, function(err) {
            	console.error('getCompMessage', err);
            });
        };
        
        /* 获取拒绝消息条数*/
        $scope.getAutomatic = function() {
            $q.when($http.post('/resume/getAutomatic'))
            .then(function(result) {
            	var data = result.data;
            	$scope.maticCount = data.count;
            }, function(err) {
            	console.error('getAutomatic', err);
            });
        };
       
        /* 通知和小火箭*/
		$(document).on('scroll', function() {
			$('#header').css({position: 'fixed'});
			if($rootScope.tabIndex == 1) $('.index-wrap').css({'margin-top': '84px'});
			
			var notify = $("#notify").css('display');
			if(notify == 'block') {
				$('#notify_view').css({margin: '167px auto 80px'});
			}else {
				$('#notify_view').css({margin: '100px auto 80px'});
			}
			
			var scrollHeight = $(document).scrollTop();
			if( scrollHeight >= 400 ) {
				$("#rocket").fadeIn().css('display', 'block');
			} else {
				$("#rocket").fadeOut();
				$("#rocket").removeClass('rocket-f');
			}
		});
		
		$(document).on('click', '#rocket', function() {
			$(this).addClass('rocket-f');
			$('html,body').animate({scrollTop:0},700);
		});        
		
        /* 登录面板切换*/
		$(document).on("mouseenter", "#peo_ico", function(){
			$("#det_box").show();
		})
		.on("mouseleave" ,"#peo_ico", function(){
			$("#det_box").hide();
		})
		.on("mouseenter", "#det_box", function(){
			$("#det_box").show();
		})
		.on("mouseleave" ,"#det_box", function(){
			$("#det_box").hide();
		});
	}]);
	
	return {
		config : function() {
			app.config.apply(this, arguments);
			return this;
		},
		controller : function() {
			app.controller.apply(this, arguments);
			return this;
		},
		directive : function() {
			app.directive.apply(this, arguments);
			return this;
		},
		service: function(){
			app.service.apply(this, arguments);
			return this;
		},
		factory: function(){
			app.service.apply(this, arguments);
			return this;
		},
		filter: function(){
			app.filter.apply(this, arguments);
			return this;
		},
		bootstrap : function() {
			angular.bootstrap(document.body, [moduleName]);
		}
	};
});