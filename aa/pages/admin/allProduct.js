Page({

  /**
   * 页面的初始数据
   */
  data: {
    url: getApp().url + "/image/",
    pages: 1,
    productList:[],
    list:[]
  },
  /**
   * 下架商品
   */
  onDelete: function (e) {
    wx.showLoading({
      title: '请稍等',
      mask:true
    })
    var that = this
    // console.log(e)
    // 获得商品id
    // console.log(e.target.dataset.productid)
    // console.log(e.target.dataset.index)
    wx.request({
      url: getApp().url + '/product/productDown',
      data: {
        productid: e.target.dataset.productid
      },
      success: function (res) {
        // res.data.status
        if (res.data.info == 1) {
          that.data.list.splice(e.target.dataset.index,1)
          that.setData({
            list: that.data.list
          })
        } else {
          wx.showToast({
            title: '下架失败',
            icon: 'none',
            duration: 1000,
            mask: true
          })
        }
      },complete(){
        wx.hideLoading();
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    getApp().isLogin();
    wx.request({
      url: getApp().url + '/admin/allProduct',
      success: function (res) {
        // console.log(res.data)
        var plist = res.data.splice(0, 50)
        for(var i in plist){
          that.data.list.push(plist[i])
        }
        that.setData({
          list: that.data.list,
          productList: res.data
        })
        // console.log(that.data.list)
      },
    })
  },
  onReachBottom(){
    var that = this;
    if (that.data.productList.length <= 0) {
      return;
    }
    wx.showLoading({
      title: '加载中',
      mask: true,
    })
    // that.data.pages += 1;
    var plist = that.data.productList.splice(0, 50);
    for (var i in plist) {
      that.data.list.push(plist[i])
    }
    // console.log(that.data.list)
    that.setData({
      list: that.data.list
    })
    wx.hideLoading()
  },
  /** 模糊查询 */
  search(event){
    var that = this;
    wx.request({
      url: getApp().url + '/product/serchProduct',
      data: {
        pname: event.detail.value,
        operationCode: 1
      },
      success: function (res) {
        var plist = res.data.splice(0, 50)
        that.data.list = [];
        for (var i in plist) {
          that.data.list.push(plist[i])
        }
        that.setData({
          list: that.data.list,
          productList: res.data
        })
        // console.log(that.data.list)
      }
    })
  }
})