'use strict';

let term;
let terminalIO;

let socket;

function getLatestLogs () {

    let selectedHostValue = $("#selected-host option:selected").text();
    let selectedProgramValue = $("#selected-program option:selected").text();

    if (selectedHostValue !== 'None' && selectedProgramValue !== 'None') {

        let webSocketProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        let webSocketLogUrl = webSocketProtocol + '//' + window.location.host + '/ws-log';

        socket = new WebSocket(webSocketLogUrl);

        socket.onopen = function () {

            let logSubscriptionRequest = {
                host: selectedHostValue,
                program: selectedProgramValue,
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
                    terminalIO.println(logDate.toLocaleString() + ' ' + logDate.getMilliseconds()
                        + 'ms' + ' [' + item.host + '] [' + item.program + '] ' + item.message);
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

function logParameterChanged() {
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