// pages/sharePage/sharePage.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    item: {},
    url: getApp().url + '/image/',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    app.isLogin();
    var that = this;
    // console.log(JSON.parse(options.item))
    this.setData({
      item: JSON.parse(options.item)
    })
    wx.request({
      url: getApp().url + '/user/shareScore',
      data: { uuu: wx.getStorageSync("userid") },
      success(res) {
        that.setData({
          shareNum: res.data
        })
      }
    })
  },
  /** 用户领奖 */
  receiveBtn(options){
    console.log(options.currentTarget.dataset.productid)
    wx.redirectTo({
      url: '/pages/admin/adminProductDetails/adminProductDetails?productid='+ options.currentTarget.dataset.productid +'&sta=1',
    })
  },
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