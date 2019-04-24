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
    wx.hideLoading()
    wx.navigateBack({
      delta: 1,
    })
  },

  /**
   * form提交
   */
  formSubmit: function (e) {
    var that = this;
    if (this.data.detailsPhoto && this.data.detailsPhoto.length > 0){
      wx.showLoading({
        title: "请稍等",
        mask: true
      })
      wx.request({
        url: getApp().url + '/admin/deleteImg',
        data:{sta: this.data.sta},
        success(res){
          that.uploadimg(that.data.detailsPhoto, that.data.sta)
        }
      })
    }else{
      wx.showLoading({
        title: '请上传图片',
        mask: true
      })
      setTimeout(function(){
        wx.hideLoading()
      },1000)
    }
  },
})