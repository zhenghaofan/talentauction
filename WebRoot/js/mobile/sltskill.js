$(function($) {
	$('.skill-wrap').show();
	var skillArray = [];
	var skill = LocalStorage.get('skill')?  LocalStorage.get('skill') : [];
	
	if(skill.length) {
		skillArray = skill;
		addSkill();
	}
	
	load();
	
	/* 后退*/
	$('.bar-back').click(function() {
		window.location.href = "/mobile/signup";
	});
	
	/* 获取技能*/
	function load() {
		$.post('/common/getSkillsList', {}, function(res){
			  res = $.parseJSON(res);
			  
			  if( res.code == 200 ) {
				  initEle(res.specialList);
			  }
		});
	};
	
	/* 增加技能*/
	function addSkill() {
		var ele = $('.sign-form .tip');
		var html = [];
		
		ele.empty();
		
		for ( var i in skillArray) {
			html.push('<span class="skill-text">' + skillArray[i] +'</span>');
		}
		
		ele.append(html.join(''));
	}
	
	/* 移除技能*/
	setTimeout(function() {
		$(document).on('click', '.tip .skill-text', function() {
			var str = $(this).text();
			subSkill(str);
			removeCss(str);
		});
	}, 100);
	
	/* 移除选中样式*/
	function removeCss(str) {
		var ele = $('.skill-box li');
		
		ele.each(function(_index) {
			var _that = $(ele[_index]);
			
			if(str ==_that.text()) {
				_that.find('.hook').css({'display': 'none'});
				_that.find('.text').css({'color': '#666'});
			}
		});
	};
	
	/* 移除技能*/
	function subSkill(str) {
		var ele = $('.sign-form .tip');
		var html = [];
		
		for ( var i in skillArray) {
			if(str == skillArray[i]) {
				skillArray.splice(i, 1);
			}
		}
		
		ele.empty();
		
		for ( var i in skillArray) {
			html.push('<span class="skill-text">' + skillArray[i] +'</span>');
		}
		
		if(!skillArray .length) {
			html.push('<span class="text">专业技能限两个</span>');
		}
	
		LocalStorage.set('skill', skillArray);
		ele.append(html.join(''));
	}
	
	/* 添加去重复*/
	function addReject(array, str) {
		for(i in array) {
			if(array[i] == str) return false;
		}
		
		array.push(str);
		
		return true;
	}
	
	/* 初始化元素*/
	function initEle(list) {
		var ele = $('.skill-box');
		var html = [];
		
		for ( var i in list) {
			var subList = list[i].skillName.split('、');
			html.push('<ul class="skill-item">');
			html.push('<li class="skill-title">');
			html.push('<span>' + list[i].name + '</span>');
			html.push('<span class="angle"></span>');
			html.push('</li>');
			
			for ( var j in subList) {
				html.push('<li>');
				html.push('<span class="text">' + subList[j] + '</span>');
				html.push('<span class="hook"></span>');
				html.push('</li>');
			}
			
			html.push('</ul>');
		}
		
		ele.append(html.join(''));
		bind();
	};
	
	
	/* 绑定元素*/
	function bind() {
		$('ul li').on('click', function() {
			var hook = $(this).find('.hook');
			var angle = $(this).find('.skill-title .angle');
			var text = $(this).find('.text');
			
			if(hook.css('display') == 'none') {
				if(skillArray.length > 1) return;
				
				if(addReject(skillArray, text.text())) {
					hook.css({'display': 'inline-block'});
					text.css({'color': '#3a89ce'});
					LocalStorage.set('skill', skillArray);
					addSkill();
				}
				
				if(skillArray.length > 1) window.location.href = "/mobile/signup" + window.location.search;
				
			}else {
				hook.css({'display': 'none'});
				text.css({'color': '#666'});
				subSkill(text.text());
			}
		});
		
		$('.skill-item .skill-title').on('click', function() {
			var angle = $(this).find('.angle');
			
			if(angle.hasClass('down')) {
				angle.removeClass('down');
				$(this).siblings().hide();
			}else {
				angle.addClass('down');
				$(this).siblings().show();
			}
		});		
	}
});



