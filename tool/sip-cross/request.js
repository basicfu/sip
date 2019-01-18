function formUrlencode(data) {
    if (!data || typeof data !== 'object') return ''
    return Object.keys(data).map(function (key) {
        return encodeURIComponent(key) + '=' + encodeURIComponent(data[key]);
    }).join('&')
}

function handleHeader(headers) {
    if (!headers) return;
    if (typeof headers === 'object') {
        return headers;
    }
    var newHeaders = {}, headers = headers.split(/[\r\n]/).forEach(function (header) {
        var index = header.indexOf(":");
        var name = header.substr(0, index);
        var value = header.substr(index + 2);
        if (name) {
            newHeaders[name] = value;
        }
    });
    return newHeaders;
}

function injectJs(path) {
    var s = document.createElement('script');
    s.src = chrome.extension.getURL(path);
    s.onload = function () {
        this.remove();
    };
    (document.head || document.documentElement).appendChild(s);
}
injectJs('index.js');

function sendAjax(req, successFn, errorFn) {
    var xhr = new XMLHttpRequest();
    xhr.timeout = req.timeout || 5000;
    req.method = req.method || 'GET';
    req.async = req.async || true;
    req.headers = req.headers || {};
    var contentType = req.headers['Content-Type'] || 'application/x-www-form-urlencoded';
    req.headers['Content-Type'] = contentType.toLowerCase();
    var method = req.method.toLowerCase();
    if (method === 'post' || method === 'put' || method === 'patch') {
        if (contentType === 'application/x-www-form-urlencoded') {
            req.data = formUrlencode(req.data);
        } else if (contentType === 'multipart/form-data') {
            delete req.headers['Content-Type'];
            var formDatas = new FormData();
            if (req.data) {
                for (var name in req.data) {
                    formDatas.append(name, req.data[name]);
                }
            }
            if (req.files) {
                for (var name in req.files) {
                    var files = document.getElementById(req.files[name]).files;
                    if (files.length > 0) {
                        formDatas.append(name, files[0]);
                    }
                }
            }
            req.data = formDatas;
        } else if (contentType === 'application/json') {
            req.data = JSON.stringify(req.data);
        } else {
            //二进制请求
            if (req.file) {
                req.data = document.getElementById(req.file).files[0];
            }
        }
    }
    xhr.open(req.method, req.url, req.async);
    for (var name in req.headers) {
        xhr.setRequestHeader(name, req.headers[name]);
    }
    var response = {};
    xhr.onload = function (e) {
        var headers = handleHeader(xhr.getAllResponseHeaders());
        response = {
            headers: headers,
            status: xhr.status,
            statusText: xhr.statusText,
            body: xhr.responseText
        };
        if (xhr.status === 200) {
            successFn(response);
        } else {
            errorFn(response);
        }
    };
    xhr.ontimeout = function (e) {
        errorFn({
            body: 'Error:Request timeout that the time is ' + xhr.timeout
        })
    };
    xhr.onerror = function (e) {
        errorFn({
            body: xhr.statusText
        })
    };
    xhr.upload.onprogress = function (e) {
    };
    try {
        xhr.send(req.data);
    } catch (error) {
        errorFn({
            body: error.message
        })
    }
}
function createNode(tagName, attributes, parentNode) {
    options = attributes || {};
    tagName = tagName || 'div';
    var dom = document.createElement(tagName);
    for (var attr in attributes) {
        if (attr === 'id') dom.id = options[attr];
        else dom.setAttribute(attr, options[attr]);
    }
    if (parentNode) parentNode.appendChild(dom);
    return dom;
}

var id = 'sip-cross-dom';
var dataKey = 'data-';
var sendIndex = 'send-index';
var receiveIndex = 'receive-index';
var sendEvent = 'sip-send-event';
var receiveEvent = 'sip-receive-event';
var div = createNode('div', {id: id, style: 'display:block'}, document.getElementsByTagName('body')[0]);
var event = document.createEvent('Event');
event.initEvent(receiveEvent, true, true);
function responseCallback(res,index) {
    div.setAttribute(receiveIndex, index);
    div.setAttribute(dataKey + index, JSON.stringify(res));
    div.dispatchEvent(event);
}
div.addEventListener(sendEvent, function () {
    var index=div.getAttribute(sendIndex);
    var req = div.getAttribute(dataKey + index);
    sendAjax(JSON.parse(req),function (res) {
        responseCallback(res, index);
    }, function (err) {
        responseCallback(err, index);
    })
});

