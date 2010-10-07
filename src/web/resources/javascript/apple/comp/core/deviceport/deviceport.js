/* create the meta tag for viewport */
var metaViewport = document.createElement('meta');
metaViewport.setAttribute('name', 'viewport');
metaViewport.setAttribute('content', 'width=device-width; initial-scale=1.0; maximum-scale=1.0');
document.getElementsByTagName('head')[0].appendChild(metaViewport);

/* create the meta tag to kill the phone link detection */
var metaFormat = document.createElement('meta');
metaFormat.setAttribute('name', 'format-detection');
metaFormat.setAttribute('content', 'telephone=no');
document.getElementsByTagName('head')[0].appendChild(metaFormat);

/* hide the address bar */
window.addEventListener('load', function() { 
	window.scroll(0, 1); 
	document.getElementsByTagName('body')[0].style['-webkit-text-size-adjust'] = 'none';
}, true);