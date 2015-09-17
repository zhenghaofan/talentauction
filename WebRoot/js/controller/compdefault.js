require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'upload',
 'info',
 'uiservice'
], 
function($, angular, app, cookie, url, upload) {
	app.controller('defaultCtrl', ['$scope', '$rootScope', '$http', '$q', '$location', 'info', 'uiservice',
   		function($scope, $rootScope, $http, $q, $location, info, uiservice) {
		$rootScope.tabIndex = 4;
		$scope.webContext = {};
		$scope.compInfo = {};
		$scope.jdInfo = {};
		$scope.teamInfo = {};
		$scope.pdtInfo = {};
		$scope.webContext.compSizeList = info.getCompSize();
		$scope.webContext.compPhaseList = info.getCompPhase();
		$scope.webContext.welfareList = info.getWelfare();
		var absUrl = $location.absUrl();
		$scope.isLock = true;
		$scope.isIe = false;
		
		
		if(absUrl.indexOf('.html') != -1) {
			var strStart = absUrl.indexOf('chome') + 5;
			var strEnd = absUrl.indexOf('.html');
			var compId = absUrl.substring(strStart, strEnd);
			$scope.isLock = false;
			
			$('.viewport').css({'padding-top': '100px', 'background-color': '#ECEEF1'});
			$('.main-wrap').css({'margin-top': '0'});
			$('#notify_view').attr({id: ''});
		}else {
			$(document).on('mouseenter', '.box-shadow' ,function() {
				$(this).find('.opt-ico-max.add-ico, .opt-ico-max.up-ico').show();
			}).on('mouseleave', '.box-shadow', function() {
				$(this).find('.opt-ico-max.add-ico, .opt-ico-max.up-ico').hide();
			});
			
			$(document).on('mouseenter', '.box-shadow .sub-item' ,function() {
				$(this).find('.opt-ico').show();
			}).on('mouseleave', '.box-shadow .sub-item', function() {
				$(this).find('.opt-ico').hide();
			});
		}
		
		if(!!window.ActiveXObject || "ActiveXObject" in window){ 
			$scope.isIe = true;
			
			$(document).on('change', '#base_upload', function() {
				$scope.imgStatus = 1;
				$scope.imgName = $(this).val();
				$scope.imgUpload('base_upload');
			});
			
			$(document).on('change', '#team_upload', function() {
				$scope.imgStatus = 3;
				$scope.imgName = $(this).val();
				$scope.imgUpload('team_upload');
			});
				
			$(document).on('change', '#env_upload', function() {
				$scope.imgStatus = 4;
				$scope.imgName = $(this).val();
				$scope.imgUpload('env_upload');
			});
			
			$(document).on('change', '#pdt_upload', function() {
				$scope.imgStatus = 2;
				$scope.imgName = $(this).val();
				$scope.imgUpload('pdt_upload');
			});
		}
		
		if(!$scope.extension){
			$('.extphone').css('dispaly','none');
		}
		
		/* 清除提示*/
		$scope._clearTip = function(ele) {
			$(ele).text('').hide();
		};
		
		/* 选择图片*/
		$scope.imgSlt = function(status) {
			if(uiservice.isNotify() || $scope.isIe) return;
			
			$scope.imgStatus = status;
			var obj = $('#upload');
			obj.unbind('change');
			obj.click();
			obj.change(function() {
				$scope.imgUpload();
			});
		};	
		
		/*图片上传*/
		$scope.imgUpload = function(item) {
			var config = {
					id: 'upload',
					url: '/fileUpload/companyImgUpload'
			};	
			
			if (item) config.id = item;
			
			if($scope.imgStatus == 4) config.url = '/fileUpload/officeUpload';
			
			if(! _checkImg(config.id)) return;
			
			upload.fileUpload(config, function(result) {
				var data = result;
				$('input[name="upload"]').val('');
				
				if(data.code == 200) {
					$safeApply($scope, function() {
						switch ($scope.imgStatus) {
						case 1:
							$scope.compInfo.tmpLogoName = data.imgName;
							break;
						case 2:
							$scope.pdtInfo.tmpLogoName = data.imgName;
							break;
						case 3:
							$scope.teamInfo.tmpLogoName = data.imgName;
							break;
						case 4:
							if(data.code == 200) $scope.getCompInfo();
							uiservice.notify(data.message, data.code != 200);
							break;
						default:
							break;
						}
					});
				}else {
					uiservice.notify(data.message, data.code != 200);	
				}
			},function(err) {
				console.error('companyImgUpload', err);
			});		
		};

		/*信息完整度*/
		$scope.getProgress = function() {
            $q.when($http.post('/company/compProgress'))
            .then(function(result) {
            	var data = result.data;
            	if(data.code == 200) {
                	$("#on_pro_text").text(data.speedOfProgress);
                	$("#on_pro_line").animate({
    					'width': data.speedOfProgress + '%'
    				}, 1000);
            	}
            }, function(err) {
            	console.error('speedOfProgress', err);
            });			
		};
		
		/* 获取省份*/
		$scope.getProvinceList = function() {
			var config = {
					superId: 0
			};		
			
            $q.when($http.post('/common/getCityList', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.webContext.provinceList = data.cityList;
            }, function(err) {
            	console.error('getAreasList', err);
            });				
		};
		
		/* 获取城市*/
		$scope.getCityList = function(id, status) {
			var config = {
					superId: id
			};
			
            $q.when($http.post('/common/getCityList',  $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.webContext.cityList = data.cityList;
            	if(status)$scope.compInfo.city = data.cityList[0].id;
            }, function(err) {
            	console.error('getAreasList', err);
            });				
		};
		
		/* 获取行业领域*/
		$scope.getAreasList = function() {
            $q.when($http.post('/common/getAreasList'))
            .then(function(result) {
            	var data = result.data;
            	$scope.webContext.areasList = data.areasList;
            }, function(err) {
            	console.error('getAreasList', err);
            });				
		};		
		
		/* 获取公司信息*/
		$scope.getCompInfo = function() {
			var config = {};
			if(compId) config.companyId = compId;
			
            $q.when($http.post('/common/getCompanyAllInfo', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.baseInfo = _fomartCompInfo(data.companyInfo);
            	$scope.products = _fomartPdtInfo(data.product);
            	$scope.demands = data.talentDemand;
            	$scope.founder = _formatTeame(data.founder);
            	$scope.getProgress();
            	if($scope.isLock) {
            		$('.viewport').show();
            	}else {
            		$('.viewport').show(0, function() {
        				$('body').css({'background-color': '#2C2F37'});
        			});
            	}
            }, function(err) {
            	console.error('getCompanyAllInfo', err);
            });
		};
		
		/*编辑公司信息*/
		$scope.editCompInfo = function(status) {
			$scope.baseInfo.isEdit = status;
			$scope.compInfo = {};
			if(status) {
				$scope.compInfo = {
					name: $scope.baseInfo.name,
					nickName: $scope.baseInfo.nickName,
					extension:$scope.baseInfo.extension,
					codephone:$scope.baseInfo.telephone.split('-')[0],
					telphone:$scope.baseInfo.telephone.split('-')[1],
					province: $scope.baseInfo.province,
					city: $scope.baseInfo.city,
					area: $scope.baseInfo.area,
					size: $scope.baseInfo.size,
					progress: $scope.baseInfo.progress,
					website: $scope.baseInfo.website,
					addr: $scope.baseInfo.addr,
					welfare: $scope.baseInfo.welfare,
					logoName: $scope.baseInfo.logoName
				};
				
            	$scope.getAreasList();
            	$scope.getProvinceList();
				$scope.getCityList($scope.compInfo.province);
				
	        	for(var i in $scope.webContext.welfareList) {
	        		for(var j in $scope.baseInfo.welfareArray) {
	        			if($scope.webContext.welfareList[i].value == $scope.baseInfo.welfareArray[j])
	            			$scope.webContext.welfareList[i].slt = true;
	        		}
	        	}
	        	
			}
		};
		
		/* 添加公司福利*/
		$scope.addWelfare = function(_event) {
			var keycode = window.event? _event.keyCode : _event.which;
			var welfareList = $scope.webContext.welfareList;
            if(keycode == 13) {
            	if(welfareList.length < 11) {
            		if(!_checkWel($scope.compInfo)) return;
            		welfareList.push({slt:false, value: $scope.compInfo.welStr});
            		$scope.compInfo.welStr = '';
            	}else {
            		$('#compInfo_wel_tip').show().text('福利输入最多生成4个！');
            	}
            }
		};
		
		/* 选择公司福利*/
		$scope.sltWelfare = function(item) {
			var welfareList = $scope.webContext.welfareList;
			var array = [];
			
			for (var i in welfareList) {
				if(welfareList[i].slt) array.push(welfareList[i]);
			}
			
			if(array.length < 4 || item.slt) item.slt = !item.slt;
		};
		
		/* 选择公司阶段*/
		$scope.sltPhase = function(id) {
			$scope.compInfo.progress = id;
		};
		
		 /* 修改公司信息*/
		$scope.upCompInfo = function() {
			var config = $scope.compInfo;
			var welfareList = $scope.webContext.welfareList;
			var tmpArray = [];
			
			if(config.tmpLogoName) {
				config.logoName = config.tmpLogoName;
			}else {
				delete config.logoName;
			}
			
			if(!_checkCompInfo(config)) return;
			
			if(config.website && config.website.indexOf('http') == -1) {
				config.website = 'http://' + config.website;
			}
			
			for(var i in welfareList) {
				if(welfareList[i].slt == true) {
					tmpArray.push(welfareList[i].value);
				}
			}
			
			config.welfare = tmpArray.toString();
			tmpArray = [];
			/*电话修改*/
			config.telephone = $scope.compInfo.codephone +"-"+ $scope.compInfo.telphone;

			if(!$scope.compInfo.codephone && !!$scope.compInfo.telphone){
				$('.ico-phone').show().text('输入主机号时，区号不允许为空！');
				return ;
			}
			
			if(!!$scope.compInfo.extension && (!$scope.compInfo.codephone || !$scope.compInfo.telphone)){
				$('.ico-phone').show().text('输入分机号时，主机号不允许为空！');
				return ;
			}
			config.extension = $scope.compInfo.extension;
			
            $q.when($http.post('/company/updateCompanyInfo', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
            	if(data.code == 200) $scope.getCompInfo();
            	uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('getCompanyInfo', err);
            });			
		};
		
		/* 编辑人才需求*/
		$scope.editJd = function(status, item) {
			$scope.jdInfo = {};
			$scope.jdInfo.isEdit = status;
			
			if(status == 2) {
				for (var key in item) {
					$scope.jdInfo[key] = item[key];
				}
			}
		};
		
		 /* 增加人才需求*/
		$scope.addJd = function() {
			var config = $scope.jdInfo;
			
			if(!_checkJdInfo(config)) return;
			if(config.website && config.website.indexOf('http') == -1) {
				config.website = 'http://' + config.website;
			}
			
            $q.when($http.post('/company/updateTalentDemand', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.jdInfo = {};
            	
            	if(data.code == 200) $scope.getCompInfo();
            	uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('updateTalentDemand', err);
            });				
		};
		
		 /* 删除人才需求*/
		$scope.delJd = function(id) {
			var config = {
					id: id
			};
			
			if(uiservice.isNotify()) return;
			
            $q.when($http.post('/company/delTalentDemand', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
            	if(data.code == 200) $scope.getCompInfo();
            	uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('delTalentDemand', err);
            });				
		};		
		
		/* 编辑创始团队*/
		$scope.editTeam = function(status, item) {
			$scope.teamInfo ={};
			$scope.teamInfo.isEdit = status;
			
			if(status == 2) {
				for (var key in item) {
					$scope.teamInfo[key] = item[key];
				}
			}
		};
		
		 /* 增加或修改创始团队*/
		$scope.upTeam = function() {
			var config = $scope.teamInfo;
			
			if(!_checkTeamInfo(config)) return;
			
			if(config.tmpLogoName) {
				config.imgName = config.tmpLogoName;
			}else {
				delete config.imgName;
			}
			
			if(config.website && config.website.indexOf('http') == -1) {
				config.website = 'http://' + config.website;
			}
			
			delete config.isEdit;			
			
            $q.when($http.post('/company/updateFounder', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
            	if(data.code == 200) $scope.getCompInfo();
            	uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('updateFounder', err);
            });				
		};
		
		 /* 删除创始团队*/
		$scope.delTeam = function(id) {
			var config = {
				id: id	
			};
			
			if(uiservice.isNotify()) return;
			
            $q.when($http.post('/company/delFounder', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
            	if(data.code == 200) $scope.getCompInfo();
            	uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('delFounder', err);
            });			
		};
		
		/* 编辑公司产品*/
		$scope.editPdt = function(status, item) {
			$scope.pdtInfo = {progress: '0'};
			$scope.pdtInfo.isEdit = status;
			
			
			if(status == 1) {
				$scope.webContext.pdtPhaseList = info.getPdtPhase();
			}else if(status == 2) {
				for (var key in item) {
					$scope.pdtInfo[key] = item[key];
				}
				
				$scope.webContext.pdtPhaseList = info.getPdtPhase();
			}
		};
		
		/* 选择产品阶段*/
		$scope.sltPdtPhase = function(id) {
			$scope.pdtInfo.progress = id;
		};
		
		 /* 增加或修改公司产品*/
		$scope.upPdt = function() {
			var config = $scope.pdtInfo;
			
			if(!_checkPdtInfo(config)) return;
			
			if(config.tmpLogoName) {
				config.imgName = config.tmpLogoName;
			}else {
				delete config.imgName;
			}
			
			if(config.website && config.website.indexOf('http') == -1) {
				config.website = 'http://' + config.website;
			}
			
			delete config.isEdit;
			
            $q.when($http.post('/company/updateProducts', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
            	if(data.code == 200) $scope.getCompInfo();
            	uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('updateProducts', err);
            });			
		};
		
		 /* 删除公司产品*/
		$scope.delPdt = function(id) {
			var config = {
					id: id
			};
			
			if(uiservice.isNotify()) return;
			
            $q.when($http.post('/company/delProducts', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
            	if(data.code == 200) $scope.getCompInfo();
            	uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('delProducts', err);
            });			
		};
		

		 /* 删除公司环境*/
		$scope.delOffice = function(imgName) {
			var config = {
					imgName: imgName
			};
			
			if(uiservice.isNotify()) return;
			
            $q.when($http.post('/company/delOffice', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
            	if(data.code == 200) $scope.getCompInfo();
        		uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('delOffice', err);
            });			
		};
		
		/* 编辑公司信息*/
		$scope.editIntro = function(status) {
			$scope.introEdit = status;
			$scope.compIntro = {}
			$scope.compIntro.text = $scope.baseInfo.companyIntro;
		};
		
		 /* 增加或修改公司介绍*/
		$scope.upIntro = function() {
			if(!$scope.compIntro.text || $scope.compIntro.text.length>800) {
				$('#compInfo_intro').focus();
				$('#compInfo_intro_tip').show().text('公司介绍长度需在1~800之间！');
				return false;				
			}
			
			var config = {
					companyIntro: $scope.compIntro.text	
			};
			
			if(uiservice.isNotify()) return;
			
            $q.when($http.post('/company/updateCompanyInfo', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.introEdit = 0;
            	
            	if(data.code == 200) $scope.getCompInfo();
        		uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('updateCompanyInfo', err);
            });			
		};

		/* 验证生成福利*/
		function _checkWel(data) {
			$scope._clearTip();
			if(!data.welStr) {
				$('#compInfo_wel').focus();
				$('#compInfo_wel_tip').show().text('福利输入有误！');
				return false;
			}
			
			return true;			
		}		
		
		/* 验证公司信息*/
		function _checkCompInfo(data) {
			/*$scope._clearTip();
			if(!data.name) {
				$('#compInfo_name').focus();
				$('#compInfo_name_tip').show().text('公司全称有误！');
				return false;
			}else if(!data.nickName) {
				$('#compInfo_nickname').focus();
				$('#compInfo_nickname_tip').show().text('公司简称输入有误！');
				return false;				
			}else if(!data.addr) {
				$('#compInfo_addr').focus();
				$('#compInfo_addr_tip').show().text('详细地址输入有误！');
				return false;				
			}else if(uiservice.isNotify()) {
				return false;
			}*/
			$scope._clearTip();
			if(!data.name.length || data.name.length>100) {
				$('#compInfo_name').focus();
				$('#compInfo_name_tip').show().text('公司名称长度应该在1~100之间！');
				return false;
			}else if(!data.nickName || data.nickName.length>100) {
				$('#compInfo_nickname').focus();
				$('#compInfo_nickname_tip').show().text('公司简称长度应该在1~100之间！');
				return false;				
			}else if(!data.addr || data.addr.length>150) {
				$('#compInfo_addr').focus();
				$('#compInfo_addr_tip').show().text('详细地址长度应该在1~150之间！');
				return false;				
			}else if(uiservice.isNotify()) {
				return false;
			}
			
			return true;			
		}
		
		/* 验证人才需求*/
		function _checkJdInfo(data) {
			var isNumber = /^[1-9]\d{0,9}$/;
			$scope._clearTip();
			
			if(!data.skills) {
				$('#jd_skills').focus();
				$('#jd_skills_tip').show().text('职位输入有误！');
				return false;
			}else if(!isNumber.test(data.minJobYear)) {
				$('#jd_exp_min').focus();
				$('#jd_exp_tip').show().text('最低经验输入有误！');
				return false;
			}else if(data.minJobYear > 18) {
				$('#jd_exp_min').focus();
				$('#jd_exp_tip').show().text('最低经验不太合理！');
				return false;
			}else if(!isNumber.test(data.maxJobYear)) {
				$('#jd_exp_max').focus();
				$('#jd_exp_tip').show().text('最高经验输入有误！');
				return false;
			}else if(data.maxJobYear > 18) {
				$('#jd_exp_max').focus();
				$('#jd_exp_tip').show().text('最高经验不太合理！');
				return false;
			}else if(Number(data.maxJobYear) < Number(data.minJobYear)) {
				$('#jd_exp_max').focus();
				$('#jd_exp_tip').show().text('经验范围输入有误！');
				return false;
			}else if(!isNumber.test(data.minSalary)) {
				$('#jd_salary_min').focus();
				$('#jd_salary_tip').show().text('最低薪资输入有误！');
				return false;
			}else if(data.minSalary > 50) {
				$('#jd_salary_min').focus();
				$('#jd_salary_tip').show().text('最低薪资不太合理！');
				return false;
			}else if(!isNumber.test(data.maxSalary)) {
				$('#jd_salary_max').focus();
				$('#jd_salary_tip').show().text('最高薪资输入有误！');
				return false;
			}else if(data.maxSalary > 50) {
				$('#jd_salary_max').focus();
				$('#jd_salary_tip').show().text('最高薪资不太合理！');
				return false;
			}else if(Number(data.maxSalary) < Number(data.minSalary)) {
				$('#jd_salary_max').focus();
				$('#jd_salary_tip').show().text('薪资范围输入有误！');
				return false;
			}else if(uiservice.isNotify()) {
				return false;
			}
			
			return true;			
		}
		
		/* 验证创始团队*/
		function _checkTeamInfo(data) {
			$scope._clearTip();
			if(!data.name) {
				$('#team_name').focus();
				$('#team_name_tip').show().text('姓名输入有误！');
				return false;
			}else if(!data.jobName) {
				$('#team_title').focus();
				$('#team_title_tip').show().text('职位输入有误！');
				return false;				
			}else if(uiservice.isNotify()) {
				return false;
			}
			
			return true;			
		}
		
		/* 验证公司产品*/
		function _checkPdtInfo(data) {
			$scope._clearTip();
			if(!data.productName) {
				$('#pdt_name').focus();
				$('#pdt_name_tip').show().text('产品名称输入有误！');
				return false;
			}else if(uiservice.isNotify()) {
				return false;
			}
			
			return true;			
		}
		
		/* 验证图片*/
		function _checkImg(id) {
			if(!$scope.isIe) {
				var files = [];
		    	var flag = true;
		    	var e = $('#' + id).get(0);
		    	for(i in e.files) {
		    		// 限制为2M
		    		if(typeof e.files[i] == 'object') {
		    			if( e.files[i].size >= 2 * 1024 *1024 ) {
		    				flag = false;
		    				uiservice.notify('图片过大！限制2M', true);
		    				$('input[name="upload"]').val('');
		    				return false;
		    			}
		    			files.push(e.files[i]);
		    		} 
		    	}
		    	
		    	if(files.length == 0 || !flag) {
		    		uiservice.notify('图片过大！限制2M', true);
		    		$('input[name="upload"]').val('');
		    		return false;			    	
		    	}		
		    }else {
		    	if ($scope.imgName.indexOf('.jpg') == -1 && $scope.imgName.indexOf('.gif') == -1 && 
	            		$scope.imgName.indexOf('.png') == -1 && $scope.imgName.indexOf('.jpeg')) {  
		    		uiservice.notify('非图片类型文件，请重传！', true);
		    		$('input[name="upload"]').val('');
	                return false;  
	            }
		    }
			
			return true;		
		}
		
		/* 创始团队格式 */
		function _formatTeame(data) {
			for( var i = 0; i < data.length; i++ ) {
				if(data[i].website) {
					if( data[i].website.indexOf('weibo.com') != -1 ) {
						data[i].webType = '1';
					} else if( data[i].website.indexOf('zhihu.com') != -1 ) {
						data[i].webType = '2';
					} else if( data[i].website.indexOf('linkedin.com') != -1 ) {
						data[i].webType = '3';
					} else {
						data[i].webType = '0';
					}						
				}
			}
			return data;
		}
		
		/* 格式化公司产品信息*/
		function _fomartPdtInfo(data) {
			var pdtPhaseList = info.getPdtPhase();
			for (var j in data) {
	        	for(var i in pdtPhaseList) {
	        		if(pdtPhaseList[i].id == data[j].progress)
	        			data[j].progressStr = pdtPhaseList[i].value;
	        	}				
			}
			
			return data;
		}
		
		/* 格式化公司信息*/
		function _fomartCompInfo(data) {
			
        	if(data.productUrl) {
        		data.productUrl = data.productUrl.split(',');
        	}
        	
        	if(data.productImg) {
        		data.productImg = data.productImg.split(',');
        	}        	
        	
        	if(data.welfare) {
        		data.welfareArray = data.welfare.split(',');
        	}
        	
        	if(data.environment) {
        		data.officeArray = data.environment.split(',');
        	}
        	
        	for(var i in $scope.webContext.compSizeList) {
        		if($scope.webContext.compSizeList[i].id == data.size)
        			data.sizeStr = $scope.webContext.compSizeList[i].value;
        	}
        	
        	for(var i in $scope.webContext.compPhaseList) {
        		if($scope.webContext.compPhaseList[i].id == data.progress)
        			data.progressStr = $scope.webContext.compPhaseList[i].value;
        	}
        	
			return data;
		}
		
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
