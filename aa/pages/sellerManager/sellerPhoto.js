// pages/sellerManager/sellerPhoto.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url+"/image/"
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (res) {
    var that = this;
    wx.request({
      url: getApp().url + '/seller/getSImageList',
      data: {
        sellerId: res.sellerid
      },
      success(e){
        that.setData({
          imageList: e.data,
          sellerName: res.sellerName
        })    
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

})