Page({
  openSuccess: function () {
    wx.showLoading({
      title: '请稍等',
      mask: true
    })
    wx.getStorage({
      key: 'userid',
      success: function (res) {
        wx.request({
          url: getApp().url + '/user/isVip',
          data: {
            userid: res.data
          },
          success(res) {
            if (res.data) {
              wx.hideLoading();
              // 服务器没返回东西
              wx.showToast({
                title: '服务器异常',
                icon: 'loading',
                duration: 1000,
                mask: true
              })
            } else if (res.data == 1) {
              wx.hideLoading();
              // 是会员
              wx.showToast({
                title: '已是会员',
                icon: 'loading',
                duration: 1000,
                mask: true
              })
            } else if (res.data == 0) {
              // 不是会员
              wx.login({
                success(res) {
                  if (res.code) {
                    wx.request({
                      url: getApp().url + '/order/vipPay',
                      data: {
                        jscode: res.code,
                        userid: wx.getStorageSync("userid")
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
                  } else {
                    console.log('登录失败！' + res.errMsg)
                  }
                }
              })
            }
          }
        })
      },
    })
    wx.hideLoading()
  },
    openFail: function () {
        wx.navigateTo({
          url: 'msg_fail'
        })
    },
    openMyPage: function(e){
        wx.switchTab({
          url: '/pages/myPage/myPage',
        })
    }
});