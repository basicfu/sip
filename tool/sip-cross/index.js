(function (win) {
    if (!document.getElementById('sip-cross')) {
        return;
    }
    var requestMap = {};

    var id = 'sip-cross-dom';
    var dataKey = 'data-';
    var sendIndex = 'send-index';
    var receiveIndex = 'receive-index';
    var sendEvent = 'sip-send-event';
    var receiveEvent = 'sip-receive-event';
    var event = document.createEvent('Event');
    event.initEvent(sendEvent, true, true);

    function customEvent(data) {
        if (typeof data === 'object') {
            var div = document.getElementById(id);
            var newIndex = parseInt(div.getAttribute(sendIndex) || 0) + 1;
            div.setAttribute(sendIndex, newIndex);
            div.setAttribute(dataKey + newIndex, JSON.stringify(data));
            requestMap[newIndex] = {
                id: newIndex,
                success: function (res) {
                    if (typeof data.success === 'function') {
                        data.success(res);
                    }
                },
                error: function (res) {
                    if (typeof data.error === 'function') {
                        data.error(res)
                    }
                }
            };
            div.dispatchEvent(event);
        }
    }

    var div = document.getElementById(id);
    div.addEventListener(receiveEvent, function () {
        var index = div.getAttribute(receiveIndex);
        var data = JSON.parse(div.getAttribute(dataKey + index));
        if (data.status === 200) {
            requestMap[index].success(data);
        } else {
            requestMap[index].error(data);
        }
    });
    win.crossRequest = customEvent;
    if (typeof define == 'function' && define.amd) {
        define('crossRequest', [], function () {
            return customEvent;
        });
    }
})(window);

