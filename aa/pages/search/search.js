// pages/search/search.js
Page({
  data: {


    inputShowed: false,
    inputVal: "",
    //历史记录的开关
    searchRecord: true,
    searchBorder:false,
    showBady:true,
    currentTab: 0,
    // 页面配置
    winWidth: 0,
    winHeight: 0,
    // scroll-into-view
    listId:"",
    scrollTop:0
  },
  onShow(res){
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
  // 点击搜索后执行
  search: function(res){
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
  //点击输入框触发的方法
  showInput: function () {
    this.setData({
      inputShowed: true,
      searchRecord: false,
      searchBorder: true,
      showBady: false
    });
  },
  // 点击输入框 取消 触发的方法
  hideInput: function () {
    this.setData({
      inputVal: "",
      inputShowed: false,
      searchRecord: true,
      searchBorder: false,
      showBady: true
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
  // 点击左方菜单变色及实现点击菜单页面滑动
  swichNav: function(e){
    // console.log(e.currentTarget.dataset.current)
    this.setData({
      currentTab: e.currentTarget.dataset.current,
      listId: "list" + e.currentTarget.dataset.current
    })
    // e.currentTarget.dataset.current
  },
  // 右方的菜单实现互动。用bindscroll绑定实现
  rigConScr: function (e) {
    var that = this;
    // console.log(e);
    var query = wx.createSelectorQuery();
    query.selectAll('.body1_right_box').boundingClientRect(function (res) {
      res.forEach(function (item) {
        // 向下滑，当盒子顶部距离屏幕顶部的高度大于0的时候，选中该盒子对应的左方菜单项
        // 向上滑，当下一个盒子顶部距离高度小于200的时候，选中该盒子对应的左方菜单项
        if (item.top > 0 && item.top < 200) {
          var curId = item.id.substr(4);
          if (e.detail.scrollTop > 4800) {
            that.setData({
              currentTab: "16"
            });
          } else {
            if (curId < 5) {
              that.setData({
                currentTab: curId,
                scrollTop: '0'
              })
            } else {
              that.setData({
                currentTab: curId,
                scrollTop: [(curId - 5)] * 54
              })
            }
          }
        }
      })
    }).exec();
  },
  scroll:function(e){
    // if(){}
  },
  onLoad: function (e) {
    var that = this;
    // 获取系统信息     
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          winWidth: res.windowWidth,
          winHeight: (res.windowHeight-44),
          url: getApp().url
        });
      }
    });
    wx.request({
      url: getApp().url+'/classfiy/selectLevel', 
      success(res) {
        // console.log(res)
        that.setData({
          categoryList: res.data,
          url: getApp().url
        })
        // console.log(res.data)
      }
    });
  },
})