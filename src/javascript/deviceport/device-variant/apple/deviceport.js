/* create the meta tag for viewport */
var meta = document.createElement('meta');
meta.setAttribute('name', 'viewport');
meta.setAttribute('content', 'width=device-width; initial-scale=1.0; maximum-scale=1.0');
document.getElementsByTagName('head')[0].appendChild(meta);

/* hide the address bar */
window.addEventListener('load', function() { window.scroll(0, 1); }, true);