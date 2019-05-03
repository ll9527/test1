// pages/admin/goods/cancel.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url + "/image/"
  },
  /**
   * 下架商品
   */
  onDelete: function (e) {
    var that = this
    console.log(e)
    // 获得商品id
    console.log(e.target.dataset.productid)
    wx.request({
      url: getApp().url + '/admin/productDown',
      data: {
        productid: e.target.dataset.productid
      },
      success: function (res) {
        // res.data.status
        if (res.data == 1) {
          wx.request({
            url: getApp().url + '/admin/selectActivityPro',
            data: {
              sta: that.data.sta
            },
            success: function (res) {
              console.log(res.data)
              that.setData({
                productList: res.data
              })
            },
          })
        } else {
          wx.showToast({
            title: '下架失败',
            icon: 'none',
            duration: 1000,
            mask: true
          })
        }
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    that.setData({
      sta: options.sta
    })
    getApp().isLogin();
    //查询通过sellerid查已上架的商品
    wx.request({
      url: getApp().url + '/admin/selectActivityPro',
      data: {
        sta: that.data.sta
      },
      success: function (res) {
        console.log(res.data)
        that.setData({
          productList: res.data
        })
      },
    })
  },

})