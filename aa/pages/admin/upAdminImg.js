// pages/admin/upAdminImg.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  onLoad(op){
    this.setData({
      sta:op.sta,
      count: op.count
    })
    
  },
  /**
   * 获取要上传的产品详情图路径
   */
  seeDetailsPhoto: function () {
    var that = this
    wx.chooseImage({
      count: that.data.count,
      // sizeType: ['original'],
      sourceType: ['album', 'camera'],
      success: function (res) {
        that.setData({
          detailsPhoto: res.tempFilePaths
        })
        console.log(res.tempFilePaths)
      }
    })
  },
  /**
   * 上传文件方法
   */
  uploadimg: function (tempFilePaths, sta) {
    console.log(tempFilePaths)
    for (var i in tempFilePaths) {
      wx.uploadFile({
        url: getApp().url + '/admin/upload',
        filePath: tempFilePaths[i],
        name: 'image',
        header: {
          "content-type": "multipart/form-data"
        },
        formData: {
          sta: sta
        }
      })
    }
  },

  /**
   * form提交
   */
  formSubmit: function (e) {
    wx.request({
      url: getApp().url + '/admin/deleteImg',
      data:{sta: this.data.sta}
    })
    this.uploadimg(this.data.detailsPhoto, this.data.sta)
    wx.navigateBack({
      delta: 1,
    })
  },
})