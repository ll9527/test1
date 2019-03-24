// pages/checkPage/checkPage.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    
  },
  /**
  * 页面加载时触发
  */
  onLoad(e) {
    var that = this;
    getApp().isLogin();
  },
  /**
   * 跳转支付页面
   */
  goReceivingAddress: function(e){
    wx.navigateTo({
      url: '/pages/receivingAddress/receivingAddress',
    })
  }
})