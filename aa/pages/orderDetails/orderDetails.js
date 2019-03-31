// pages/orderDetails/orderDetails.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url + "/image/"
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (e) {
    var that = this;
    getApp().isLogin();
    console.log(e)
    wx.request({
      url: getApp().url + '/order/selectOrderDetailsByid',
      data: {
        o: e.oId
      },
      success(res){
        var addTime = new Date(+new Date(res.data.shopOrder.addTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
        that.setData({
          coupons: res.data.coupons ? res.data.coupons : "",
          shopOrder: res.data.shopOrder,
          shopOrderGoodsAndSellerList: res.data.shopOrderGoodsAndSellerList,
          addTime: addTime,
          status: e.status
        })
      }
    })
  },
  /**
   * 支付
   */
  goPay(e) {
    console.log(e)
    wx.request({
      url: getApp().url + '/order/againPay',
      data: {
        id: this.data.shopOrder.id
      },
      success(res) {
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
  closeOrder(e) {
    var that = this;
    wx.request({
      url: getApp().url + '/order/closeOrder',
      data: {
        id: this.data.shopOrder.id
      },
      success(res) {
        if (res.data == 1) {
          wx.navigateBack({
            delta: 1
          })
        }else{
          wx.showToast({
            title: '服务器异常',
            icon: 'loading',
            duration: 1000,
            mask: true
          })
        }
      }
    })
  },
  /**
   * 退款
   */
  orderRefund(e){
    var that = this;
    wx.request({
      url: getApp().url + '/order/orderRefund',
      data: {
        id: this.data.shopOrder.id,
        status: e.currentTarget.dataset.status
      },
      success(res){
        if(res.data.status==200){
          if (e.currentTarget.dataset.status==1){
            wx.showToast({
              title: '退款成功',
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
          } else if (e.currentTarget.dataset.status == 2){
            wx.switchTab({
              url: '/pages/myPage/myPage',
            })
          }
        }
      },fail(){
        wx.showToast({
          title: '服务器异常',
          icon: 'loading',
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
      }
    })
  },
  /**
   * 确认收货
   */
  confirmG(e){
    if (e.detail.value.comment){
    console.log(e)
    wx.request({
      url: getApp().url + '/order/confirmG',
      data:{
        userId: wx.getStorageSync("userid"),
        id: this.data.shopOrder.id,
        comment: e.detail.value.comment
      },success(res){
        if (res.data.status == 200){
          wx.navigateBack({
            delta:1
          })
        } else if (res.data.status == 400){
          wx.showToast({
            title: '请联系客服',
            icon: 'loading',
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
        }
      },fail(){
        wx.showToast({
          title: '服务器异常',
          icon: 'loading',
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
      }
    })
    } else if (!e.detail.value.comment){
      wx.showToast({
        title: '请输入评价',
        icon: 'loading',
        duration: 1000,
        mask: true,
      })
    }
  }
})