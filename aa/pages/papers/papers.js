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
    var userData = wx.getStorageSync("userData")
    // console.log( wx.getStorageSync("userData"))
    if(userData != "" && userData.info == 1){
      that.setData({
        isShow: true
      })
    }
    wx.request({
      url: getApp().url + '/admin/adminZImg',
      success(res){
        that.setData({
          imgList: res.data
        })
      }
    })
  },
  del(e){
    var that = this;
    console.log(e)
    var id = e.currentTarget.dataset.id
    var index = e.currentTarget.dataset.index
    wx.request({
      url: getApp().url + '/admin/deleteZImg',
      data: { imgId: id},
      success(res){
        if(res.data == 1){
          that.data.imgList.splice(index,1)
          that.setData({
            imgList: that.data.imgList
          })
        }
      }
    })
  }
})