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
    wx.request({
      url: getApp().url + '/order/selectAllByUserOrSeller',
      data: {
        status: 1,
        sellerId: -1
      },
      success(res) {
        that.setData({
          waitSendNum: res.data.length ? res.data.length : 0
        })
      }
    })
    wx.request({
      url: getApp().url + '/order/selectAllOrder',
      data: {
        sellerId: -1
      },
      success(res) {
        that.setData({
          allOrderNum: res.data.length ? res.data.length : 0
        })
      }
    })
  },
})