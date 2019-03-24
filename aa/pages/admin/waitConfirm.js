// pages/admin/waitConfirm.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userid:"",
    sellerid:""
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    this.data.userid = options.userid
    this.data.sellerid = options.sellerid
    // console.log(options)
    wx.request({
      url: getApp().url +'/admin/sellersMessage',
      data: {
        sellerid: options.sellerid
      },
      success:function(res){
        console.log(1+res.data)
        that.setData({
          // 商品详情信息
          sellsMessage: res.data,
          url: getApp().url
        })
      }
    })
  },
  /**
   * 批准入驻
   */
  approval:function(e){
    var that = this
    wx.request({
      url: getApp().url +'/admin/sellerRecipt',
      data: {
        id: that.data.userid,
        sellerid: that.data.sellerid,
        operation: 1,
      },
      success:function(e){
        wx.redirectTo({
          url: '/pages/admin/admin',
        })
      }
    })
    
  },
  /**
   * 拒绝入驻
   */
  refuse: function (e) {
    console.log(e.detail.value)
    console.log(e.detail.value.mark)
    var that = this
    if(e.detail.value.mark != ""){
      wx.request({
        url: getApp().url + '/admin/sellerRecipt',
        data: {
          id: that.data.userid,
          sellerid: that.data.sellerid,
          operation: 0,
          appliedMark: e.detail.value.mark
        },
        success: function (e) {
          wx.redirectTo({
            url: '/pages/admin/admin',
          })
        }
      })
    }else{
      wx.showToast({
        title: '请输入拒绝理由',
        icon: 'loading',
        duration: 1000,
        mask: true
      })
    }
   },
  onShareAppMessage: function () {

  }
})