import ElementUI from 'element-ui';

const WS_API = 'ws://100.64.0.14:8080'
function initWebSocket(pid, onmessage) {
    // console.log(e)
    const wsUri = WS_API +'/websocket/Entry/' + pid;
    console.log('wsUri', wsUri)
    this.socket = new WebSocket(wsUri)//这里面的this都指向vue
    this.socket.onerror = webSocketOnError;
    this.socket.onmessage = onmessage; // 接收到消息后调用方法处理
    this.socket.onclose = closeWebsocket;
}
function webSocketOnError(e) {
    ElementUI.Notification({
        title: '',
        message: "WebSocket连接发生错误" + e,
        type: 'error',
        duration: 0,
    });
}
function webSocketOnMessage(e) {
    const data = JSON.parse(e.data);
    console.log('webSocketOnMessage', data)
    if (data.msgType === "INFO") {
        ElementUI.Notification({
            title: '',
            message: data.msg,
            type: 'success',
            duration: 3000,
        });

    } else if (data.msgType === "ERROR") {
        ElementUI.Notification({
            title: '',
            message: data.msg,
            type: 'error',
            duration: 0,
        });
    }
}
// 关闭websocket
// function closeWebsocket() {
//     console.log('连接已关闭...')
// }
function closeWebsocket() {
    this.socket.close() // 关闭 websocket
    this.socket.onclose = function (e) {
        console.log(e)//监听关闭事件
        console.log('关闭')
    }
}
function webSocketSend(agentData) {
    this.socket.send(agentData);
}
export default {
    initWebSocket, closeWebsocket
}
