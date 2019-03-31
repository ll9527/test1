// pages/orderDelivery/allOrder.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    o: 0,
    statusList:[
      "待支付", "待发货", "待收货", "交易成功", "买家退货中", "退款成功", "退款成功"
    ],
    url: getApp().url + "/image/"
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    this.data.o = options.o
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    if(this.data.o==0){
      console.log(0)
    } else if (this.data.o == 1){
      var that = this;
      getApp().isLogin();
      wx.request({
        url: getApp().url + '/order/selectAllOrder',
        data: {
          userId: wx.getStorageSync("userid")
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
  },

})