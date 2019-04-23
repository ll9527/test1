// pages/admin/kouDian.js
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
    var that = this;
    this.setData({
      sta: options.sta
    })
  },
  onShow(e){
    var that = this;
    var options = this.data;
    wx.request({
      url: getApp().url + '/admin/selectAP',
      success(res) {
        var dianShu;
        if (options.sta == 1) {
          dianShu = res.data.discountPersent
        } else if (options.sta == 2) {
          dianShu = res.data.poolsPersent
        } else if (options.sta == 3) {
          dianShu = res.data.shopPercent * 100
        }
        that.setData({
          ap: res.data,
          dianShu: dianShu
        })
      }
    })
  },
  commit(e){
    var sta = this.data.sta;
    if (e.detail.value.point){
    if(sta == 3){
      e.detail.value.point = e.detail.value.point/100
    }
    var isOk = this.kouDian(sta, e.detail.value.point)
    
    }else{
      wx.showLoading({
        title: '请填入点数',
      })
      setTimeout(function () {
        wx.hideLoading()
      }, 1000)
    }
  },
  /** 抽点，扣点设置 */
  kouDian(sta,point){
    wx.request({
      url: getApp().url + '/admin/upPoint',
      data: {
        sta: sta,
        point: point
      },success(res){
        if (res.data == 1) {
          wx.showLoading({
            title: '设置成功',
          })
          setTimeout(function () {
            wx.hideLoading()
            wx.navigateBack({
              delta: 1
            })
          }, 1000)
        }
      }
    })
  },

})