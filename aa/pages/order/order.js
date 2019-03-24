
Page({

  /**
   * 页面的初始数据
   */
  data: {
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
  },
  onShow(res){
    var that = this;
    //查询用户的地址，采用最新一条
    wx.request({
      url: getApp().url +'/user/selectUserNewAddress',
      data: {
        userid: wx.getStorageSync("userid")
      },
      success(res){
        that.setData({
          // user: res.data.user,
          addressList: res.data.addressList
        })
      }
    })
  },
  onLoad(res){
    console.log(res)
    console.log(res.aaa)
    console.log(JSON.parse(res.product))
    var that = this;
    that.setData({
      count: res.count,
      versionName: res.versionName,
      version1Name: res.version1Name,
      userid: res.userid,
      product: JSON.parse(res.product)[0],
      price: res.price,
      allPrice: res.count * res.price,
      skuId: res.skuId
    })
    // 获取商品得优惠卷和运费
    wx.request({
      url: getApp().url+ '/user/selectFreightAndCoupons',
      data:{
        userid: res.userid,
        productid: JSON.parse(res.product)[0].id
      },
      success(res){
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
  pay(res){
    var that = this;
    wx.login({
      success(res) {
        wx.showLoading({
          title: '请稍等',
          mask: true
        })
        if (res.code) {
          wx.request({
            url: getApp().url + '/order/ordPay',
            data: {
              jscode: res.code,
              userid: that.data.userid,
              skuId: that.data.skuId,
              productid: that.data.product.id,
              num: that.data.count,
              version: that.data.versionName +","+ that.data.version1Name,
              // 1是自提
              isPick: that.data.isPack ? 1 : 2,
              addressid: that.data.addressList[0].id
            },
            success(res){
              wx.hideLoading();
                if (res.data != "") {
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
                } else {
                  console.log("后台没有返回数据")
                  console.log(res.data)
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
  ziTi(res){
    var that = this;
    if(this.data.isPack){
      // 不是自提
      this.setData({
        isPack: false,
        settlement: that.data.settlement + that.data.freight
      })
    }else{
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
  goSetAddr(res){
    wx.navigateTo({
      url: '/pages/setAddress/setAddress',
    })
  }
})