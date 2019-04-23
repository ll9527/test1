// pages/papers/papers.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url+"/image/admin/"
  },
  onLoad(op){
    var that = this;
    wx.request({
      url: getApp().url + '/admin/adminZImg',
      success(res){
        that.setData({
          imgList: res.data
        })
      }
    })
  },

})