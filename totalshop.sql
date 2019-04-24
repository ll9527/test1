/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.5.49 : Database - totalshop
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`totalshop` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `totalshop`;

/*Table structure for table `address` */

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '地址id',
  `address` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '地址详情',
  `user_id` int(20) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `tel` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '收货电话',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='用户地址关联表';

/*Table structure for table `admin_img` */

DROP TABLE IF EXISTS `admin_img`;

CREATE TABLE `admin_img` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片名字',
  `role` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '作用码 1 证件 2 广告图',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `admin_profit` */

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
  `discount_persent` int(20) DEFAULT NULL COMMENT '全场优惠百分比',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统设置表';

/*Table structure for table `class_with_product` */

DROP TABLE IF EXISTS `class_with_product`;

CREATE TABLE `class_with_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `product_id` int(20) DEFAULT NULL,
  `level2_class_id` int(20) DEFAULT NULL COMMENT '二级目录id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品与2级目录的关联表';

/*Table structure for table `collect` */

DROP TABLE IF EXISTS `collect`;

CREATE TABLE `collect` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `seller_id` int(20) DEFAULT NULL,
  `product_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='收藏表';

/*Table structure for table `coupons` */

DROP TABLE IF EXISTS `coupons`;

CREATE TABLE `coupons` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `isAdmin` int(20) DEFAULT NULL COMMENT '是否全场通用 1通用',
  `product_id` int(20) DEFAULT NULL,
  `seller_id` int(20) DEFAULT NULL,
  `preferential_money` int(20) DEFAULT NULL COMMENT '优惠券金额',
  `order_id` int(20) DEFAULT NULL,
  `onDelete` int(10) DEFAULT NULL COMMENT '0 没删除，1删除',
  `add_time` datetime DEFAULT NULL,
  `over_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='优惠券表';

/*Table structure for table `product_comment_freight` */

DROP TABLE IF EXISTS `product_comment_freight`;

CREATE TABLE `product_comment_freight` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `product_id` int(20) DEFAULT NULL,
  `comment` varchar(500) DEFAULT NULL COMMENT '评论',
  `freight` decimal(12,2) DEFAULT NULL COMMENT '运费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COMMENT='商品运费评论关联表';

/*Table structure for table `product_img` */

DROP TABLE IF EXISTS `product_img`;

CREATE TABLE `product_img` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `product_id` int(20) DEFAULT NULL,
  `image` varchar(500) DEFAULT NULL COMMENT '图片名字',
  `is_cover` int(20) DEFAULT '0' COMMENT '是否封面 1：是封面',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `product_id` (`product_id`) USING BTREE,
  CONSTRAINT `product_img_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product_item` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品图片关联表';

/*Table structure for table `product_item` */

DROP TABLE IF EXISTS `product_item`;

CREATE TABLE `product_item` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '商品id，同时也是商品编号',
  `title` varchar(100) DEFAULT NULL COMMENT '商品标题',
  `sell_point` varchar(500) DEFAULT NULL COMMENT '商品卖点',
  `price` decimal(20,2) DEFAULT NULL COMMENT '商品价格，单位为：角',
  `group_price` decimal(20,2) DEFAULT NULL COMMENT '商品拼团价格，单位为：角',
  `is_group` int(20) DEFAULT NULL COMMENT '是否团购商品',
  `num` int(10) DEFAULT NULL COMMENT '库存数量',
  `cid` int(10) DEFAULT NULL COMMENT '所属类目，叶子类目',
  `status` tinyint(4) DEFAULT '1' COMMENT '商品状态，1-正常，2-下架，3-删除',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `sales_volume` int(255) DEFAULT '0' COMMENT '销量',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `cid` (`cid`) USING BTREE,
  KEY `status` (`status`) USING BTREE,
  KEY `updated` (`updated`) USING BTREE,
  CONSTRAINT `product_item_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `shop_classify` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品表';

/*Table structure for table `referrer` */

DROP TABLE IF EXISTS `referrer`;

CREATE TABLE `referrer` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `referrer_id` int(20) DEFAULT NULL COMMENT '推荐人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户与推荐人关联表';

/*Table structure for table `seller` */

DROP TABLE IF EXISTS `seller`;

CREATE TABLE `seller` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '商户id',
  `tel` bigint(50) DEFAULT NULL COMMENT '商户电话',
  `title_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '店铺名字',
  `user_id` int(20) DEFAULT NULL COMMENT '用户id',
  `seller_class` varchar(225) COLLATE utf8_bin DEFAULT '' COMMENT '商户类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `title_name` (`title_name`) USING BTREE,
  CONSTRAINT `seller_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='商户表';

/*Table structure for table `seller_address` */

DROP TABLE IF EXISTS `seller_address`;

CREATE TABLE `seller_address` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '商家地址id',
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商家地址',
  `seller_id` int(20) DEFAULT NULL COMMENT '商家id',
  `seller_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '店长名字',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `seller_id` (`seller_id`) USING BTREE,
  CONSTRAINT `seller_address_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='商户地址关联表';

/*Table structure for table `seller_bcimg` */

DROP TABLE IF EXISTS `seller_bcimg`;

CREATE TABLE `seller_bcimg` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '证件id',
  `img` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '证件路径',
  `user_id` int(20) DEFAULT NULL COMMENT '用户id',
  `seller_id` int(20) DEFAULT NULL COMMENT '商家id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `seller_id` (`seller_id`) USING BTREE,
  CONSTRAINT `seller_bcimg_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

/*Table structure for table `seller_cover` */

DROP TABLE IF EXISTS `seller_cover`;

CREATE TABLE `seller_cover` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片名字',
  `sid` int(11) DEFAULT NULL COMMENT '商家id',
  `role` int(11) DEFAULT NULL COMMENT '作用码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `seller_visit` */

DROP TABLE IF EXISTS `seller_visit`;

CREATE TABLE `seller_visit` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `seller_id` int(20) DEFAULT NULL,
  `visit_num` bigint(50) DEFAULT '0' COMMENT '每天访问人数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='商户每天访问人数表';

/*Table structure for table `seller_with_product_img` */

DROP TABLE IF EXISTS `seller_with_product_img`;

CREATE TABLE `seller_with_product_img` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `seller_id` int(20) DEFAULT NULL,
  `product_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `seller_id` (`seller_id`) USING BTREE,
  CONSTRAINT `seller_with_product_img_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商户和商品的关联表';

/*Table structure for table `shop_classify` */

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
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='分类表';

/*Table structure for table `shop_order` */

DROP TABLE IF EXISTS `shop_order`;

CREATE TABLE `shop_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_sn` varchar(225) DEFAULT NULL COMMENT '订单编号',
  `express_sn` varchar(225) DEFAULT NULL COMMENT '快递单号',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `user_address` varchar(200) DEFAULT NULL COMMENT '用户地址',
  `tel` bigint(50) DEFAULT NULL COMMENT '用户电话',
  `user_id` int(11) DEFAULT NULL COMMENT '用户Id',
  `seller_id` int(11) DEFAULT NULL,
  `seller_name` varchar(225) DEFAULT NULL,
  `total_money` decimal(12,2) DEFAULT NULL COMMENT '总金额',
  `order_status` int(20) DEFAULT '0' COMMENT '0为待支付，1为待发货，2待收货，3确认收货，4退款，买家已发货，5退款，商家已收到, 6仅退款，7团购订单没付款，8团购订单付款  -1作废单 -2仅退款单 -3退货退款单',
  `mark` varchar(225) DEFAULT NULL,
  `is_pick` int(20) DEFAULT NULL COMMENT '1.用户自提 2.快递发货',
  `prepay_id` varchar(200) DEFAULT NULL COMMENT 'prepay_id',
  `is_group` int(11) DEFAULT '0' COMMENT '是否团购订单 1团购订单',
  `group_oid` int(11) DEFAULT NULL COMMENT '团购总订单的id',
  `end_time` datetime DEFAULT NULL COMMENT '成交时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=103 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单表';

/*Table structure for table `shop_order_goods` */

DROP TABLE IF EXISTS `shop_order_goods`;

CREATE TABLE `shop_order_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goods_id` int(11) DEFAULT NULL COMMENT '商品id',
  `user_id` int(11) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL COMMENT '创建订单时间，如果为null代表作废单',
  `goods_name` varchar(200) DEFAULT NULL COMMENT '商品名字',
  `is_group` int(10) DEFAULT NULL,
  `price` decimal(12,2) DEFAULT NULL,
  `group_price` decimal(12,2) DEFAULT NULL,
  `go_num` int(11) DEFAULT NULL COMMENT '购买数量',
  `total_price` decimal(12,2) DEFAULT NULL COMMENT '总价',
  `order_id` int(11) DEFAULT NULL COMMENT '订单表id',
  `p_version` varchar(225) DEFAULT NULL COMMENT '商品版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='商品和订单关联表';

/*Table structure for table `shop_order_group` */

DROP TABLE IF EXISTS `shop_order_group`;

CREATE TABLE `shop_order_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `add_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '1是拼团成功',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `shop_product_specification` */

DROP TABLE IF EXISTS `shop_product_specification`;

CREATE TABLE `shop_product_specification` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `product_id` int(20) DEFAULT NULL COMMENT '商品id',
  `v_id` int(20) DEFAULT NULL COMMENT '0代表父规格，其余代表父规格的id，空则代表sku',
  `version_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '规格名字',
  `price` decimal(20,2) DEFAULT NULL COMMENT '正价',
  `group_price` decimal(20,2) DEFAULT NULL COMMENT '团购价钱',
  `num` int(20) DEFAULT NULL COMMENT '库存',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='规格颜色表';

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `password` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `is_vip` int(10) DEFAULT '0' COMMENT '是否为vip 1-vip',
  `is_seller` int(20) DEFAULT '0' COMMENT '是否为商家 1-是商家',
  `tel` bigint(50) DEFAULT NULL COMMENT '手机号码',
  `score` int(20) DEFAULT '0' COMMENT '积分',
  `money` decimal(20,2) DEFAULT '0.00' COMMENT '用户余额',
  `is_admin` int(20) DEFAULT '0' COMMENT '是否为管理员 1-是管理员',
  `is_apply` int(20) DEFAULT '0' COMMENT '是否提交商家注册 1-提交注册',
  `apply_money` decimal(50,2) DEFAULT '0.00' COMMENT '申请金额',
  `applied_mark` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '申请后返回备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='用户表';

/*Table structure for table `version` */

DROP TABLE IF EXISTS `version`;

CREATE TABLE `version` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '型号id',
  `product_id` int(20) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名字',
  `version_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '型号名字',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `product_id` (`product_id`) USING BTREE,
  CONSTRAINT `version_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product_item` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='商品的规格表';

/*Table structure for table `version1` */

DROP TABLE IF EXISTS `version1`;

CREATE TABLE `version1` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '型号id',
  `product_id` int(20) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名字',
  `version1_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '型号（颜色）名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品的型号颜色表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
