/* create the meta tag for viewport */
var metaViewport = document.createElement('meta');
metaViewport.setAttribute('name', 'viewport');
metaViewport.setAttribute('content', 'width=480; initial-scale=0.7; maximum-scale=1.0');
document.getElementsByTagName('head')[0].appendChild(metaViewport);