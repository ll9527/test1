
Page({
  /**
   * 页面的初始数据
   */
  data: {

  },
  /**
 * 生命周期函数--监听页面加载
 */
  onLoad: function (options) {
    getApp().isLogin();
  },
  onShow(options){
    var that = this;
    wx.request({
      url: getApp().url + '/order/getSOrderNum',
      data: {sssssss: wx.getStorageSync("sellerId")},
      success(res){
        that.setData({
          orderNum: res.data
        })
      }
    })
  },
})