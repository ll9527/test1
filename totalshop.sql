/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : totalshop

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-03-01 11:26:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '地址id',
  `address` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '地址详情',
  `user_id` int(20) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of address
-- ----------------------------

-- ----------------------------
-- Table structure for admin_profit
-- ----------------------------
DROP TABLE IF EXISTS `admin_profit`;
CREATE TABLE `admin_profit` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(225) DEFAULT NULL COMMENT '密码',
  `percent_class` varchar(225) DEFAULT NULL COMMENT '扣点类目名',
  `shop_percent` double(50,2) DEFAULT NULL COMMENT '平台扣点',
  `seller_apply_money` decimal(12,2) DEFAULT NULL COMMENT '商家入驻费用',
  `vip_money` decimal(12,2) DEFAULT NULL COMMENT '会员费用',
  `bonus_pools` decimal(12,2) DEFAULT NULL COMMENT '奖金池金额',
  `pools_persent` int(20) DEFAULT NULL COMMENT '奖金池百分比',
  `discount_amount` decimal(12,2) DEFAULT NULL COMMENT '全场优惠金额',
  `discount_persent` int(20) DEFAULT NULL COMMENT '奖金池百分比',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_profit
-- ----------------------------

-- ----------------------------
-- Table structure for class_with_product
-- ----------------------------
DROP TABLE IF EXISTS `class_with_product`;
CREATE TABLE `class_with_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `product_id` int(20) DEFAULT NULL,
  `level2_class_id` int(20) DEFAULT NULL COMMENT '二级目录id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of class_with_product
-- ----------------------------
INSERT INTO `class_with_product` VALUES ('25', '39', '11');
INSERT INTO `class_with_product` VALUES ('26', '40', '56');
INSERT INTO `class_with_product` VALUES ('27', '41', '56');
INSERT INTO `class_with_product` VALUES ('28', '42', '11');
INSERT INTO `class_with_product` VALUES ('29', '43', '21');
INSERT INTO `class_with_product` VALUES ('30', '44', '29');
INSERT INTO `class_with_product` VALUES ('31', '45', '11');

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `seller_id` int(20) DEFAULT NULL,
  `product_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES ('5', '33', null, null);

-- ----------------------------
-- Table structure for product_comment_freight
-- ----------------------------
DROP TABLE IF EXISTS `product_comment_freight`;
CREATE TABLE `product_comment_freight` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `product_id` int(20) DEFAULT NULL,
  `comment` varchar(500) DEFAULT NULL COMMENT '评论',
  `freight` decimal(12,2) DEFAULT NULL COMMENT '运费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product_comment_freight
-- ----------------------------

-- ----------------------------
-- Table structure for product_img
-- ----------------------------
DROP TABLE IF EXISTS `product_img`;
CREATE TABLE `product_img` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `product_id` int(20) DEFAULT NULL,
  `image` varchar(500) DEFAULT NULL,
  `is_cover` int(20) DEFAULT '0' COMMENT '是否封面',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `product_id` (`product_id`) USING BTREE,
  CONSTRAINT `product_img_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product_item` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of product_img
-- ----------------------------
INSERT INTO `product_img` VALUES ('67', '39', '5f686614-7571-45ce-a4ca-07f66c366b4d.png', '1');
INSERT INTO `product_img` VALUES ('68', '39', 'd8b5bb6d-6f7e-4104-9aba-1842ed069cef.jpg', '0');
INSERT INTO `product_img` VALUES ('69', '40', 'c98c001e-d0c5-4fbb-bd6e-0852c8823eaa.jpg', '1');
INSERT INTO `product_img` VALUES ('70', '40', '274fff63-f7d6-4d35-be03-95c16e25ab3e.jpg', '0');
INSERT INTO `product_img` VALUES ('71', '40', '3508f5f6-a983-45fc-af5a-3feb6fdf1c2f.jpg', '0');
INSERT INTO `product_img` VALUES ('72', '40', '2ed0790a-af2d-4fb1-b867-dbabbdea314f.jpg', '0');
INSERT INTO `product_img` VALUES ('73', '41', '8a4c7bc4-3781-4093-92e0-fb37b23d2c9b.jpg', '1');
INSERT INTO `product_img` VALUES ('74', '41', '947664d7-911a-46bc-97a0-427de0bac2d6.jpg', '0');
INSERT INTO `product_img` VALUES ('75', '41', '4202da3f-7dfe-4eaf-a4df-5ab94b9502db.jpg', '0');
INSERT INTO `product_img` VALUES ('76', '41', 'daa88041-8270-4b42-9284-0f09c4a7beb6.jpg', '0');
INSERT INTO `product_img` VALUES ('77', '42', 'be468e87-bc46-49c4-b42b-2efd94d6c31a.jpg', '1');
INSERT INTO `product_img` VALUES ('78', '42', '02cd8b9a-2c35-4040-b70f-9afc29cd8b44.jpg', '0');
INSERT INTO `product_img` VALUES ('79', '42', 'f563f1d8-665a-4fcc-af8a-0fcfaa7e33de.jpg', '0');
INSERT INTO `product_img` VALUES ('80', '42', 'e426c9ee-39f4-4820-a825-ebd1ce8568f7.jpg', '0');
INSERT INTO `product_img` VALUES ('81', '43', '5131915f-a871-4156-ae2a-465ed734dbbb.jpg', '1');
INSERT INTO `product_img` VALUES ('82', '43', '60d0e115-0d90-4b85-9aa2-10fd03349b7d.jpg', '0');
INSERT INTO `product_img` VALUES ('83', '43', '5fa21393-3eef-4d64-a2a3-08f8c3853359.jpg', '0');
INSERT INTO `product_img` VALUES ('84', '43', 'c370eb20-1ddc-4375-8181-246749988f4a.jpg', '0');
INSERT INTO `product_img` VALUES ('85', '44', '5908c4e5-fa90-4fb8-b71f-b4487320e794.jpg', '0');
INSERT INTO `product_img` VALUES ('86', '44', '35d45cbf-39f8-4e15-8fa5-0fc6b654fea0.jpg', '1');
INSERT INTO `product_img` VALUES ('87', '44', 'd7583e13-b902-49e3-9846-5c2ddb000299.jpg', '0');
INSERT INTO `product_img` VALUES ('88', '45', '54bfca1d-a25b-43ba-b3dd-744eb2cb963e.png', '1');
INSERT INTO `product_img` VALUES ('89', '45', '2169b3a7-8f93-41f1-9e41-16777bfe7a87.png', '0');

-- ----------------------------
-- Table structure for product_item
-- ----------------------------
DROP TABLE IF EXISTS `product_item`;
CREATE TABLE `product_item` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '商品id，同时也是商品编号',
  `title` varchar(100) DEFAULT NULL COMMENT '商品标题',
  `sell_point` varchar(500) DEFAULT NULL COMMENT '商品卖点',
  `price` decimal(20,2) DEFAULT NULL COMMENT '商品价格，单位为：角',
  `group_price` decimal(20,2) DEFAULT NULL COMMENT '商品拼团价格，单位为：角',
  `is_group` int(20) DEFAULT NULL,
  `num` int(10) DEFAULT NULL COMMENT '库存数量',
  `cid` int(10) DEFAULT NULL COMMENT '所属类目，叶子类目',
  `status` tinyint(4) DEFAULT '1' COMMENT '商品状态，1-正常，2-下架，3-删除',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `sales_volume` int(255) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `cid` (`cid`) USING BTREE,
  KEY `status` (`status`) USING BTREE,
  KEY `updated` (`updated`) USING BTREE,
  CONSTRAINT `product_item_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `shop_classify` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品表';

-- ----------------------------
-- Records of product_item
-- ----------------------------
INSERT INTO `product_item` VALUES ('39', '外套男士春秋季2019新款韩版潮流春装潮牌上衣服', null, '128.00', '118.00', '1', '22', '11', '1', '2019-02-16 20:04:12', '2019-02-16 20:04:12', '0');
INSERT INTO `product_item` VALUES ('40', 'VIVO X21 全屏幕指纹 vivox21 手机', null, '1770.00', '1700.00', '1', '10', '56', '0', '2019-02-18 15:37:13', '2019-02-18 15:37:13', '0');
INSERT INTO `product_item` VALUES ('41', 'VIVO X21 全屏幕指纹 vivox21 手机', null, '1790.00', '1680.00', '1', '100', '56', '1', '2019-02-18 15:43:47', '2019-02-18 15:43:47', '0');
INSERT INTO `product_item` VALUES ('42', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', null, '35.00', '29.00', '1', '50', '11', '0', '2019-02-18 15:52:21', '2019-02-18 15:52:21', '0');
INSERT INTO `product_item` VALUES ('43', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', null, '35.00', '29.00', '1', '50', '21', '1', '2019-02-18 15:55:23', '2019-02-18 15:55:23', '0');
INSERT INTO `product_item` VALUES ('44', '苏泊尔5L双胆智能电压力锅家用高压锅电饭煲全自动官', null, '268.00', '240.00', '1', '50', '29', '1', '2019-02-18 16:04:30', '2019-02-18 16:04:30', '0');
INSERT INTO `product_item` VALUES ('45', '测试商品', null, '20.90', '10.99', '1', '22', '11', '1', '2019-02-21 16:13:14', '2019-02-21 16:13:14', '0');

-- ----------------------------
-- Table structure for referrer
-- ----------------------------
DROP TABLE IF EXISTS `referrer`;
CREATE TABLE `referrer` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `referrer_id` int(20) DEFAULT NULL COMMENT '推荐人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of referrer
-- ----------------------------

-- ----------------------------
-- Table structure for seller
-- ----------------------------
DROP TABLE IF EXISTS `seller`;
CREATE TABLE `seller` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '商户id',
  `tel` bigint(50) DEFAULT NULL COMMENT '商户电话',
  `title_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺名字',
  `user_id` int(20) DEFAULT NULL COMMENT '用户id',
  `seller_class` varchar(225) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `title_name` (`title_name`) USING BTREE,
  CONSTRAINT `seller_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of seller
-- ----------------------------
INSERT INTO `seller` VALUES ('24', '18718212688', '几何符号旗舰店', '35', '连锁超市');
INSERT INTO `seller` VALUES ('25', '13763012343', '七彩人生', '36', '服饰其他');

-- ----------------------------
-- Table structure for seller_address
-- ----------------------------
DROP TABLE IF EXISTS `seller_address`;
CREATE TABLE `seller_address` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '商家地址id',
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商家地址',
  `seller_id` int(20) DEFAULT NULL COMMENT '商家id',
  `seller_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '店长名字',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `seller_id` (`seller_id`) USING BTREE,
  CONSTRAINT `seller_address_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of seller_address
-- ----------------------------
INSERT INTO `seller_address` VALUES ('30', '湛江市海田路65号', '24', '李生');
INSERT INTO `seller_address` VALUES ('31', '湛江市赤坎区', '25', '13149988');

-- ----------------------------
-- Table structure for seller_bcimg
-- ----------------------------
DROP TABLE IF EXISTS `seller_bcimg`;
CREATE TABLE `seller_bcimg` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '证件id',
  `img` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '证件路径',
  `user_id` int(20) DEFAULT NULL COMMENT '用户id',
  `seller_id` int(20) DEFAULT NULL COMMENT '商家id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `seller_id` (`seller_id`) USING BTREE,
  CONSTRAINT `seller_bcimg_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of seller_bcimg
-- ----------------------------
INSERT INTO `seller_bcimg` VALUES ('21', 'e42079d4-3784-4304-a555-fa4cc2b1318a.jpg', '35', '24');
INSERT INTO `seller_bcimg` VALUES ('22', '7d9d2ec1-07cf-4649-9c01-a7b65e1e042c.jpg', '36', '25');

-- ----------------------------
-- Table structure for seller_visit
-- ----------------------------
DROP TABLE IF EXISTS `seller_visit`;
CREATE TABLE `seller_visit` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `seller_id` int(20) DEFAULT NULL,
  `visit_num` bigint(50) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seller_visit
-- ----------------------------
INSERT INTO `seller_visit` VALUES ('1', '24', '0');
INSERT INTO `seller_visit` VALUES ('3', '25', '0');

-- ----------------------------
-- Table structure for seller_with_product_img
-- ----------------------------
DROP TABLE IF EXISTS `seller_with_product_img`;
CREATE TABLE `seller_with_product_img` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `seller_id` int(20) DEFAULT NULL,
  `product_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `seller_id` (`seller_id`) USING BTREE,
  CONSTRAINT `seller_with_product_img_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of seller_with_product_img
-- ----------------------------
INSERT INTO `seller_with_product_img` VALUES ('25', '24', '39');
INSERT INTO `seller_with_product_img` VALUES ('26', '25', '40');
INSERT INTO `seller_with_product_img` VALUES ('27', '25', '41');
INSERT INTO `seller_with_product_img` VALUES ('28', '25', '42');
INSERT INTO `seller_with_product_img` VALUES ('29', '25', '43');
INSERT INTO `seller_with_product_img` VALUES ('30', '25', '44');
INSERT INTO `seller_with_product_img` VALUES ('31', '24', '45');

-- ----------------------------
-- Table structure for shop_classify
-- ----------------------------
DROP TABLE IF EXISTS `shop_classify`;
CREATE TABLE `shop_classify` (
  `class_id` int(11) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `class_key` int(11) DEFAULT NULL COMMENT '外键',
  `level` int(11) DEFAULT NULL COMMENT '1一级目录  2二级目录',
  `ondelect` int(11) DEFAULT '0' COMMENT '显示 0显示，1不显示',
  `images` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`class_id`) USING BTREE,
  KEY `fk_id` (`class_key`) USING BTREE,
  CONSTRAINT `fk_id` FOREIGN KEY (`class_key`) REFERENCES `shop_classify` (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='分类表';

-- ----------------------------
-- Records of shop_classify
-- ----------------------------
INSERT INTO `shop_classify` VALUES ('1', '服装饰品', null, '1', '0', null);
INSERT INTO `shop_classify` VALUES ('2', '鞋包配件', null, '1', '0', null);
INSERT INTO `shop_classify` VALUES ('3', '电器玩具', null, '1', '0', null);
INSERT INTO `shop_classify` VALUES ('4', '零食生鲜', null, '1', '1', null);
INSERT INTO `shop_classify` VALUES ('5', '百货家具', null, '1', '1', null);
INSERT INTO `shop_classify` VALUES ('6', '家电数码', null, '1', '0', null);
INSERT INTO `shop_classify` VALUES ('7', '办公五金', null, '1', '1', null);
INSERT INTO `shop_classify` VALUES ('8', '户外乐器', null, '1', '1', null);
INSERT INTO `shop_classify` VALUES ('9', '美妆洗护', null, '1', '1', null);
INSERT INTO `shop_classify` VALUES ('10', '游戏影视', null, '1', '1', null);
INSERT INTO `shop_classify` VALUES ('11', '男装', '1', '2', '0', 'nanxie.png');
INSERT INTO `shop_classify` VALUES ('12', '女装', '1', '2', '0', 'lianyiqun.png');
INSERT INTO `shop_classify` VALUES ('13', '羽绒服', '1', '2', '0', 'yurongfu.png');
INSERT INTO `shop_classify` VALUES ('14', '棉服', '1', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('15', '大衣夹克', '1', '2', '0', 'jiake.png');
INSERT INTO `shop_classify` VALUES ('16', '卫衣', '1', '2', '0', 'weiyi.png');
INSERT INTO `shop_classify` VALUES ('17', '牛仔裤', '1', '2', '0', 'niuziku.png');
INSERT INTO `shop_classify` VALUES ('18', '休闲裤', '1', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('19', '衬衫', '1', '2', '0', 'chenshan.png');
INSERT INTO `shop_classify` VALUES ('20', '女鞋', '2', '2', '0', 'nvxie.png');
INSERT INTO `shop_classify` VALUES ('21', '男鞋', '2', '2', '0', 'nanxie.png');
INSERT INTO `shop_classify` VALUES ('22', '靴子', '2', '2', '0', 'xiezi.png');
INSERT INTO `shop_classify` VALUES ('23', '低帮鞋', '2', '2', '0', 'xiezi_1.png');
INSERT INTO `shop_classify` VALUES ('24', '高帮鞋', '2', '2', '0', 'chaoyipu.png');
INSERT INTO `shop_classify` VALUES ('25', '皮鞋', '2', '2', '0', 'pixie.png');
INSERT INTO `shop_classify` VALUES ('26', '帆布鞋', '2', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('27', '箱包皮具', '2', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('28', '双肩包', '2', '2', '0', 'shuangjianbao.png');
INSERT INTO `shop_classify` VALUES ('29', '饭锅电磁炉', '3', '2', '0', 'dianfanguo.png');
INSERT INTO `shop_classify` VALUES ('30', '取暖电器', '3', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('31', '洗衣机', '3', '2', '0', 'xiyiji.png');
INSERT INTO `shop_classify` VALUES ('32', '厨房电器', '3', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('33', '个护健康', '3', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('34', '冰箱空调', '3', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('35', '生活电器', '3', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('36', '电视', '3', '2', '0', 'dianshi.png');
INSERT INTO `shop_classify` VALUES ('37', '烟灶热水器', '3', '2', '0', 'reshuiqi.png');
INSERT INTO `shop_classify` VALUES ('38', '休闲零食', '4', '2', '1', 'xiuxianlingshi.png');
INSERT INTO `shop_classify` VALUES ('39', '乳制品', '4', '2', '1', 'tubiao.png');
INSERT INTO `shop_classify` VALUES ('40', '饼干糕点', '4', '2', '1', 'gaodian.png');
INSERT INTO `shop_classify` VALUES ('41', '茶叶冲饮', '4', '2', '1', 'chaye.png');
INSERT INTO `shop_classify` VALUES ('42', '速食', '4', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('43', '中外名酒', '4', '2', '1', 'jiu.png');
INSERT INTO `shop_classify` VALUES ('44', '新鲜水果', '4', '2', '1', 'shuiguo.png');
INSERT INTO `shop_classify` VALUES ('45', '进口优选', '4', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('46', '海鲜水产', '4', '2', '1', 'haixian.png');
INSERT INTO `shop_classify` VALUES ('47', '床和床垫', '5', '2', '1', 'chuang.png');
INSERT INTO `shop_classify` VALUES ('48', '沙发电视柜', '5', '2', '1', 'shafa.png');
INSERT INTO `shop_classify` VALUES ('49', '餐桌电脑桌', '5', '2', '1', 'fanzhuo.png');
INSERT INTO `shop_classify` VALUES ('50', '柜子架子', '5', '2', '1', 'jiazi.png');
INSERT INTO `shop_classify` VALUES ('51', '椅子凳子', '5', '2', '1', 'yizi.png');
INSERT INTO `shop_classify` VALUES ('52', '儿童家具', '5', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('53', '办公家具', '5', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('54', '户外家具', '5', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('55', '学习桌', '5', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('56', '平板电脑', '6', '2', '0', 'pingbandiannao.png');
INSERT INTO `shop_classify` VALUES ('57', '学习机', '6', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('58', '电脑整机', '6', '2', '0', 'icon-test.png');
INSERT INTO `shop_classify` VALUES ('59', '笔类', '6', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('60', '外设配件', '6', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('61', '学生文具', '6', '2', '0', 'wenjuwujin.png');
INSERT INTO `shop_classify` VALUES ('62', '游戏设备', '6', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('63', '办公设备', '6', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('64', '网络设备', '6', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('65', '面部护理', '9', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('66', '面膜', '9', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('67', '彩妆香氛', '9', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('68', '美发护发', '9', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('69', '口红唇膏', '9', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('70', '水乳面霜', '9', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('71', '护手霜', '9', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('72', '个人洗护', '9', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('73', '美妆工具', '9', '2', '1', null);
INSERT INTO `shop_classify` VALUES ('74', '手机', '6', '2', '0', 'shouji.png');

-- ----------------------------
-- Table structure for shop_order
-- ----------------------------
DROP TABLE IF EXISTS `shop_order`;
CREATE TABLE `shop_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_sn` varchar(225) DEFAULT NULL COMMENT '订单编号',
  `express_sn` varchar(225) DEFAULT NULL COMMENT '快递单号',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `user_address` varchar(200) DEFAULT NULL COMMENT '用户地址',
  `tel` bigint(50) DEFAULT NULL COMMENT '用户电话',
  `
user_id` int(11) DEFAULT NULL COMMENT '用户Id',
  `seller_id` int(11) DEFAULT NULL,
  `seller_name` varchar(225) DEFAULT NULL,
  `total_money` decimal(12,2) DEFAULT NULL COMMENT '总金额',
  `order_status` int(20) DEFAULT '0' COMMENT '0为待支付，1为支付成功，2为待发货，3已发货，',
  `mark` varchar(225) DEFAULT NULL,
  `is_pick` int(20) DEFAULT NULL COMMENT '1.用户自提 2.快递发货',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of shop_order
-- ----------------------------

-- ----------------------------
-- Table structure for shop_order_goods
-- ----------------------------
DROP TABLE IF EXISTS `shop_order_goods`;
CREATE TABLE `shop_order_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goods_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL COMMENT '商品上架时间',
  `goods_name` varchar(200) DEFAULT NULL,
  `is_group` int(10) DEFAULT NULL,
  `price` decimal(12,2) DEFAULT NULL,
  `group_price` decimal(12,2) DEFAULT NULL,
  `go_num` int(11) DEFAULT NULL COMMENT '购买数量',
  `total_price` decimal(12,2) DEFAULT NULL COMMENT '总价',
  `order_id` int(11) DEFAULT NULL COMMENT '订单表id',
  `p_version` varchar(225) DEFAULT NULL COMMENT '商品版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of shop_order_goods
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `password` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `is_vip` int(10) DEFAULT '0' COMMENT '是否为vip',
  `is_seller` int(20) DEFAULT '0' COMMENT '是否为商家',
  `tel` bigint(50) DEFAULT NULL COMMENT '手机号码',
  `score` int(20) DEFAULT '0' COMMENT '积分',
  `money` decimal(20,0) unsigned DEFAULT '0' COMMENT '用户余额',
  `is_admin` int(20) DEFAULT '0' COMMENT '是否为管理员',
  `is_apply` int(20) DEFAULT '0' COMMENT '是否提交商家注册',
  `apply_money` decimal(50,0) DEFAULT '0' COMMENT '申请金额',
  `applied_mark` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '申请后返回备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('33', '1', 'c4ca4238a0b923820dcc509a6f75849b', '0', '1', '1', '0', '0', '1', '1', '0', '');
INSERT INTO `user` VALUES ('34', '2', 'c81e728d9d4c2f636f067f89cc14862c', '0', '1', '2', '0', '0', '0', '1', '0', '');
INSERT INTO `user` VALUES ('35', '李生', '202cb962ac59075b964b07152d234b70', '0', '1', '18718212688', '0', '0', '1', '1', '0', '');
INSERT INTO `user` VALUES ('36', '13149988', '12413116f0327ab857ceb2b9dcf7580e', '0', '1', '13763012343', '0', '0', '1', '1', '0', '');
INSERT INTO `user` VALUES ('37', '8058', 'f54c77d59132ac26a4fb7e8e611d1dd3', '0', '0', '18520081873', '0', '0', '0', '0', '0', '');
INSERT INTO `user` VALUES ('38', '19860729', '7c7f6cd41099e91abdc5a89ba3a3687a', '0', '0', '18218870150', '0', '0', '0', '0', '0', '');
INSERT INTO `user` VALUES ('39', '720108', '629ddea77ebb0009363816cf35cbc3f9', '0', '0', '15113611112', '0', '0', '0', '0', '0', '');
INSERT INTO `user` VALUES ('40', '1823810554', '4a6ea9d394695e30110f0ba9f75822a3', '0', '0', '15768862291', '0', '0', '0', '0', '0', '');

-- ----------------------------
-- Table structure for version
-- ----------------------------
DROP TABLE IF EXISTS `version`;
CREATE TABLE `version` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '型号id',
  `product_id` int(20) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名字',
  `version_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '型号名字',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `product_id` (`product_id`) USING BTREE,
  CONSTRAINT `version_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product_item` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of version
-- ----------------------------
INSERT INTO `version` VALUES ('49', '39', '外套男士春秋季2019新款韩版潮流春装潮牌上衣服', 'M');
INSERT INTO `version` VALUES ('50', '39', '外套男士春秋季2019新款韩版潮流春装潮牌上衣服', 'L');
INSERT INTO `version` VALUES ('51', '40', 'VIVO X21 全屏幕指纹 vivox21 手机', '6+128G');
INSERT INTO `version` VALUES ('52', '40', 'VIVO X21 全屏幕指纹 vivox21 手机', '6+64G');
INSERT INTO `version` VALUES ('53', '41', 'VIVO X21 全屏幕指纹 vivox21 手机', '6+64G');
INSERT INTO `version` VALUES ('54', '41', 'VIVO X21 全屏幕指纹 vivox21 手机', '6+128G');
INSERT INTO `version` VALUES ('55', '42', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', 'H07白黑');
INSERT INTO `version` VALUES ('56', '42', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '39');
INSERT INTO `version` VALUES ('57', '42', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '40');
INSERT INTO `version` VALUES ('58', '42', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '41');
INSERT INTO `version` VALUES ('59', '42', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '42');
INSERT INTO `version` VALUES ('60', '42', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '');
INSERT INTO `version` VALUES ('61', '42', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '43');
INSERT INTO `version` VALUES ('62', '43', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', 'H07');
INSERT INTO `version` VALUES ('63', '43', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '39');
INSERT INTO `version` VALUES ('64', '43', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '40');
INSERT INTO `version` VALUES ('65', '43', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '41');
INSERT INTO `version` VALUES ('66', '43', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '42');
INSERT INTO `version` VALUES ('67', '43', '2019新款鞋子男韩版潮流小白鞋百搭休闲运动潮鞋春', '43');
INSERT INTO `version` VALUES ('68', '44', '苏泊尔5L双胆智能电压力锅家用高压锅电饭煲全自动官', '5L');
INSERT INTO `version` VALUES ('69', '44', '苏泊尔5L双胆智能电压力锅家用高压锅电饭煲全自动官', 'CYSB50YCWI');
INSERT INTO `version` VALUES ('70', '45', '测试商品', '22');
