// pages/wa/wa.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgUrl: getApp().url + '/image/admin/timg.jpg',
    url: getApp().url + '/image/'
  },

  // 后期更改成分享不同好友，需要更改url，原本url  /pages/share/treasure/treasure"
  pageShare: function (event){
    var item = event.currentTarget.dataset.item
    wx.navigateTo({
      url: '/pages/share/sharePage?item=' + JSON.stringify(item),
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    getApp().isLogin();
    wx.request({
      url: getApp().url + '/admin/selectActivityPro',
      data: {
        sta: 2
      },
      success: function (res) {
        that.setData({
          navs: res.data
        })
      },
    })
  },

  /**
   * 用户点击右上角分享
   */
  // 分享触发函数
  onShareAppMessage(res) {
    var that = this;
    // console.log(that.data.imgName)
    wx.showShareMenu({
      withShareTicket: true
    })
    var path1 = '/pages/index/index?refereesId=' + wx.getStorageSync("userid")
    wx.request({
      url: getApp().url + '/user/share',
      data: {
        uwea: wx.getStorageSync("userid")
      }
    })
    return {
      title: '共店商城',
      path: path1,
      // imageUrl: getApp().url + "/image/shareCover.jpg"
      // imageUrl: getApp().url + "/image/admin/" + that.data.imgName
    }
  }
  
})