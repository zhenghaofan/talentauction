;(function($) {
	
	var T = function () {};
	
	T.alert = function( text ) {
		if( !text ) return;
		
		var handle;
		
		_init();
		
		function _init() {
			_clear();
			var html = _createHtml(text);
			$(document.body).append(html);
			_setStyle();
			_bind();
		}
		
		function _bind() {
			$(".alert-dialog").on('tap', function() {
				_clear();
			});
		}
		
		function _setStyle() {
			var screen = _getScreen();
			var textDom = $(".alert-content");
			var _w = textDom.width();
			var _h = textDom.height();
			var _top = Number((screen.h - _h)/2) - 30;
			var _left = Number( (screen.w - _w)/2 )
			var w = screen.w  * 3/4 <  _w ? screen.w * 3/4 : _w;
			w -= 30;
			textDom.css({
				top: _top + 'px',
				left: _left+ 'px',
				width: w+ 'px'
			});
		}
		
		function _createHtml(text) {
			var html = [];
			html.push('<div class="alert-dialog">');
			html.push('<div class="alert-content">');
			html.push(text);
			html.push('</div></div>');
			return html.join('');
		}
		
		function _clear() {
			var d = $(".alert-dialog");
			d.off('tap');
			d.remove();
		}
		
		function _getScreen() {
			var h = $(window).height();
			var w = $(window).width();
			
			return {w: w, h: h}
		}
		
		
	};
	
	T.alertClose = function() {
		var d = $(".alert-dialog");
		d.off('tap');
		d.remove();
	};
	
	T.loading = function( state ) {
		var type = 'open';
		if( !!state ) {
			type = 'close';
		}
		
		function _createHtml(text) {
			var html = [];
			html.push('<div class="alert-dialog">');
			html.push('<div class="alert-content">');
			html.push(text);
			html.push('</div></div>');
			return html.join('');
		}
		
		
		function _getScreen() {
			var h = $(window).height();
			var w = $(window).width();
			
			return {w: w, h: h}
		}
		
	};
	
	window.T = T;
	
})($);
