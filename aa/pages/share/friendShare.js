// pages/friendShare/friendShare.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    navs: [
      // {
    //     name: '眼镜1111111111111111111111111',
    //     imgUrl: '/images/tuan.png',
    //     num: 0,
    //     count: 200
    //   },
    ],
    url: getApp().url + '/image/'
  },
  onLoad(options){
    app.isLogin();
    var that = this;
    wx.request({
      url: getApp().url + '/admin/selectActivityPro',
      data: {
        sta: 1
      },
      success: function (res) {
        that.setData({
          navs: res.data
        })
      },
    })
  },
  receiveBtn: function(event) {
    // console.log(event.currentTarget.dataset.item)
    var item = event.currentTarget.dataset.item
    wx.navigateTo({
      url: '/pages/share/sharePage?item=' + JSON.stringify(item),
    })
  }
})