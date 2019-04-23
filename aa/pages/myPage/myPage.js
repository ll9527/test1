// pages/myPage/myPage.js

var app = getApp();

Page({
  data:{

  },
  /**
   * 页面加载时触发
   */
  onLoad: function(e) {
    var that = this;
    // getApp().isLogin();
    // 获取系统信息     
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          winWidth: res.windowWidth,
          winHeight: res.windowHeight
        });
      }
    });
    // wx.getStorage({
    //   key: 'userData',
    //   success: function(res) {
    //     console.log(res)
    //     that.setData({
    //       userData: res.data
    //     })
    //   },
    // })
  },
  signOut: function(res){
    // wx.removeStorageSync("userData")
    // wx.removeStorageSync("sellerId")
    try {
      wx.clearStorageSync()
    } catch (e) {
      console.log(e)
    }
    wx.switchTab({
      url: '/pages/index/index',
    })
  },
  // 授权函数
  click: function (res) {
    getApp().click(res, this)
  },
  /**
   * 页面每次显示时触发
   */
  onShow: function(e){
    getApp().isLogin();
    var that = this;
    console.log(that.data.root);
    console.log(getApp().root)
    that.setData({
      root: getApp().root
    })
    console.log(that.data.root);
    console.log(getApp().root)
    wx.getStorage({
      key: 'userData',
      success: function (e) {
        that.setData({
          userData: e.data
        })
        wx.request({
          url: getApp().url + '/user/isVip',
          data: {
            userid: e.data.userId
          },success(res){
            that.setData({
              isVip: res.data
            })
          }
        })
        wx.request({
          url: getApp().url + '/order/getUserOrderNum',
          data: {
            uuuuu: e.data.userId
          }, success(res) {
            that.setData({
              orderNum: res.data
            })
          }
        })
        wx.request({
          url: getApp().url + '/user/selectMark',
          data: {
            userid: e.data.userId
          },
          success: function (res) {
            console.log(res.data)
            that.setData({
              username: res.data.username,
              mark: res.data.mark
            })
          }
        })
        wx.request({
          url: getApp().url + '/user/selectScore',
          data: {
            userid: e.data.userId
          },
          success: function(res){
            that.setData({
              score: res.data
            })
          }
        })
        // 用用户id查该用户的已付款拼团订单
        wx.request({
          url: getApp().url + '/order/selectGroupByU',
          data: {
            u: e.data.userId
          },
          success: function (res) {
            that.setData({
              orderList: res.data
            })
          }
        })
        wx.request({
          url: getApp().url +  '/user/jiangjin',
          success(res){
            that.setData({
              jiangjin: res.data
            })
          }
        })
      },
      fail: function(){
        wx.reLaunch({
          url: '/pages/login/login',
        })
      }
    })   
  },
  /** 打开扫一扫 */
  qr(){
    wx.scanCode({
      success(res) {
        console.log(res)
        wx.navigateTo({
          url: "/" + res.path,
        })
      }
    })
  },
  // 跳转商家后台
  goSellerManager(res){
    wx.getStorage({
      key: 'userid',
      success: function(e) {
        wx.request({
          url: getApp().url +'/seller/isSeller',
          data: {
            userid: e.data
          },
          success(res){
            if (res.data.isSeller != 1) {
              // 如果不是商户
              wx.request({
                url: getApp().url + '/user/checkIsApply',
                data: {
                  userid: e.data
                },
                success: function (res) {
                  if (res.data == 1) {   
                    // 如果申请没批准                
                    wx.showToast({
                      title: '等待批准',
                      icon: 'loading',
                      duration: 1000,
                      mask: true
                    })
                  } else if (res.data == -1){
                    // 没申请或者资料不足
                    wx.navigateTo({
                      url: '/pages/sellerManager/sellerShuoMingShu',
                    })
                  }
                },
                fail(res){
                  wx.showToast({
                    title: '服务器异常',
                    icon: 'loading',
                    duration: 1000,
                    mask: true
                  })
                }
              })
            } else {
              // 如果是商户
              wx.setStorage({
                key: 'sellerId',
                data: res.data.sellerId,
                success(res){
                  wx.navigateTo({
                    url: '/pages/sellerManager/sellerManager',
                  })
                }
              })
            }
          },
          fail(res){
            wx.showToast({
              title: '服务器异常',
              icon: 'loading',
              duration: 1000,
              mask: true
            })
          }
        })
      },
      fail: function(res){
        wx.navigateTo({
          url: '/pages/login/login',
        })
      }
    })
  },
  // 升级店长
  toDianZhang: function () {
    wx.navigateTo({
      url: '/pages/upgradeStoreManager/msg',
    })
  },
  // 线下开店
  toKaiDian : function(){
    wx.navigateTo({
      // url: '/pages/sellerManager/register',
      url: '/pages/sellerManager/shuoMingShu',
    })
  },
  // 我的团购页面
  toTuangou : function(){
    wx.navigateTo({
      url: '/pages/myGroup/myGroup',
    })
  },
  // 去业绩积分
  toYeji: function () {
    // wx.navigateTo({
    //   url: '',
    // })
  },
  // 跳转查看全部订单页面
  selectAllList : function(){
    wx.navigateTo({
      url: '/pages/orderDelivery/allOrder?o=1',
    })
  },
  // 跳转订单待付款
  dFukuan : function(){
    wx.navigateTo({
      url: '/pages/orderDelivery/orderDelivery',
    })
  },
  // 跳转订单待发货
  dFahuo: function () {
    wx.navigateTo({
      url: '/pages/orderDelivery/orderWaitSendGoods',
    })
  },
  // 跳转订单待收货
  dShouhuo: function () {
    wx.navigateTo({
      url: '/pages/orderTakeDelivery/orderTakeDelivery',
    })
  },
  // 跳转订单待评价
  dPingjia: function () {
    wx.navigateTo({
      url: '/pages/orderDelivery/allOrder?o=0',
    })
  },
  // 跳转售后页面
  shouHou: function () {
    wx.navigateTo({
      url: '/pages/myAfterSale/myAfterSale',
    })
  },
})