// pages/selleProduct/DealProduct.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url + "/image/",
    statusList: [
      "待支付", "待发货", "待收货", "交易成功", "买家退货中", "退款成功", "退款成功"
    ],
  },
  /**
 * 生命周期函数--监听页面加载
 */
  onLoad(options) {

  },
  onShow: function (options) {
    var that = this;
    getApp().isLogin();
    wx.request({
      url: getApp().url + '/order/selectAllOrder',
      data: {
        sellerId: wx.getStorageSync("sellerId")
      },
      success(res) {
        for (var i in res.data) {
          res.data[i].shopOrder.addTime = new Date(+new Date(res.data[i].shopOrder.addTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
        }
        that.setData({
          list: res.data
        })
      }
    })
  }
})