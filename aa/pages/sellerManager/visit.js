// pages/sellerManager/visit.js
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
    var that = this;
    wx.request({
      url: getApp().url + '/seller/getVisitNum',
      data:{
        sellerid: wx.getStorageSync("sellerId")
      },success(res){
        that.setData({
          visitNum: res.data
        })
      }
    })
  },

})