// pages/admin/adminPage.js
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
  },
  /** 生命周期函数 */
  onShow(){
    var that = this;
    wx.request({
      url: getApp().url + '/admin/selectAllUserOrVip',
      success: function (res) {
        that.setData({
          data: res.data
        })
      }
    })
  },
})