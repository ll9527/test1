// pages/admin/order/order.js
Page({
  data: {
    url: getApp().url + "/image/",
    isPack: false, // 是否自提
    count: "", // 商品的件数
    versionName: "",// 型号名字
    version1Name: "",// 型号（颜色）
    userid: "",// 用户id
    product: "",// 商品
    allPrice: "",// 商品总金额
    freight: 0,// 运费
    coupons: 0,// 优惠券
    settlement: 0,// 合计金额
    isGroup: 0,// 是否团购订单 1代表是
    groupOid: ""//团购订单id
  },
  onShow(res) {
    var that = this;
    //查询用户的地址，采用最新一条
    wx.request({
      url: getApp().url + '/user/selectUserNewAddress',
      data: {
        userid: wx.getStorageSync("userid")
      },
      success(res) {
        that.setData({
          // user: res.data.user,
          addressList: res.data.addressList
        })
      }
    })
  },
  onLoad(res) {
    console.log(res)
    console.log(res.aaa)
    console.log(JSON.parse(res.product))
    var that = this;
    if (res.isGroup == 1) {
      this.data.isGroup = 1
    } else {
      this.data.isGroup = 0
    }
    that.setData({
      count: res.count,
      versionName: res.versionName,
      version1Name: res.version1Name,
      userid: res.userid,
      product: JSON.parse(res.product)[0],
      price: res.price,
      allPrice: res.count * res.price,
      skuId: res.skuId,
      groupOid: res.groupOid ? res.groupOid : -1
    })
    // 获取商品得优惠卷和运费
    wx.request({
      url: getApp().url + '/user/selectFreightAndCoupons',
      data: {
        userid: res.userid,
        productid: JSON.parse(res.product)[0].id
      },
      success(res) {
        that.data.freight = res.data.freight
        that.data.coupons = res.data.couponsMoney
        that.data.settlement = that.data.allPrice + that.data.freight - that.data.coupons
        that.setData({
          coupons: that.data.coupons,
          freight: that.data.freight,
          settlement: that.data.settlement
        })
      }
    })
  },
  /**
   * 创建订单，并且调用支付接口
   */
  pay(res) {
    var that = this;
    wx.login({
      success(res) {
        wx.showLoading({
          title: '请稍等',
          mask: true
        })
        if (that.data.addressList.length == 0) {
          wx.hideLoading();
          return wx.showToast({
            title: '请修改地址',
            icon: 'loading',
            duration: 1000,
            mask: true
          })
        }
        if (res.code) {
          wx.request({
            url: getApp().url + '/order/ordPayAdminPro',
            data: {
              jscode: res.code,
              userid: that.data.userid,
              skuId: that.data.skuId,
              productid: that.data.product.id,
              num: that.data.count,
              version: that.data.versionName + "," + that.data.version1Name,
              // 1是自提
              isPick: that.data.isPack ? 1 : 2,
              addressid: that.data.addressList[0].id,
              isGroup: that.data.isGroup,
              groupOid: that.data.groupOid
            },
            success(res) {
              wx.hideLoading();
              if (res.data != "") {
                if(res.data.status == 300){
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
                        return;
                      }, 500);
                    }
                  })
                } else if (res.data.status == 404) {
                  wx.showToast({
                    title: '已领取过',
                    icon: 'loading',
                    duration: 2000,
                    mask: true
                  })
                  return;
                } else if(res.data.status == 200){
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
                      wx.redirectTo({
                        url: '/pages/orderDelivery/orderDelivery',
                      })
                    }
                  })
                  console.log(res.data)
                }
              } else {
                console.log("后台没有返回数据")
                wx.showToast({
                  title: '请联系客服',
                  icon: 'loading',
                  duration: 2000,
                  mask: true,
                  success() {
                    setTimeout(function () {
                      wx.navigateBack({
                        delta: 2
                      })
                    }, 2000)
                  }
                })
              }
            },
            fail: function (res) {
              wx.hideLoading();
              wx.showToast({
                title: '服务器异常',
                icon: 'loading',
                duration: 1000,
                mask: true,
              })
            }
          })
          console.log(res.code)
        }
      }
    })
    wx.hideLoading()
  },
  /**
   * 是否自提
   */
  ziTi(res) {
    var that = this;
    if (this.data.isPack) {
      // 不是自提
      this.setData({
        isPack: false,
        settlement: that.data.settlement + that.data.freight
      })
    } else {
      // 自提
      this.setData({
        isPack: true,
        settlement: that.data.settlement - that.data.freight
      })
    }
  },
  /**
   * 更改地址
   */
  goSetAddr(res) {
    wx.navigateTo({
      url: '/pages/setAddress/setAddress',
    })
  }
})