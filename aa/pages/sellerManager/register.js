var userId = ""
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 商品的类型
    version: [],
    // 清空类型input框
    versionValue: "",
    detailsPhoto:[],
    // 商铺类型列表
    twoLevelName: [
      "流行女装",
      "品质男装",
      "鞋靴箱包",
      "手机数码",
      "电脑办公",
      "家用电器",
      "内衣配饰",
      "运动户外",
      "水果生鲜",
      "美妆个护",
      "食品饮料",
      "家具百货",
      "家纺布艺",
      "家居家装",
      "母婴玩具",
      "汽车车品",
      "海外精选"
    ],
    sellerClass:""
  },
  /**
 * 生命周期函数--监听页面加载
 */
  onLoad: function (options) {
    var that = this
    getApp().isLogin();
    if (options.isUpdata == 1){
      wx.request({
        url: getApp().url + '/seller/getSeller',
        data:{
          sellerId: wx.getStorageSync("sellerId")
        },success(res){
          that.setData({
            sellerData: res.data,
            isUpdata: 1
          })
        }
      })
    }
    wx.getStorage({
      key: 'userData',
      success: function(res) {
        userId = res.data.userId
      },
    })
    // wx.request({
    //   url: getApp().url + '/user/checkIsApply',
    //   data: {
    //     userid: getApp().userData.userId
    //   },
    //   success: function (res) {
    //     if (res.data == 1) {
    //       // console.log(getApp().userData.userId)
    //       wx.navigateBack({
    //         delta: 1
    //       })
    //       wx.showToast({
    //         title: '等待批准',
    //         icon: 'loading',
    //         duration: 1000,
    //         mask: true
    //       })
    //     }
    //   }
    // })
  },
  /**
   * 增加产品型号
   */
  addVersion: function (e) {
    if (e.detail.value!=""){
      this.data.version.push(e.detail.value)
      console.log()
      this.setData({
        version: this.data.version,
        versionValue: this.data.versionValue
      })
    }else{
      wx.showToast({
        title: '型号不能为空',
        icon: 'loading',
        duration: 1000,
        mask: true
      })
    }
    
  },
  /**
   * 获取要上传的产品头像图片路径
   */
  seeHeadPhoto: function () {
    var that = this
    wx.chooseImage({
      count: 1,
      // sizeType: ['original'],
      sourceType: ['album', 'camera'],
      success: function (res) {
        console.log(res.tempFilePaths)
        console.log(res.tempFiles)
        that.setData({
          headPhoto: res.tempFilePaths
        })
      }
    })
  },
  /**
   * 获取要上传的产品详情图路径
   */
  seeDetailsPhoto: function () {
    var that = this
    wx.chooseImage({
      count: 9,
      // sizeType: ['original'],
      sourceType: ['album', 'camera'],
      success: function (res) {
        that.setData({
          detailsPhoto: res.tempFilePaths
        })
        console.log(res.tempFilePaths)
      }
    })
  },
  /**
   * 上传文件方法
   */
  uploadimg: function (tempFilePaths,userId) {
    console.log(tempFilePaths)
    for (var i in tempFilePaths) {
      wx.uploadFile({
        url: getApp().url+'/seller/upload',
        filePath: tempFilePaths[i],
        name: 'image',
        header: {
          "content-type": "multipart/form-data"
        },
        formData: {
          userid: userId
        }
      })
    }
  },
  /**
   * picker  value 改变时触发 change 事件
   */
  bindPickerChange(e) {
    var that = this
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      index: e.detail.value,
    })
    // 获取店铺类型
    that.data.sellerClass = that.data.twoLevelName[e.detail.value]
    console.log(that.data.sellerClass)
  },
  /**
   * form提交
   */
  formSubmit: function (e) {
    // console.log(userId)
    // console.log(e.detail.value)
    // 如果input框里面的值不为空
    var that = this
    if (e.detail.value.title != "" && e.detail.value.tel != "" && e.detail.value.address != "") {
      if (that.data.sellerClass == "") {
        wx.showToast({
          title: '请选择类型',
          icon: 'loading',
          duration: 1000,
          mask: true
        })
        return;
      }
      if (that.data.detailsPhoto.length === 0) {
        wx.showToast({
          title: '请上传图片',
          icon: 'loading',
          duration: 1000,
          mask: true
        })
      } else {
        // 传数据给后台
        wx.request({
          url: getApp().url + '/seller/applyforSeller',
          data: {
            userid: userId,
            // applyMoney: applyMoney
            titleName: e.detail.value.title,
            tel: e.detail.value.tel,
            address: e.detail.value.address,
            sellerClass: that.data.sellerClass
          },
          success: function (e) {
            // -1注册失败  1 成功  -9 已经是商户
            console.log(e)
            if (e.data.info === 1) {
              // if (this.data.headPhoto.length != 0) {
              //   this.uploadimg(this.data.headPhoto)
              // }
              that.uploadimg(that.data.detailsPhoto, userId)
              wx.switchTab({
                url: '/pages/myPage/myPage',
              })
            } else {
              // 如果表单注册失败则弹框
              wx.showToast({
                title: '注册失败',
                icon: 'loading',
                duration: 1000,
                mask: true
              })
            }
          }
        })
      }
    } else {
      wx.showToast({
        title: '输入框不能为空',
        icon: 'loading',
        duration: 1000,
        mask: true
      })
    }
  },
  /** 更新数据 */
  sellerUpData: function (e) {
    var that = this
    if (e.detail.value.title != "" && e.detail.value.tel != "" && e.detail.value.address != "") {
      if (that.data.sellerClass == "") {
        wx.showToast({
          title: '请选择类型',
          icon: 'loading',
          duration: 1000,
          mask: true
        })
        return;
      }
      else {
        // 传数据给后台
        wx.request({
          url: getApp().url + '/seller/upData',
          data: {
            userid: userId,
            titleName: e.detail.value.title,
            tel: e.detail.value.tel,
            address: e.detail.value.address,
            sellerClass: that.data.sellerClass,
            id: wx.getStorageSync("sellerId")
          },
          success: function (e) {
            console.log(e)
            if (e.data.info === 1 && that.data.detailsPhoto.length > 0) {
              that.uploadimg(that.data.detailsPhoto, userId)
            }
            wx.switchTab({
              url: '/pages/myPage/myPage',
            })
          }
        })
      }
    } else {
      wx.showToast({
        title: '输入框不能为空',
        icon: 'loading',
        duration: 1000,
        mask: true
      })
    }
  }
})