// pages/searchItems/search2search.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url + "/image/",
    tab: 1
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (res) {
    var that = this
    getApp().isLogin();
    that.setData({
      pname: res.body,
    })
    console.log(res)
    // 综合
    wx.request({
      url: getApp().url +'/product/serchProduct',
      data: {
        pname: res.body,
        operationCode: 1
      },
      success: function(e){
        that.setData({
          productList: e.data
        })
      }
    })
    // 销量排序List
    wx.request({
      url: getApp().url + '/product/serchProduct',
      data: {
        pname: res.body,
        operationCode: 2
      },
      // method: "POST",
      success: function (res) {
        console.log(res)
        that.setData({
          productList2: res.data,
          // url: getApp().url+"/image/"
        })
      }
    }) // 价格排序List
    wx.request({
      url: getApp().url + '/product/serchProduct',
      data: {
        pname: res.body,
        operationCode: 3
      },
      // method: "POST",
      success: function (res) {
        console.log(res)
        that.setData({
          productList3: res.data,
          // url: getApp().url+"/image/"
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
  }
})