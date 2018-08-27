define({ "api": [
  {
    "type": "函数",
    "url": "getBankcardByBankName(\"bankName\")",
    "title": "按银行名获取卡号",
    "group": "BANKCARD",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "bankName",
            "description": "<p>银行名称,如:CCB:建设,CITIC:中信,HXB:华夏,GDB:广发,PAB:平安,ABC:农业,BOC:中国,CMB:招商,CEB:光大,BCOM:民生,SPDB:浦发,ICBC:工商,CIB:兴业,PSBC:邮政</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getBankcardByBankName(\"CCB\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./BankCardUtils.java",
    "groupTitle": "BANKCARD",
    "name": "__GetbankcardbybanknameBankname"
  },
  {
    "type": "函数",
    "url": "getBankcardByUserId_db(\"userId\")",
    "title": "按userid获取卡号",
    "group": "BANKCARD",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getBankcardByUserId_db(\"123\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./BankCardUtils.java",
    "groupTitle": "BANKCARD",
    "name": "__Getbankcardbyuserid_dbUserid"
  },
  {
    "type": "函数",
    "url": "getUnUseBankcard_db()",
    "title": "未使用的卡号",
    "group": "BANKCARD",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUnUseBankcard_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./BankCardUtils.java",
    "groupTitle": "BANKCARD",
    "name": "__Getunusebankcard_db"
  },
  {
    "type": "函数",
    "url": "getUnUseBankcardByBankName_db(\"bankName\")",
    "title": "按名称获取未使用的卡号",
    "group": "BANKCARD",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "bankName",
            "description": "<p>银行名称,如:CCB:建设,CITIC:中信,HXB:华夏,GDB:广发,PAB:平安,ABC:农业,BOC:中国,CMB:招商,CEB:光大,BCOM:民生,SPDB:浦发,ICBC:工商,CIB:兴业,PSBC:邮政</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUnUseBankcardByBankName_db(\"CCB\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./BankCardUtils.java",
    "groupTitle": "BANKCARD",
    "name": "__Getunusebankcardbybankname_dbBankname"
  },
  {
    "type": "函数",
    "url": "getUseBankcardRandom_db()",
    "title": "随机获取已经存在的银行卡号码",
    "group": "BANKCARD",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUseBankcardRandom_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./BankCardUtils.java",
    "groupTitle": "BANKCARD",
    "name": "__Getusebankcardrandom_db"
  },
  {
    "type": "函数",
    "url": "getUsedBankcardByBankName_db(\"bankName\")",
    "title": "按名称获取已使用的卡号",
    "group": "BANKCARD",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "bankName",
            "description": "<p>银行名称，如：CCB:建设,CITIC:中信,HXB:华夏,GDB:广发,PAB:平安,ABC:农业,BOC:中国,CMB:招商,CEB:光大,BCOM:民生,SPDB:浦发,ICBC:工商,CIB:兴业,PSBC:邮政</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUsedBankcardByBankName_db(\"CCB\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./BankCardUtils.java",
    "groupTitle": "BANKCARD",
    "name": "__Getusedbankcardbybankname_dbBankname"
  },
  {
    "type": "函数",
    "url": "get36Hex1(\"bId\")",
    "title": "生成bo_identity",
    "group": "CAL",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "bId",
            "description": "<p>bpId</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${get36Hex1(\"123\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./Arithmetic.java",
    "groupTitle": "CAL",
    "name": "__Get36hex1Bid"
  },
  {
    "type": "函数",
    "url": "md5Encode(\"str\")",
    "title": "md5加密",
    "group": "CAL",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "str",
            "description": "<p>待加密字符串</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${md5Encode(\"it789123\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./Arithmetic.java",
    "groupTitle": "CAL",
    "name": "__Md5encodeStr"
  },
  {
    "type": "函数",
    "url": "operator(\"str\")",
    "title": "四则运算",
    "group": "CAL",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "str",
            "description": "<p>四则运算表达式</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${operator(\"1+2*5\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./Arithmetic.java",
    "groupTitle": "CAL",
    "name": "__OperatorStr"
  },
  {
    "type": "函数",
    "url": "getMultField2(\"db_Config\",\"sql\")",
    "title": "按数据源查询多字段",
    "group": "DB_OPER",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "db_Config",
            "description": "<p>数据源别名，在平台中已配好的数据源</p>"
          },
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "sql",
            "description": "<p>sql语句</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getMultField2(\"STBDS\", \"select id,name from user_info;\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./DBOperator.java",
    "groupTitle": "DB_OPER",
    "name": "__Getmultfield2Db_configSql"
  },
  {
    "type": "函数",
    "url": "getMultField_db(\"sql\")",
    "title": "多字段查询",
    "group": "DB_OPER",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "sql",
            "description": "<p>sql语句</p>"
          },
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "args",
            "description": "<p>可选参数，数据库配置名称，默认default</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getMultField_db(\"select id,name from user_info;\", \"default\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./DBOperator.java",
    "groupTitle": "DB_OPER",
    "name": "__Getmultfield_dbSql"
  },
  {
    "type": "函数",
    "url": "getOneField2(\"db_Config\",\"sql\")",
    "title": "按数据源查询单字段",
    "group": "DB_OPER",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "db_Config",
            "description": "<p>数据源别名，在平台中已配好的数据源</p>"
          },
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "sql",
            "description": "<p>sql语句</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getOneField2(\"STBDS\", \"select name from user_info;\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./DBOperator.java",
    "groupTitle": "DB_OPER",
    "name": "__Getonefield2Db_configSql"
  },
  {
    "type": "函数",
    "url": "getOneField_db(\"sql\")",
    "title": "单字段查询",
    "group": "DB_OPER",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "sql",
            "description": "<p>sql语句</p>"
          },
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "args",
            "description": "<p>可选参数，数据库配置名称，默认default</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getOneField_db(\"select name from user_info\", \"default\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./DBOperator.java",
    "groupTitle": "DB_OPER",
    "name": "__Getonefield_dbSql"
  },
  {
    "type": "函数",
    "url": "insertValues2(\"db_Config\",\"sql\")",
    "title": "按数据源插入数据",
    "group": "DB_OPER",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "db_Config",
            "description": "<p>数据源别名，在平台中已配好的数据源</p>"
          },
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "sql",
            "description": "<p>插入语句</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${insertValues2(\"STBDS\", \"insert into user_info values('1')\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./DBOperator.java",
    "groupTitle": "DB_OPER",
    "name": "__Insertvalues2Db_configSql"
  },
  {
    "type": "函数",
    "url": "insertValues_db(\"sql\")",
    "title": "插数据",
    "group": "DB_OPER",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "sql",
            "description": "<p>插入sql语句</p>"
          },
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "args",
            "description": "<p>可选参数，数据库配置名称，默认default</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${insertValues_db(\"insert into user_info values('1')\", \"default\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./DBOperator.java",
    "groupTitle": "DB_OPER",
    "name": "__Insertvalues_dbSql"
  },
  {
    "type": "函数",
    "url": "updateValues2(\"db_Config\",\"sql\")",
    "title": "按数据源修改数据",
    "group": "DB_OPER",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "db_Config",
            "description": "<p>数据源别名，在平台中已配好的数据源</p>"
          },
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "sql",
            "description": "<p>修改语句</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${updateValues2(\"STBDS\", \"update user_info set user_name='user1' where id=1;\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./DBOperator.java",
    "groupTitle": "DB_OPER",
    "name": "__Updatevalues2Db_configSql"
  },
  {
    "type": "函数",
    "url": "updateValues_db(\"sql\")",
    "title": "修改数据",
    "group": "DB_OPER",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "sql",
            "description": "<p>sql修改语句</p>"
          },
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "args",
            "description": "<p>可选参数，数据库配置名称，默认default</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${updateValues_db(\"update user_info set user_name='user1' where id=1;\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./DBOperator.java",
    "groupTitle": "DB_OPER",
    "name": "__Updatevalues_dbSql"
  },
  {
    "type": "函数",
    "url": "generate()",
    "title": "随机生成身份证号",
    "group": "IDCARD",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${generate()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./IdCardGenerator.java",
    "groupTitle": "IDCARD",
    "name": "__Generate"
  },
  {
    "type": "函数",
    "url": "generateByYear(\"area\")",
    "title": "按地区生成身份证号",
    "group": "IDCARD",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "area",
            "description": "<p>地区</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${generateByYear(\"xxxx\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./IdCardGenerator.java",
    "groupTitle": "IDCARD",
    "name": "__GeneratebyyearArea"
  },
  {
    "type": "函数",
    "url": "generateByYear(year)",
    "title": "按年生成身份证号",
    "group": "IDCARD",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "int",
            "optional": false,
            "field": "year",
            "description": "<p>出生年份</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${generateByYear(\"1982\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./IdCardGenerator.java",
    "groupTitle": "IDCARD",
    "name": "__GeneratebyyearYear"
  },
  {
    "type": "函数",
    "url": "generateByYearEndWithLowerCase(\"year\")",
    "title": "根据年份生成x结尾的身份证",
    "group": "IDCARD",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "year",
            "description": "<p>出生年份</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${generateByYearEndWithLowerCase(\"1990\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./IdCardGenerator.java",
    "groupTitle": "IDCARD",
    "name": "__GeneratebyyearendwithlowercaseYear"
  },
  {
    "type": "函数",
    "url": "generateByYearEndWithX(\"year\")",
    "title": "根据年份生成X结尾的身份证",
    "group": "IDCARD",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "year",
            "description": "<p>出生年份</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${generateByYearEndWithX(\"1990\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./IdCardGenerator.java",
    "groupTitle": "IDCARD",
    "name": "__GeneratebyyearendwithxYear"
  },
  {
    "type": "函数",
    "url": "generateEndWithX(\"year\")",
    "title": "生成X结尾的身份证",
    "group": "IDCARD",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${generateEndWithX()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./IdCardGenerator.java",
    "groupTitle": "IDCARD",
    "name": "__GenerateendwithxYear"
  },
  {
    "type": "函数",
    "url": "getRegisterIDCardRandom_db()",
    "title": "已注册的身份证号",
    "group": "IDCARD",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getRegisterIDCardRandom_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./IdCardGenerator.java",
    "groupTitle": "IDCARD",
    "name": "__Getregisteridcardrandom_db"
  },
  {
    "type": "函数",
    "url": "getUnRegisterByAdultIDCard_db()",
    "title": "未使用的身份证号码(16-40岁)",
    "group": "IDCARD",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUnRegisterByAdultIDCard_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./IdCardGenerator.java",
    "groupTitle": "IDCARD",
    "name": "__Getunregisterbyadultidcard_db"
  },
  {
    "type": "函数",
    "url": "getUnRegisterIDCard_db()",
    "title": "未使用的身份证号",
    "group": "IDCARD",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUnRegisterIDCard_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./IdCardGenerator.java",
    "groupTitle": "IDCARD",
    "name": "__Getunregisteridcard_db"
  },
  {
    "type": "函数",
    "url": "getUnRegisterIDCardByYear_db(\"year\")",
    "title": "按年生成未注册的身份证号",
    "group": "IDCARD",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "year",
            "description": "<p>出生年份</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUnRegisterIDCardByYear_db(\"1990\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./IdCardGenerator.java",
    "groupTitle": "IDCARD",
    "name": "__Getunregisteridcardbyyear_dbYear"
  },
  {
    "type": "函数",
    "url": "getBankcardMobile_db()",
    "title": "已绑卡号码",
    "group": "MOBILE",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getBankcardMobile_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Getbankcardmobile_db"
  },
  {
    "type": "函数",
    "url": "getIdCard_db(\"mobile\")",
    "title": "按手机号获取身份证号",
    "group": "MOBILE",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "mobile",
            "description": "<p>手机号码</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getIdCard_db(\"mobile\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Getidcard_dbMobile"
  },
  {
    "type": "函数",
    "url": "getPayPassword_db(\"mobile\")",
    "title": "按手机号获取支付密码",
    "group": "MOBILE",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "mobile",
            "description": "<p>手机号码</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getPayPassword_db(\"mobile\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Getpaypassword_dbMobile"
  },
  {
    "type": "函数",
    "url": "getRealName_db(\"mobile\")",
    "title": "按手机号获取真实姓名",
    "group": "MOBILE",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "mobile",
            "description": "<p>手机号码</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getRealName_db(\"mobile\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Getrealname_dbMobile"
  },
  {
    "type": "函数",
    "url": "getRealnameMobileRandom_db()",
    "title": "已实名号码",
    "group": "MOBILE",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getRealnameMobileRandom_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Getrealnamemobilerandom_db"
  },
  {
    "type": "函数",
    "url": "getRegisterMobileRandom_db()",
    "title": "已注册号码",
    "group": "MOBILE",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getRegisterMobileRandom_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Getregistermobilerandom_db"
  },
  {
    "type": "函数",
    "url": "getUnRegisterAmericaMobile_db()",
    "title": "美国未注册号码",
    "group": "MOBILE",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUnRegisterAmericaMobile_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Getunregisteramericamobile_db"
  },
  {
    "type": "函数",
    "url": "getUnRegisterMobile_db()",
    "title": "未注册号码",
    "group": "MOBILE",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUnRegisterMobile_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Getunregistermobile_db"
  },
  {
    "type": "函数",
    "url": "getUserId_db(\"phoneNo\")",
    "title": "按手机号获取UserId",
    "group": "MOBILE",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "mobile",
            "description": "<p>手机号码</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUserId_db(\"180123123123\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Getuserid_dbPhoneno"
  },
  {
    "type": "函数",
    "url": "getUserName_db(\"phoneNo\")",
    "title": "按手机号获取UserName",
    "group": "MOBILE",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "mobile",
            "description": "<p>测试参数</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUserName_db(\"180123123123\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Getusername_dbPhoneno"
  },
  {
    "type": "函数",
    "url": "md5MobileDate(\"mobile\")",
    "title": "MD5加密手机号码和当前日期",
    "group": "MOBILE",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "mobile",
            "description": "<p>手机号码</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${md5MobileDate(\"mobile\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE",
    "name": "__Md5mobiledateMobile"
  },
  {
    "type": "函数",
    "url": "getLoginPassword_db(\"mobile\")",
    "title": "按手机号获取登陆密码",
    "name": "xxx",
    "group": "MOBILE",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "mobile",
            "description": "<p>手机号码</p>"
          },
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "requestXML",
            "description": "<p>请求xml</p>"
          },
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "id",
            "description": "<p>用户id</p>"
          }
        ],
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Object[]",
            "optional": false,
            "field": "f1",
            "description": "<p>f1数组</p>"
          },
          {
            "group": "Parameter",
            "type": "Object",
            "optional": false,
            "field": "f1.f2",
            "description": "<p>f2字段</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "f1.f2.f3",
            "description": "<p>f3字段</p>"
          },
          {
            "group": "Parameter",
            "type": "Object[]",
            "optional": false,
            "field": "BOTTOMMODULES",
            "description": "<p>数组BOTTOMMODULES</p>"
          },
          {
            "group": "Parameter",
            "type": "Object",
            "optional": false,
            "field": "BOTTOMMODULES.e",
            "description": "<p>数组BOTTOMMODULES的元素</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "BOTTOMMODULES.e.name",
            "description": "<p>数组BOTTOMMODULES的元素的属性name</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "BOTTOMMODULES.e.version",
            "description": "<p>数组BOTTOMMODULES的元素的属性version</p>"
          },
          {
            "group": "Parameter",
            "type": "Object",
            "optional": false,
            "field": "resultBean",
            "description": "<p>对象resultBean</p>"
          },
          {
            "group": "Parameter",
            "type": "Object[]",
            "optional": false,
            "field": "resultBean.bottomModules",
            "description": "<p>数组BOTTOMMODULES</p>"
          },
          {
            "group": "Parameter",
            "type": "Object",
            "optional": false,
            "field": "resultBean.bottomModules.e",
            "description": "<p>数组BOTTOMMODULES的元素</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "resultBean.bottomModules.e.IOSUrl",
            "description": "<p>字段IOSUrl</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Request-Example:",
          "content": "requestXML= \n\t\"<xml>\n\t\t<id>1234</id>\n\t<xml>\"",
          "type": "String"
        }
      ]
    },
    "success": {
      "fields": {
        "请求响应": [
          {
            "group": "请求响应",
            "type": "String",
            "optional": false,
            "field": "code",
            "description": "<p>响应状态码</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n  \"code\": \"0000\"\n}",
          "type": "json"
        },
        {
          "title": "fail-Response:",
          "content": "HTTP/1.1 200 OK\n{\n  \"code\": \"0001\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./MobileUtil.java",
    "groupTitle": "MOBILE"
  },
  {
    "type": "函数",
    "url": "getRateCutCode_db()",
    "title": "获得未使用的减息券",
    "group": "OTHER",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getRateCutCode_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./RateCutCodeUtils.java",
    "groupTitle": "OTHER",
    "name": "__Getratecutcode_db"
  },
  {
    "type": "函数",
    "url": "generateRandomStr(\"arr\")",
    "title": "随机返回数组中一个元素",
    "group": "RANDOM",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "arr",
            "description": "<p>任意数组</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${generateRandomStr(\"aaa,bbbb,ccc,ddd\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./RandomDataUtils.java",
    "groupTitle": "RANDOM",
    "name": "__GeneraterandomstrArr"
  },
  {
    "type": "函数",
    "url": "generateRandomValue(\"length\")",
    "title": "生成指定长度随机数",
    "group": "RANDOM",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "length",
            "description": "<p>随机数的长度</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${generateRandomValue(\"length\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./RandomDataUtils.java",
    "groupTitle": "RANDOM",
    "name": "__GeneraterandomvalueLength"
  },
  {
    "type": "函数",
    "url": "getSignature_db()",
    "title": "获取签名",
    "group": "SIGNATURE",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getSignature_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./SignatureUtils.java",
    "groupTitle": "SIGNATURE",
    "name": "__Getsignature_db"
  },
  {
    "type": "函数",
    "url": "getServerTime()",
    "title": "获取服务器时间",
    "group": "TIME",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getServerTime()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./TimestampUtils.java",
    "groupTitle": "TIME",
    "name": "__Getservertime"
  },
  {
    "type": "函数",
    "url": "getServerTimeWithFormat(\"timeFormat\")",
    "title": "按格式获取服务器时间,YYMMDD/hhmmss",
    "group": "TIME",
    "version": "0.1.0",
    "parameter": {
      "fields": {
        "入参": [
          {
            "group": "入参",
            "type": "String",
            "optional": false,
            "field": "timeFormat",
            "description": "<p>时间格式</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getServerTimeWithFormat(\"timeFormat\")}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./TimestampUtils.java",
    "groupTitle": "TIME",
    "name": "__GetservertimewithformatTimeformat"
  },
  {
    "type": "函数",
    "url": "getRegisterEmailByRandom_db()",
    "title": "获取已经存在的email",
    "group": "USER",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getRegisterEmailByRandom_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./UserUtils.java",
    "groupTitle": "USER",
    "name": "__Getregisteremailbyrandom_db"
  },
  {
    "type": "函数",
    "url": "getRegisterUserNameByRandom_db()",
    "title": "获取已经存在的用户名",
    "group": "USER",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getRegisterUserNameByRandom_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./UserUtils.java",
    "groupTitle": "USER",
    "name": "__Getregisterusernamebyrandom_db"
  },
  {
    "type": "函数",
    "url": "getUnRegisterEmail_db()",
    "title": "获取未使用的email",
    "group": "USER",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUnRegisterEmail_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./UserUtils.java",
    "groupTitle": "USER",
    "name": "__Getunregisteremail_db"
  },
  {
    "type": "函数",
    "url": "getUnRegisterUserName_db()",
    "title": "获取未注册的用户名",
    "group": "USER",
    "version": "0.1.0",
    "success": {
      "examples": [
        {
          "title": "调用说明:",
          "content": "${getUnRegisterUserName_db()}",
          "type": "invoke"
        }
      ]
    },
    "filename": "./UserUtils.java",
    "groupTitle": "USER",
    "name": "__Getunregisterusername_db"
  }
] });
