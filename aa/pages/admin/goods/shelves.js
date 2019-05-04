// pages/admin/goods/shelves.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    // 商品的颜色
    version1: [
      // "白色", "红色"
    ],
    // 商品的类型
    version: [
      // "苹果6+64G", "苹果7+64G","苹果6+128G"
    ],
    versionValue: "",
    // 二级目录对象列表
    twoLevelList: [],
    // 二级目录名字
    twoLevelName: [],
    // 二级目录的id
    cid: "",
  },
  /**
 * 生命周期函数--监听页面加载
 */
  onLoad: function (options) {
    var that = this
    console.log(options.sta)
    that.setData({
      sta: options.sta
    })
    // getApp().isLogin();
    wx.request({
      url: getApp().url + '/classfiy/selectTwo',
      success: function (res) {
        for (var i in res.data) {
          that.data.twoLevelName.push(res.data[i].className)
          // console.log(i)
        }
        that.data.twoLevelList = res.data
        that.setData({
          twoLevelName: that.data.twoLevelName
        })
        // console.log(res.data)
        // console.log(that.data.twoLevelName)
      }
    })
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
    // 获取二级目录的id
    that.data.cid = that.data.twoLevelList[e.detail.value].classId
    console.log(that.data.cid)
  },
  /**
   * 增加产品型号
   */
  addVersion: function (e) {
    this.data.version.push(e.detail.value)
    this.setData({
      version: this.data.version,
      versionValue: this.data.versionValue
    })
  },
  /**
 * 增加产品颜色或者其他
 */
  addVersion1: function (e) {
    this.data.version1.push(e.detail.value)
    this.setData({
      version1: this.data.version1,
      versionValue: this.data.versionValue
    })
  },
  /**
   * 获取要上传的产品头像图片路径
   */
  seeHeadPhoto: function () {
    var that = this
    wx.chooseImage({
      count: 1,
      sizeType: ['original'],
      sourceType: ['album', 'camera'],
      success: function (res) {
        // console.log(res.tempFilePaths)
        // console.log(res.tempFiles)
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
      sizeType: ['original'],
      sourceType: ['album', 'camera'],
      success: function (res) {
        that.setData({
          detailsPhoto: res.tempFilePaths
        })
        // console.log(res.tempFilePaths)
      }
    })
  },
  /**
   * 上传文件方法
   */
  uploadimg: function (tempFilePaths, productid, isCover) {
    // isCover  1代表封面，
    // console.log(tempFilePaths)
    for (var i in tempFilePaths) {
      wx.uploadFile({
        url: getApp().url + '/product/upload',
        filePath: tempFilePaths[i],
        header: {
          "content-type": "multipart/form-data"
        },
        name: 'image',
        formData: {
          productid: productid,
          isCover: isCover
        }
      })
    }
  },
  /**
   * form提交
   */
  formSubmit: function (e) {
    var that = this;
    // console.log(e.detail.value);
    // 型号和价钱的List
    var skuList = [];
    // 是否统一价钱的参数 1 是统一  ,0 是不统一
    var allSku = 0;
    var eValue = e.detail.value;
    console.log(eValue)
    // 如果全部得价钱填写了，则发送全部得价钱
    // if (eValue.allPrice != "" && eValue.allGroupPrice != "" && eValue.allNum != "") {
    if (eValue.allNum != "") {
      for (var i in this.data.version) {
        for (var a in this.data.version1) {
          var sku = {
            version: this.data.version[i],
            version1: this.data.version1[a],
            price: "0",
            groupPrice: "0",
            num: eValue["allNum"],
          }
          skuList.push(sku)
        }
      }
      allSku = 1;
      console.log(skuList);
    } else {
      // 全部的价钱没 没有全部填写 
      for (var i in this.data.version) {
        for (var a in this.data.version1) {
          // if (eValue["price" + i + "-" + a] != "" && eValue["groupPrice" + i + "-" + a] != "" && eValue["num" + i + "-" + a] != "") {
          if (eValue["num" + i + "-" + a] != "") {
            // console.log(this.data.version[i] + this.data.version1[a])
            var sku = {
              version: this.data.version[i],
              version1: this.data.version1[a],
              price: "0",
              groupPrice: "0",
              num: eValue["num" + i + "-" + a],
            }
            skuList.push(sku)
          // } else if (eValue["price" + i + "-" + a] == "") {
          //   wx.showToast({
          //     title: '请填入正价',
          //     icon: 'loading',
          //     duration: 1000,
          //     mask: true
          //   })
          //   return;
          // } else if (eValue["groupPrice" + i + "-" + a] == "") {
          //   wx.showToast({
          //     title: '请填入团购价',
          //     icon: 'loading',
          //     duration: 1000,
          //     mask: true
          //   })
          //   return;
          } else if (eValue["num" + i + "-" + a] == "") {
            wx.showToast({
              title: '请填入库存',
              icon: 'loading',
              duration: 1000,
              mask: true
            })
            return;
          }
        }
      }
      allSku = 0;
      console.log(skuList);
    }
    // 如果input框里面的值不为空
    if (e.detail.value.title != "" && eValue.peopleNum) {
      // 如果商品类型为空，则弹框
      if (this.data.version.length == 0) {
        wx.showToast({
          title: '请增加商品型号',
          icon: 'loading',
          duration: 1000,
          mask: true
        })
        return;
      } else {
        if (this.data.version1.length == 0) {
          wx.showToast({
            title: '请增加型号（颜色）',
            icon: 'loading',
            duration: 1000,
            mask: true
          })
          return;
        }
        if (this.data.headPhoto) {
          if (this.data.detailsPhoto) {
            // 如果类型没选择则弹框提示
            if (that.data.cid != "") {
              // 传数据给后台
              wx.request({
                url: getApp().url + '/product/insertP',
                data: {
                  cid: that.data.cid,
                  title: e.detail.value.title,
                  // num: e.detail.value.num,
                  // price: e.detail.value.price,
                  // groupPrice: e.detail.value.groupPrice,
                  skuList: JSON.stringify(skuList),
                  allSku: allSku,
                  versionList: JSON.stringify(that.data.version),
                  versionList1: JSON.stringify(that.data.version1),
                  sellerid: -1,
                  freight: 0,//e.detail.value.freight
                  peopleNum: eValue.peopleNum,
                  sta: that.data.sta
                },
                success: function (res) {
                  // console.log(res)
                  if (res.data.info != 1) {
                    // console.log("res.data.info:" + res.data.info)
                    wx.showToast({
                      title: '注册失败',
                      icon: 'loading',
                      duration: 1000,
                      mask: true,
                      success: function (res) {
                        setTimeout(function () {
                          wx.navigateBack({
                            delta: 1
                          })
                        }, 2000)
                      }
                    })
                  } else {
                    // console.log("文件上传")
                    that.uploadimg(that.data.headPhoto, res.data.productid, 1)
                    that.uploadimg(that.data.detailsPhoto, res.data.productid, 0)
                    wx.navigateBack({
                      delta: 1
                    })
                  }
                }
              })
            } else {
              wx.showToast({
                title: '请选择类型',
                icon: 'loading',
                duration: 1000,
                mask: true
              })
            }
          } else {
            wx.showToast({
              title: '请上传详情图',
              icon: 'loading',
              duration: 1000,
              mask: true
            })
          }
        } else {
          wx.showToast({
            title: '请上传头像',
            icon: 'loading',
            duration: 1000,
            mask: true
          })
        }
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