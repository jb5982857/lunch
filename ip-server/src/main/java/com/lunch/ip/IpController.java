package com.lunch.ip;

import com.lunch.ip.dao.IpDao;
import com.lunch.support.tool.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ip")
public class IpController {
    private static final int BLACK = 1;
    @Autowired
    private IpDao mDao;

    @RequestMapping(value = "/check", method = {RequestMethod.GET, RequestMethod.POST})
    public boolean black(@RequestParam("ip") String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        int state = mDao.getStateByIp(ip);
        return state == BLACK;
    }
}
