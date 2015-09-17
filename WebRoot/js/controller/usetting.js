require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'uiservice'
], 
function($, angular, app, cookie, url) {
	app.controller('usettingCtrl', ['$scope', '$rootScope', '$http', '$q', 'uiservice',
   		function($scope, $rootScope, $http, $q, uiservice) {
		
		setTimeout(function() {
			$('.viewport').show(0, function() {
				$('body').css({'background-color': '#2C2F37'});
			});
		});
		
		/*初始化数据*/
		$scope.isComp = $rootScope.user.status;
		var EMAIL_REGEXP = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		$scope.userInfo = {
				emailLock: false,
				currentEmail: $rootScope.user.email
		};
		
		/*获取联系人信息*/
		$scope.getUserInfo = function(){
			var config = {};	
			$q.when($http.post('/userset/getContacts', $.param(config)))
			.then(function(retsult) {
				var data = retsult.data;
				if(data.code == 200) {
					$scope.userInfo.name = data.queryList.name;
					$scope.userInfo.jobTitle = data.queryList.jobTitle;
				}else {
					$('#agreement_tip').show().text(retsult.data.message);
				};
			}, function(err) {
				console.error('userRegister', err);
			});
		}
		$scope.getUserInfo();
		
		/*更改联系人信息*/
		$scope.changeUserInfo = function(option, str){
			if(option==0){
				if(!_checkedInputs(3)) {
					return;
				}
				var config = {
						name: $scope.userInfo.newName,
						jobTitle: ''			
				};			
			}else{
				if(!_checkedInputs(4)) {
					return;
				}
				var config = {
						name: '',
						jobTitle: $scope.userInfo.newTitle			
				};
			}
			
			$q.when($http.post('/userset/updateContacts', $.param(config)))
			.then(function(retsult) {
				var data = retsult.data;
				
				if(data.code == 200) {
					$scope.userInfo.newTitle='';
					$scope.userInfo.newName='';
					$('#newName').value="";
					$('#newTitle').value="";
					$scope.getUserInfo();
				}
				uiservice.notify(data.message, data.code != 200);
			}, function(err) {
				console.error('userRegister', err);
			});	
		}
		
		/*更改登陆邮箱*/
		$scope.changeEmail = function(){
			if(!_checkedInputs(1)) {
				return;
			}
			
			var config = {
					email: $scope.userInfo.newEmail
			};
			
			$q.when($http.post('/userset/setEmail', $.param(config)))
			.then(function(retsult) {
				var data = retsult.data;
				
				if(data.code == 200) {
					$rootScope.user.email=$scope.userInfo.newEmail;
					$scope.userInfo.newEmail='';
					$scope.userInfo.currentEmail=$rootScope.user.email;
				}
				uiservice.notify(data.message, data.code != 200);
			}, function(err) {
				console.error('userRegister', err);
			});
		}
		
		/*更改密码*/
		$scope.changePassword = function(){
			if(!_checkedInputs(2)) {
				return;
			}
			
			var config = {
					newpwd: $scope.userInfo.new_p,
					oldpwd: $scope.userInfo.old_p		
			};
			
			$q.when($http.post('/userset/editPassword', $.param(config)))
			.then(function(retsult) {
				var data = retsult.data;
				
				if(data.code == 200) {
					$scope.userInfo.old_p='';
					$scope.userInfo.new_p='';
					$scope.userInfo.con_n_p = '';
				}
				uiservice.notify(data.message, data.code != 200);
			}, function(err) {
				console.error('userRegister', err);
			});
		}
		
		/* 清除提示*/
		$scope._clearTip = function(ele) {
			$(ele).text('').hide();
		};
		
		/* 验证更改设置*/
		function _checkedInputs(status) {
			if($scope.userInfo.emailLock && $scope.userInfo.newEmail){
				$('#newEmail').focus();
				return;
			}
			$('.checked label').hide();
			
			if(!$scope.userInfo.newEmail && status==1) {
				$('#newEmail').focus();
				$('#email_tip').show().text('请输入邮箱！');
				return false;
			}else if((!$scope.userInfo.old_p || $scope.userInfo.old_p.length < 6) && status==2) {
				$('#oldPassword').focus();
				$('#oldPwd_tip').show().text('请输入6-16位密码！');
				return false;
			}else if((!$scope.userInfo.new_p || $scope.userInfo.new_p.length) < 6 && status==2) {
				$('#newPassword').focus();
				$('#newPwd_tip').show().text('请输入6-16位密码！');
				return false;
			}else if((!$scope.userInfo.con_n_p || $scope.userInfo.con_n_p.length < 6) && status==2) {
				$('#confirmPassword').focus();
				$('#conPwd_tip').show().text('请输入6-16位密码！');
				return false;
			}else if(($scope.userInfo.con_n_p != $scope.userInfo.new_p) && status==2) {
				console.log($scope.userInfo.con_n_p,$scope.userInfo.new_p);
				$('#confirmPassword').focus();
				$('#conPwd_tip').show().text('确认密码与新密码不一致！');
				return false;
			}else if(!$scope.userInfo.newName && status==3) {
				$('#newName').focus();
				$('#name_tip').show().text('请输入新姓名');
				return false;
			}else if(!$scope.userInfo.newTitle && status==4) {
				$('#newTitle').focus();
				$('#job_tip').show().text('请输入新职位');
				return false;
			}
			return true;
		}
		
		/* 验证邮箱*/
		$scope.checkEmail = function() {
			$scope.userInfo.newEmail = $('#newEmail').val();
			if(!$scope.userInfo.newEmail) return;
			
			if(EMAIL_REGEXP.test($scope.userInfo.newEmail)) {
				
				$q.when($http.post('/user/emailVerify?email=' + $scope.userInfo.newEmail))
				.then(function(result) {
					if(result.data.code != 200) {
						$('.checked label').hide();
						$('#newEmail').focus();
						$('#email_tip').show().text(result.data.message);
						$scope.userInfo.emailLock = true;
					 }else {
						 $scope.userInfo.emailLock = false;
					 }
				}, function(err) {
	            	  console.error('checkEmail', err);
				});
				 
			}else {
				$('.checked label').hide();
				$('#email_tip').show().text('请输入有效邮箱！');
				$scope.userInfo.emailLock = true;
			}		
		};
		
 	}]);
	
	app.bootstrap();
});
