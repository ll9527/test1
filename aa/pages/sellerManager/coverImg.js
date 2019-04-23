
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  /**
   * 获取要上传的产品详情图路径
   */
  seeDetailsPhoto: function () {
    var that = this
    wx.chooseImage({
      count: 9,
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
  uploadimg: function (tempFilePaths, sta, sid) {
    console.log(tempFilePaths)
    for (var i in tempFilePaths) {
      wx.uploadFile({
        url: getApp().url + '/seller/addCover',
        filePath: tempFilePaths[i],
        name: 'image',
        header: {
          "content-type": "multipart/form-data"
        },
        formData: {
          sta: sta,
          sid: sid
        }
      })
    }
  },

  /**
   * form提交
   */
  formSubmit: function (e) {
    var sid = wx.getStorageSync("sellerId")
    this.uploadimg(this.data.detailsPhoto, 2, sid)
    wx.navigateBack({
      delta: 1,
    })
  },
})