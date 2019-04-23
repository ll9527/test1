// pages/storeNewList/storeNewList.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url+ "/image/"
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    getApp().isLogin();
    that.setData({
      sellerid: options.sellerid
    })
    console.log("storeNewList:" + options)
    wx.request({
      url: getApp().url + '/product/selectPdBySellerid',
      data: {
        sellerid: options.sellerid,
        operation: 4
      },
      success: function (res) {
        that.setData({
          productList: res.data[0]
        })
      }
    })
  },

})