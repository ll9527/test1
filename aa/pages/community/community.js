// pages/search/search.js
Page({
  data: {
    url: getApp().url,
    inputShowed: false,
    inputVal: "",
    //历史记录的开关
    searchRecord: true,
    searchBorder: false,
    // showBady: true,
    // 滑动块的index
    current: 0,
    // 导航栏的index
    currentTab: 0,
    // 商家集合对象
    sellerProList:{},
    // 页面配置
    winWidth: 0,
    winHeight: 0,
    // scroll-into-view
    // listId: "",
    // scrollTop: 0
    // 显示收藏按钮
    showCollect: true,
    //目录分类
    classfiy: [
      // "连锁超市", "便利店", "副食店", "食店", "水果店", "服饰其他", "运动户外",
      // "日用百货", "母婴", "玩具", "鞋包", "配饰", "美妆日化", "数码家电", "汽车用品", "办公文教", "电工安防", "照明电子", "家装家饰", "家纺家饰", "橡塑化工"
      "流行女装",
      "品质男装",
      "鞋靴箱包",
      "手机数码",
      "电脑办公",
      "家用电器",
      "内衣配饰",
      "运动户外",
      "水果生鲜",
      "美妆个护",
      "食品饮料",
      "家具百货",
      "家纺布艺",
      "家居家装",
      "母婴玩具",
      "汽车车品",
      "海外精选",
      ]
  },
  //点击输入框触发的方法
  showInput: function () {
    this.setData({
      inputShowed: true,
      searchRecord: false,
      searchBorder: true,
      // showBady: false
    });
  },
  // 点击输入框 取消 触发的方法
  hideInput: function () {
    this.setData({
      inputVal: "",
      inputShowed: false,
      searchRecord: true,
      searchBorder: false,
      // showBady: true
    });
  },
  // 点击输入框 清空 触发
  clearInput: function () {
    this.setData({
      inputVal: "",
      searchRecord: false
    });
  },
  // 输入框输入 触发
  inputTyping: function (e) {
    this.setData({
      inputVal: e.detail.value,
      searchRecord: true
    });
  },
  /** 
   * 点击tab切换 
   */
  swichNav: function (e) {
    var cur = e.target.dataset.current;
    var that = this;
    this.setData({
      navScrollLeft: (cur - 2) * 85
    })
    if (this.data.currentTab === cur) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
  },
  getLocation: function () {
    var page = this
    wx.getLocation({
      type: 'wgs84',   //默认为 wgs84 返回 gps 坐标，gcj02 返回可用于 wx.openLocation 的坐标 
      success: function (res) {
        // success  
        var longitude = res.longitude
        var latitude = res.latitude
        page.loadCity(longitude, latitude)
      }
    })
  },
  loadCity: function (longitude, latitude) {
    var page = this
    wx.request({
      url: 'https://api.map.baidu.com/geocoder/v2/?ak=SSZu5mwED0o5cGTjC5duXCPFpvTas6XM&location=' + latitude + ',' + longitude + '&output=json',
      data: {},
      header: {
        'Content-Type': 'application/json'
      },
      success: function (res) {
        // success  
        console.log(res);
        var city = res.data.result.addressComponent.city;
        page.setData({ currentCity: city });
        //查热门商品
        wx.request({
          url: getApp().url + '/seller/selectAllSeller',
          data: {
            currentCity: res.data.result.addressComponent.city
          },
          success: function (res) {
            console.log(res)
            page.setData({
              hotList: res.data
            })
            console.log(page.data.hotList)
          }
        })
      },
      fail: function () {
        page.setData({ currentCity: "获取定位失败" });
      },

    })
  },
  onShow(res) {
    var that = this;
    console.log(that.data.root);
    console.log(getApp().root)
    that.setData({
      root: getApp().root
    })
    console.log(that.data.root);
    console.log(getApp().root)
  },
  // 授权函数
  click: function (res) {
    getApp().click(res, this)
  },
  /**
   * 加载后运行
   */
  onLoad: function (e) {
    var that = this;
    // 获取系统信息     
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          winWidth: res.windowWidth,
          winHeight: res.windowHeight
        });
        console.log(res.windowHeight);
      }
    });
    wx.authorize({
      scope: 'scope.userLocation',
      success(res) {
        that.getLocation();
      },
      fail() {
        console.log("shibai")
        wx.authorize({
          scope: 'scope.userLocation',
        })
      }
    })
    // wx.request({
    //   url: getApp().url + '/seller/selectSellerClass',
    //   data: {
    //     sellerClass: "连锁超市"
    //   },
    //   success: function (res) {
    //     console.log(res)
    //     that.setData({
    //       lianSuoList: res.data
    //     })
    //     console.log(that.data.lianSuoList)
    //   }
    // })
    // wx.request({
    //   url: getApp().url + '/seller/selectSellerClass',
    //   data: {
    //     sellerClass: "便利店"
    //   },
    //   success: function (res) {
    //     console.log(res)
    //     that.setData({
    //       bianLiList: res.data
    //     })
    //     console.log(that.data.bianLiList)
    //   }
    // })
    // wx.request({
    //   url: getApp().url + '/seller/selectSellerClass',
    //   data: {
    //     sellerClass: "副食店"
    //   },
    //   success: function (res) {
    //     console.log(res)
    //     that.setData({
    //       fuChiList: res.data
    //     })
    //     console.log(that.data.fuChiList)
    //   }
    // })
    // wx.request({
    //   url: getApp().url + '/seller/selectSellerClass',
    //   data: {
    //     sellerClass: "食店"
    //   },
    //   success: function (res) {
    //     console.log(res)
    //     that.setData({
    //       shiList: res.data
    //     })
    //     console.log(that.data.shiList)
    //   }
    // })
    // wx.request({
    //   url: getApp().url + '/seller/selectSellerClass',
    //   data: {
    //     sellerClass: "水果店"
    //   },
    //   success: function (res) {
    //     console.log(res)
    //     that.setData({
    //       shuiGuoList: res.data
    //     })
    //     console.log(that.data.shuiGuoList)
    //   }
    // })
    // wx.request({
    //   url: getApp().url + '/seller/selectSellerClass',
    //   data: {
    //     sellerClass: "服饰其他"
    //   },
    //   success: function (res) {
    //     console.log(res)
    //     that.setData({
    //       fuShiList: res.data
    //     })
    //     console.log(that.data.fuShiList)
    //   }
    // })
  },
  // 点击搜索后执行
  search: function (res) {
    var that = this
    console.log(res.detail)
    wx.navigateTo({
      url: '/pages/searchItems/search2search?body=' + res.detail.value,
      // success: function(res){
      //   that.setData({
      //     inputShowed: false
      //   })
      // }
    })
  },
  // 点击收藏
  switchCollect:function(e){
    if(this.data.showCollect){ 
      this.setData({
        showCollect:false,
      })
    } else{
      this.setData({
        showCollect: true,
      })
    }
  },
  /**
   * 用商户类型名字查询该类型所有商户
   */
  selectProByClass(sellerClass, that){
    wx.request({
      url: getApp().url + '/seller/selectSellerClass',
      data: {
        sellerClass: sellerClass,
        currentCity: that.data.currentCity
      },
      success: function (res) {
        console.log(res)
        that.data.sellerProList[sellerClass] = res.data
        that.setData({
          sellerProList: that.data.sellerProList
        })
        wx.hideLoading()
        console.log(that.data.sellerProList)
      }, fail() {
        wx.hideLoading({
          success() {
            // that.setData({
            //   isShow: true,
            // })
          }
        })
      }
    })
  },
  /** 
  * 滑动切换tab 
  */
  bindChange: function (e) {
    var cur = e.detail.current;
    var that = this;
    wx.showLoading({
      title: '请稍等',
      mask: true
    })
    that.setData({
      // currentTab: e.detail.current,
      current: e.detail.current,
      navScrollLeft: (cur - 2) * 85
    });
    if (e.detail.current > 0){
      //获取 类型名字
      var sellerClass = that.data.classfiy[e.detail.current - 1]
      that.selectProByClass(sellerClass, that)
    }else{
      wx.hideLoading()
    }
  },
})