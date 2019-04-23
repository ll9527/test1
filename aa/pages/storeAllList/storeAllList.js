
Page({

  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url + "/image/",
    tab: 1,
    sellerid: ""
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (res) {
    var that = this
    getApp().isLogin();
    that.setData({
      // pname: res.body,
      sellerid: res.sellerid
    })
    console.log(res)
        wx.request({
       url: getApp().url +'/product/selectPdBySellerid',
      data: {
        sellerid: that.data.sellerid,
        operation: 1
      },
        success: function(res){
        that.setData({
          productList: res.data[0]
        })
      }
    })
  },
  // 选择排序方式
  rank: function (e) {
    var that = this
    console.log(e)
    that.setData({
      tab: e.currentTarget.dataset.tab
    })
    wx.request({
      url: getApp().url +'/product/selectPdBySellerid',
      data: {
        sellerid: that.data.sellerid,
        operation: e.currentTarget.dataset.tab
      },
      success: function(res){
        that.setData({
          productList: res.data[0]
        })
      }
    })
  }
})