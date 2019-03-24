
Page({

  /**
   * 页面的初始数据
   */
  data: {
    items: [
      { name: '依依官方旗舰店', title: '新款 高腰毛呢格子百褶裙 ins超火的半身裙子冬裙', num: 1, price: 118.2, checked: true, stock: 10, totalPrice: 118.2 },
      { name: '哈哈旗舰店', title: '新款 高腰毛呢格子百褶裙 ins超火的半身裙子冬裙', num: 1, price: 118.9, checked: true, stock: 10, totalPrice: 118.9 },
      { name: '哈哈旗舰店', title: '新款 高腰毛呢格子百褶裙 ins超火的半身裙子冬裙', num: 1, price: 118.9, checked: true, stock: 10, totalPrice: 118.9 },
      { name: '哈哈旗舰店', title: '新款 高腰毛呢格子百褶裙 ins超火的半身裙子冬裙', num: 1, price: 118.9, checked: true, stock: 10, totalPrice: 118.9 },
      { name: '哈哈旗舰店', title: '新款 高腰毛呢格子百褶裙 ins超火的半身裙子冬裙', num: 1, price: 118.9, checked: true, stock: 10, totalPrice: 118.9 },
    ],
    pageTotalPrice: 0,
    // 收藏的tab开关
    showCollect: "commodity",
    url: getApp().url+"/image/",
    // 用户id
    userid: "",
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("onLoad")
    var that = this
    getApp().isLogin();
    console.log(options)
    // this.allPrice();
    this.setData({
      userid: options.userid,
      pageTotalPrice: this.data.pageTotalPrice
    });
    // console.log(this.data.pageTotalPrice);
  },
  onShow: function(res){
    var that = this
    console.log("onShow")
    // 获取已收藏得商品数据
    wx.request({
      url: getApp().url + '/user/selectCollectDetail',
      data: {
        userid: that.data.userid
      },
      success: function (res) {
        that.setData({
          pList: res.data
        })
      }
    })
  },
  /**
   *  点击选中复选框
   */
  selectCheckbox: function (e) {
    // console.log(e);
    var index = e.currentTarget.dataset.index;
    var item = this.data.items[index];
    if (item.checked) {
      item.checked = false;
      this.data.pageTotalPrice -= item.totalPrice;
    } else {
      item.checked = true;
      this.data.pageTotalPrice += item.totalPrice;
    }
    this.setData({
      items: this.data.items,
      pageTotalPrice: Math.floor(this.data.pageTotalPrice * 10) / 10
    })
  },
  /**
   * 点击右符号 增 数量
   */
  add: function (e) {
    // console.log(e);
    var index = e.currentTarget.dataset.index;
    var item = this.data.items[index];
    // 旧商品总价价格
    // var oldPrice = item.totalPrice;
    if (item.num < item.stock) {
      item.num += 1;
      // Number(item.num)
    } else {
      item.num = item.stock;
    }
    // 新商品总价价格
    item.totalPrice = Math.floor(item.price * item.num * 10) / 10;
    // this.data.pageTotalPrice = this.data.pageTotalPrice + item.totalPrice - oldPrice
    this.allPrice();
    this.setData({
      items: this.data.items,
      pageTotalPrice: this.data.pageTotalPrice
    })
    // console.log(this.data.items);
  },

  /**
  * 页面总价钱
  */
  allPrice: function () {
    var pageTotalPrice = 0;
    var items = this.data.items;
    for (var i in items) {
      if (items[i].checked) {
        pageTotalPrice += items[i].totalPrice;
      }
      // console.log(pageTotalPrice);
    }
    this.data.pageTotalPrice = Math.floor(pageTotalPrice * 10) / 10;
  },

  /**
   * 点击右符号 减 数量
   */
  reduce: function (e) {
    // console.log(e);
    var index = e.currentTarget.dataset.index;
    var item = this.data.items[index];
    if (item.num > 1) {
      item.num -= 1;
    } else {
      item.num = 1;
    }
    item.totalPrice = Math.floor(item.price * item.num * 10) / 10;
    this.allPrice();
    this.setData({
      items: this.data.items,
      pageTotalPrice: this.data.pageTotalPrice
    })
  },
  /**
   * 删除购物车item
   */
  del: function (e) {
    var items = this.data.items;
    var index = 0;
    for (var i = 0; i < items.length; i++) {
      if (items[i].checked) {
        items.splice(i, 1);
        i--;
        // console.log(items[i]);
      }
    }
    this.allPrice();
    this.setData({
      items: this.data.items,
      pageTotalPrice: this.data.pageTotalPrice
    });
    // console.log(this.data.items);
  },
  /**
   * 提交表单
   */
  bindsubmit: function (e) {
    console.log(this.data.items);
  },
  /**
   * 显示店铺收藏
   */
  shop: function(e){
    this.setData({
      showCollect: "shop"
    })
  },
  /**
   * 显示商品收藏
   */
  commodity: function (e) {
    this.setData({
      showCollect: "commodity"
    })
  }
})