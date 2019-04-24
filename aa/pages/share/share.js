Page({
  onLoad(op){
    var that = this;
    wx.request({
      url: getApp().url + '/admin/adminShareImg',
      success(res){
        if(res.data.length > 0)
        that.setData({
          imgName: res.data[0].name
        })
      }
    })
    // wx.request({
    //   url: getApp().url + '/user/shareNum',
    //   data:{uuu: wx.getStorageSync("userid")},
    //   success(res){
    //     that.setData({
    //       shareNum: res.data
    //     })
    //   }
    // })
  },
  openSuccess: function () {
    
  },
  openFail: function () {
    wx.navigateTo({
      url: 'msg_fail'
    })
  },
  openMyPage: function (e) {
    wx.switchTab({
      url: '/pages/myPage/myPage',
    })
  },
    // 分享触发函数
  onShareAppMessage(res) {
    var that = this;
    // console.log(that.data.imgName)
    wx.showShareMenu({
      withShareTicket: true
    })
    var path1 = '/pages/index/index?refereesId=' + wx.getStorageSync("userid")
    wx.request({
      url: getApp().url + '/user/share',
      data: {
        uwea: wx.getStorageSync("userid")
      }
    })
    return {
      title: '共店商城',
      path: path1,
      // imageUrl: getApp().url + "/image/shareCover.jpg"
      imageUrl: getApp().url + "/image/admin/" + that.data.imgName
    }
  }
});