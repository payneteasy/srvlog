'use strict';

let term;
let terminalIO;

let socket;


function getSelectedValues(selectId) {

    let selectedElements = $(selectId).find('.multi-select-selected').find(".multi-select-option-text");
    let result = [];

    for (let element of selectedElements) {
      result.push(element.innerText);
    }

    return result;
}

function getLatestLogs () {

    let selectedHosts = getSelectedValues('#selected-hosts');
    let selectedPrograms = getSelectedValues('#selected-programs');

    if (selectedHosts.length > 0 && selectedPrograms.length > 0) {

        let webSocketProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        let webSocketLogUrl = webSocketProtocol + '//' + window.location.host + applicationContextPath + '/ws-log';

        socket = new WebSocket(webSocketLogUrl);

        socket.onopen = function () {

            let logSubscriptionRequest = {
                hosts: selectedHosts,
                programs: selectedPrograms,
                subscriptionState: 'INITIAL'
            };

            this.send(JSON.stringify(logSubscriptionRequest));
        };

        socket.onclose = function (event) {

            if (event.wasClean) {
                terminalIO.println('WEBSOCKET: Connection was closed');
            } else {
                terminalIO.println('WEBSOCKET: Connection was closed with unsuccessful code ' +
                    '[' + event.code + '] and reason [' + event.reason + ']');
            }
        };

        socket.onmessage = function (event) {

            let logSubscriptionResponse = JSON.parse(event.data);

            if (logSubscriptionResponse.success) {

                logSubscriptionResponse.logDataList.forEach(function (item, index, array) {

                    let logDate = new Date(item.date);

                    if (isMultilineMessage(item.message)) {
                        printMultilineMessage(logDate, item, terminalIO);
                    } else {
                        terminalIO.println(logDate.toLocaleString() + ' ' + logDate.getMilliseconds()
                                            + 'ms' + ' [' + item.host + '] [' + item.program + '] ' + item.message);
                    }

                });

            } else {
                terminalIO.println('WEBSOCKET: Log subscription error message: [' + logSubscriptionResponse.errorMessage + ']');
            }
        };

        socket.onerror = function (error) {
            terminalIO.println('WEBSOCKET: Error has occurred')
        }
    }
}

function isMultilineMessage(text) {
    return text.indexOf('\n') > -1;
}

function printMultilineMessage(logDate, item, terminalIO) {

    let lines = item.message.split(/\r?\n|\r|\n/g);

    lines.forEach(function (line, index, array) {
        if (index == 0) {
            terminalIO.println(logDate.toLocaleString() + ' ' + logDate.getMilliseconds() + 'ms' +
             ' [' + item.host + '] [' + item.program + '] ' + line);
        } else {
            terminalIO.println(line);
        }
    });
}

function handleMultiSelectAction() {
    if (socket !== undefined) {
        socket.close();
    }
    setupHterm();
    getLatestLogs();
}

function setupHterm() {

    term = new hterm.Terminal();

    term.onTerminalReady = function() {
        terminalIO = this.io.push();
        function printPrompt() {
            terminalIO.print(
                '\x1b[38:2:51:105:232mh' +
                '\x1b[38:2:213:15:37mt' +
                '\x1b[38:2:238:178:17me' +
                '\x1b[38:2:51:105:232mr' +
                '\x1b[38:2:0:153:37mm' +
                '\x1b[38:2:213:15:37m>' +
                '\x1b[0m ');
        }

        terminalIO.onVTKeystroke = (string) => {
            switch (string) {
                case '\r':
                    terminalIO.println('');
                    printPrompt();
                    break;
                case '\x7f':
                    // \x08 = backspace, \x1b[K = 'Erase in line'.
                    terminalIO.print('\x08\x1b[K');
                    break;
                default:
                    terminalIO.print(string);
                    break;
            }
        };
        terminalIO.sendString = terminalIO.print;
    };
    term.decorate(document.querySelector('#terminal'));

    // Useful for console debugging.
    window.term_ = term;
}

window.onload = async function() {
    await lib.init();
    setupHterm();
};