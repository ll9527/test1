// pages/searchItems/searchItems.js
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
  onLoad: function (e) {
    var that = this;
    getApp().isLogin();
    console.log(e);

    // 综合排序List
    wx.request({
      url: getApp().url+'/product/selectLevel2P',
      data: {
        classid: e.search,
        operationCode: 1
      },
      // method: "POST",
      success:function(res){
        console.log(res)
        that.setData({
          productList: res.data,
          // url: getApp().url+"/image/"
        })
      }
    })
    // 销量排序List
    wx.request({
      url: getApp().url + '/product/selectLevel2P',
      data: {
        classid: e.search,
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
      url: getApp().url + '/product/selectLevel2P',
      data: {
        classid: e.search,
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
  rank: function(e){
    var that = this
    console.log(e)
    that.setData({
      tab: e.currentTarget.dataset.tab
    })
  }
})