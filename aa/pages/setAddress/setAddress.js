// pages/setAddress/setAddress.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    region: ['广东省', '湛江市', '赤坎区']
  },
  /**
  * 生命周期函数--监听页面加载
  */
  onLoad: function (options) {
    getApp().isLogin();
  },
  /**
   * 取消返回
   */
  cancel:function(e){
    wx.navigateBack({
      delta: 2
    })
  },
  /**
   * 提交表单
   */
  submit: function(e){
    // var address = ""
    var that = this
    var userid;
    console.log(e);
    // 空=false
    if(!e.detail.value.uAddress){
      console.log("空")
      wx.showToast({
        title: '请输入地址',
        icon: 'loading',
        duration: 1000,
        mask: true
      })
      return
    }
    if (!e.detail.value.uname) {
      console.log("空")
      wx.showToast({
        title: '请输入收货人',
        icon: 'loading',
        duration: 1000,
        mask: true
      })
      return
    }
    if (!e.detail.value.uphone) {
      console.log("空")
      wx.showToast({
        title: '请输入手机',
        icon: 'loading',
        duration: 1000,
        mask: true
      })
      return
    }
    for (var i in that.data.region){
      that.data.region[i]
    }
        wx.getStorage({
          key: 'userData',
          success: function(res) {
            console.log(res.data.userId)
            wx.request({
              url: getApp().url + '/user/insertAddress',
              data: {
                userId: res.data.userId,
                address: that.data.region.join("")+e.detail.value.uAddress,
                // 这个是电话
                tel: Number(e.detail.value.uphone),
                userName: e.detail.value.uname
              },
              success:function(res){
                if(res.data.info==1){
                  
                  wx.navigateBack({
                    delta: 1
                  })
                }else{
                  wx.showToast({
                    title: '保存失败',
                    icon: 'loading',
                    duration: 1000,
                    mask: true
                  })
                }
              }
            })
          },
        })
  },
  /**
   * 省市区
   */
  bindRegionChange: function (e) {
    this.setData({
      region: e.detail.value
    })
  }
})