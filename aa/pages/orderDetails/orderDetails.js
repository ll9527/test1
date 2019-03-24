// pages/orderDetails/orderDetails.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (e) {
    getApp().isLogin();
    // wx.request({
    //   url: '',
    // })
  },
  /**
   * 确认发货
   */
  deliverGoods: function(e){
    // wx.request({
    //   url: '',
    // })
  }
})