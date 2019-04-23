// pages/selleProduct/qrCode.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url+'/image/qrCode/'
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    wx.request({
      url: getApp().url + '/order/getQrCode',
      data:{
        sel: wx.getStorageSync("sellerId")
      },success(res){
        that.setData({
          imgName: getApp().url + '/image/qrCode/' + res.data
        })
      }
    })
  },
})