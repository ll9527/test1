// pages/login/login.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    time: "获取验证码",
    currentTime: 61,
    disabled: false,
    suffix: '',
    phone: '',
  },
  /**
   * 跳转注册页面
   */
  register: function(e){
    wx.redirectTo({
      url: '/pages/register/yongHuXieYi',
    })
  },
  /**
   * 登录
   */
  login: function (e) {
    if (e.detail.value.phone != "" && e.detail.value.password !=""){
      wx.request({
        url: getApp().url+'/user/login',
        data:{
          tel: e.detail.value.phone,
          password: e.detail.value.password
        },
        method:"POST",
        header: {
          'content-type': 'application/x-www-form-urlencoded' 
        },
        success: function(e){
          // 登录失败
          console.log(e.data.info)
          if(e.data.info === -1){
            wx.showToast({
              title: '手机或验证码错误',
              icon: 'loading',
              duration: 1000,
              mask: true
            })
          } else if (e.data.status === "ok" || e.data.status === "yes"){
            // 登录成功，将数据放入缓存
            // console.log(e)
            // var userData = JSON.stringify(e.data)
            wx.setStorage({
              key: 'userid',
              data: e.data.userId
            })
            wx.setStorage({
              key: 'userData',
              data: e.data,
              success:function(e){
                wx.switchTab({
                  url: '/pages/index/index',
                })
              }
            })
            // wx.getStorage({
            //   key: 'userData',
            //   success(res) {
            //     console.log(res.data)
            //     console.log(res.data.info)
            //   }
            // })
          }else{
            wx.showToast({
              title: '无法连接服务器',
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
  },
  /**
   * 跳转首页
   */
  goIndex:function(e){
    wx.switchTab({
      url: '/pages/index/index',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },
  /** 获得验证码 */
  getVerificationCode() {
    var that = this;
    if (that.data.disabled || !that.isPhoneAvailable(that.data.phone)) {
      return
    }
    wx.showLoading({
      title: '请稍等',
      mask: true
    })
    wx.request({
      url: getApp().url + '/user/loginSMSCode',
      data: {
        tel: this.data.phone
      },
      success(res) {
        if (res.data == -1) {
          wx.showToast({
            title: '用户不存在',
            icon: 'loading',
            duration: 1000,
            mask: true
          })
          wx.hideLoading()
        } else if (res.data == 1) {
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
      }, fail() { wx.hideLoading() }
    })
  },
  /** 获得手机输入框的值 */
  getTel(e) {
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
})