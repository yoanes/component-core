var MobileRequest = new Class({
	Extends: Request,
	
	initialize: function(options) {
		options.url += (options.url.contains('?') ? '&' : '?') + 'xrw=xhr';
		this.parent(options);
	}

});