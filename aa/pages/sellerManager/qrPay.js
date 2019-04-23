// pages/sellerManager/qrPay.js
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
    var that = this;
    // scene 需要使用 decodeURIComponent 才能获取到生成二维码时传入的 scene
    // 商家id
    const scene = decodeURIComponent(options.scene)
    console.log("scene:" + scene)
    this.setData({
      sellerId : scene
    })
    console.log(options)
    wx.getStorage({
      key: 'userid',
      success: function (res) {
        wx.request({
          url: getApp().url + '/user/isUser',
          data: {
            userId: res.data
          },
          success(e) {
            if (e.data != 1) {
              try {
                wx.clearStorageSync()
                that.setData({
                  isShow: false
                })
              } catch (e) {
                // console.log(e)
                wx.reLaunch({
                  url: '/pages/login/login',
                })
              }
            } else if (e.data == 1) {
              that.setData({
                isShow: true
              })
              wx.getStorage({
                key: 'userData',
                success: function (e) {
                  getApp().userData = e.data
                },
                fail: function (e) {
                  // console.log(e.data==null)
                  wx.reLaunch({
                    url: '/pages/login/login',
                  })
                }
              })
            }
          }
        })
      },
      fail() {
        that.setData({
          isShow: false
        })
      }
    })
  },
  /** 跳转登陆页面 */
  toLogin(){
    wx.reLaunch({
      url: '/pages/login/login',
    })
  },
  /** 跳转首页 */
  goIndex(){
    wx.reLaunch({
      url: '/pages/index/index',
    })
  },
  /** 支付 */
  pay(e){
    wx.showLoading({
      title: '请稍等',
      mask: true
    })
    var that = this;
    if (e.detail.value.price){
      wx.login({
        success(res) {
          if (res.code) {
            wx.request({
              url: getApp().url + '/order/qrPay',
              data: {
                jscode: res.code,
                userid: wx.getStorageSync("userid"),
                sel: that.data.sellerId,
                mon: e.detail.value.price
              },
              success: function (res) {
                if (res.data != "") {
                  wx.hideLoading()
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
                        duration: 1000,
                        mask: true,
                        success(res) {
                          setTimeout(function () {
                            wx.switchTab({
                              url: '/pages/myPage/myPage',
                            })
                          }, 1000);
                        }
                      })
                    },
                    fail(res) {
                      console.log(res)
                    }
                  })
                  console.log(res.data)
                } else {
                  console.log("后台没有返回数据")
                  console.log(res.data)
                }
              },
              fail: function (res) {
                console.log("调用失败")
              }
            })
            console.log(res.code)
            wx.hideLoading()
          } else {
            console.log('登录失败！' + res.errMsg)
          }
        }
      })
    }else{
      wx.showLoading({
        title: '请输入金额',
        mask:true
      })
      setTimeout(function () {
        wx.hideLoading()
      }, 1000)
    }
  }
})