// pages/productDetails/productDetails.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 商品得型号
    classify:[],
    // 商品的型号（颜色）
    classify1: [],
    // skuList
    skuList: [],
    // 所有sku名字得集合
    skuNameList:[],
    // 收藏显示开关
    collection: false,
    // 用户选择商品的型号(颜色)index
    radioId1:"0",
    // 用户选择商品得数量
    count: 0,
    // 商品的库存
    num: 0,
    // 商品id
    productid:"",
    // 拼团型号显示开关
    showModalStatus: false,
    // 拼团列表显示开关
    showGroupListStatus: false,
    // 拼团订单的id
    groupOid:""
  },
  /**
   * 点击右符号 增 数量
   */
  add: function (e) {
    if (this.data.count < this.data.num) {
      this.data.count += 1;
      this.setData({
        count: this.data.count
      })
    } else {
      // 如果件数大于库存数，则件数等于库存数
      this.data.count = this.data.num;
      this.setData({
        count: this.data.count
      })
    }
    
  },
  /**
   * 点击右符号 减 数量
   */
  reduce: function (e) {
    // 如果件数大于0，可以减件数
    if (this.data.count > 0) {
      this.data.count -= 1;
      this.setData({
        count: this.data.count
      })
    } else {
      // 件数小于0，件数等于0
      this.data.count = 0;
      this.setData({
        count: this.data.count
      })
    }
  }, 
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(res){
    var that = this;
    getApp().isLogin();
    console.log(res)
    that.data.productid = res.productid;
    wx.request({
      url: getApp().url + '/seller/addVisitNum',
      data: {
        productid: res.productid
      }
    })
    wx.request({
      url: getApp().url +'/product/selectDetail',
      data: {
        productid:res.productid
      },
      success: function(res) {
        // 组装skuNameList
        var skuNameList = [];
        for (var i in res.data.skuList){
          skuNameList.push(res.data.skuList[i].versionName)
        }
        console.log(res)
        that.setData({
          productDetail: res.data,
          num: res.data.productList[0].num,
          price: res.data.productList[0].price,
          groupPrice: res.data.productList[0].groupPrice,
          classify: res.data.productVersion,
          classify1: res.data.productVersion1,
          skuList: res.data.skuList,
          skuNameList: skuNameList,
          url: getApp().url+"/image/"
        }) 
        console.log(that.data.skuList)      
      }
    })
    wx.getStorage({
      key: 'userData',
      success: function(e) {
        wx.request({
          url: getApp().url + '/user/checkIsCollect',
          data: {
            productid: res.productid,
            userid: e.data.userId
          },
          success: function(e){
            console.log("checkIsCollect："+e.data)
            // 1代表已经收藏
            if(e.data == 1){
              that.setData({
                collection: true,
              })  
            }else{
              that.setData({
                collection: false
              })
            }
          }
        })
      },
      fail: function(e){
        that.setData({
          collection: false
        })
      }
    })
    
  },
  onShow(res){
    var that = this;
    wx.request({
      url: getApp().url + '/order/selectGroupByP',
      data: {
        p: this.data.productid
      }, success(e) {
        for (var i in e.data) {
          e.data[i].shopOrderGroup.addTime = new Date(+new Date(e.data[i].shopOrderGroup.addTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
        }
        that.setData({
          orderList: e.data
        })
      }
    })
  },
  /**
   * 查询sku得方法
   */
  selectSku(that){
    var skuName = that.data.classify[that.data.radioId].versionName + that.data.classify1[that.data.radioId1].versionName
    var index = that.data.skuNameList.indexOf(skuName)
    if (index != -1){
      that.setData({
        num: that.data.skuList[index].num,
        price: that.data.skuList[index].price,
        groupPrice: that.data.skuList[index].groupPrice,
        skuId: that.data.skuList[index].id
      })
    }
  },
  /**
   * 选择商品分类
   */
  radioChange:function(e){
    var that = this;
    console.log(e.detail.value)
    this.setData({
      radioId: e.detail.value
    });
    this.selectSku(that)
  },
  /**
 * 选择商品颜色分类
 */
  radioChange1: function (e) {
    var that = this;
    console.log(e.detail.value)
    this.setData({
      radioId1: e.detail.value
    });
    this.selectSku(that)
  },
  /**
   * 点击收藏
   */
  onCollection: function(e){
    var that = this
    wx.getStorage({
      key: 'userData',
      success: function (e) {
        if (that.data.collection==true){
          // 取消收藏
          wx.request({
            url: getApp().url +'/user/collectOperation',
            data: {
              productid: that.data.productid,
              userid: e.data.userId,
              operation: 0
            },
            success: function(e){
              that.setData({
                collection: false
              })
            }
          })
        }else{
          // 收藏
          wx.request({
            url: getApp().url + '/user/collectOperation',
            data: {
              productid: that.data.productid,
              userid: e.data.userId,
              operation: 1
            },
            success: function (e) {
              that.setData({
                collection: true
              })
              console.log("收藏操作返回：" + e.data)
            }
          })
        }
      },
      fail: function (e) {
        that.setData({
          collection: false
        })
      }
    })
  },
  /**
   * 跳转开团页面
   */
  goGroup: function(e){
    // wx.navigateTo({
    //   url: '/pages/goGroup/goGroup'
    // })
  },
  /**
   * 跳转订单页面
   */
  goPay(res){
    console.log("groupOid"+this.data.groupOid)
    var that = this;
    var isGroup;
    if (res.currentTarget.dataset.isgroup){
      isGroup = 1
    }else{
      isGroup = 0
    }
    console.log(isGroup)
    if (this.data.count <= 0){
      wx.showToast({
        title: '请选择数量',
        icon: 'loading',
        duration: 1000,
        mask: true
      })
      return;
    }
    if( !this.data.radioId){
      wx.showToast({
        title: '请选择型号',
        icon: 'loading',
        duration: 1000,
        mask: true
      })
      return;
    }
    console.log(this.data.radioId)
    if (!this.data.radioId1) {
      wx.showToast({
        title: '请选择型号',
        icon: 'loading',
        duration: 1000,
        mask: true
      })
      return;
    }
    wx.getStorage({
      key: 'userid',
      success: function(res) {
        console.log(res)
        console.log(that.data.classify)
        console.log(that.data.classify[that.data.radioId].versionName)
        console.log(that.data.productDetail.productList)
        var version1Name
        if (that.data.classify1.length > 0){
          version1Name = that.data.classify1[that.data.radioId1].versionName
        }else{
          version1Name = ""
        }
        var url = '/pages/order/order?userid=' + res.data + "&versionName=" + that.data.classify[that.data.radioId].versionName
          + "&version1Name=" + version1Name + "&count=" + that.data.count
          + "&product=" + JSON.stringify(that.data.productDetail.productList)
          + "&price=" + (isGroup == 1 ? that.data.groupPrice : that.data.price)
          + "&skuId=" + that.data.skuId
          + "&isGroup=" + isGroup
          + "&groupOid=" + that.data.groupOid
        wx.navigateTo({
          url: url
        })
      },
      fail(res){
        wx.redirectTo({
          url: '/pages/login/login'
        })
      }
    })
  },

// 显示遮罩层
  showModal: function () {
    // 显示遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      showModalStatus: true,
      // 清空groupOid
      groupOid: ""
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export()
      })
    }.bind(this), 200)
  },
  hideModal: function () {
    // 隐藏遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export(),
        showModalStatus: false
      })
    }.bind(this), 200)
  },

  // 显示遮罩层
  showGroupListModal: function () {
    var that = this;
    wx.request({
      url: getApp().url + '/order/selectGroupByP',
      data: {
        p: this.data.productid
      }, success(e) {
        for (var i in e.data) {
          e.data[i].shopOrderGroup.addTime = new Date(+new Date(e.data[i].shopOrderGroup.addTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
        }
        that.setData({
          orderList: e.data
        })
      }
    })
    // 显示遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationDataGroup: animation.export(),
      showGroupListStatus: true
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationDataGroup: animation.export()
      })
    }.bind(this), 200)
  },
  hideGroupListModal: function () {
    // 隐藏遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationDataGroup: animation.export(),
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationDataGroup: animation.export(),
        showGroupListStatus: false
      })
    }.bind(this), 200)
  },
  /** 点击拼单执行的方法 */
  pindan(res){
    var that = this;
    this.hideGroupListModal();
    this.showModal();
    console.log(res)
    this.setData({
      groupOid: res.currentTarget.dataset.oid
    })
    console.log(this.data.groupOid)
  }
})