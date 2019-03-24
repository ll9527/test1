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
    imgUrls: [
      {
        link: '/pages/test/test',
        url: '/image/14.png'
      }, {
        link: '/pages/test/test',
        url: '/image/14.png'
      }, {
        link: '/pages/test/test',
        url: '/image/14.png'
      }
    ],
    indicatorDots: true,  //小点
    autoplay: true,  //是否自动轮播
    interval: 3000,  //间隔时间
    duration: 1000,  //滑动时间
    // 页面配置
    winWidth: 0,
    winHeight: 0,
    // tab切换  
    currentTab: 0,
    navScrollLeft: 0,
    //置顶初始高度
    topNum : 0,
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
    ]
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
        console.log(res.windowHeight);
      }
    });
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
    console.log(that.data.root);
    console.log(getApp().root)
    that.setData({
      root: getApp().root
    })
    console.log(that.data.root);
    console.log(getApp().root)
    // 获得热门商品
    wx.request({
      url: getApp().url +'/product/selectHotP',
      success: function(res){
        console.log(res)
        that.setData({
          hotList: res.data
        })
      }
    })
  },
  // 获取二级目录下的所有商品函数
  select2PList: function(id,that){
    wx.request({
      url: getApp().url + '/product/selectLevel1P',
      data: {
        classid: id,
        operationCode: 0
      },
      success: function (res) {
        console.log(res)
        that.setData({
          pList: res.data
        })
      }
    })
  },
  /** 
  * 滑动切换tab 
  */
  bindChange: function (e) {
    var that = this
    if (e.detail.current > 4){
      var cur = e.detail.current;
    }else{
      var cur = 4
    }
    that.setData({
      currentTab: e.detail.current,
      navScrollLeft: (cur - 4) * 75
    });
    console.log("e.detail.current:"+e.detail.current)
    if (e.detail.current > 0){
      console.log("that.data.level2List[e.detail.current - 1].classId:" + that.data.level2List[e.detail.current - 1].classId)
      var cid = that.data.level2List[e.detail.current - 1].classId
      that.select2PList(cid, that)
    }
    that.setData({
      currentTab: e.detail.current,
      navScrollLeft: (cur - 4) * 75
    });
    // console.log("that.data.level2List[e.detail.current - 1].classId:" + JSON.parse(JSON.stringify(that.data.level2List[e.detail.current - 1])).classId)
    

    console.log("bindChange:"+this.data.navScrollLeft)
  },
  /** 
   * 点击tab切换 
   */
  swichNav: function (e) {
    console.log(e)
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
    console.log("swichNav:"+this.data.navScrollLeft)
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