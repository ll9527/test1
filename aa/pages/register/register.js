// pages/register/register.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    time: "获取验证码",
    currentTime: 61,
    disabled: false,
    suffix: '',
    phone:'',
  },
  /**
   * 用户注册
   */
  register: function(e){
    // if (e.detail.value.password != e.detail.value.password2){
    //     wx.showToast({
    //       title: '密码不一致',
    //       icon: 'loading',
    //       duration: 1000,
    //       mask: true
    //     })
    // }else{
      if (e.detail.value.phone != "" && e.detail.value.password != "") {
        wx.request({
          url: getApp().url +'/user/registered',
          data: {
            // username: e.detail.value.username,
            tel: e.detail.value.phone,
            password: e.detail.value.password,
            referrer_id: getApp().refereesId ? getApp().refereesId : -1
          },
          success: function (e) {
            if (e.data.info === 0) {
              wx.showToast({
                title: '注册成功',
                icon: 'loading',
                duration: 1000,
                mask: true,
                success: function(res){
                  setTimeout(function () {
                    wx.setStorage({
                      key: 'userid',
                      data: e.data.userId
                    })
                    wx.setStorage({
                      key: 'userData',
                      data: e.data,
                      success: function (e) {
                        wx.switchTab({
                          url: '/pages/index/index',
                        })
                      }
                    })
                  }, 1000) //延迟时间 这里是1秒
                }
              })
            // }
            // else if (e.data.info === -9){
            //   wx.showToast({
            //     title: '手机已存在',
            //     icon: 'loading',
            //     duration: 1000,
            //     mask: true
            //   })
            } else {
              console.log(e)
              wx.showToast({
                title: '验证码或手机错误',
                icon: 'loading',
                duration: 1000,
                mask: true
              })
            }
          }
        })
      }else{
        wx.showToast({
          title: '输入框不能为空',
          icon: 'loading',
          duration: 1000,
          mask: true
        })
      }
    // }
  },
  /** 获得验证码 */
  getVerificationCode(){
    var that = this;
    if (that.data.disabled || !that.isPhoneAvailable(that.data.phone)){
      return
    }
    wx.showLoading({
      title: '请稍等',
      mask: true
    })
    wx.request({
      url: getApp().url + '/user/registerSMSCode',
      data:{
        tel: this.data.phone
      },
      success(res){
        if(res.data.info == -9){
          wx.showToast({
            title: '手机已存在',
            icon: 'loading',
            duration: 1000,
            mask: true
          })
          wx.hideLoading()
        }else if(res.data.info == 1){
          that.setData({
            disabled: true
          })
          wx.hideLoading()
          let currentTime = that.data.currentTime;
          var interval = ''
          interval = setInterval(function () {
            currentTime--;
            that.setData({
              time: currentTime,
              suffix: '秒后可重新获取'
            })
            if (currentTime <= 0) {
              clearInterval(interval)
              that.setData({
                time: '重新发送',
                suffix: '',
                currentTime: 61,
                disabled: false
              })
            }
          }, 1000)
        }
      },fail(){wx.hideLoading()}
    })
  },
  /** 获得手机输入框的值 */
  getTel(e){
    this.data.phone = e.detail.value
  },
  // 验证手机号码是否有效
  isPhoneAvailable(phone) {
    var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
    if (!myreg.test(phone)) {
      return false;
    } else {
      return true;
    }
  },
  /**
 * 跳转首页
 */
  goIndex: function (e) {
    wx.switchTab({
      url: '/pages/index/index',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },
})