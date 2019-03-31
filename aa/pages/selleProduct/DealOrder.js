Page({
  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url + "/image/",
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
      url: getApp().url + '/order/selectAllByUserOrSeller',
      data: {
        status: 3,
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