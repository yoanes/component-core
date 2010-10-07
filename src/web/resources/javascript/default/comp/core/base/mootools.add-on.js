Element.implement({
	clearStyles: function(styles){
		for (var style in styles) this.setStyle(style, '');
		return this;
	}
});