CREATE TABLE `richmj_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fromId` varchar(2049) DEFAULT NULL COMMENT '发消息的人',
  `toId` varchar(2049) DEFAULT NULL COMMENT '接收消息的人',
  `type` varchar(20) DEFAULT NULL COMMENT '类型',
  `message` text NOT NULL COMMENT '消息内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

