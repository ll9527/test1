
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  /**
 * 生命周期函数--监听页面加载
 */
  onLoad: function (options) {
    var that = this
    getApp().isLogin();
    // console.log(getApp().userData.userId)
    // wx.request({
    //   url: getApp().url + '/admin/isAdmin',
    //   data: {
    //     userid: getApp().userData.userId
    //   },
    //   success:function(res){
    //     if(res.data==1){
    //       wx.request({
    //         url: getApp().url + '/admin/selectWtihoutConfirm',
    //         success: function (e) {
    //           console.log(e.data)
    //           that.setData({
    //             selleList: e.data,
    //           })
    //           console.log(that.data.selleList)
    //         }
    //       })
    //     }else{
    //       wx.showToast({
    //         title: '没有权限',
    //         icon: 'loading',
    //         duration: 1000,
    //         mask: true
    //       })
    //     }
    //   }
    // })
    wx.request({
      url: getApp().url + '/admin/selectWtihoutConfirm',
      success: function (e) {
        that.setData({
          selleList: e.data,
        })
        console.log(that.data.selleList)
      }
    })
  },

})