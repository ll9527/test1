// pages/sellerManager/sellerShuoMingShu.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url+"/image/"
  },
  // 点击同意
  pass(res){
    wx.redirectTo({
      url: '/pages/sellerManager/register',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 2000,
      mask: true
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },
})