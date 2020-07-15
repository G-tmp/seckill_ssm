//

var seckill = {

    // 地址
    url:{
        now:function () {
            return "/seckill/time/now";
        },
        exposer:function (seckillId) {
            return "/seckill/"+seckillId+"/exporser";
        },
        execution:function (seckillId,md5) {
            return "/seckill/"+seckillId+"/"+md5+"/execution";
        }

    },


    // 验证手机号
    validatePhone : function(phone){
        if (phone && phone.length === 11 && !isNaN(phone)){
            return true;
        }else {
            return false;
        }
    },


    // 执行秒杀流程
    handleSeckill : function(seckillId,node){
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">start</button>');

        $.post(seckill.url.exposer(seckillId),{},function (result) {
            if (result && result["success"]){
                var exposer = result["data"];

                if (exposer && exposer["exposed"]){
                    var md5 = exposer["md5"];
                    var killUrl = seckill.url.execution(seckillId,md5);
                    console.log("kill url : "+killUrl);
                    //绑定一次点击事件
                    $("#killBtn").one("click",function () {
                        //执行秒杀请求

                        //1:先禁用按钮
                        $(this).addClass("disabled");

                        //2:发送秒杀请求执行秒杀 （成功，重复，结束都应该为 true）
                        $.post(killUrl,{},function (result) {
                            if (result && result["success"]){
                                var killResult = result["data"];
                                var state = killResult["state"];
                                var stateInfo = killResult["stateInfo"];

                                //3:显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                }else {
                    //未开启秒杀
                    var now = exposer["now"];
                    var start = exposer["start"];
                    var end = exposer["end"];
                    //重新计算计时逻辑
                    seckillId.countDown(seckillId,now,start,end);
                }

            }else {
                console.log('result:'+result);
            }
        });

    },


    // 倒计时
    countDown : function(seckillId , nowTime , startTime , endTime){
        var seckillBox = $("#seckill-box");

        if (nowTime > endTime){     // 秒杀结束
             seckillBox.html("秒杀结束");
        }else if (nowTime < startTime){     // 秒杀未开始，倒计时
            var killTime = new Date(startTime + 1000);

            // a bootstrap build-in function with display countdown
            seckillBox.countdown(killTime,function (event) {
               var format = event.strftime("countdown: %Dday %Hhour %Mmin %Ss");
               seckillBox.html(format);
            }).on("finish.countdown",function () {
                // 倒计时结束 秒杀开始
                seckill.handleSeckill(seckillId,seckillBox);
            });
        }else {         // 秒杀开始
            seckill.handleSeckill(seckillId,seckillBox);
        }
    },


    // 进入详情月页 验证手机号
    detail:{
        init:function (params) {
            // weather exist cookie
            var killPhone = $.cookie("killPhone");

            if (!seckill.validatePhone(killPhone)) {
                // 登录弹出层
                var killPhoneModal = $("#killPhoneModal");
                killPhoneModal.modal({
                    show: true,         // enable
                    backdrop: "static", // disable drag
                    keyboard: false     // disable keyboard`esc`
                });

                console.log("XDXXX");

                $("#killPhoneBtn").click(function () {
                    var inputPhone = $("#killPhoneKey").val();

                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie("killPhone", inputPhone, {expires: 7, path: "/seckill"})
                        window.location.reload();
                    } else {
                        $("#killPhoneMessage").hide().html("<label class='label label-danger'>手机号错误！</label>").show(200);
                    }
                });
            }


            var seckillId = params["seckillId"];
            var startTime = params["startTime"];
            var endTime = params["endTime"];
            $.get(seckill.url.now(),{},function (result) {
                if (result && result["success"]){
                    var now = result["data"];

                    // 判断时间
                    seckill.countDown(seckillId,now,startTime,endTime);
                }else {
                    console.log("result : "+result);
                }
            })
        }


    }

};