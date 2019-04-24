// pages/index/index.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 授权显示开关
    // root: getApp().root,
    //轮播图
    url: getApp().url,
    imgUrls: [],
    indicatorDots: true,  //小点
    autoplay: true,  //是否自动轮播
    interval: 3000,  //间隔时间
    duration: 1000,  //滑动时间
    // 页面配置
    winWidth: 0,
    winHeight: 0,
    // tab切换  
    currentTab: 0,
    // 默认热门按钮变色
    current: 0,
    navScrollLeft: 0,
    //置顶初始高度
    topNum : 0,
    // 是否显示
    isShow: true,
    // 商品所有数据集合，对象
    pList:{},
    // 剩余要显示得商品集合
    allPList:{},
    // 剩余要显示得热门商品
    allHotList:[],
    // 是否显示二级目录
    isShowLevel2: false,
    // 二级目录
    level2List:[
      { classId: 75, className: "女装" },
      { classId: 76, className: "男装" },
      { classId: 77, className: "鞋包" },
      { classId: 78, className: "手机" },
      { classId: 79, className: "电脑" },
      { classId: 80, className: "电器" },
      { classId: 81, className: "内衣" },
      { classId: 82, className: "运动" },
      { classId: 83, className: "水果" },
      { classId: 84, className: "美妆" },
      { classId: 85, className: "食品" },
      { classId: 86, className: "百货" },
      { classId: 87, className: "家纺" },
      { classId: 88, className: "家装" },
      { classId: 89, className: "母婴" },
      { classId: 90, className: "汽车" },
      { classId: 91, className: "海外" }
    ],
    // 二级目录2
    level2List2: [
      { classId: 75, className: "女装" },
      { classId: 76, className: "男装" },
      { classId: 77, className: "鞋包" },
      { classId: 78, className: "手机" },
      { classId: 79, className: "电脑" },
      { classId: 80, className: "电器" },
      { classId: 81, className: "内衣" },
      { classId: 82, className: "运动" },
      // { classId: 83, className: "水果" },
      { classId: 84, className: "美妆" },
      // { classId: 85, className: "食品" },
      // { classId: 86, className: "百货" },
      { classId: 87, className: "家纺" },
      { classId: 88, className: "家装" },
      { classId: 89, className: "母婴" },
      { classId: 90, className: "汽车" },
      // { classId: 91, className: "海外" }
    ]
  },
  /** 跳转证件页面 */
  goPapers(){
    wx.navigateTo({
      url: '/pages/papers/papers',
    })
  },
  // 授权函数
  click: function (res) {
    getApp().click(res, this)
  },
  /**
   * 页面第一次加载触发   
   */
  onLoad: function(res){
    var that = this;
    // 获取系统信息     
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          winWidth: res.windowWidth,
          winHeight: res.windowHeight
        });
        // console.log(res.windowHeight);
      }
    })
    // 是否显示2级目录全部
    wx.request({
      url: getApp().url + '/classfiy/selectLevel2',
      success(res){
        if(res.data == true){
          that.setData({
            isShowLevel2: res.data
          })
        }
        console.log(that.data.isShowLevel2)
      }
    })
    // 获得热门商品
    wx.request({
      url: getApp().url + '/product/selectHotP',
      success: function (res) {
        // console.log(res)
        var plist = res.data.splice(0, 20)
        that.setData({
          hotList: plist,
          allHotList: res.data
        })
        // that.setData({
        //   hotList: res.data
        // })
      }
    })
    // 获取所有的二级目录
    // wx.request({
    //   url: getApp().url +'/classfiy/selectTwo',
    //   success: function(res){
    //     console.log(res)
    //     console.log(res.data)
    //     that.setData({
    //       level2List: res.data
    //     })
    //     console.log(that.data.level2List)
    //   }
    // })
  },
  /**
   * 页面显示/切入前台时触发。
   */
  onShow : function(e){
    var that = this;
    // console.log(that.data.root);
    // console.log(getApp().root)
    that.setData({
      root: getApp().root
    })
    // 获取广告图
    wx.request({
      url: getApp().url + '/admin/adminGGImg',
      success(res) {
        that.setData({
          imgUrls: res.data
        })
      }
    })
    // console.log(that.data.root);
    // console.log(getApp().root)
    wx.request({
      url: getApp().url + '/admin/selectGG',
      success: function (res) {
        that.setData({
          gonggao: res.data
        })
      }
    })
  },
  // 获取二级目录下的所有商品函数
  select2PList: function(id,that){
    if (that.data.pList["id" + id] == null){
      wx.request({
        url: getApp().url + '/product/selectLevel1P',
        data: {
          classid: id,
          operationCode: 0
        },
        success: function (res) {
          // console.log(res)
          that.data.pList["id" + id] = res.data.splice(0, 20)
          that.data.allPList["id" + id] = res.data
          that.setData({
            pList: that.data.pList
          })
          // that.data.pList["id"+id] = res.data
          // that.setData({
          //   pList: that.data.pList,
          // })
          setTimeout(function () {
            wx.hideLoading({
              success(){
                that.setData({
                  isShow: true,
                })
              }
            })
          }, 700)
        },fail(){
          wx.hideLoading({
            success() {
              that.setData({
                isShow: true,
              })
            }
          })
        }
      })
    }else{
      setTimeout(function () {
        wx.hideLoading({
          success() {
            that.setData({
              isShow: true,
            })
          }
        })
      }, 500)
    }  
  },
  /** 热门商品触底触发方法 */
  hotReachBottom() {
    var that = this;
    if (that.data.allHotList.length <= 0) {
      return;
    }
    wx.showLoading({
      title: '加载中',
      mask: true,
    })
    var plist = that.data.allHotList.splice(0, 20);
    that.data.hotList = that.data.hotList.concat(plist)
    that.setData({
      hotList: that.data.hotList
    })
    setTimeout(function () {
    wx.hideLoading()
    }, 400)
  },
  /** 其他商品触底触发方法 */
  pReachBottom() {
    var that = this;
    var index = that.data.current - 1
    var id = that.data.level2List[index].classId
    if (that.data.allPList["id"+id].length <= 0) {
      return;
    }
    wx.showLoading({
      title: '加载中',
      mask: true,
    })
    var plist = that.data.allPList["id" + id].splice(0, 20);
    that.data.pList["id" + id] = that.data.pList["id" + id].concat(plist)
    that.setData({
      pList: that.data.pList
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 400)
  },
  /** 
  * 滑动切换tab 
  */
  bindChange: function (e) {
    var that = this
    that.setData({
      isShow: false,
    })
    wx.showLoading({
      title: '请稍等',
      mask: true
    })
    if (e.detail.current > 4){
      var cur = e.detail.current;
    }else{
      var cur = 4
    }
    that.setData({
      // currentTab: e.detail.current,
      current: e.detail.current,
      navScrollLeft: (cur - 4) * 75
    });
    // console.log("e.detail.current:"+e.detail.current)
    if (e.detail.current > 0){
      // console.log("that.data.level2List[e.detail.current - 1].classId:" + that.data.level2List[e.detail.current - 1].classId)
      var cid = that.data.level2List[e.detail.current - 1].classId
      that.select2PList(cid, that)
    }else{
      wx.hideLoading()
      that.setData({
        isShow: true,
      })
    }
    // that.setData({
    //   currentTab: e.detail.current,
    //   navScrollLeft: (cur - 4) * 75
    // });
    // console.log("that.data.level2List[e.detail.current - 1].classId:" + JSON.parse(JSON.stringify(that.data.level2List[e.detail.current - 1])).classId)
    // console.log("bindChange:"+this.data.navScrollLeft)
  },
  /** 
   * 点击tab切换 
   */
  swichNav: function (e) {
    // console.log(e)
    // wx.showLoading({
    //   title: '请稍等',
    //   mask: true
    // })
    var cur = e.target.dataset.current;
    var that = this;
    if(cur > 4){
      this.setData({
        navScrollLeft: (cur - 4) * 75
      })
    }
    if (this.data.currentTab === cur) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
    // console.log("swichNav:"+this.data.navScrollLeft)
  },
  /**
   * 跳转超市区事件
   */
  toSupermarket : function(){
    wx.navigateTo({
      url: "/pages/test/test"
    })
  },
  /**
   * 跳转品牌区事件
   */
  toBrand : function(){
    wx.navigateTo({
      url: "/pages/test/test"
    })
  },
  /**
   * 跳转详情页事件
   */
  goDetails : function(){
    wx.navigateTo({
      url: "/pages/test/test"
    })
  },
  /**
   * 跳转评论页事件
   */
  goComment : function(){
    wx.navigateTo({
      url: "/pages/test/test"
    })
  },
  /**
   * 跳到顶部
   */
  goTop : function(e){
    this.setData({
      topNum : 0
    })
  },
})