if (typeof define !== 'function') {
	var define = require('amdefine')(module);
}

define(function() {
	var cookie = {
			
		set: function(name, value, flag) {
			var Days = 30;
			var exp = new Date();
			exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
			if( flag == true ) {
				document.cookie = name + "=" + window.escape(value) ;
				return;
			}
			document.cookie = name + "=" + window.escape(value) + ";expires=" + exp.toGMTString();
		},

		get: function(name) {
			var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
			if (arr !== null) {
				return window.unescape(arr[2]);
			}
			return null;
		},

		del: function(name) {
			var exp = new Date();
			exp.setTime(exp.getTime() - 1);
			var cval = cookie.get(name);
			if (cval !== null) {
				document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
			}
		}
	};

	return cookie;
});