// pages/seller/sellerList.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    getApp().isLogin();
    wx.request({
      url: getApp().url + '/admin/selectAllSeller',
      success: function (e) {
        that.setData({
          selleList: e.data,
        })
        console.log(that.data.selleList)
      }
    })
  },

})