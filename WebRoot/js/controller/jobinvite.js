require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'uiservice',
 'zero_clipboard'
], 
function($, angular, app, cookie, url, zero_clipboard) {
	app.controller('inviteCtrl', ['$scope', '$rootScope', '$http', '$q', 'uiservice',
   		function($scope, $rootScope, $http, $q, uiservice) {
		$('.viewport').show();
		$rootScope.tabIndex = 7;
		$scope.userInfo = {};
		$scope.step = 1;
		
//		$scope.joinResult = {
//			code: "200",
//			email: "li@test.com",
//			inviteCode: "1M3d2U0w",
//			inviteUrl: "http://localhost:8080/invite1M3d2U0w",
//			message: "添加成功！",
//			name: "小白"
//		};
		

		/* 获取推荐信息*/
		$scope.initInvite = function() {
			$q.when($http.post('/invite/getUserName', $.param({})))
			.then(function(result) {
				if(result.data.code == 200) {
					$scope.joinResult = result.data;
					$scope.userInfo.email = $rootScope.user.email;
					$scope.userInfo.name = result.data.userName;
				}
			}, function(err) {
				console.error('queryLoginName', err);
			});							
		};
		
		/* 加入朋友推荐*/
		$scope.joinInvite = function() {
			var config = {
				name : $scope.userInfo.name,
				email : $scope.userInfo.email
			};
			
			if(!_checkedInvite(config)) return;

			$q.when($http.post('/invite/joinInvite', $.param(config)))
			.then(function(result) {
				var data = result.data;
				
				if(data.code == 200) {
					$scope.step = 2;
					$scope.userInfo = {};
					$scope.joinResult = result.data;
					$scope.getCaptcha();
					$scope.copyUrl();
					_initWb();
				}else {
					$('#email_tip').show().text(data.message);
				}
			}, function(err) {
				console.error('joinInvite', err);
			});							
		};
		
		/* 验证码*/
		$scope.getCaptcha = function() {
			$q.when($http.post('/invite/getCaptcha'))
			.then(function(result) {
				if(result.data.code == 200) {
					$scope.captcha = result.data.captcha;
				}
			}, function(err) {
				console.error('getCaptcha', err);
			});
		};
		
		/* 发送朋友邀请*/
		$scope.sendInvite = function() {
			var config = {
				name: $scope.userInfo.name,
				email: $scope.userInfo.email,
				sms: $scope.userInfo.sms,
				inviteCode: $scope.joinResult.inviteCode,
				answer: $scope.userInfo.answer
			};
			
			if(!_checkedInvite(config)) return;
			
			$q.when($http.post('/invite/sendInvite', $.param(config)))
			.then(function(result) {
				var data = result.data;

				if(data.code == 200) {
					$scope.userInfo = {};
					$scope.getInviteList();
				}
				
				$scope.getCaptcha();
				uiservice.notify(data.message, data.code != 200);
			}, function(err) {
				console.error('sendInvite', err);
			});
		};
		
		/* 获取自己邀请列表*/
		$scope.getInviteList = function() {
			var config = {
				inviteCode: $scope.joinResult.inviteCode
			};
			
			$q.when($http.post('/invite/getInviteList', $.param(config)))
			.then(function(result) {
				$scope.inviteList = result.data.queryList;
				
				setTimeout(function() {
					$('.list-box .tr').click(function() {
						if($(this).next().css('display') == 'none') {
							$(this).next().slideDown("slow");
						}else {
							$(this).next().slideUp("slow");
						}
					});
				});
			}, function(err) {
				console.error('getInviteList', err);
			});			
		};
		
		/* 复制链接*/
		$scope.copyUrl = function() {
			setTimeout(function() {
		        var clip = new ZeroClipboard.Client();
		        clip.setHandCursor(true);
		        clip.setText($scope.joinResult.inviteUrl);
		        clip.addEventListener( "mouseUp", function(client) {
		        	uiservice.alert("复制成功！");
		        });
		        clip.glue("cpBtn");				
			}, 100);
		};
		
		/* 清除提示*/
		$scope._clearTip = function(ele) {
			if(ele) {
				$(ele).text('').hide();
			}else {
				$('.checked label').text('').hide();
			}
			
			$('#answer_tip').text('').hide();
		};
		
		
		/* 验证注册*/
		function _checkedInvite(data) {
			$scope._clearTip('.checked label');
			var EMAIL_REGEXP = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			
			if(!data.name) {
				$('#name').focus();
				$('#name_tip').show().text('姓名输入有误！');
				return false;
			}else if(!EMAIL_REGEXP.test(data.email)) {
				$('#email').focus();
				$('#email_tip').show().text('邮箱输入有误！');			
				return false;
			}else if(!data.answer && $scope.step == 2) {
				$('#answer').focus();
				$('#answer_tip').show().text('验证码输入有误！');
				return false;
			}else if(uiservice.isNotify()) {
				return false;
			}
			
			return true;
		}
		
		/*初始化微博分享*/
		function _initWb() {
			window.jiathis_config = {
					data_track_clickback:true,
					url: $scope.joinResult.inviteUrl,
					summary:"申请实力拍人才拍卖，就能在一周内邂逅心仪企业，匿名求职，还能被众多互联网公司疯抢，快来试试吧！  ",
					title:"朋友，听过人才拍卖吗？",
					ralateuid:{
						"tsina":"@实力拍人才拍卖"
					},
					appkey:{
						"tsina":"1963629593"
					},
					shortUrl:false,
					hideMore:false
				};
			
			$('<script src="http://v3.jiathis.com/code/jia.js?uid=2021792"></script>').appendTo('head');
		};
		
		function $safeApply($scope, fn) {
			var phase = $scope.$$phase;
			if (phase == '$apply' || phase == '$digest') {
				$scope.$eval(fn);
			} else {
				$scope.$apply(fn);
			}
		}
		
 	}]);
	
	app.bootstrap();
});
