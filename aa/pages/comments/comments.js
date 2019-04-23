// pages/comments/comments.js
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
      url: getApp().url + '/product/selectCommentsByPid',
      data:{
        pId: options.pId
      },success(res){
        that.setData({
          list: res.data
        })
      }
    })
  },

})