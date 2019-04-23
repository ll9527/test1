// pages/admin/setGG.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
 
  commit(e) {
    if (e.detail.value.point) {
      this.kouDian(e.detail.value.point)
    } else {
      wx.showLoading({
        title: '请输入公告',
      })
      setTimeout(function () {
        wx.hideLoading()
      }, 1000)
    }
  },
  /** 抽点，扣点设置 */
  kouDian(point) {
    wx.request({
      url: getApp().url + '/admin/upAdminP',
      data: {
        password: point
      }, success(res) {
          wx.showLoading({
            title: '请稍等',
          })
          setTimeout(function () {
            wx.hideLoading()
            wx.navigateBack({
              delta: 1
            })
          }, 1000)
        }
    })
  },

})