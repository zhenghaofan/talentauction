require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'uiservice'
], 
function($, angular, app, cookie, url) {
	app.controller('bidlistCtrl', ['$scope', '$rootScope', '$http', '$q', 'uiservice',
   		function($scope, $rootScope, $http, $q, uiservice) {
		$rootScope.tabIndex = 5;
		$scope.replayState = 0;
		$scope.pageIndex = 1;
		$scope.pageSize = 15;
		$('.viewport').show();
		
		bind();
		
		/* 清除提示*/
		$scope._clearTip = function(ele) {
			if(ele) {
				$(ele).text('').hide();
			}else {
				$('.err-wrap label').text('').hide();
			}
		};
		
		/* 关闭窗口*/
		$scope._close = function(ele, fn) {
			$scope._clearTip();
			$(ele).fadeOut(150);
			
			if(typeof _errback === 'function') fn();
		};
		
		/* 开启窗口*/
		$scope._show = function(ele, fn) {
			$scope._clearTip();
			$(ele).fadeIn(150);
			if(typeof _errback === 'function') fn();
		};
		
		/* 选择排序方法*/
		$scope.sltSort = function(status) {
			$scope.replayState = status;
			$scope.pageIndex = 1;
			$scope.getBidLogs();
		};
		
		/* 获取拍卖记录*/
		$scope.getBidLogs = function() {
			var config = {
					pageSize: $scope.pageSize,
					page: $scope.pageIndex,
					reply: $scope.replayState
			};			
			
            $q.when($http.post('/resume/getBidLogs', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.listData = data.bidLogs;
            	$scope.totalPage = data.totalPage;
            	$scope.pageIndex = data.page;
            	
            	_initPager();
            	setTimeout(function() {
        			$('.viewport').show(0, function() {
        				$('body').css({'background-color': '#2C2F37'});
        			});
        		});
            }, function(err) {
            	console.error('getBidLogs', err);
            });
		};
		
		/* 拒绝面板*/
		$scope.rejectBox = function(item) {
			item.optReply = !item.optReply;
		};
		
		/* 拒绝操作*/
		$scope.rejectOpt = function(item) {
			var checkbox = $(".rjt-con  input:checked");
			var textarea = $(".rjt-con  textarea");
			var config = {
					id: '',
					isReply: 2
			};
			
			if(checkbox.length) {
				config.rejectReason = checkbox.next().text();
			}else if(textarea.val()) {
				config.rejectReason = textarea.val();
			}else {
				$('#rjt_tip').show().text('请选择拒绝理由！');
				return;
			}

			return;
			$scope.subReply(config, item);
		};
		
		/* 接受面试*/
		$scope.agreeOpt = function(item) {
			var config = {
					id: item.id,
					isReply: 1
				};
			
			uiservice.confirm('确定联系面试吗？', function() {
				$scope.subReply(config);
			});
		};
		
		/* 提交回复*/
		$scope.subReply = function(config, item) {
            $q.when($http.post('/resume/userReply', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.getBidLogs();
            }, function(err) {
            	console.error('userReply', err);
            });
		};
		
		/* 翻页*/
		$scope.turnPage = function(_event, status) {
			if(status == '+') {
				$scope.pageIndex ++;
			}else if(status == '-') {
				$scope.pageIndex --;
			}else {
				$scope.pageIndex = status;
				$(_event.target).addClass('slt').siblings().removeClass('slt');
			}
			$scope.getBidLogs();
		};	

		/* 初始化翻页*/
		function _initPager() {
			$scope._pageCount = [];
			var index = Math.ceil($scope.pageIndex / 6);
			var total = Math.ceil($scope.totalPage / 6);
			var size = index < total? index * 6 : $scope.totalPage;
			var i = (index - 1) * 6;
			
			for (i; i < size; i++) {
				$scope._pageCount.push(i + 1);
			}
		}
		
		/* 事件绑定*/
		function bind() {
			$('.rjt-con input:checkbox').click(function() {
				$scope._clearTip();
				$(this).siblings(':checkbox').attr('checked', !this.checked);
			});
			
			$('.rjt-box textarea').focus(function() {
				$scope._clearTip();
				$('.rjt-con input:checkbox').attr('checked', false);
			});
		}
		
		/*验证回复*/
		function _checkReply(data) {
			$scope._clearTip();
			
			if(!data.rejectReason) {
				$('#reject_inp_tip').show().text('拒绝原因输入有误！');
				return false;
			}
			
			return true;
		}
 	}]);
	
	app.bootstrap();
});
