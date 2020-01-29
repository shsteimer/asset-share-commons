document.addEventListener('DOMContentLoaded', function(event) {
console.log('dom content loaded');
    // Set all links that specify a scheme and don't already have a target to open in a new tab
    document.querySelectorAll('a').forEach(function(el) {
        var href = el.getAttribute('href');
        if (href && (href.startsWith('http://') || href.startsWith('https://'))) {
            if (!el.getAttribute('target')) {
                el.setAttribute('target', '_blank');
            }
        }
    });
});


console.log('finished the js');