$(function($) {
	var userId = _getParams('userId');
	var isOption = 0;
	var bidCount = _getParams('bidCount');
	var special = _getParams('special');
	var specialCount = _getParams('specialCount');
	var salary = _getParams('salary');
	
	$('.top-bar-spec').click(function() {
		window.history.back();
	});	
	
	if(salary) {
		$('#minBidPrice').val(Number(salary) - 2);
		$('#maxBidPrice').val(Number(salary) + 2);
	}
	
	$('.info-status-warp .radio').click(function(_event) {
		if($(this).hasClass('disabled')) {
			$(this).removeClass("disabled").siblings('.radio').addClass("disabled");
			if($(this).hasClass('opt_0')) {
				isOption = 0;
			}else {
				isOption = 1;
			}
		}
	});
	
	$('.button').click(function() {
		_formatAuction();
	});	
	
	var jump = function() {
		var userId = _getParams('userId');
		var time = 3;
		
		setInterval(function() {
			if(time == 0) window.location.href =  '/mobile/placedetail?userId=' + userId;
			$('i').text(time--);
		}, 1000);
		
		$('a').attr('href', '/mobile/placedetail?userId=' + userId);
	}
	
	var _formatAuction = function() {
		var minBidPrice = $('#minBidPrice').val();
		var maxBidPrice = $('#maxBidPrice').val();
		var workTitle = $('#workTitle').val();
		var reg = /^[1-9]{1}[0-9]{0,2}([.]{1}[0-9]{1,2})?$/;
		
		if(!reg.test(minBidPrice) || minBidPrice > 50) {
			T.alert('最低薪资不太合理，不大于50K！');
			$('#minBidPrice').focus().val('');
			return;
		}else if(!reg.test(maxBidPrice) || maxBidPrice > 50) {
			T.alert('最高薪资不太合理，不大于50K！');
			$('#maxBidPrice').focus().val('');
			return;
		}else if(!maxBidPrice > minBidPrice) {
			T.alert('最高薪资不太合理，不大于最低薪资！');
			$('#maxBidPrice').focus().val('');
			return;
		}else if( !workTitle || workTitle == '') {
			T.alert('请正确输入职位！');
			$('#workTitle').val('');
			return;
		} 
		
		var config = {
				'userId': userId,
				'special': special,
				'specialCount': specialCount,
				'bidCount': bidCount,
				'minBidPrice': minBidPrice,
				'maxBidPrice': maxBidPrice,
				'isOption': isOption,
				'workTitle': workTitle			
		}
		
		_doPost('/company/companyAuction', config, function(result) {
			if(result.code == 200) jump();
		}, function(err) {
			console.log('companyAuction', err);
		});
	};
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