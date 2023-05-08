Page({
  data: ({
   openid: ""
  }),
  // 点击事件
  clickEvent(event) {
    // 订阅消息模板
   wx.requestSubscribeMessage({
     tmplIds: ['xxx'], // TODO 填写自己申请的模板ID
     success (res) { 
       console.log("订阅消息模板结果：",res)
     }
   })
   // 获取用户openid
   this.getOpenid();
  },
  // 获取用户openid
  getOpenid() {
   let that = this;
   wx.cloud.callFunction({
    name: "getopenid",
    success(res) {
     console.log('云开发请求的openid：', res.result.openid)
     that.setData({
      openid: res.result.openid
     })
    },
    fail(err) {
     console.log('云开发请求失败：', err)
    }
   })
  }
})