# Netty IM

参考

- https://gitee.com/bluexsx/box-im

### java客户端

[NettyClient.java](netty-client/src/test/java/com/zhengqing/demo/netty/NettyClient.java)

### websocket在线测试

http://docs.wildfirechat.cn/web/wstool/index.html

连接地址

```
ws://127.0.0.1:10081/im
```

#### 登录

```json
{
  "cmd": 0,
  "data": {
    "accessToken": "xxx"
  }
}
```

#### 心跳

```json
{
  "cmd": 1,
  "data": "ping"
}
```

#### 私聊

```json
{
  "cmd": 3,
  "data": {
    "sender": {
      "userId": 2,
      "terminal": 0
    },
    "receiverList": [
      {
        "userId": 1,
        "terminal": 0
      }
    ],
    "isCallbackResult": true,
    "serviceName": "im-client-A",
    "data": {
      "sendId": 2,
      "recvId": 1,
      "content": "hello"
    }
  }
}
```

#### 群聊

```json
{
  "cmd": 4,
  "data": {
    "sender": {
      "userId": 2,
      "terminal": 0
    },
    "receiverList": [
      {
        "userId": 1,
        "terminal": 0
      }
    ],
    "isCallbackResult": true,
    "serviceName": "im-client-A",
    "data": {
      "sendId": 2,
      "recvId": 1,
      "content": "hello"
    }
  }
}
```