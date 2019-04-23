// pages/myWallet/myWallet.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(e){
    getApp().isLogin();
    var that = this
    wx.getStorage({
      key: 'userData',
      success: function(res) {
        console.log(res)
        wx.request({
          url: getApp().url +'/user/selectMoney',
          data: {
            userid: res.data.userId
          },
          success:function(res){
            that.setData({
              money: res.data
            })
          }
        })
      },
      fail: function(){
        wx.navigateTo({
          url: '/pages/login/login',
        })
      }
    })
  },
  /**
   * 提现功能
   */
  toCash: function(e){
    // wx.navigateTo({
    //   url: '/pages/msg/msg_success'
    // })
  }
})