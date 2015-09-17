$(function($) {
	var config = {userId: 678};

	//var config = {};
	
	if(_getParams('userId')) config.userId = _getParams('userId');
	
	$('.top-bar-spec').click(function() {
		window.history.back();
	});
	
	/* 基本信息*/
	var getResume = function(data) {
		var _obj = $('.info-base-warp .context');
		_obj.find('.ico_sex').text(data.sex);
		_obj.find('.ico_clock').text(data.age + '岁');
		_obj.find('.ico_book').text(data.education);
		_obj.find('.ico_year').text(data.jobYear + '年');
		_obj.find('.ico_addr').text(data.destination);
		$('.info-item-title .skill').text(data.skills);
		$('.info-item-title .job-num').text('[' + data.code + '号]' );
	};
	
	/* 教育经历*/
	var getEdu = function(data) {
		var _obj = $('.info-detail-warp .edu');
		for (var i in data) {
			var dom = '<div>'+
			   '<span>' + data[i].startTime + '-' + data[i].endTime +'&nbsp;</span>'+
			   '<span>' + data[i].school +'</span>'+
			   '<span>&nbsp;| ' + data[i].specialty +'</span>'+
		   '</div>';			
			_obj.append(dom);
		}
	};
	
	/* 工作经历*/
	var getWork = function(data) {
		var _obj = $('.info-detail-warp .work');
		
		if(!data) {
			_obj.hide().prev('.title').hide();
			return;
		}
		
		for(var i in data) {
			var dom = '<div class="f_b">'+
				'<span>' + data[i].companyName +'</span>' +		
			'</div>'+
			'<div class="f_b">'+
				'<span>' + data[i].startTime + '-' + data[i].endTime +'</span>'+
				'<span>&nbsp;|&nbsp;' + data[i].jobTitle +'</span>'+		
			'</div>'+
			'<div class="mg_b16">'+
				'<p>' + data[i].workContent +'</p>'+
			'</div>'
			_obj.append(dom);
		}
	};
	
	/* 项目经验*/
	var getProject = function(data) {
		var _obj = $('.info-detail-warp .project');
		
		if(!data) {
			_obj.hide().prev('.title').hide();
			return;
		}
		
		for(var i in data) {
			var dom = '<div class="f_b">'+
			'<span>' + data[i].name +'&nbsp;</span>'+
			'<span>' + data[i].startTime + '-' + data[i].endTime +'&nbsp;</span>'+			
		'</div>'+
		'<div class="f_b">'+
			'<span>项目描述</span>'+		
		'</div>'+
		'<div class="mg_b16">'+
			'<p>' + data[i].projectDetails +'</p>'+
		'</div>'	
			_obj.append(dom);
		}
	};
	
	/* 个人总结*/
	var getSummary = function(data) {
		var _obj = $('.info-detail-warp .summary');
		if(!data.details) {
			_obj.hide().prev('.title').hide();
			return;
		}
		_obj.find('p').text(data.details);
	};
	
	/* 求职意向*/
	var getIntention = function(data) {
		var _obj = $('.info-detail-warp .intention');
		
		if(Number(data.expectedSalary) < 1) {
			_obj.find('span').text('期望薪资：' + '面议');
		}else {
			_obj.find('span').text('期望薪资：' + data.expectedSalary + 'k');
		}
	};
	
	/* 公司出价*/
	var getbidList = function(data) {
		var _obj = $('.info-bid-warp .context');
		for(var i in data) {
			var tmp = _formatBidLogs(data[i]);
			var dom = '<div class="bid-list-item">'+
			'<div class="list-state">' + tmp.progress +'</div>'+
			'<div class="list-time"> ' + tmp.bidTime +'</div>'+
			'<div class="list-money c_r"> ' + tmp.bidPrice + 'Kx' + tmp.monthCount +'月</div> &nbsp;'+
			'<div class="list-opt">' + tmp.isOption +'</div>'+
		'</div>'
			_obj.append(dom);
		}
	};
	
	_doPost('/resume/getUserResume', config, function(result) {
		var resume = _formatResume(result.resume);
		var education = result.educations;
		var project = result.projects;
		var work = result.work_experiences;
		getResume(resume);
		getEdu(education);
		getWork(work);
		getProject(project);
		getSummary(resume);
		getIntention(resume);
		$('.viewport').show();
		$('.button').attr('href', '/mobile/placebid?userId=' + 
				resume.userId + '&bidCount=' + resume.bidCount + '&special=' + 
				resume.special + '&specialCount=' + resume.specialCount).show();
		
		if(resume.expectedSalary < 1) {
			$('.button').attr('href', '/mobile/placebid?userId=' + 
					resume.userId + '&bidCount=' + resume.bidCount + '&special=' + 
					resume.special + '&specialCount=' + resume.specialCount).show();
		}else {
			$('.button').attr('href', '/mobile/placebid?userId=' + 
					resume.userId + '&bidCount=' + resume.bidCount + '&special=' + 
					resume.special + '&specialCount=' + resume.specialCount + 
					'&salary=' + resume.expectedSalary).show();
		}
	}, function(err) {
		console.log('queryResumeAll', err);
	});
});

function _getParams(name) {
	var query = window.location.search;
	if (name) {
		var r = new RegExp("[?&]" + name + "=([^&]*)");
		var m = r.exec(query);
		if (m) {
			return m[1];
		} else {
			return null;
		}
	} else {
		return this.parseQueryString(query);
	}	
}

function _doPost(url, params, callback, error) {
	_ajax(url, 'post',callback, error, params);
}

function _doGet(url, callback, error) {
	_ajax(url, 'get', callback, error);
}

function _ajax(_url, _type, _callback, _error, _params) {
	$.ajax({
		url: _url,
		type: _type,
		dataType: 'json',
		data: _params,
		success: function(result) {
			_callback(result);
		},
		error: function(err) {
			_error(err);
		}
	});
}

/* 基本信息格式*/
function _formatResume (item) {
	if(!item) return item;
	var education = item.education;
	var sex = item.sex;
	if (!education) {
		item.education = '不限';
	} else if (education == 1) {
		item.education = '大专';
	} else if (education == 2) {
		item.education = '本科';
	} else if (education == 3) {
		item.education = '硕士';
	} else if (education == 4) {
		item.education = '博士';
	} else {
		item.education = '不限';
	}
	if (!sex) {
		item.sex = '男';
	} else {
		item.sex = '女';
	}
	return item;
}

/* 公司出价表格式*/
function _formatBidLogs(bidLogs) {
	
	if(bidLogs.bidTime == '0') {
		bidLogs.bidTime = '今天';
	}else if(bidLogs.bidTime == '1') {
		bidLogs.bidTime = '昨天';
	}else {
		bidLogs.bidTime = bidLogs.bidTime + '天前';
	}
	
	if(!bidLogs.isOption) {
		bidLogs.isOption = 'X';
	}else {
		bidLogs.isOption = '提供';
	}	
	
	switch (bidLogs.progress) {
	case 0:
		bidLogs.progress = "未融资";
		break;
	case 1:
		bidLogs.progress = "天使轮";
		break;
	case 2:
		bidLogs.progress = "A轮";
		break;
	case 3:
		bidLogs.progress = "B轮";
		break;
	case 4:
		bidLogs.progress = "C轮";
		break;
	case 5:
		bidLogs.progress = "D轮";
		break;
	case 6:
		bidLogs.progress = "上市公司";
		break;
	case 7:
		bidLogs.progress = "不需要融资";
		break;
	default:
		bidLogs.progress = "未融资";
		break;
	}
	return bidLogs;
}
