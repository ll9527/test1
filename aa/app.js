//app.js
App({
  url: "https://www.gongshop.com.cn",
  // url: "http://127.0.0.1:8080",
  // 推荐人id
  refereesId:"",
  
  // 权限屏幕得显示开关
  // root: false,
  root: true,
  userData: "",
  onLaunch(res) {
    // 检测是否有新版本，如有下载更新
    const updateManager = wx.getUpdateManager()
    updateManager.onCheckForUpdate(function (res) {
      // console.log(res.hasUpdate)
      if (res.hasUpdate) {
        updateManager.onUpdateReady(function () {
          updateManager.applyUpdate()
        })
      }
    })
  },
  /**
  * 登录验证
  */
  isLogin: function () {
    // wx.getStorage({
    //   key: 'userData',
    //   success:function(e){
    //     getApp().userData = e.data
    //   },
    //   fail: function (e) {
    //     // console.log(e.data==null)
    //     wx.redirectTo({
    //       url: '/pages/login/login',
    //     })
    //   }
    // })
    var userid = wx.getStorageSync('userid');
    wx.getStorage({
      key: 'userid',
      success: function (res) {
        wx.request({
          url: getApp().url + '/user/isUser',
          data: {
            userId: userid
          },
          success(e) {
            if (e.data != 1) {
              try {
                wx.clearStorageSync()
              } catch (e) {
                // console.log(e)
                wx.reLaunch({
                  url: '/pages/login/login',
                })
              }
              wx.reLaunch({
                url: '/pages/login/login',
              })
            } else if (e.data == 1) {
              wx.getStorage({
                key: 'userData',
                success: function (e) {
                  getApp().userData = e.data
                },
                fail: function (e) {
                  // console.log(e.data==null)
                  wx.reLaunch({
                    url: '/pages/login/login',
                  })
                }
              })
            }
          }
        })
      },
      fail() {
        wx.reLaunch({
          url: '/pages/login/login',
        })
      }
    })
  },
  //授权函数
  click: function (res, that) {
    // var that = this
    // console.log("res1:" + res)
    // wx.getUserInfo({
    //   success: function (res) {
    //     console.log(res)
    //     getApp().root = true
    //     that.setData({
    //       root: getApp().root
    //     })
    //     console.log("APProot:" + getApp().root)
    //     console.log("root:" + that.data.root)
    //   },
    //   fail: function (res) {
    //     console.log(res)
    //   }
    // })
    // getApp().getCode()
  },
  //获得code
  getCode: function(){
    wx.login({
      success(res) {
        if (res.code) {
          // 发起网络请求
          // console.log(res.code)
          
        } else {
          // console.log('登录失败！' + res.errMsg)
        }
      }
    })
  },
  onShow(res){
    // console.log(res)
    if (res.query.refereesId){
      // 如果是通过分享连接进来的，都有推荐人id
      this.refereesId = res.query.refereesId;
      // console.log(res.query.refereesId)
      // console.log(this.refereesId)
      // console.log("get:"+getApp().refereesId)
    }
  }
})
// success(res){
//   if (res.data.status == 200) {
//     wx.showToast({
//       title: '发货成功',
//       icon: 'loading',
//       duration: 500,
//       mask: true,
//       success() {
//         setTimeout(function () {
//           wx.navigateBack({
//             delta: 1
//           })
//         }, 500)
//       }
//     })
//   } else {
//     wx.showToast({
//       title: '请稍后再试',
//       icon: 'loading',
//       duration: 500,
//       mask: true,
//       success() {
//         setTimeout(function () {
//           wx.navigateBack({
//             delta: 1
//           })
//         }, 500)
//       }
//     })
//   }
// },
// fail(){
//   wx.showToast({
//     title: '服务器异常',
//     icon: 'loading',
//     duration: 500,
//     mask: true,
//     success() {
//       setTimeout(function () {
//         wx.switchTab({
//           url: '/pages/myPage/myPage',
//         })
//       }, 500)
//     }
//   })
// }
//       })