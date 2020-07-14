package org.xd.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xd.dto.Exposer;
import org.xd.dto.SeckillExecution;
import org.xd.dto.SeckillResult;
import org.xd.enmus.SeckillStateEnmu;
import org.xd.entity.Seckill;
import org.xd.exception.SeckillCloseException;
import org.xd.exception.SeckillRepeatException;
import org.xd.service.SeckillService;

import javax.annotation.Resource;
import java.lang.reflect.Executable;
import java.util.Date;
import java.util.List;


@Controller // 类似 @Service @Component
@RequestMapping("/seckill")
public class SeckillController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){

        List<Seckill> seckillList = seckillService.getSeckillList();
        model.addAttribute("list",seckillList);

        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(Model model,@PathVariable("seckillId") Long seckillId){

        if (seckillId == null){
            return "redirect:/seckill/list";
        }

        Seckill seckill = seckillService.getSeckillById(seckillId);
        if (seckill == null){
            return "forward:/seckill/list";
        }

        model.addAttribute("seckill",seckill);
        return "detail";
    }

    // ajax json
    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exporser",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public SeckillResult expose(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result = null;

        try {
            Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
            result = new SeckillResult<>(true,exposer);
        } catch (Exception e) {
            result = new SeckillResult<>(false,e.getMessage());
            logger.error(e.getMessage(),e);
        }

        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @CookieValue(value = "killPhone",required = false) Long phone,
                                                   @PathVariable("md5") String md5){
        if (phone == null){
            return new SeckillResult<>(false,"未注册");
        }


        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId,phone,md5);
            return  new SeckillResult<>(true,execution);
        }catch (SeckillRepeatException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnmu.REPEAT);
            return new SeckillResult<>(false,execution);
        }catch (SeckillCloseException e){
            SeckillExecution execution = new SeckillExecution(seckillId,SeckillStateEnmu.END);
            return new SeckillResult<>(false,execution);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            SeckillExecution execution = new SeckillExecution(seckillId,SeckillStateEnmu.ERROR);
            return new SeckillResult<>(false,execution);
        }

    }


    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<>(true,now.getTime());
    }
}
