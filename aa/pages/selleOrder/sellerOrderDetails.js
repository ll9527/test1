// pages/selleOrder/sellerOrderDetails.js
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
      success(res) {
        var addTime = new Date(+new Date(res.data.shopOrder.addTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
        that.setData({
          coupons: res.data.coupons ? res.data.coupons : "",
          shopOrder: res.data.shopOrder,
          shopOrderGoodsAndSellerList: res.data.shopOrderGoodsAndSellerList,
          addTime: addTime
        })
      }
    })
  },
  /**
   * 发货
   */
  sendGoods(e){
    var that = this;
    if (e.detail.value.sn){
      wx.request({
        url: getApp().url + '/order/sendGoods',
        data:{
          o: that.data.shopOrder.id,
          sn: e.detail.value.sn
        },
        success(res){
          if(res.data.status == 200){
            wx.showToast({
              title: '发货成功',
              icon: 'loading',
              duration: 500,
              mask: true,
              success() {
                setTimeout(function () {
                  wx.navigateBack({
                    delta: 1
                  })
                }, 500)
              }
            })
          }else{
            wx.showToast({
              title: '请稍后再试',
              icon: 'loading',
              duration: 500,
              mask: true,
              success() {
                setTimeout(function () {
                  wx.navigateBack({
                    delta: 1
                  })
                }, 500)
              }
            })
          }
        },
        fail(){
          wx.showToast({
            title: '服务器异常',
            icon: 'loading',
            duration: 500,
            mask: true,
            success() {
              setTimeout(function () {
                wx.switchTab({
                  url: '/pages/myPage/myPage',
                })
              }, 500)
            }
          })
        }
      })
    } else if (!e.detail.value.sn){
      wx.showToast({
        title: '请输入快递单号',
        icon: 'loading',
        duration: 500,
        mask: true
      })
    }
  },
  /** 退款 */
  tuiKuan(e) {
    var that = this;
    wx.request({
      url: getApp().url + '/order/refund',
      data: {
        o: that.data.shopOrder.id,
        s: wx.getStorageSync("sellerId")
      },
      success(res) {
        if (res.data.status == 200) {
          wx.showToast({
            title: '退款成功',
            icon: 'loading',
            duration: 500,
            mask: true,
            success() {
              setTimeout(function () {
                wx.navigateBack({
                  delta: 1
                })
              }, 500)
            }
          })
        } else {
          wx.showToast({
            title: '请稍后再试',
            icon: 'loading',
            duration: 500,
            mask: true,
            success() {
              setTimeout(function () {
                wx.navigateBack({
                  delta: 1
                })
              }, 500)
            }
          })
        }
      },
      fail() {
        wx.showToast({
          title: '服务器异常',
          icon: 'loading',
          duration: 500,
          mask: true,
          success() {
            setTimeout(function () {
              wx.switchTab({
                url: '/pages/myPage/myPage',
              })
            }, 500)
          }
        })
      }
    })
  },
})