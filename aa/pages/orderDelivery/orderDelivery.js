
Page({

  /**
   * 页面的初始数据
   */
  data: {
   url: getApp().url+"/image/",
  },
  /**
 * 生命周期函数--监听页面加载
 */
  onLoad(options){

  },
  onShow: function (options) {
    var that = this;
    getApp().isLogin();
    wx.request({
      url: getApp().url + '/order/selectAllByUserOrSeller',
      data: {
        status: 0,
        userId: wx.getStorageSync("userid")
      },
      success(res){
        for (var i in res.data){
          res.data[i].shopOrder.addTime = new Date(+new Date(res.data[i].shopOrder.addTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
        }
        that.setData({
          list: res.data
        })
      }
    })
  },
  /**
   * 支付
   */
  goPay(e){
    console.log(e)
    wx.request({
      url: getApp().url + '/order/againPay',
      data: {
        id: e.currentTarget.dataset.oid
      },
      success(res){
        wx.requestPayment({
            timeStamp: res.data.timeStamp,
            nonceStr: res.data.nonceStr,
            package: res.data.package,
            paySign: res.data.paySign,
            signType: "MD5",
            success(res) {
              wx.showToast({
                title: '支付成功',
                icon: 'success',
                duration: 500,
                mask: true,
                success(res) {
                  setTimeout(function () {
                    wx.switchTab({
                      url: '/pages/myPage/myPage',
                    })
                  }, 500);
                }
              })
            },
            fail(res) {
              console.log(res)
              // wx.redirectTo({
              //   url: '/pages/orderDelivery/orderDelivery',
              // })
            }
          })
      }
    })
  },
  /**
   * 关闭订单
   */
  closeOrder(e){
    var that = this;
    wx.request({
      url: getApp().url + '/order/closeOrder',
      data: {
        id: e.currentTarget.dataset.oid
      },
      success(res){
        if(res.data==1){
          that.data.list.splice(e.currentTarget.dataset.index,1)
          that.setData({
            list: that.data.list
          })
        }
      }
    })
  }
})