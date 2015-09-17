if (typeof define !== 'function') {
	var define = require('amdefine')(module);
}

define(function() {
	var url = {
		/*
		 * return an string from query by the arg name.
		 *
		 * if arg name is not specialed, it will return an object parsed from query.
		 */
		get: function(name) {
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
		},

		/*
		 * return an object parsed from query.
		 */
		parseQueryString: function(query) {
			if (query[0] === '?')
				query = query.substring(1);
			var args = {};
			var fragments = query.split('&');
			var fragment, pos, i, length = fragments.length;
			for (i = 0; i < length; i++) {
				fragment = fragments[i];
				pos = fragment.indexOf('=');
				if (pos > 0) {
					args[fragment.substring(0, pos)] = fragment.substring(pos + 1);
				} else {
					args[fragment] = null;
				}
			}
			return args;
		},

		/*
		 * return an new url joined all args.
		 */
		join: function(url, args) {
			if (!args) {
				return url;
			}
			var array = [],
				k, v;
			for (k in args) {
				v = args[k];
				if (typeof v === 'function' || typeof v === 'undefined') {
					continue;
				}
				if (v === null) {
					array.push(k + '=');
				} else {
					array.push(k + '=' + encodeURIComponent(v));
				}
			}
			if (array.length === 0) {
				return url;
			}
			var joined = url;
			joined += (joined.indexOf('?') >= 0) ? '&' : '?';
			joined += array.join('&');
			return joined;
		},
		
		replace: function(url, args) {
			if(!args) {
				return url;
			}
			
			var k, v;
			for( k in args ) {
				v = args[k];
				if (typeof v === 'function' || typeof v === 'undefined') {
					continue;
				}
				
				if( url.indexOf(k) == -1 ) {
					if( args[k] == 0 || !!args[k] ) {
						url += url.indexOf('?') >= 0 ? '&' : '?';
						url += k + '=' + encodeURIComponent(args[k]);
					}
				} else {
					var r = new RegExp(k + "=([^&]*)");
					var m = r.exec(url);
					
					if( !m ) continue;
					var str = m[0];
					var _string = (args[k] == 0 || !!args[k]) ? k + '=' + encodeURIComponent(args[k]) : '';
					url = url.replace(str, _string);
					if( !_string ) url = url.replace("&&", "&");
				}
			}
			var _len = url.length;
			
			url = url.lastIndexOf('?') == _len - 1 ? url.substring(0, _len - 1) : url;
			url = url.lastIndexOf('&') == _len - 1 ? url.substring(0, _len - 1) : url;
			
			return url;
		}
		
	};

	return url;
});